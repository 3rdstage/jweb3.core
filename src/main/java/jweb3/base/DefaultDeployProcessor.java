package jweb3.base;

import java.math.BigInteger;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

@ThreadSafe
public class DefaultDeployProcessor implements DeployProcessor{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final Web3j web3;

  private final long chainId;

  public long getChainId() {
    return this.chainId;
  }

  private final BigInteger defaultGasPrice;

  public BigInteger getDefaultGasPrice() {
    return this.defaultGasPrice;
  }

  private final BigInteger defaultGasLimit;

  public BigInteger getDefaultGasLimit() {
    return this.defaultGasLimit;
  }

  public DefaultDeployProcessor(@Nonnull final Web3j web3,
      @PositiveOrZero final BigInteger defaultGasPrice,
      @Positive final BigInteger defaultGasLimit) {

    Validate.isTrue(web3 != null, "Web3j client should be provided.");
    this.web3 = web3;

    try {
      // NOTE Ignore when the chain ID is out of `long` scope
      chainId = web3.ethChainId().send().getChainId().longValue();
    }catch(Exception ex) {
      throw new RuntimeException("Provided Web3 intance dosen't provide expected function.");
    }

    this.defaultGasPrice = defaultGasPrice;
    this.defaultGasLimit = defaultGasLimit;
  }


  @Override
  public @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") String process(
      @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") @Nonnull final String senderAddress,
      @NotBlank final String byteCode,
      @PositiveOrZero final BigInteger gasPrice,
      @Positive final BigInteger gasLimit){

    try {
      final BigInteger nonce = web3.ethGetTransactionCount(
          senderAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();

      // serialized tx = RLP(tx)
      // msg = Keccak(serialized tx) = Keccak(RLP(tx))
      // signatre = Sign(msg) = Sign(Keccak(RLP(tx)))
      // signed tx = tx + signature = tx + Sign(Keccak(RLP(tx)))
      // serialized signed tx = RLP(signed tx) = RLP(tx + Sign(Keccak(RLP(tx))))

      //final byte[] serializedTx = this.serialize(nonce, gasPrice, gasLimit, null, BigInteger.ZERO, byteCode, chainId);
      //final byte[] msg = Hash.sha3(serializedTx);
      //final Signature signature = this.signer.sign(msg, senderAddress);


      final Transaction tx = Transaction.createContractTransaction(
          senderAddress, nonce, gasPrice, gasLimit, BigInteger.ZERO, byteCode);
      final String txHash = web3.ethSendTransaction(tx).send().getTransactionHash();

      this.logger.info("Deploying a contract. - sender: {}, contract: {}..., nonce: {}, gas: {}, gas price: {}, tx hash: {}",
          senderAddress, StringUtils.substring(byteCode, 0, 20), nonce, gasLimit, gasPrice, txHash);

      final TransactionReceiptProcessor rcptProcessor
        = new PollingTransactionReceiptProcessor(this.web3, 1000L, 10);
      final TransactionReceipt rcpt = rcptProcessor.waitForTransactionReceipt(txHash);

      if(!rcpt.isStatusOK()) {
        throw new RuntimeException(String.format("Fail to deploy conract. - sender: %s, nonce: %,d, gas: %,d, gas price: %,d, reason: %s",
            senderAddress, nonce, gasLimit, gasPrice, rcpt.getRevertReason()));
      }

      final String cntrAddr = rcpt.getContractAddress();
      this.logger.info("Successfully deployed a contract. - sender: {}, contract byte code: {}, contract address: {}, tx hash: {}",
          senderAddress, StringUtils.substring(byteCode, 0, 20), cntrAddr, txHash);

      return cntrAddr;

    }catch(Exception ex) {
      this.logger.error("Fail to deploy contract. {}", ex.getMessage());
      if(ex instanceof RuntimeException) throw (RuntimeException)ex;
      else throw new RuntimeException(ex);
    }
  }


  @Override
  public @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") String process(
      @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") final String senderAddress,
      @NotBlank final String byteCode){

    return this.process(senderAddress, byteCode, this.defaultGasPrice, this.defaultGasLimit);
  }

}

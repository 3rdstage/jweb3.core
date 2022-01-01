package jweb3.base;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import jweb3.base.func.FunctionCall;
import jweb3.base.func.Output;
import jweb3.base.func.OutputSignature;

@ThreadSafe
public class DefaultCallProcessor implements CallProcessor{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private Web3j web3;

  public DefaultCallProcessor(@Nonnull final Web3j web3) {

    Validate.isTrue(web3 != null, "Web3j client should be provided.");
    this.web3 = web3;
  }


  @Override
  public Output process(
      @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") final String senderAddress,
      @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") final String contractAddress,
      @Nonnull final FunctionCall funcCall,
      @Nonnull final OutputSignature outputSignature){

    Validate.isTrue(StringUtils.isNotBlank(senderAddress), "");

    Validate.isTrue(funcCall != null, "Function call should be specified.");

    // execute `eth_call` via Web3j
    Transaction call = Transaction.createEthCallTransaction(
        senderAddress, contractAddress, funcCall.getEncoded());

    try {
      EthCall resp = this.web3.ethCall(call, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> output = FunctionReturnDecoder.decode(resp.getResult(), outputSignature.getParams());

      return new Output(output);

    }catch(Throwable ex) {
      this.logger.error(String.format("Fail to call smart contract - sender: %s, contract: %s, function: %s",
          senderAddress, contractAddress, funcCall.getName()));

      if(ex instanceof RuntimeException) { throw (RuntimeException)ex; }
      else throw new RuntimeException(ex);
    }
  }

}

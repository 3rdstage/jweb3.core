package jweb3.base.tx;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jweb3.util.CryptoUtils;

/**
 * @author Sangmoon Oh
 * @since 0.8
 */
public class DefaultTransactionSigner implements TransactionSigner{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private Map<Class, TransactionSerializer> serializers = new HashMap<>();

  private ECDSASigner ecdsaSigner;

  public DefaultTransactionSigner(@NotNull ECDSASigner ecdsaSigner) {
    Validate.isTrue(ecdsaSigner != null, "Transaction sginer requires ECDSA signer.");

    this.ecdsaSigner = ecdsaSigner;
    this.registerSerializers();
  }

  protected void registerSerializers() {
    this.serializers.put(LegacyTransaction.class, new LegacyTransactionSerializer());
  }

  protected <T extends Transaction> TransactionSerializer lookupSerializer(@NotNull Class<T> type){

    final TransactionSerializer serializer = this.serializers.get(type);

    if(serializer == null) {
      throw new RuntimeException(
          String.format("No transaction serializer is found for the type of transaction : %s", type.getName()));
    }
    return serializer;
  }

  @Override
  public <T extends Transaction> byte[] sign(@NotNull T tx,
      @Pattern(regexp = "0x[0-9A-Fa-f]{40}") @NotNull final String signerAddr) {

    Validate.isTrue(tx != null, "Can't sign null.");
    Validate.isTrue(signerAddr != null, "Signer should be specified.");

    this.logger.debug("Signing tx - singer: {}, {}", signerAddr, tx.toShortString());

    // serialize transaction before signing
    final TransactionSerializer<T> serializer = this.lookupSerializer(tx.getClass());
    final byte[] serializedTx = serializer.serialize(tx);
    this.logger.debug("Serialized tx - signer: {}, {}, serialized: {}...",
        signerAddr, tx.toShortString(), ""); // TODO

    // hash serialized transaction before signing
    // expecting 256 bytes
    final byte[] hashedTx = CryptoUtils.keccak(serializedTx);
    this.logger.debug("Hashed serialized tx - signer: {}, tx: {}, serialized: {}..., hashed: {}...",
        signerAddr, tx.toShortString(), "",
        Hex.encodeHexString(ArrayUtils.subarray(hashedTx, 0, 8)));

    // generate signature of the transaction
    final Pair<BigInteger, BigInteger> signature = this.ecdsaSigner.sign(hashedTx, signerAddr);

    // build signed transaction
    final T signedTx = tx.withSignature(signature.getLeft(), signature.getRight());

    // TODO Logging
    // TODO Whether to verify the generate signature using ecrecover function ?
    //      Is this to be determined via additional input parameter ?
    //      refer

    // serialize transaction after signing
    return serializer.serialize(signedTx);
  }





}

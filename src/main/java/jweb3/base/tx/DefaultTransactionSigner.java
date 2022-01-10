package jweb3.base.tx;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTransactionSigner implements TransactionSigner{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private Map<Class, TransactionSerializer> serializers = new HashMap<>();

  private ECDSASigner messageSigner;

  public DefaultTransactionSigner(@Nonnull ECDSASigner msgSigner) {
    Validate.isTrue(msgSigner != null, "Transaction sginer requires ECDSA signer.");

    this.messageSigner = msgSigner;
  }

  protected void registerSerializers() {
    this.serializers.put(LegacyTransaction.class, new LegacyTransactionSerializer());
  }

  protected <T extends Transaction> TransactionSerializer lookupSerializer(@Nonnull Class<T> type){

    final TransactionSerializer serializer = this.serializers.get(type);

    if(serializer == null) {
      throw new RuntimeException(
          String.format("No transaction serializer is found for the type of transaction : %s", type.getName()));
    }
    return serializer;
  }

  @Override
  public <T extends Transaction> byte[] sign(@Nonnull T tx,
      @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") @Nonnull final String signerAddr) {

    Validate.isTrue(tx != null, "Can't sign null.");
    Validate.isTrue(signerAddr != null, "Signer should be specified.");

    this.logger.debug("Signing tx - singer: {}, {}", signerAddr, tx.toShortString());

    // serialize transaction before signing
    final TransactionSerializer<T> serializer = this.lookupSerializer(tx.getClass());
    final byte[] serializedTx = serializer.serialize(tx);
    this.logger.debug("Serialized tx - signer: {}, {}, serialized: {}...",
        signerAddr, tx.toShortString(), ""); // TODO

    // generate signature of the transaction
    final Pair<BigInteger, BigInteger> signature = this.messageSigner.sign(serializedTx, signerAddr);

    // build signed transaction
    final T signedTx = tx.withSignature(signature.getLeft(), signature.getRight());
    // TODO logging

    // serialize transaction after signing
    return serializer.serialize(signedTx);
  }





}

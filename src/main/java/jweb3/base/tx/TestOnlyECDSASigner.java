package jweb3.base.tx;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;

public class TestOnlyECDSASigner implements ECDSASigner{


  private Map<String, BigInteger> keys = new HashMap<>(); // map from address to private key;

  @Override
  public Pair<BigInteger, BigInteger> sign(final byte[] message, final String address){

    final BigInteger prvKey = this.keys.get(address);
    Validate.validState(prvKey != null, String.format("Can't find privake key for the specified address '%s'.", address));

    // https://github.com/web3j/web3j/blob/v4.8.8/crypto/src/main/java/org/web3j/crypto/ECKeyPair.java
    org.bouncycastle.crypto.signers.ECDSASigner signer
      = new org.bouncycastle.crypto.signers.ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
    signer.init(true, new ECPrivateKeyParameters(prvKey, EC_DOMAIN_PARAMS));
    BigInteger[] rs = signer.generateSignature(message);

    return Pair.of(rs[0], rs[1]);
  }


  public TestOnlyECDSASigner(@NotEmpty Pair<String, String>[] keypairs) {

    // TODO

  }

}

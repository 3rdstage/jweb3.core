package jweb3.base.tx;

import java.math.BigInteger;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;

public interface ECDSASigner{

  static final String EC_NAME = "secp256k1";
  static final ECDomainParameters EC_DOMAIN_PARAMS =
      new ECDomainParameters(CustomNamedCurves.getByName(EC_NAME));

  Pair<BigInteger, BigInteger> sign(final byte[] message, final String address);

}

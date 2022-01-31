package jweb3.base.tx;

import java.math.BigInteger;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;

/**
 * @author Sangmoon Oh
 * @since 0.8
 *
 * @see <a href="https://github.com/bcgit/bc-java/blob/r1rv68/core/src/main/java/org/bouncycastle/crypto/signers/ECDSASigner.java#L93"><tt>org.bouncycastle.crypto.signers.ECDSASigner.generateSignature(byte[])</tt></a>
 * @see <a href="https://github.com/indutny/elliptic/blob/43ac7f230069bd1575e1e4a58394a512303ba803/lib/elliptic/ec/index.js#L91"><tt>elliptic.EC.prototype.sign(msg, key, enc, options)</tt></a>
 */
public interface ECDSASigner{

  static final String EC_NAME = "secp256k1";
  static final ECDomainParameters EC_DOMAIN_PARAMS =
      new ECDomainParameters(CustomNamedCurves.getByName(EC_NAME));

  Pair<BigInteger, BigInteger> sign(final byte[] message,
      @Pattern(regexp = "0x[0-9A-Fa-f]{40}") @NotNull  final String signerAddr);

}

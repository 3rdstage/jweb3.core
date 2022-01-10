package jweb3.util;

import java.math.BigInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Arrays;

public class KeyUtils{

  private static final ECDomainParameters _EC_DOMAIN_PARAMS =
      new ECDomainParameters(CustomNamedCurves.getByName("secp256k1"));

  @Nonnull
  public static BigInteger getPublicKeyFromPrivateKey(@Positive @Nonnull final BigInteger privateKey) {
    Validate.isTrue(privateKey != null, "Private key cann't be null");
    Validate.isTrue(privateKey.signum() > 0, "Ethereum private key should be positive.");

    ECPoint q = _EC_DOMAIN_PARAMS.getG().multiply(privateKey);
    ECPublicKeyParameters pubKeyParams = new ECPublicKeyParameters(q, _EC_DOMAIN_PARAMS);

    byte[] pubKey = pubKeyParams.getQ().getEncoded(false);
    return new BigInteger(1, Arrays.copyOfRange(pubKey, 1, pubKey.length));
  }

  @Nonnull
  public static BigInteger getPublicKeyFromPrivateKey(
      @Pattern(regexp = "(0x)?[0-9A-Fa-f]{1,64}") @Nonnull String privateKey) {

    Validate.isTrue(privateKey != null, "Private key cann't be null");

    final BigInteger key = new BigInteger(StringUtils.removeStart(privateKey, "0x"), 16);
    return getPublicKeyFromPrivateKey(key);
  }

  @Nullable
  public static String getPublicKeyAsString(@Nullable final BigInteger publicKey, final boolean prefixed) {

    if(publicKey == null) return null;

    if(prefixed) return "0x" + publicKey.toString(16);
    else return publicKey.toString(16);
  }


}

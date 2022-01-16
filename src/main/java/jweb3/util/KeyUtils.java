package jweb3.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

/**
 * @author Sangmoon Oh
 * @since 0.8
 */
public class KeyUtils{

  private static final ECDomainParameters _EC_DOMAIN_PARAMS =
      new ECDomainParameters(CustomNamedCurves.getByName("secp256k1"));

  /**
   * ECDSA/secp256k1 public key size in bytes
   */
  public static final int PRIVATE_KEY_SIZE = 32;

  /**
   * ECDSA/secp256k1 private key size in bytes
   */
  public static final int PUBLIC_KEY_SIZE = 64;

  /**
   * Ethereum address size in bytes
   */
  public static final int ADDRESS_SIZE = 20;

  @NotNull
  public static BigInteger getPublicKeyFromPrivateKey(@Positive @NotNull final BigInteger privateKey) {
    Validate.isTrue(privateKey != null, "Private key cann't be null");
    Validate.isTrue(privateKey.signum() > 0, "Ethereum private key should be positive.");

    ECPoint q = _EC_DOMAIN_PARAMS.getG().multiply(privateKey);
    ECPublicKeyParameters pubKeyParams = new ECPublicKeyParameters(q, _EC_DOMAIN_PARAMS);

    byte[] pubKey = pubKeyParams.getQ().getEncoded(false);
    return new BigInteger(1, Arrays.copyOfRange(pubKey, 1, pubKey.length));
  }

  @NotNull
  public static BigInteger getPublicKeyFromPrivateKey(
      @Pattern(regexp = "(0x)?[0-9A-Fa-f]{1,64}") @NotNull String privateKey) {

    Validate.isTrue(privateKey != null, "Private key cann't be null");
    return getPublicKeyFromPrivateKey(new BigInteger(StringUtils.removeStart(privateKey, "0x"), 16));
  }

  /**
   * @param privateKey
   * @return
   *
   * @see #getChecksumAddressFromPrivateKey(BigInteger)
   * @see #getAddressFromPrivateKey(String)
   * @see #getAddressFromPrivateKey(BigInteger)
   * @see #getAddressFromPrivateKey(BigInteger, boolean, boolean)
   */
  @Pattern(regexp = "0x[0-9A-Fa-f]{40}") @Nullable
  public static String getChecksumAddressFromPrivateKey(
      @Pattern(regexp = "(0x)?[0-9A-Fa-f]{1,64}") @Nullable String privateKey) {

    return getAddressFromPrivateKey(new BigInteger(StringUtils.removeStart(privateKey, "0x"), 16), true, true);
  }

  /**
   * @param privateKey
   * @return
   *
   * @see #getChecksumAddressFromPrivateKey(String)
   * @see #getAddressFromPrivateKey(String)
   * @see #getAddressFromPrivateKey(BigInteger)
   * @see #getAddressFromPrivateKey(BigInteger, boolean, boolean)
   */
  @Pattern(regexp = "0x[0-9A-Fa-f]{40}") @Nullable
  public static String getChecksumAddressFromPrivateKey(
      @Positive @Nullable BigInteger privateKey) {

    return getAddressFromPrivateKey(privateKey, true, true);
  }

  /**
   * @param privateKey
   * @return
   *
   * @see #getChecksumAddressFromPrivateKey(String)
   * @see #getChecksumAddressFromPrivateKey(BigInteger)
   * @see #getAddressFromPrivateKey(BigInteger)
   * @see #getAddressFromPrivateKey(BigInteger, boolean, boolean)
   */
  @Pattern(regexp = "0x[0-9a-f]{40}") @Nullable
  public static String getAddressFromPrivateKey(
      @Pattern(regexp = "(0x)?[0-9A-Fa-f]{1,64}") @Nullable String privateKey) {

    return getAddressFromPrivateKey(new BigInteger(StringUtils.removeStart(privateKey, "0x"), 16), false, true);
  }

  /**
   * @param privateKey
   * @return
   *
   * @see #getChecksumAddressFromPrivateKey(String)
   * @see #getChecksumAddressFromPrivateKey(BigInteger)
   * @see #getAddressFromPrivateKey(String)
   * @see #getAddressFromPrivateKey(BigInteger, boolean, boolean)
   */
  @Pattern(regexp = "0x[0-9a-f]{40}") @Nullable
  public static String getAddressFromPrivateKey(@Positive @Nullable BigInteger privateKey) {

    return getAddressFromPrivateKey(privateKey, false, true);
  }

  /**
   * @param privateKey
   * @param checksumed
   * @param prefixed
   * @return
   *
   * @see #getChecksumAddressFromPrivateKey(String)
   * @see #getChecksumAddressFromPrivateKey(BigInteger)
   * @see #getAddressFromPrivateKey(String)
   * @see #getAddressFromPrivateKey(BigInteger)
   */
  @Pattern(regexp = "(0x)?[0-9A-Fa-f]{40}") @Nullable
  public static String getAddressFromPrivateKey(
      @Positive @Nullable BigInteger privateKey, boolean checksumed, boolean prefixed) {
    if(privateKey == null) return null;

    try {
      // padded unprefixed 128 length string
      final String pubKey = getPublicKeyAsString(getPublicKeyFromPrivateKey(privateKey), true, false);

      // 20 bytes
      final byte[] addr = Arrays.copyOfRange(CryptoUtils.keccak(Hex.decodeHex(pubKey)),
          CryptoUtils.KECCAK_HASH_SIZE - ADDRESS_SIZE, CryptoUtils.KECCAK_HASH_SIZE);

      if(checksumed) {
        // @TODO Review https://stackoverflow.com/questions/33791230/char-array-to-byte-array-in-utf-8-without-using-string-or-charset

        // 40 chars
        final char[] addr2 = Hex.encodeHex(addr);
        final char[] hash = Hex.encodeHex(CryptoUtils.keccak(String.valueOf(addr2).getBytes(StandardCharsets.UTF_8)));
        assert (addr2.length == ADDRESS_SIZE * 2);
        assert (hash.length == CryptoUtils.KECCAK_HASH_SIZE * 2);

        for(int i = 0; i < ADDRESS_SIZE * 2; i ++) {
          //if(Character.compare(hash[i], '7') > 0) addr2[i] = Character.toUpperCase(addr2[i]);
          if(Character.compare(hash[i], '7') > 0) addr2[i] = Character.toUpperCase(addr2[i]);
        }

        return (prefixed) ? "0x" + String.valueOf(addr2) : String.valueOf(addr2);
      }else {
        return (prefixed) ? "0x" + Hex.encodeHexString(addr) : Hex.encodeHexString(addr);
      }

    }catch(Exception ex) {
      if (ex instanceof RuntimeException) throw (RuntimeException)ex;
      else throw new RuntimeException(ex);
    }
  }

  // @TODO Not yet working correctly
  // @TODO Need performance comparison with `getAddressFromPrivateKey`
  public static String getAddressFromPrivateKey2(
      BigInteger privateKey, boolean checksumed, boolean prefixed) {

    try {
      // public key in char array representing unpadded hexdecimal
      char[] pubKey = Hex.encodeHex(getPublicKeyFromPrivateKey(privateKey).toByteArray(), true);

      final int more = PUBLIC_KEY_SIZE * 2 - pubKey.length;
      if(more > 0) {
        final char[] head = new char[more];
        for(int i = 0; i < more; i++) head[i] = '0';
        final char[] pubKey2 = pubKey;
        pubKey = new char[PUBLIC_KEY_SIZE * 2];
        for(int i = 0; i < more; i++) pubKey[i] = head[i];
        for(int i = more ; i < PUBLIC_KEY_SIZE * 2; i++) pubKey[i] = pubKey2[i - more];
      }else if(more < 0) {
        final char[] pubKey2 = pubKey;
        pubKey = new char[PUBLIC_KEY_SIZE * 2];
        for(int i = 0; i < PUBLIC_KEY_SIZE * 2; i++) pubKey[i] = pubKey2[- more + i];
      }

      assert pubKey.length == PUBLIC_KEY_SIZE * 2;

      // 20 bytes
      final byte[] addr = Arrays.copyOfRange(CryptoUtils.keccak(Hex.decodeHex(pubKey)),
          CryptoUtils.KECCAK_HASH_SIZE - ADDRESS_SIZE, CryptoUtils.KECCAK_HASH_SIZE);

      if(checksumed) {
        // @TODO Review https://stackoverflow.com/questions/33791230/char-array-to-byte-array-in-utf-8-without-using-string-or-charset

        // 40 chars
        final char[] addr2 = Hex.encodeHex(addr);
        final char[] hash = Hex.encodeHex(CryptoUtils.keccak(String.valueOf(addr2).getBytes(StandardCharsets.UTF_8)));
        assert (addr2.length == ADDRESS_SIZE * 2);
        assert (hash.length == CryptoUtils.KECCAK_HASH_SIZE * 2);

        for(int i = 0; i < ADDRESS_SIZE * 2; i ++) {
          //if(Character.compare(hash[i], '7') > 0) addr2[i] = Character.toUpperCase(addr2[i]);
          if(Character.compare(hash[i], '7') > 0) addr2[i] = Character.toUpperCase(addr2[i]);
        }

        return (prefixed) ? "0x" + String.valueOf(addr2) : String.valueOf(addr2);
      }else {
        return (prefixed) ? "0x" + Hex.encodeHexString(addr) : Hex.encodeHexString(addr);
      }

    }catch(Exception ex) {
      if (ex instanceof RuntimeException) throw (RuntimeException)ex;
      else throw new RuntimeException(ex);
    }
  }


  @Pattern(regexp = "0x[0-9A-Fa-f]{128}") @Nullable
  public static String getPublicKeyAsString(@Nullable final BigInteger publicKey) {
    return getPublicKeyAsString(publicKey, true, true);
  }


  @Pattern(regexp = "(0x)?[0-9A-Fa-f]+") @Nullable
  public static String getPublicKeyAsString(@Nullable final BigInteger publicKey, final boolean padded, final boolean prefixed) {

    if(publicKey == null) return null;

    final String str = publicKey.toString(16);
    if(str.length() > PUBLIC_KEY_SIZE * 2) throw new IllegalArgumentException("Too large number for an Ethereum public key.");

    if(padded) {
      final StringBuilder sb
        = (prefixed) ? new StringBuilder(PUBLIC_KEY_SIZE * 2 + 2).append("0x") : new StringBuilder(PUBLIC_KEY_SIZE * 2);
      for(int i = str.length(); i < PUBLIC_KEY_SIZE * 2; i++) sb.append('0');
      return sb.append(str).toString();
    }else {
      return (prefixed) ? "0x" + str : str;
    }
  }

}

package jweb3.base.tx;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.util.Arrays;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jweb3.util.KeyUtils;

/**
 * @author Sangmoon Oh
 * @since 0.8
 */
public class TestOnlyECDSASigner implements ECDSASigner{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private Map<@Pattern(regexp = "0x[0-9A-Fa-f]{40}") String, @Positive BigInteger> keys = new HashMap<>(); // map from address to private key;

  @Override
  public Pair<BigInteger, BigInteger> sign(final byte @NotNull @NotEmpty [] message,
      @Pattern(regexp = "0x[0-9A-Fa-f]{40}") @NotNull final String signerAddr){

    // TODO What if `message` is null or empty ?

    final BigInteger prvKey = this.keys.get(signerAddr);
    Validate.validState(prvKey != null, String.format("Can't find privake key for the specified address '%s'.", signerAddr));

    // https://github.com/web3j/web3j/blob/v4.8.8/crypto/src/main/java/org/web3j/crypto/ECKeyPair.java
    org.bouncycastle.crypto.signers.ECDSASigner signer
      = new org.bouncycastle.crypto.signers.ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
    signer.init(true, new ECPrivateKeyParameters(prvKey, EC_DOMAIN_PARAMS));
    BigInteger[] rs = signer.generateSignature(message);

    return Pair.of(rs[0], rs[1]);
  }

  /**
   * @param pairs array of address and private key pairs.
   *              Address and private key are expected to be in '0x' prefixed hexadecimal values.
   */
  public TestOnlyECDSASigner(
      Pair<@Pattern(regexp = "0x[0-9A-Fa-f]{40}") @NotNull String, @Pattern(regexp = "0x[0-9A-Fa-f]{1,64}") @NotNull String> @NotEmpty [] pairs) {

    Validate.isTrue(pairs != null && pairs.length > 0, "At least one pair should be provided.");

    String addr = null;
    BigInteger key = null;
    for(Pair<String, String> pair: pairs) {
      // TODO validation

      addr = pair.getLeft();
      key = new BigInteger(StringUtils.removeStart(pair.getRight(), "0x"), 16);
      Validate.isTrue(key.signum() > 0, "Private key should be positive. {} is negative", pair.getRight());

      this.keys.put(addr, key);
      this.logger.debug("A key is added. - address: {}, private key: {}", addr, key);
    }
  }

  public TestOnlyECDSASigner(@Pattern(regexp = "0x[0-9A-Fa-f]{1,64}") @NotNull String @NotEmpty [] privateKeys) {

    Validate.isTrue(!Arrays.isNullOrEmpty(privateKeys), "At least one private key should be provided.");

    String addr = null;
    for(String key: privateKeys) {
      addr = KeyUtils.getChecksumAddressFromPrivateKey(key);
      this.keys.put(addr, new BigInteger(StringUtils.removeStart(key, "0x"), 16));

      this.logger.debug("A key is added. - address: {}, private key: {}", addr, keys.get(addr));
    }
  }


  /**
   * @param jsonFile a file contains JSON string for arrays of private key and address paris.
   *        JSON keys for private key and address are expected to be "privateKey" and "address"
   *
   * @see #TestOnlyECDSASigner(File)
   */
  public TestOnlyECDSASigner(@NotNull final Path jsonFile) throws Exception{

    Validate.isTrue(jsonFile != null, "No input file is specified.");

    final String json = new String(Files.readAllBytes(jsonFile));
    final JSONArray items = new JSONArray(json);
    Validate.isTrue(items.length() > 0, "At least one item should be provided.");

    String addr = null;
    BigInteger key = null;
    for(int i = 0, n = items.length(); i < n; i++) {
      addr = items.getJSONObject(i).getString("address");
      key = new BigInteger(StringUtils.removeStart(items.getJSONObject(i).getString("privateKey"), "0x"), 16);
      Validate.isTrue(key.signum() > 0, "Private key should be positive. {} is negative", items.getJSONObject(i).getString("privateKey"));

      this.keys.put(addr, key);
      this.logger.debug("A key is added. - address: {}, private key: {}", addr, key);
    }
  }

  /**
   * @param jsonFile a file contains JSON string for arrays of private key and address paris.
   *        JSON keys for private key and address are expected to be "privateKey" and "address"
   *
   * @see #TestOnlyECDSASigner(Path)
   */
  public TestOnlyECDSASigner(@NotNull final File jsonFile) throws Exception{
    this(jsonFile != null ? jsonFile.toPath() : null);

  }


}

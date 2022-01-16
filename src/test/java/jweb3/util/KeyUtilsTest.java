package jweb3.util;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import org.apache.commons.lang3.RandomUtils;
import org.json.JSONArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class KeyUtilsTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  @BeforeAll
  void beforeAll() throws Exception{

  }


  @Test
  @DisplayName("`getPublicKeyFromPrivateKey(BigInteger)` would throw exception with null input")
  void testGetPublicKeyFromPrivateKeyInIntWithNullInput() {

    final BigInteger nullKey = null;
    Assertions.assertThrows(RuntimeException.class, () -> { KeyUtils.getPublicKeyFromPrivateKey(nullKey); });
  }

  @Test
  @DisplayName("`getPublicKeyFromPrivateKey(BigInteger)` would throw exception with zero or negative input")
  void testGetPublicKeyFromPrivateKeyInIntWithZeroOrNegativeInput() {

    final BigInteger zeroKey = BigInteger.ZERO;
    Assertions.assertThrows(RuntimeException.class, () -> { KeyUtils.getPublicKeyFromPrivateKey(zeroKey); });

    final BigInteger negKey = BigInteger.valueOf(RandomUtils.nextLong(1, Long.MAX_VALUE)).not();
    Assertions.assertThrows(RuntimeException.class, () -> { KeyUtils.getPublicKeyFromPrivateKey(negKey); });
  }

  @Test
  @DisplayName("`getPublicKeyFromPrivateKey(String)` would throw exception with null input")
  void testGetPublicKeyFromPrivateKeyInStringWithNullInput() {

    final String nullKey = null;
    Assertions.assertThrows(RuntimeException.class, () -> { KeyUtils.getPublicKeyFromPrivateKey(nullKey); });
  }

  @Test
  void testGetPublicKeyFromPrivateKeyInString() {

  }

  @Test
  @DisplayName("`getPublicKeyAsStringWithNull` would return null with null input")
  void testGetPublicKeyAsStringWithNull(){

    String str = KeyUtils.getPublicKeyAsString(null, true, true);
    Assertions.assertNull(str);

    str = KeyUtils.getPublicKeyAsString(null, true, false);
    Assertions.assertNull(str);

    str = KeyUtils.getPublicKeyAsString(null, false, true);
    Assertions.assertNull(str);

    str = KeyUtils.getPublicKeyAsString(null, false, false);
    Assertions.assertNull(str);

  }


  @Test
  @DisplayName("`getPublicKeyAsString()` would convert `BigInteger` type public key to hexadecimal value string.")
  void testGetPublicKeyAsString() throws Exception{

    final File jsonFile = new File(ClassLoader.getSystemResource("jweb3/base/util/sample-keypairs-1.json").toURI());
    final String json = new String(Files.readAllBytes(jsonFile.toPath()));
    final JSONArray keyPairs = new JSONArray(json);

    String pubKey = null;
    BigInteger pubKeyInt = null;
    String pubKey2 = null;
    for(int i = 0, n = keyPairs.length(); i < n; i++) {
      pubKey = keyPairs.getJSONObject(i).getString("publicKey");
      pubKeyInt = new BigInteger(keyPairs.getJSONObject(i).getString("publicKeyInt"), 10);

      pubKey2 = KeyUtils.getPublicKeyAsString(pubKeyInt);
      Assertions.assertEquals(pubKey, pubKey2);
      Assertions.assertEquals(pubKey2.length(), 130);
      Assertions.assertEquals(pubKey2.substring(0, 2), "0x");

      this.logger.debug("Public Key          : {}", pubKey);
      this.logger.debug("Public Key Converted: {}", pubKey2);
    }
  }


  @Test
  void testPublicKeyFromPrivateKey() throws Exception{

    final File jsonFile = new File(ClassLoader.getSystemResource("jweb3/base/util/sample-keypairs-1.json").toURI());
    final String json = new String(Files.readAllBytes(jsonFile.toPath()));
    final JSONArray keyPairs = new JSONArray(json);

    BigInteger prvKey = null, pubKey = null, pubKey2 = null, pubKey3;
    String prvKeyStr = null;
    for(int i = 0, n = keyPairs.length(); i < n; i++) {
      prvKey = new BigInteger(keyPairs.getJSONObject(i).getString("privateKeyInt"), 10);
      prvKeyStr = keyPairs.getJSONObject(i).getString("privateKey");
      pubKey = new BigInteger(keyPairs.getJSONObject(i).getString("publicKeyInt"), 10);

      pubKey2 = KeyUtils.getPublicKeyFromPrivateKey(prvKey);
      Assertions.assertEquals(pubKey, pubKey2);

      pubKey3 = KeyUtils.getPublicKeyFromPrivateKey(prvKeyStr);
      Assertions.assertEquals(pubKey, pubKey3);

      this.logger.debug("Public Key            : {}", pubKey);
      this.logger.debug("Public Key Generated 1: {}", pubKey2);
      this.logger.debug("Public Key Generated 2: {}", pubKey3);
    }
  }


  @Test
  void testGetChecksumAddressFromPrivateKey() throws Exception{

    final File jsonFile = new File(ClassLoader.getSystemResource("jweb3/base/util/sample-keypairs-1.json").toURI());
    final String json = new String(Files.readAllBytes(jsonFile.toPath()));
    final JSONArray keyPairs = new JSONArray(json);

    String prvKey = null, addr = null, addr2 = null, addr3 = null;
    for(int i = 0, n = keyPairs.length(); i < n; i++) {
      prvKey = keyPairs.getJSONObject(i).getString("privateKey");
      addr = keyPairs.getJSONObject(i).getString("address");
      addr2 = KeyUtils.getChecksumAddressFromPrivateKey(prvKey);

      Assertions.assertEquals(addr, addr2);
      this.logger.debug("Private Key      : {}", prvKey);
      this.logger.debug("Address          : {}", addr);
      this.logger.debug("Address Generated: {}", addr2);
    }
  }

  @Test
  void testGetAddressFromPrivateKey2() throws Exception{

    final File jsonFile = new File(ClassLoader.getSystemResource("jweb3/base/util/sample-keypairs-1.json").toURI());
    final String json = new String(Files.readAllBytes(jsonFile.toPath()));
    final JSONArray keyPairs = new JSONArray(json);

    BigInteger prvKey = null;
    String addr = null, addr2 = null, addr3 = null;
    for(int i = 0, n = keyPairs.length(); i < n; i++) {
      prvKey = new BigInteger(keyPairs.getJSONObject(i).getString("privateKeyInt"));
      addr = keyPairs.getJSONObject(i).getString("address");
      addr2 = KeyUtils.getAddressFromPrivateKey2(prvKey, true, true);

      Assertions.assertEquals(addr, addr2);
      this.logger.debug("Private Key      : {}", prvKey);
      this.logger.debug("Address          : {}", addr);
      this.logger.debug("Address Generated: {}", addr2);
    }
  }
}

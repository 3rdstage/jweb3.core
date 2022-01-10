package jweb3.util;

import java.math.BigInteger;
import org.apache.commons.lang3.RandomUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class KeyUtilsTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  private JSONObject sampleKeyPairs;

  @BeforeAll
  void beforeAll() {



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

    String str = KeyUtils.getPublicKeyAsString(null, true);

    Assertions.assertNull(str);

    str = KeyUtils.getPublicKeyAsString(null, false);

    Assertions.assertNull(str);
  }

  @Test
  void testGetPublicKeyAsString() {

    BigInteger intKey = new BigInteger("5000000000000");
    String strKey = KeyUtils.getPublicKeyAsString(intKey, true);

    this.logger.debug("Integer: {}, String: {}", intKey, strKey);

  }


}

package jweb3.base.tx;

import java.math.BigInteger;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jweb3.util.KeyUtils;


/**
 * @author Sangmoon Oh
 */
class TestOnlyECDSASignerTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  @BeforeAll
  public void beforeAll() throws Exception{

    //final File jsonFile = new File(ClassLoader.getSystemResource("jweb3/base/util/sample-keypairs-1.json").toURI());
    //final ECDSASigner signer = new TestOnlyECDSASigner(jsonFile);
  }


  @Test
  @DisplayName("`sign()` should satisfy a simple case example in EIP-155 documentation (https://eips.ethereum.org/EIPS/eip-155)")
  void testSign1() throws Exception{

    // an example from EIP-155 (https://eips.ethereum.org/EIPS/eip-155)
    //   signing hash : 0xdaf5a779ae972f972197303d7b574746c7ef83eadac0f2791ad23db92e4c8e53
    //   private key  : 0x4646464646464646464646464646464646464646464646464646464646464646
    //   signature = secp256k1.sign(signing hash, private key)
    //   signature.v  : 37
    //   signatrue.r  : 18515461264373351373200002665853028612451056578545711640558177340181847433846
    //   signature.s  : 46948507304638947509940763649030358759909902576025900602547168820602576006531

    final String[] keys = {
        "0x4646464646464646464646464646464646464646464646464646464646464646"
    };

    final String msg = "0xdaf5a779ae972f972197303d7b574746c7ef83eadac0f2791ad23db92e4c8e53";
    final BigInteger r = new BigInteger("18515461264373351373200002665853028612451056578545711640558177340181847433846");
    final BigInteger s = new BigInteger("46948507304638947509940763649030358759909902576025900602547168820602576006531");

    final ECDSASigner signer = new TestOnlyECDSASigner(keys);

    final String addr = KeyUtils.getChecksumAddressFromPrivateKey(keys[0]);
    final Pair<BigInteger, BigInteger> rs = signer.sign(
        Hex.decodeHex(StringUtils.removeStart(msg, "0x")), addr);

    Assertions.assertEquals(r, rs.getLeft());
    Assertions.assertEquals(s, rs.getRight());

    logger.info("addr : {}", addr);
    logger.info("r    : {}", rs.getLeft());
    logger.info("s    : {}", rs.getRight());

  }

}

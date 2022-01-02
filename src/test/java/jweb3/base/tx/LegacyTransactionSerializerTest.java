package jweb3.base.tx;

import java.math.BigInteger;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LegacyTransactionSerializerTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private LegacyTransactionSerializer testee = new LegacyTransactionSerializer();


  @Test
  @DisplayName("Test transaction serialization exampled in EIP-155 documentation")
  void testSerialize1(){
    // https://eips.ethereum.org/EIPS/eip-155

    // nonce = 9, gasprice = 20 * 10**9, startgas = 21000,
    // to = 0x3535353535353535353535353535353535353535, value = 10**18, data='' (empty).
    final BigInteger nonce = BigInteger.valueOf(9);
    final BigInteger gasPrice = BigInteger.TEN.pow(9).multiply(BigInteger.valueOf(20));
    final BigInteger gasLimit = BigInteger.valueOf(21000);
    final String to = "0x3535353535353535353535353535353535353535";
    final BigInteger value = BigInteger.TEN.pow(18);
    final String data = "";
    final long chainId = 1;
    final LegacyTransaction tx = new LegacyTransaction(nonce, gasPrice, gasLimit, to, value, data, chainId);
    final String expected = "0xec098504a817c800825208943535353535353535353535353535353535353535880de0b6b3a764000080018080";

    final byte[] serialized = testee.serialize(tx);

    this.logger.info(Hex.encodeHexString(serialized));
    Assertions.assertEquals(expected.substring(2), Hex.encodeHexString(serialized));
  }

}

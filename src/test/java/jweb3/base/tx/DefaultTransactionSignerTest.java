package jweb3.base.tx;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jweb3.util.KeyUtils;

class DefaultTransactionSignerTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  void testSign1(){

    final String[] keys = {
        "0x4646464646464646464646464646464646464646464646464646464646464646"
    };

    final String[] addrs = {
        KeyUtils.getChecksumAddressFromPrivateKey(keys[0]),
    };

    final ECDSASigner ecSigner = new TestOnlyECDSASigner(keys);
    final TransactionSigner txSigner = new DefaultTransactionSigner(ecSigner);

    final Transaction tx = new LegacyTransaction(
      BigInteger.valueOf(9),
      BigInteger.valueOf(20).multiply(BigInteger.TEN.pow(9)), // 20 * 10**9
      BigInteger.valueOf(21000),
      "0x3535353535353535353535353535353535353535",
      BigInteger.TEN.pow(18), // 10**18
      1);

    txSigner.sign(tx, addrs[0]);


  }

}

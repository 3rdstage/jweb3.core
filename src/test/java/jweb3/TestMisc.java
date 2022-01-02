package jweb3;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMisc{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  public void testSecp256k1() {
    final X9ECParameters ecParams = CustomNamedCurves.getByName("secp256k1");

    Assertions.assertNull(ecParams.getSeed());

    logger.info("Secp256k1 : ");
    logger.info("  G : {}", ecParams.getG().toString());
    logger.info("  N : {}", ecParams.getN());
    logger.info("  H : {}", ecParams.getH());
  }

}

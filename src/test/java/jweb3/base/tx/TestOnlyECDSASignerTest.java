package jweb3.base.tx;

import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class TestOnlyECDSASignerTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private TestOnlyECDSASigner testee = null;

  @BeforeAll
  public void beforeAll() throws Exception{

    final File jsonFile = new File(ClassLoader.getSystemResource("jweb3/base/util/sample-keypairs-1.json").toURI());
    this.testee = new TestOnlyECDSASigner(jsonFile);
  }


  @Test
  void testSign1(){


    fail("Not yet implemented");
  }

}

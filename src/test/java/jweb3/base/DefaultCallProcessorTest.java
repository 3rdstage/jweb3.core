package jweb3.base;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;

class DefaultCallProcessorTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private Web3j web3j = new TestEthClientProvider().localClient();

  @Test
  void testProcess1(){

    this.logger.info(this.web3j.toString());

  }

}

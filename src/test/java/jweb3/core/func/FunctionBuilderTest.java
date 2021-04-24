package jweb3.core.func;

import java.math.BigInteger;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FunctionBuilderTest{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  void testFunctionSelectorWithSingleUintInputArgOnly(){

    final FunctionBuilder fb = new FunctionBuilder();
    fb.getRequestBuilder()
      .setName("foo")
      .addUintArg(256, BigInteger.valueOf(RandomUtils.nextLong()));

    final String encoded = fb.buildAndEncode();

    this.logger.info("Function : foo(uint256)(?)");
    this.logger.info("Function selector : {}", encoded.substring(0, 10));
    this.logger.info("Encoded function : {}", encoded);

    // Note that : web3.eth.abi.encodeFunctionSignature('foo(uint256)') == '0x2fbebd38'
    Assertions.assertEquals("0x2fbebd38", encoded.substring(0, 10));

  }

  @Test
  void testFunctionSelectorWithTwoUintArrayArgs() {

    final FunctionBuilder fb = new FunctionBuilder();
    fb.getRequestBuilder()
      .setName("foo")
      .addUintArrayArg(8, new BigInteger[] {BigInteger.ONE, BigInteger.TWO})
      .addUintArrayArg(256,
          new BigInteger[] {BigInteger.valueOf(RandomUtils.nextLong()), BigInteger.valueOf(RandomUtils.nextLong())});

    final String encoded = fb.buildAndEncode();

    this.logger.info("Function : {}(uint8[],uint256[])({?,?},{?,?})", fb.getRequestBuilder().getName());
    this.logger.info("Function selector : {}", encoded.substring(0, 10));
    this.logger.info("Encoded function : {}", encoded);

    // Note that : web3.eth.abi.encodeFunctionSignature('foo(uint8[],uint256[])') == '0x6cd57036'
    Assertions.assertEquals("0x6cd57036", encoded.substring(0, 10));

    final FunctionBuilder fb2 = new FunctionBuilder();
    fb2.getRequestBuilder()
      .setName("bar")
      .addUintArrayArg(256,
          BigInteger.valueOf(RandomUtils.nextLong()), BigInteger.valueOf(RandomUtils.nextLong()))
      .addUintArrayArg(256,
          BigInteger.valueOf(RandomUtils.nextLong()), BigInteger.valueOf(RandomUtils.nextLong()));

    final String encoded2 = fb2.buildAndEncode();

    this.logger.info("Function : {}(uint256[],uint256[])({?,?},{?,?})", fb2.getRequestBuilder().getName());
    this.logger.info("Function selector : {}", encoded2.substring(0, 10));
    this.logger.info("Encoded function : {}", encoded2);

    // Note that : web3.eth.abi.encodeFunctionSignature('bar(uint256[],uint256[])') == '0xd3ca0cb6'
    Assertions.assertEquals("0xd3ca0cb6", encoded2.substring(0, 10));

  }
}

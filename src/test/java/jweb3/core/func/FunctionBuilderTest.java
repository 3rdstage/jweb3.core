package jweb3.core.func;

import java.math.BigInteger;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Hash;
import jweb3.util.RandomGenerator;

class FunctionBuilderTest{

  private Logger logger = LoggerFactory.getLogger(this.getClass());


  private void verifyFunctionSelector(@Nonnull final Function fnct, @NotBlank final String sig) {

    final String encoded = fnct.encode();
    final String sltr = Hash.sha3String(sig).substring(0, 10);

    Assertions.assertEquals(sltr, encoded.substring(0, 10));

    this.logger.info("Verified Function Selector: ");
    this.logger.info("  Function Signature: {}", sig);
    this.logger.info("  Expected function selector: {}", sltr);
    this.logger.info("  Computed function selector: {}", encoded.substring(0, 10));
    this.logger.info("  Encoded function : {}", encoded);
  }


  @Test
  public void testFunctionSelectorWithSingleUintInputArgOnly(){

    final FunctionBuilder fb = new FunctionBuilder();
    fb.getInputBuilder()
      .setName("foo")
      .addUintArg(256, BigInteger.valueOf(RandomUtils.nextLong()));

    // web3.eth.abi.encodeFunctionSignature('foo(uint256)') == '0x2fbebd38'
    this.verifyFunctionSelector(fb.build(), "foo(uint256)");
  }

  @Test
  public void testFunctionSelectorWithTwoUintArrayArgs() {

    final FunctionBuilder fb = new FunctionBuilder();
    fb.getInputBuilder()
      .setName("foo")
      .addUintArrayArg(8, RandomGenerator.uintArray(8, 2))
      .addUintArrayArg(256, RandomGenerator.uintArray(256, 3));

    // web3.eth.abi.encodeFunctionSignature('foo(uint8[],uint256[])') == '0x6cd57036'
    this.verifyFunctionSelector(fb.build(), "foo(uint8[],uint256[])");

    final FunctionBuilder fb2 = new FunctionBuilder();
    fb2.getInputBuilder()
      .setName("bar")
      .addUintArrayArg(256, RandomGenerator.uintArray(256, 2))
      .addUintArrayArg(256, RandomGenerator.uintArray(256, 5));

    this.verifyFunctionSelector(fb2.build(), "bar(uint256[],uint256[])");

  }

  @Test
  public void testFunctionSelectorWithIntArrayArgs() {

    final FunctionBuilder fb = new FunctionBuilder();
    fb.getInputBuilder()
      .setName("foo")
      .addIntArrayArg(8, RandomGenerator.intArray(8, 3))
      .addIntArrayArg(128, RandomGenerator.intArray(128, 5));

    this.verifyFunctionSelector(fb.build(), "foo(int8[],int128[])");

    final FunctionBuilder fb2 = new FunctionBuilder();
    fb2.getInputBuilder()
      .setName("bar")
      .addIntArrayArg(256, RandomGenerator.intArray(256, 3))
      .addIntArrayArg(256, RandomGenerator.intArray(256, 5))
      .addIntArrayArg(128,  RandomGenerator.intArray(128, 7));

    this.verifyFunctionSelector(fb2.build(), "bar(int256[],int256[],int128[])");
  }

  @Test
  public void testFunctionSelectorWithAddressArrayArgs() {

    final FunctionBuilder fb = new FunctionBuilder();
    fb.getInputBuilder()
      .setName("foo")
      .addAddressArrayArg(RandomGenerator.addressArray(3))
      .addAddressArrayArg(RandomGenerator.addressArray(5));

    this.verifyFunctionSelector(fb.build(), "foo(address[],address[])");
  }

  @Test
  public void testFunctionSelectorWithStaticBytesArgs() {

    final FunctionBuilder fb = new FunctionBuilder();
    fb.getInputBuilder().setName("bar")
      .addStaticBytesArg(2, RandomUtils.nextBytes(2))
      .addStaticBytesArg(12, RandomUtils.nextBytes(12))
      .addStaticBytesArg(26, RandomUtils.nextBytes(26))
      .addStaticBytesArg(32, RandomUtils.nextBytes(32));

    this.verifyFunctionSelector(fb.build(), "bar(bytes2,bytes12,bytes26,bytes32)");
  }


}

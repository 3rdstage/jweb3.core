package jweb3.core.func2;

import java.math.BigInteger;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Hash;
import jweb3.core.func2.SampleStructs.DimensionsStruct;
import jweb3.core.func2.SampleStructs.PositionStruct;
import jweb3.util.RandomGenerator;

class FunctionCallBuilderTest{

  private Logger logger = LoggerFactory.getLogger(this.getClass());


  private void verifyFunctionSelector(@Nonnull final FunctionCall fnct, @NotBlank final String sig) {

    final String encoded = fnct.getEncoded();
    final String sltr = Hash.sha3String(sig).substring(0, 10);

    Assertions.assertEquals(sltr, encoded.substring(0, 10));

    this.logger.info("Verified Function Selector: ");
    this.logger.info("  Function Signature: {}", sig);
    this.logger.info("  Expected function selector: {}", sltr);
    this.logger.info("  Computed function selector: {}", encoded.substring(0, 10));
    this.logger.info("  Encoded function : {}", encoded);
  }


  @Test
  @DisplayName("Test function selector for a `uintN` type argument")
  public void testFunctionSelectorWithSingleUintArg() {

    final FunctionCallBuilder bldr = new FunctionCallBuilder();
    bldr.setName("foo")
      .addUintArg(256, BigInteger.valueOf(RandomUtils.nextLong()));

    // web3.eth.abi.encodeFunctionSignature('foo(uint256)') == '0x2fbebd38'
    this.verifyFunctionSelector(bldr.build(), "foo(uint256)");

  }

  @Test
  @DisplayName("Test function selector for two `uintN[]` type arguments.")
  public void testFunctionSelectorWithTwoUintArrayArgs() {

    final FunctionCallBuilder bldr =
        new FunctionCallBuilder().setName("foo")
          .addUintArrayArg(8, RandomGenerator.uintArray(8, 2))
          .addUintArrayArg(256, RandomGenerator.uintArray(256, 3));

    // web3.eth.abi.encodeFunctionSignature('foo(uint8[],uint256[])') == '0x6cd57036'
    this.verifyFunctionSelector(bldr.build(), "foo(uint8[],uint256[])");

    final FunctionCallBuilder bldr2 =
        new FunctionCallBuilder().setName("bar")
          .addUintArrayArg(256, RandomGenerator.uintArray(256, 2))
          .addUintArrayArg(256,  RandomGenerator.uintArray(256, 5));

    this.verifyFunctionSelector(bldr2.build(), "bar(uint256[],uint256[])");

  }

  @Test
  @DisplayName("Test function selector for `intN` type argument(s)")
  public void testFunctionSelectorWithIntArgs(){

    final FunctionCallBuilder bldr =
       new FunctionCallBuilder().setName("foo")
         .addIntArg(256, BigInteger.valueOf(RandomUtils.nextLong()));

    //  web3.eth.abi.encodeFunctionSignature('foo(int256)') == '0x4c970b2f'
    this.verifyFunctionSelector(bldr.build(), "foo(int256)");

    final FunctionCallBuilder bldr2 =
        new FunctionCallBuilder().setName("bar")
          .addIntArg(256, BigInteger.valueOf(RandomUtils.nextLong()))
          .addIntArg(8, RandomUtils.nextBoolean() ? BigInteger.ZERO : BigInteger.TEN)
          .addIntArg(128, BigInteger.valueOf(RandomUtils.nextLong()).negate())
          .addIntArg(16, BigInteger.valueOf(RandomUtils.nextLong(0, 255))
              .multiply(BigInteger.valueOf(RandomUtils.nextBoolean() ? 1 : -1)));

    // web3.eth.abi.encodeFunctionSignature('bar(int256,int8,int128,int16)') == '0xa12546af'
    this.verifyFunctionSelector(bldr2.build(), "bar(int256,int8,int128,int16)");

    final FunctionCallBuilder bldr3 =
        new FunctionCallBuilder().setName("baz")
          .addIntArg(8, RandomGenerator.intVal(8))
          .addIntArg(24, RandomGenerator.intVal(24))
          .addIntArg(48, RandomGenerator.intVal(48))
          .addIntArg(72, RandomGenerator.intVal(72))
          .addIntArg(144, RandomGenerator.intVal(144))
          .addIntArg(192, RandomGenerator.intVal(192))
          .addIntArg(232, RandomGenerator.intVal(232));

    // web3.eth.abi.encodeFunctionSignature('baz(int8,int24,int48,int72,int144,int192,int232)') == '0x66c10e17'
    this.verifyFunctionSelector(bldr3.build(), "baz(int8,int24,int48,int72,int144,int192,int232)");

  }

  @Test
  @DisplayName("Test function selector for `intN[]` type 33arguments")
  public void testFunctionSelectorWithIntArrayArgs() {

    final FunctionCallBuilder bldr =
        new FunctionCallBuilder().setName("foo")
          .addIntArrayArg(8, RandomGenerator.intArray(8, RandomUtils.nextInt(3, 9)))
          .addIntArrayArg(128,  RandomGenerator.intArray(128, RandomUtils.nextInt(5, 15)));

    // web3.eth.abi.encodeFunctionSignature('foo(int8[],int128[])') == '0xfe40cf5c'
    this.verifyFunctionSelector(bldr.build(), "foo(int8[],int128[])");

    final FunctionCallBuilder bldr2 =
        new FunctionCallBuilder().setName("bar")
          .addIntArrayArg(256, RandomGenerator.intArray(256, RandomUtils.nextInt(4, 12)))
          .addIntArrayArg(256, RandomGenerator.intArray(256, RandomUtils.nextInt(7, 21)))
          .addIntArrayArg(128, RandomGenerator.intArray(128, RandomUtils.nextInt(2, 10)));

    // web3.eth.abi.encodeFunctionSignature('bar(int256[],int256[],int128[])') == '0x0d3c0881'
    this.verifyFunctionSelector(bldr2.build(), "bar(int256[],int256[],int128[])");
  }


  @Test
  @DisplayName("Test function selector for `address` type argument(s).")
  public void testFunctionSelectorWithAddrArgs() {

    final FunctionCallBuilder bldr =
        new FunctionCallBuilder().setName("foo")
          .addAddressArg(RandomGenerator.address());

    // web3.eth.abi.encodeFunctionSignature('foo(address)') == '0xfdf80bda'
    this.verifyFunctionSelector(bldr.build(), "foo(address)");

    final FunctionCallBuilder bldr2 =
        new FunctionCallBuilder().setName("bar")
          .addAddressArg(RandomGenerator.address())
          .addAddressArg(RandomGenerator.address())
          .addAddressArg(RandomGenerator.address())
          .addAddressArg(RandomGenerator.address())
          .addAddressArg(RandomGenerator.address());

    // web3.eth.abi.encodeFunctionSignature('bar(address,address,address,address,address)') == '0x97f8a44c'
    this.verifyFunctionSelector(bldr2.build(), "bar(address,address,address,address,address)");
  }


  @Test
  @DisplayName("Test function selector for `address[]` type argument(s)")
  public void testFunction2SelectorWithAddrArrayArgs() {

    final FunctionCallBuilder bldr =
        new FunctionCallBuilder().setName("foo")
          .addAddressArrayArg(RandomGenerator.addressArray(3));

    // web3.eth.abi.encodeFunctionSignature('foo(address[])') == '0x13cb49d4'
    this.verifyFunctionSelector(bldr.build(), "foo(address[])");

    final FunctionCallBuilder bldr2 =
        new FunctionCallBuilder().setName("bar")
          .addAddressArrayArg(RandomGenerator.addressArray(3))
          .addAddressArrayArg(RandomGenerator.addressArray(5))
          .addAddressArrayArg(RandomGenerator.addressArray(2));

    // web3.eth.abi.encodeFunctionSignature('bar(address[],address[],address[])') == '0xca5a7b42'
    this.verifyFunctionSelector(bldr2.build(), "bar(address[],address[],address[])");

  }

  @Test
  @DisplayName("Test function selector for `bytesN` type argument(s)")
  public void testFunctionSelectorForStaticBytesArgs() {

    final FunctionCallBuilder bldr =
        new FunctionCallBuilder().setName("foo")
          .addStaticBytesArg(2, RandomGenerator.staticBytes(2));

    // web3.eth.abi.encodeFunctionSignature('foo(bytes2)') == '0x1ef605e8'
    this.verifyFunctionSelector(bldr.build(), "foo(bytes2)");

    final FunctionCallBuilder bldr2 =
        new FunctionCallBuilder().setName("bar")
          .addStaticBytesArg(31, RandomGenerator.staticBytes(31));

    // web3.eth.abi.encodeFunctionSignature('bar(bytes31)') == '0xa87c82a3'
    this.verifyFunctionSelector(bldr2.build(), "bar(bytes31)");

    final FunctionCallBuilder bldr3 =
        new FunctionCallBuilder().setName("baz")
          .addStaticBytesArg(3, RandomGenerator.staticBytes(3))
          .addStaticBytesArg(9, RandomGenerator.staticBytes(9))
          .addStaticBytesArg(15, RandomGenerator.staticBytes(15))
          .addStaticBytesArg(21, RandomGenerator.staticBytes(21))
          .addStaticBytesArg(30, RandomGenerator.staticBytes(30));

    // web3.eth.abi.encodeFunctionSignature('baz(bytes3,bytes9,bytes15,bytes21,bytes30)') == '0x3c1ab5d9'
    this.verifyFunctionSelector(bldr3.build(), "baz(bytes3,bytes9,bytes15,bytes21,bytes30)");

    final FunctionCallBuilder bldr4 =
        new FunctionCallBuilder().setName("quux")
          .addStaticBytesArg(1, RandomGenerator.staticBytes(1))
          .addStaticBytesArg(16, RandomGenerator.staticBytes(16))
          .addStaticBytesArg(32, RandomGenerator.staticBytes(32));

    // web3.eth.abi.encodeFunctionSignature('quux(bytes1,bytes16,bytes32)') == '0x1a6c2167'
    this.verifyFunctionSelector(bldr4.build(), "quux(bytes1,bytes16,bytes32)");
  }


  @Test
  @DisplayName("Test function selector for `bytesN[]` type argument(s)")
  public void testFunctionSelectorWithStaticBytesArrayArgs() {

    final FunctionCallBuilder bldr =
        new FunctionCallBuilder().setName("foo")
        .addStaticBytesArrayArg(16, RandomGenerator.staticBytesArray(16, 3));

    // web3.eth.abi.encodeFunctionSignature('foo(bytes16[])') == '0x27c56191'
    this.verifyFunctionSelector(bldr.build(), "foo(bytes16[])");
  }


  @Test
  @DisplayName("Test function selector for `struct` type arguement(s) including only static type fields")
  public void testFunctionSelectorWithStaticStructArgs() {

    final FunctionCallBuilder bldr =
        new FunctionCallBuilder().setName("foo")
        .addStructArg(new PositionStruct(100, -100));

    // web3.eth.abi.encodeFunctionSignature('foo((int256,int256))') == '0x6a39f104'
    this.verifyFunctionSelector(bldr.build(), "foo((int256,int256))");


    final FunctionCallBuilder bldr2 =
        new FunctionCallBuilder().setName("bar")
        .addStructArg(new DimensionsStruct(RandomUtils.nextLong(), RandomUtils.nextLong(), RandomUtils.nextLong()));

    // web3.eth.abi.encodeFunctionSignature('bar((uint256,uint256,uint256))') == '0x44aed92e'
    this.verifyFunctionSelector(bldr2.build(), "bar((uint256,uint256,uint256))");

  }


}

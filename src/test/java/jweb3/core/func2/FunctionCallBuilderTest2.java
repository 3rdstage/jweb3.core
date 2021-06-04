package jweb3.core.func2;

import javax.validation.constraints.NotBlank;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionCallBuilderTest2{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private void verifyEncodedFunctionCall(final String str, final FunctionCallBuilder bldr,
      @NotBlank final String expectedSelector, @NotBlank final String expectedEncodedParams) {

    final String actual = bldr.build().getEncoded();

    Assertions.assertEquals(expectedSelector + expectedEncodedParams.substring(2), actual);
    this.logger.info("Verified Function Encoding : {}\n{}", str, actual);
  }

  @Test
  @DisplayName("Test ABI encoding of a funcation with a single `uintN` type argument.")
  public void testEncodeFunctionCallWithSingleUintArg() {

    final FunctionCallBuilder bldr1 = new FunctionCallBuilder()
        .setName("foo")
        .addUintArg(256, 3_000_000L);

    // web3.eth.abi.encodeFunctionSignature('foo(uint256)')
    final String slct1 = "0x2fbebd38";
    // web3.eth.abi.encodeParameters(['uint256'],[3e6])
    final String params1 = "0x00000000000000000000000000000000000000000000000000000000002dc6c0";

    this.verifyEncodedFunctionCall("foo(uint256)(3000000)", bldr1, slct1, params1);

    final FunctionCallBuilder bldr2 = new FunctionCallBuilder()
        .setName("bar")
        .addUintArg(144, 255_000_000);

    // web3.eth.abi.encodeFunctionSignature('bar(uint144)')
    final String slct2= "0x1e15f6d3";
    // web3.eth.abi.encodeParameters(['uint144'],[255e6])
    final String params2 = "0x000000000000000000000000000000000000000000000000000000000f32fdc0";

    this.verifyEncodedFunctionCall("bar(uint144)(255000000)", bldr2, slct2, params2);

  }

  @Test
  @DisplayName("Test ABI encoding of a funcation with two `uintN` array type arguments.")
  public void testEncodeFunctionCallWithTwoUintArrayArgs() {

    final FunctionCallBuilder bldr1 =
        new FunctionCallBuilder().setName("foo")
        .addUintArrayArg(8, 127, 203)
        .addUintArrayArg(256, 100_000_000, 1_780_000_000);

    // web3.eth.abi.encodeFunctionSignature('foo(uint8[],uint256[])')
    final String slct1 = "0x6cd57036";
    // web3.eth.abi.encodeParameters(['uint8[]','uint256[]'],[[127,203],[1e8,178e7]])
    final String params1 = "0x0000000000000000000000000000000000000000000000000000000000000040"
        + "00000000000000000000000000000000000000000000000000000000000000a0"
        + "0000000000000000000000000000000000000000000000000000000000000002"
        + "000000000000000000000000000000000000000000000000000000000000007f"
        + "00000000000000000000000000000000000000000000000000000000000000cb"
        + "0000000000000000000000000000000000000000000000000000000000000002"
        + "0000000000000000000000000000000000000000000000000000000005f5e100"
        + "000000000000000000000000000000000000000000000000000000006a18a500";

    this.verifyEncodedFunctionCall("foo(uint8[],uint256[])([127,203],[1e8,178e7])", bldr1, slct1, params1);

    final FunctionCallBuilder bldr2 =
        new FunctionCallBuilder().setName("bar")
        .addUintArrayArg(256, 0, 30_000_000_000_000L)
        .addUintArrayArg(256, 1, 287_000_000_000L, 31, 20_210_602, 126);

    // web3.eth.abi.encodeFunctionSignature('bar(uint256[],uint256[])')
    final String slct2 = "0xd3ca0cb6";
    // web3.eth.abi.encodeParameters(['uint256[]','uint256[]'],[[0,3e13],[1,287e9,31,20210602,126]])
    final String params2 = "0x0000000000000000000000000000000000000000000000000000000000000040"
        + "00000000000000000000000000000000000000000000000000000000000000a0"
        + "0000000000000000000000000000000000000000000000000000000000000002"
        + "0000000000000000000000000000000000000000000000000000000000000000"
        + "00000000000000000000000000000000000000000000000000001b48eb57e000"
        + "0000000000000000000000000000000000000000000000000000000000000005"
        + "0000000000000000000000000000000000000000000000000000000000000001"
        + "00000000000000000000000000000000000000000000000000000042d2887600"
        + "000000000000000000000000000000000000000000000000000000000000001f"
        + "00000000000000000000000000000000000000000000000000000000013463aa"
        + "000000000000000000000000000000000000000000000000000000000000007e";

    this.verifyEncodedFunctionCall("bar(uint256[],uint256[])([0,3e13],[1,287e9,31,20210602,126])",
        bldr2, slct2, params2);

  }

}

package jweb3.core.func2;

import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;


@SuppressWarnings({"rawtypes", "unchecked"})
class OutputSignatureBuilderTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  public void testAddUintArrayParam() {

    OutputSignatureBuilder bldr = new OutputSignatureBuilder().addUintArrayParam();

    // web3.eth.abi.encodeParameters(['uint256[]'], [[10,20]])
    String encoded = "0x0000000000000000000000000000000000000000000000000000000000000020"
        + "0000000000000000000000000000000000000000000000000000000000000002"
        + "000000000000000000000000000000000000000000000000000000000000000a"
        + "0000000000000000000000000000000000000000000000000000000000000014";

    List<Type> output = FunctionReturnDecoder.decode(encoded, bldr.build().getParams());
    List<Uint256> output0 = ((DynamicArray<Uint256>)output.get(0)).getValue();

    Assertions.assertEquals(2, output0.size());
    Assertions.assertEquals(BigInteger.valueOf(10), output0.get(0).getValue());
    Assertions.assertEquals(BigInteger.valueOf(20), output0.get(1).getValue());

    // web3.eth.abi.encodeParameters(['uint256[]'], [[777888,3e9,31,0]])
    encoded = "0x0000000000000000000000000000000000000000000000000000000000000020"
        + "0000000000000000000000000000000000000000000000000000000000000004"
        + "00000000000000000000000000000000000000000000000000000000000bdea0"
        + "00000000000000000000000000000000000000000000000000000000b2d05e00"
        + "000000000000000000000000000000000000000000000000000000000000001f"
        + "0000000000000000000000000000000000000000000000000000000000000000";

    output = FunctionReturnDecoder.decode(encoded, bldr.build().getParams());
    output0 = ((DynamicArray<Uint256>)output.get(0)).getValue();

    Assertions.assertEquals(4, output0.size());
    Assertions.assertEquals(BigInteger.valueOf(777888), output0.get(0).getValue());
    Assertions.assertEquals(BigInteger.valueOf(3_000_000_000L), output0.get(1).getValue());
    Assertions.assertEquals(BigInteger.valueOf(31), output0.get(2).getValue());
    Assertions.assertEquals(BigInteger.ZERO, output0.get(3).getValue());

  }
}

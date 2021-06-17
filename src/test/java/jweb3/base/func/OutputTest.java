package jweb3.base.func;

import java.math.BigInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionReturnDecoder;
import jweb3.base.func.Output;
import jweb3.base.func.OutputSignatureBuilder;

@SuppressWarnings({"rawtypes", "unchecked"})
public class OutputTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  @Test
  public void testGetInt() {

    OutputSignatureBuilder bldr = new OutputSignatureBuilder().addUintParam(128);

    // web3.eth.abi.encodeParameters(['uint128'], [20210616])
    String encoded = "0x00000000000000000000000000000000000000000000000000000000013463b8";

    Output out = new Output(FunctionReturnDecoder.decode(encoded,  bldr.build().getParams()));
    Assertions.assertEquals(BigInteger.valueOf(20210616), out.getInt(0));

    bldr = new OutputSignatureBuilder().addUintParam(8).addUintParam(256).addUintParam();

    // web3.eth.abi.encodeParameters(['uint8','uint256','uint256'],[31,19910525,2e10])
    encoded = "0x000000000000000000000000000000000000000000000000000000000000001f"
        + "00000000000000000000000000000000000000000000000000000000012fcf7d"
        + "00000000000000000000000000000000000000000000000000000004a817c800";

    out = new Output(FunctionReturnDecoder.decode(encoded,  bldr.build().getParams()));
    Assertions.assertEquals(BigInteger.valueOf(31), out.getInt(0));
    Assertions.assertEquals(BigInteger.valueOf(19910525), out.getInt(1));
    Assertions.assertEquals(BigInteger.valueOf((long)2e10), out.getInt(2));


    bldr = new OutputSignatureBuilder().addIntParam(144);

    // web3.eth.abi.encodeParameters(['int144'],[-6400])
    encoded = "0xffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe700";

    out = new Output(FunctionReturnDecoder.decode(encoded,  bldr.build().getParams()));
    Assertions.assertEquals(BigInteger.valueOf(-6400), out.getInt(0));

    // web3.eth.abi.encodeParameters(['int144'],[3e5])
    encoded = "0x00000000000000000000000000000000000000000000000000000000000493e0";

    out = new Output(FunctionReturnDecoder.decode(encoded,  bldr.build().getParams()));
    Assertions.assertEquals(BigInteger.valueOf(300_000), out.getInt(0));

    // web3.eth.abi.encodeParameters(['int144'],[-3141529])
    encoded = "0xffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd01067";
    out = new Output(FunctionReturnDecoder.decode(encoded,  bldr.build().getParams()));
    Assertions.assertEquals(BigInteger.valueOf(-3_141_529), out.getInt(0));


    bldr = new OutputSignatureBuilder().addIntParam(8).addIntParam(128).addIntParam(256);

    // web3.eth.abi.encodeParameters(['int8','int128','int256'],[-10,19910525,-7e11])
    encoded = "0xfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff6"
        + "00000000000000000000000000000000000000000000000000000000012fcf7d"
        + "ffffffffffffffffffffffffffffffffffffffffffffffffffffff5d04bfa800";

    out = new Output(FunctionReturnDecoder.decode(encoded,  bldr.build().getParams()));
    Assertions.assertEquals(BigInteger.valueOf(-10), out.getInt(0));
    Assertions.assertEquals(BigInteger.valueOf(19_910_525), out.getInt(1));
    Assertions.assertEquals(BigInteger.valueOf((long)(-7e11)), out.getInt(2));


    bldr = new OutputSignatureBuilder().addUintParam(16).addIntParam(192).addUintParam(64).addIntParam();

    // web3.eth.abi.encodeParameters(['uint16','int192','uint64','int256'],[0,-1101401,303031,-9e15])
    encoded = "0x0000000000000000000000000000000000000000000000000000000000000000"
        + "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffef31a7"
        + "0000000000000000000000000000000000000000000000000000000000049fb7"
        + "ffffffffffffffffffffffffffffffffffffffffffffffffffe0068c35058000";

    out = new Output(FunctionReturnDecoder.decode(encoded,  bldr.build().getParams()));
    Assertions.assertEquals(BigInteger.ZERO, out.getInt(0));
    Assertions.assertEquals(BigInteger.valueOf(-1_101_401), out.getInt(1));
    Assertions.assertEquals(BigInteger.valueOf(303_031), out.getInt(2));
    Assertions.assertEquals(BigInteger.valueOf((long)(-9e15)), out.getInt(3));

  }



}

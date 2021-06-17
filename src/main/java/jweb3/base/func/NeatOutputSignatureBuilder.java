package jweb3.base.func;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.web3j.abi.datatypes.StructType;
import org.web3j.abi.datatypes.Type;

public class NeatOutputSignatureBuilder{


  private final OutputSignatureBuilder bldr = new OutputSignatureBuilder();

  public OutputSignature build() {
    return this.bldr.build();
  }

  public NeatOutputSignatureBuilder uint(@Min(8) @Max(256) final int bitSize) {

    this.bldr.addUintParam(bitSize);
    return this;
  }

  public NeatOutputSignatureBuilder uint() {

    this.bldr.addUintParam();
    return this;
  }

  public NeatOutputSignatureBuilder uintArray(@Min(8) @Max(256) final int bitSize) {

    this.bldr.addUintArrayParam(bitSize);
    return this;
  }

  public NeatOutputSignatureBuilder uintArray() {

    this.bldr.addUintArrayParam();
    return this;
  }

  public NeatOutputSignatureBuilder sint(@Min(8) @Max(256) final int bitSize) {

    this.bldr.addIntParam(bitSize);
    return this;
  }

  public NeatOutputSignatureBuilder sint() {

    this.bldr.addIntParam();
    return this;
  }

  public NeatOutputSignatureBuilder sintArray(@Min(8) @Max(256) final int bitSize) {

    this.bldr.addIntArrayParam(bitSize);
    return this;
  }

  public NeatOutputSignatureBuilder sintArray() {

    this.bldr.addIntArrayParam();
    return this;
  }


  public NeatOutputSignatureBuilder address() {

    this.bldr.addAddressParam();
    return this;
  }

  public NeatOutputSignatureBuilder addressArray() {

    this.bldr.addAddressArrayParam();
    return this;
  }

  public NeatOutputSignatureBuilder bool() {

    this.bldr.addBoolParam();
    return this;
  }


  public NeatOutputSignatureBuilder boolArray() {

    this.bldr.addBoolArrayParam();
    return this;
  }


  public NeatOutputSignatureBuilder staticBytes(@Min(1) @Max(8) final int size) {

    this.bldr.addStaticBytesParam(size);
    return this;
  }


  public NeatOutputSignatureBuilder staticBytesArray(@Min(1) @Max(8) final int size) {

    this.bldr.addStaticBytesArrayParam(size);
    return this;
  }


  public NeatOutputSignatureBuilder bytes() {

    this.bldr.addBytesParam();
    return this;
  }


  public NeatOutputSignatureBuilder bytesArray(){

    this.bldr.addBytesArrayParam();
    return this;
  }


  public NeatOutputSignatureBuilder string() {

    this.bldr.addStringParam();
    return this;
  }

  public NeatOutputSignatureBuilder stringArray() {

    this.bldr.addStringArrayParam();
    return this;
  }

  public <T extends StructType & Type> NeatOutputSignatureBuilder struct(Class<T> structType) {

    this.bldr.addStructParam(structType);
    return this;
  }

  public <T extends StructType & Type> NeatOutputSignatureBuilder structArray(Class<T> structType) {

    this.bldr.addStructArrayParam(structType);
    return this;
  }

}

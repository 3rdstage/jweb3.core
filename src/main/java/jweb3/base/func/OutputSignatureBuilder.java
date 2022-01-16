package jweb3.base.func;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.concurrent.NotThreadSafe;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.StructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint256;
import jweb3.base.AbiAware;

@NotThreadSafe
public class OutputSignatureBuilder implements AbiAware{

  @SuppressWarnings("rawtypes")
  private final List<TypeReference<? extends Type>> params = new ArrayList<>();


  public OutputSignature build() {
    return new OutputSignature(this.params);
  }

  @SuppressWarnings("unchecked")
  public void appendParam(@NotBlank final String solidityType) {

    try {
      this.params.add(TypeReference.makeTypeReference(solidityType));
    }catch(RuntimeException ex) {
      throw ex;
    }catch(Throwable th) {
      throw new RuntimeException(th);
    }
  }

  /**
   * Append a {@code int<M>} ABI type parameter to current signature
   *
   * @param bitSize
   * @return
   */
  public OutputSignatureBuilder addUintParam(@Min(8) @Max(256) final int bitSize) {

    this.validateIntBitSize(bitSize);
    this.appendParam("uint" + bitSize);
    return this;
  }

  /**
   * Append a {@code uint256} ABI type parameter to current signature
   *
   * @return
   */
  public OutputSignatureBuilder addUintParam() {

    this.params.add(new TypeReference<Uint256>() {});
    return this;
  }


  /**
   * Append 1-dimensional array of {@code uint<M>} ABI type parameter to current signature
   *
   * @param bitSize
   * @return
   */
  public OutputSignatureBuilder addUintArrayParam(@Min(8) @Max(256) final int bitSize) {

    this.validateIntBitSize(bitSize);
    this.appendParam("uint" + bitSize + "[]");
    return this;
  }


  /**
   * Append 1-dimensional array of {@code uint256} ABI type parameter to current signature
   *
   * @return
   */
  public OutputSignatureBuilder addUintArrayParam() {

    this.params.add(new TypeReference<DynamicArray<Uint256>>() {});
    return this;
  }


  /**
   * Append a multi-dimensional array of {@code uint<M>} ABI type parameter to current signature
   *
   * @param bitSize
   * @param dimension
   * @return
   */
  public OutputSignatureBuilder addUintArrayParam(@Min(8) @Max(256) final int bitSize, @Positive final int dimension) {

    if(dimension < 1) throw new IllegalArgumentException("Array dimension should be positive.");

    final StringBuilder type = new StringBuilder("uint").append(bitSize);
    for(int i = 0; i < dimension; i++) { type.append("[]"); }

    this.appendParam(type.toString());
    return this;
  }


  /**
   * Append a {@code int<M>} ABI type parameter to current signature
   *
   * @param bitSize
   * @return
   */
  public OutputSignatureBuilder addIntParam(@Min(8) @Max(256) final int bitSize) {

    this.validateIntBitSize(bitSize);
    this.appendParam("int" + bitSize);
    return this;
  }

  /**
   * Append a {@code int256} ABI type parameter to current signature
   *
   * @return
   */
  public OutputSignatureBuilder addIntParam() {

    this.params.add(new TypeReference<Int256>() {});
    return this;
  }

  /**
   * Append 1-dimensional array of {@code int<M>} ABI type parameter to current signature
   *
   * @param bitSize
   * @return
   */
  public OutputSignatureBuilder addIntArrayParam(@Min(8) @Max(256) final int bitSize) {

    this.validateIntBitSize(bitSize);
    this.appendParam("int" + bitSize + "[]");
    return this;
  }


  /**
   * Append 1-dimensional array of {@code int256} ABI type parameter to current signature
   *
   * @return
   */
  public OutputSignatureBuilder addIntArrayParam() {

    this.params.add(new TypeReference<DynamicArray<Int256>>() {});
    return this;
  }


  public OutputSignatureBuilder addAddressParam() {

    this.params.add(new TypeReference<Address>() {});
    return this;
  }


  /**
   * Append 1-dimensional array of {@code address} ABI type parameter to current signature
   *
   * @return
   */
  public OutputSignatureBuilder addAddressArrayParam() {

    this.params.add(new TypeReference<DynamicArray<Address>>() {});
    return this;
  }

  /**
   * Append a multi-dimensional array of {@code address} ABI type parameter to current signature
   *
   * @param dimension
   * @return
   */
  public OutputSignatureBuilder addAddressArrayParam(@Positive final int dimension) {

    if(dimension < 1) throw new IllegalArgumentException("Array dimension should be positive.");

    final StringBuilder type = new StringBuilder("address");
    for(int i = 0; i < dimension; i++) { type.append("[]"); }

    this.appendParam(type.toString());
    return this;
  }


  public OutputSignatureBuilder addBoolParam() {

    this.params.add(new TypeReference<Bool>() {});
    return this;
  }


  public OutputSignatureBuilder addBoolArrayParam() {

    this.params.add(new TypeReference<DynamicArray<Bool>>() {});
    return this;
  }


  public OutputSignatureBuilder addStaticBytesParam(@Min(1) @Max(8) final int size) {

    this.validateStaticBytesLength(size);
    this.appendParam("bytes" + size);
    return this;
  }


  public OutputSignatureBuilder addStaticBytesArrayParam(@Min(1) @Max(8) final int size) {

    this.validateStaticBytesLength(size);
    this.appendParam("bytes" + size + "[]");
    return this;
  }


  public OutputSignatureBuilder addBytesParam() {

    this.params.add(new TypeReference<DynamicBytes>() {});
    return this;
  }


  public OutputSignatureBuilder addBytesArrayParam() {

    this.params.add(new TypeReference<DynamicArray<DynamicBytes>>() {});
    return this;
  }


  public OutputSignatureBuilder addStringParam() {

    this.params.add(new TypeReference<Utf8String>() {});
    return this;
  }


  public OutputSignatureBuilder addStringArrayParam() {

    this.params.add(new TypeReference<DynamicArray<Utf8String>>() {});
    return this;
  }


  public <T extends StructType & Type> OutputSignatureBuilder addStructParam(Class<T> structType) {

    //@TODO Caching `TypeReference`s for struct types

    this.params.add(TypeReference.create(structType));
    return this;

  }

  public <T extends StructType & Type> OutputSignatureBuilder addStructArrayParam(Class<T> structType) {

    //@TODO Caching `TypeReference`s for dynamic array of struct types.

    this.params.add(this.createDynamicArrayReference(structType));
    return this;
  }


}

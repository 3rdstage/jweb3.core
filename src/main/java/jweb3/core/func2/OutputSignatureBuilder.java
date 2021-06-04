package jweb3.core.func2;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.StructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import jweb3.core.AbiAware;

public class OutputSignatureBuilder implements AbiAware{

  @SuppressWarnings("rawtypes")
  private final List<TypeReference<? extends Type>> params = new ArrayList<>();


  public OutputSignature build() {
    return new OutputSignature(this.params);
  }

  @SuppressWarnings("unchecked")
  public void appendParam(@Nonnull @NotBlank final String solidityType) {

    try {
      this.params.add(TypeReference.makeTypeReference(solidityType));
    }catch(RuntimeException ex) {
      throw ex;
    }catch(Throwable th) {
      throw new RuntimeException(th);
    }
  }

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


  public <T extends StructType & Type> OutputSignatureBuilder addStructParam(Class<T> structType) {

    //@TODO Caching `TypeReference`s for struct types

    this.params.add(TypeReference.create(structType));
    return this;

  }

  public <T extends StructType & Type> OutputSignatureBuilder addStructArrayParam(Class<T> structType) {

    //@TODO Caching `TypeReference`s for dynamic array of struct types.

    this.params.add(createDynamicArrayReference(structType));
    return this;
  }





}

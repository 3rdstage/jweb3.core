package jweb3.base;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;

/**
 * @author Sangmoon Oh
 * @since 2021-05-12
 */
public interface AbiAware{

  default void validateIntBitSize(final int size) {

    if(size < 8 || size > 256 || size%8 != 0) {
      throw new IllegalArgumentException(
          "Bit size of uint/int type should be multiple of 8 between 8 and 256.");
    }
  }

  default void validateStaticBytesLength(final int len) {

    if(len < 1 || len > 32) {
      throw new IllegalArgumentException(
          "Length of static(fixed-size) bytes should be between 1 and 32.");
    }

  }

  default void validateAddressString(final String str) {

    if(str == null || !str.startsWith("0x")) {
      throw new IllegalArgumentException("The string is not in Ethereum address format.");
    }
  }

  default <T extends Type> TypeReference<DynamicArray<T>> createDynamicArrayReference(Class<T> elmntType){

    return new TypeReference<DynamicArray<T>>() {
      public java.lang.reflect.Type getType(){
        return TypeUtils.parameterize(DynamicArray.class, elmntType);
      }
    };
  }

}

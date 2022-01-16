package jweb3.base.func;

import java.math.BigInteger;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.IntType;
import org.web3j.abi.datatypes.Type;


@Immutable @ThreadSafe
public class Output{

  // @TODO Replace with unmodifiable list
  @SuppressWarnings({"rawtypes"})
  final private List<Type> values;

  @SuppressWarnings({"rawtypes"})
  public Output(@NotNull final List<Type> values) {
    this.values = values;
  }

  @PositiveOrZero public int size() {
    return this.values.size();
  }

  public BigInteger getInt(@PositiveOrZero int index) {
    //@SuppressWarnings("rawtypes")
    final Type<?> value = this.values.get(index);

    if(value instanceof IntType) {  // IntType : super for `Int` and `Uint` types
      return ((IntType)value).getValue();
    }else {
      throw new IllegalStateException(
          String.format("The %dth output argument is not Solidity `uint<M>` type nor `int<M>` type.", index));
    }

  }


  public BigInteger[] getIntArray(@PositiveOrZero int index) {

    final Type<?> value = this.values.get(index);

    if(value instanceof DynamicArray) {
      @SuppressWarnings({"rawtypes", "unchecked"})
      final List<? extends IntType> list = ((DynamicArray)value).getValue();
      final BigInteger[] arg = new BigInteger[list.size()];

      for(int i = 0; i < list.size(); i++) arg[i] = list.get(i).getValue();

      return arg;
    }else {
      throw new IllegalStateException(
          String.format("The %dth output argument is not Solidity array type.", index));
    }
  }


  public String getAddress(@PositiveOrZero int index) {

    final Type<?> value = this.values.get(index);

    if(value instanceof Address) {
      return ((Address)value).getValue();
    }else {
      throw new IllegalStateException(
          String.format("The %dth output argument is not Solidity `address` type.", index));
    }
  }


  public String[] getAddressArray(@PositiveOrZero int index) {


    return null;

  }



}

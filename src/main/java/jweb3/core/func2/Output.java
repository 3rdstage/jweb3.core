package jweb3.core.func2;

import java.math.BigInteger;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.PositiveOrZero;
import org.web3j.abi.datatypes.IntType;
import org.web3j.abi.datatypes.Type;


@Immutable @ThreadSafe
public class Output{

  // @TODO Replace with unmodifiable list
  final private List<Type> values;

  public Output(@Nonnull final List<Type> values) {
    this.values = values;
  }

  @PositiveOrZero public int size() {
    return this.values.size();
  }

  public BigInteger getIntValue(@PositiveOrZero int index) {
    @SuppressWarnings("rawtypes") final Type value = this.values.get(index);

    if(value instanceof IntType) {  // IntType : super for `Int` and `Uint` types
      return ((IntType)value).getValue();
    }else {
      throw new IllegalStateException(String.format("The %dth output parameter is not Solidity uint<M> type nor int<M> type.", index));
    }

  }


  public BigInteger[] getIntArray(@PositiveOrZero int index) {


    return null;
  }




}

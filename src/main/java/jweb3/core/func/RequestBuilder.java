package jweb3.core.func;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.Validate;
import org.web3j.abi.datatypes.AbiTypes;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.StructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;

@NotThreadSafe
public class RequestBuilder{

  // cache for 'Class' instances for Solidity ABI types
  protected final static Map<String, Class<? extends Type<?>>> classes = new HashMap<>();

  static {

    String name;
    for(int i = 1; i <= 32; i++) {
      name = "uint" + (8 * i);
      classes.put(name, AbiTypes.getType(name).asSubclass(Uint.class));

      name = "int" + (8 * i);
      classes.put(name, AbiTypes.getType(name).asSubclass(Int.class));
    }
  }


  private String name = null;

  public String getName() { return this.name; }

  public RequestBuilder setName(final String name) {
    this.name = name;
    return this;
  }


  final private List<Type> args = new ArrayList<>();

  public List<Type> getArgs(){ return this.args; }

  protected void validateIntBitSize(final int size) {


    //@TODO

  }

  protected void validateStaticBytesSize(final int size) {

  }


  protected <T extends Type, V> void appendTypedArg(Class<T> abiType, Class<V> valueType, V value) {

    try {
      this.args.add(abiType.getConstructor(valueType).newInstance(value));
    }catch(RuntimeException ex) {
      throw ex;
    }catch(Throwable ex) {
      throw new RuntimeException(ex);
    }
  }


  protected <T extends Type, V> void appendArrayArg(Class<T> abiType, Class<V> valueType, V[] value) {

    try {
      final List<T> val = new ArrayList<>();
      for(int i = 0, n = value.length; i < n; i++) {
        val.add(abiType.getConstructor(valueType).newInstance(value[i]));
      }

      this.args.add(new DynamicArray(abiType, val));
    }catch(RuntimeException ex) {
      throw ex;
    }catch(Throwable ex) {
      throw new RuntimeException(ex);
    }

  }

  public RequestBuilder addUintArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger value) {

    this.validateIntBitSize(bitSize);

    this.appendTypedArg(classes.get("uint" + bitSize).asSubclass(Uint.class), BigInteger.class, value);
    return this;

//    try {
//      final Uint arg = classes.get("uint" + bitSize)
//          .asSubclass(Uint.class)
//          .getConstructor(BigInteger.class)
//          .newInstance(value);
//
//      this.args.add(arg);
//      return this;
//    }catch(RuntimeException ex){
//      throw ex;
//    }catch(Throwable ex) {
//      throw new RuntimeException(ex);
//    }
  }

  public RequestBuilder addUintArg(@Min(8) @Max(256) final int bitSize,
      @PositiveOrZero final long value) {

    return this.addUintArg(bitSize, BigInteger.valueOf(value));
  }

  public RequestBuilder addUintArg(@Nonnull @PositiveOrZero final BigInteger value) {
    return this.addUintArg(256, value);
  }

  public RequestBuilder addUintArg(@PositiveOrZero final long value) {
    return this.addUintArg(256, BigInteger.valueOf(value));
  }


  public RequestBuilder addUintArrayArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final BigInteger ... value) {

    this.validateIntBitSize(bitSize);
    if(value == null || value.length == 0) {
      throw new IllegalArgumentException("Empty array is unacceptable.");
    }

    Class<? extends Uint> clazz = classes.get("uint" + bitSize).asSubclass(Uint.class);
    try {
      final List<Uint> val = new ArrayList<>();
      for(int i = 0, n = value.length; i < n; i++) {
        Validate.isTrue(BigInteger.ZERO.compareTo(value[i]) <= 0, "Elements of value should be non-negative.");
        val.add(clazz.getConstructor(BigInteger.class).newInstance(value[i]));
      }

      this.args.add(new DynamicArray(clazz, val));
      return this;
    }catch(RuntimeException ex) {
      throw ex;
    }catch(Throwable ex) {
      throw new RuntimeException(ex);
    }
  }

  public RequestBuilder addUintArrayArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final long ... value) {

    if(value == null || value.length == 0) {
      throw new IllegalArgumentException("Empty array is unacceptable.");
    }

    final BigInteger[] val = new BigInteger[value.length];
    for(int i = 0, n = value.length; i < n; i++) {
      val[i] = BigInteger.valueOf(value[i]);
    }

    return this.addUintArrayArg(bitSize, val);
  }

  public RequestBuilder addIntArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger value) {

    this.validateIntBitSize(bitSize);

    try {
      final Int arg = classes.get("int" + bitSize)
          .asSubclass(Int.class)
          .getConstructor(BigInteger.class)
          .newInstance(value);

      this.args.add(arg);
      return this;
    }catch(RuntimeException ex){
      throw ex;
    }catch(Throwable ex) {
      throw new RuntimeException(ex);
    }
  }

  public RequestBuilder addIntArg(@Min(8) @Max(256) final int bitSize,
      @PositiveOrZero final long value) {

    return this.addIntArg(bitSize, BigInteger.valueOf(value));
  }

  public RequestBuilder addIntArg(@Nonnull @PositiveOrZero final BigInteger value) {
    return this.addIntArg(256, value);
  }

  public RequestBuilder addIntArg(@PositiveOrZero final long value) {
    return this.addIntArg(256, BigInteger.valueOf(value));
  }

  public RequestBuilder addIntArrayArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final BigInteger ... value) {

    this.validateIntBitSize(bitSize);
    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");

    Class<? extends Int> clazz = classes.get("int" + bitSize).asSubclass(Int.class);
    this.appendArrayArg(clazz, BigInteger.class, value);
    return this;
  }


  public RequestBuilder addStructArg(final StructType value) {

    if(value instanceof StaticStruct) {
      this.args.add((StaticStruct)value);
    }else if(value instanceof DynamicStruct) {
      this.args.add((DynamicStruct)value);
    }else {
      throw new IllegalArgumentException("The type of value is not supported.");
    }

    return this;
  }




}

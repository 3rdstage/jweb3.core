package jweb3.core.func;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.Validate;
import org.web3j.abi.datatypes.AbiTypes;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.StructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;

@NotThreadSafe
public class InputBuilder{

  // cache for 'Class' instances for Solidity ABI types
  protected final static Map<String, Class<? extends Type<?>>> classes = new HashMap<>();

  static {

    String name;
    for(int i = 1; i <= 32; i++) {
      name = "uint" + (8 * i);
      classes.put(name, AbiTypes.getType(name).asSubclass(Uint.class));

      name = "int" + (8 * i);
      classes.put(name, AbiTypes.getType(name).asSubclass(Int.class));

      name = "bytes" + i;
      classes.put(name, AbiTypes.getType(name).asSubclass(Bytes.class));
    }
  }


  private String name = null;

  public String getName() { return this.name; }

  public InputBuilder setName(final String name) {
    this.name = name;
    return this;
  }


  final private List<Type> args = new ArrayList<>();

  public List<Type> getArgs(){ return this.args; }

  protected void validateIntBitSize(final int size) {

    if(size < 8 || size > 256 || size%8 != 0) {
      throw new IllegalArgumentException(
          "Bit size of uint/int type should be multiple of 8 between 8 and 256.");
    }
  }

  protected void validateStaticBytesLength(final int len) {

    if(len < 1 || len > 32) {
      throw new IllegalArgumentException(
          "Length of static(fixed-size) bytes should be between 1 and 32.");
    }

  }

  protected void validateAddressString(final String str) {

    if(str == null || !str.startsWith("0x")) {
      throw new IllegalArgumentException("The string is not in Ethereum address format.");
    }
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

      this.args.add(new DynamicArray<T>(abiType, val));
    }catch(RuntimeException ex) {
      throw ex;
    }catch(Throwable ex) {
      throw new RuntimeException(ex);
    }

  }

  public InputBuilder addUintArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger value) {

    this.validateIntBitSize(bitSize);

    this.appendTypedArg(classes.get("uint" + bitSize)
        .asSubclass(Uint.class), BigInteger.class, value);
    return this;
  }

  public InputBuilder addUintArg(@Min(8) @Max(256) final int bitSize,
      @PositiveOrZero final long value) {

    return this.addUintArg(bitSize, BigInteger.valueOf(value));
  }

  public InputBuilder addUintArg(@Nonnull @PositiveOrZero final BigInteger value) {
    return this.addUintArg(256, value);
  }

  public InputBuilder addUintArg(@PositiveOrZero final long value) {
    return this.addUintArg(256, BigInteger.valueOf(value));
  }


  public InputBuilder addUintArrayArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final BigInteger ... value) {

    this.validateIntBitSize(bitSize);
    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    // @TODO Maybe it can be necessary to check each element of array should be non-negative

    Class<? extends Uint> clazz = classes.get("uint" + bitSize).asSubclass(Uint.class);
    this.appendArrayArg(clazz, BigInteger.class, value);


//    try {
//      final List<Uint> val = new ArrayList<>();
//      for(int i = 0, n = value.length; i < n; i++) {
//        Validate.isTrue(BigInteger.ZERO.compareTo(value[i]) <= 0, "Elements of value should be non-negative.");
//        val.add(clazz.getConstructor(BigInteger.class).newInstance(value[i]));
//      }
//
//      this.args.add(new DynamicArray(clazz, val));
//    }catch(RuntimeException ex) {
//      throw ex;
//    }catch(Throwable ex) {
//      throw new RuntimeException(ex);
//    }

    return this;

  }

  public InputBuilder addUintArrayArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final long ... value) {

    this.validateIntBitSize(bitSize);
    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");

    final BigInteger[] val = new BigInteger[value.length];
    for(int i = 0, n = value.length; i < n; i++) {
      val[i] = BigInteger.valueOf(value[i]);
      Validate.isTrue(BigInteger.ZERO.compareTo(val[i]) <= 0, "Elements of value should be non-negative.");
    }

    Class<? extends Uint> clazz = classes.get("uint" + bitSize).asSubclass(Uint.class);
    this.appendArrayArg(clazz, BigInteger.class, val);
    return this;
  }

  public InputBuilder addIntArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger value) {

    this.validateIntBitSize(bitSize);

    this.appendTypedArg(classes.get("int" + bitSize)
        .asSubclass(Int.class), BigInteger.class, value);
    return this;
  }

  public InputBuilder addIntArg(@Min(8) @Max(256) final int bitSize,
      @PositiveOrZero final long value) {

    return this.addIntArg(bitSize, BigInteger.valueOf(value));
  }

  public InputBuilder addIntArg(@Nonnull @PositiveOrZero final BigInteger value) {
    return this.addIntArg(256, value);
  }

  public InputBuilder addIntArg(@PositiveOrZero final long value) {
    return this.addIntArg(256, BigInteger.valueOf(value));
  }

  public InputBuilder addIntArrayArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final BigInteger ... value) {

    this.validateIntBitSize(bitSize);
    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");

    Class<? extends Int> clazz = classes.get("int" + bitSize).asSubclass(Int.class);
    this.appendArrayArg(clazz, BigInteger.class, value);
    return this;
  }


  public InputBuilder addAddressArg(
      @NotBlank @Pattern(regexp = "0x[0-9a-fA-F]{1,40}") final String value) {

    this.validateAddressString(value);

    this.args.add(new Address(value));
    return this;
  }

  public InputBuilder addAddressArrayArg(@NotEmpty final String ... value ) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<Address> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) {
      val.add(new Address(value[i]));
    }

    this.args.add(new DynamicArray<Address>(Address.class, val));
    return this;
  }



  public InputBuilder addBoolArg(final boolean value) {
    this.args.add(new Bool(value));
    return this;
  }


  public InputBuilder addBoolArrayArg(final boolean ...value) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<Bool> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) {
      val.add(new Bool(value[i]));
    }

    this.args.add(new DynamicArray<Bool>(Bool.class, val));
    return this;
  }

  public InputBuilder addStaticBytesArg(@Min(1) @Max(8) final int size, final byte[] value) {

    this.validateStaticBytesLength(size);

    Class<? extends Bytes> clazz = classes.get("bytes" + size).asSubclass(Bytes.class);
    this.appendTypedArg(clazz, byte[].class, value);

    return this;
  }


  public InputBuilder addStaticBytesArrayArg(
      @Min(1) @Max(8) final int size, @NotEmpty final byte[] ...value) {

    this.validateStaticBytesLength(size);
    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");

    Class<? extends Bytes> clazz = classes.get("bytes" + size).asSubclass(Bytes.class);
    this.appendArrayArg(clazz, byte[].class, value);
    return this;
  }


  public InputBuilder addBytesArg(final byte[] value) {

    this.args.add(new DynamicBytes(value));
    return this;
  }

  public InputBuilder addBytesArg(final String value) {

    return this.addBytesArg(value, StandardCharsets.UTF_8);
  }

  /**
   * @param value
   * @param charset
   * @return
   *
   * @see java.lang.String#getBytes(Charset)
   * @see #addBytesArg(byte[])
   * @see #addBytesArg(String)
   */
  public InputBuilder addBytesArg(final String value, @Nonnull final Charset charset) {

    Validate.isTrue(charset != null, "Character set should be specified.");

    final byte[] bytes = (value != null) ? value.getBytes(charset) : null;
    return this.addBytesArg(bytes);
  }


  public InputBuilder addBytesArrayArg(@NotEmpty final byte[] ...value) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<DynamicBytes> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) {
      val.add(new DynamicBytes(value[i]));
    }

    this.args.add(new DynamicArray<DynamicBytes>(DynamicBytes.class, val));
    return this;
  }


  public InputBuilder addStringArg(final String value) {

    this.args.add(new Utf8String(value));
    return this;
  }


  public InputBuilder addStringArrayArg(@NotEmpty final String ...value) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<Utf8String> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) {
      val.add(new Utf8String(value[i]));
    }

    this.args.add(new DynamicArray<Utf8String>(Utf8String.class, val));
    return this;
  }



  public InputBuilder addStructArg(final StructType value) {

    if(value instanceof StaticStruct) {
      this.args.add((StaticStruct)value);
    }else if(value instanceof DynamicStruct) {
      this.args.add((DynamicStruct)value);
    }else {
      throw new IllegalArgumentException("The type of value is not supported.");
    }

    return this;
  }


  public InputBuilder addStructArrayArg(final StructType ...value) {
    //@TODO

    return this;
  }



}

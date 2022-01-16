package jweb3.base.func;

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
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint256;
import jweb3.base.AbiAware;

@NotThreadSafe
public class FunctionCallBuilder implements AbiAware{

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

  final private List<Type> args = new ArrayList<>();

  public FunctionCall build() {

    return new FunctionCall(this.name == null ? "" : this.name, this.args);
  }

  public String getName() { return this.name; }

  public FunctionCallBuilder setName(final String name) {
    this.name = name;
    return this;
  }

  public List<Type> getArguments(){ return this.args; }

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

  /*
   * - uint(int, BigInteger)
   * - uint(int, long)
   * - uint(BigInteger)
   * - uint(long)
   * - uintArray(int, BigInteger[])
   * - uintArray(int, long[])
   * - uintArray(BigInteger[])
   * - uintArray(long[])
   * - int(int, BigInteger)
   * - int(int, long)
   * - int(BigInteger)
   * - int(long)
   * - intArray(int, BigInteger[])
   * - intArray(int, long[])
   * - intArray(BigInteger[])
   * - intArray(long[])
   * - address(String)
   * - addressArray(String[])
   * - bool(boolean)
   * - boolArray(boolean[])
   * - staticBytes(int, byte[])
   * - staticBytesArray(int, byte[][])
   * - bytes(byte[])
   * - bytes(String)
   * - bytes(String, Charset)
   * - bytesArray(byte[][])
   * - bytesArray(String[])
   * - bytesArray(Charset, String[])
   * - string(String)
   * - stringArray(String[])
   * - struct(StructType)
   * - structArray(StructType[])
   */


  public FunctionCallBuilder addUintArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger value) {

    this.validateIntBitSize(bitSize);

    this.appendTypedArg(classes.get("uint" + bitSize)
        .asSubclass(Uint.class), BigInteger.class, value);
    return this;
  }

  public FunctionCallBuilder addUintArg(@Min(8) @Max(256) final int bitSize,
      @PositiveOrZero final long value) {

    return this.addUintArg(bitSize, BigInteger.valueOf(value));
  }

  public FunctionCallBuilder addUintArg(@Nonnull @PositiveOrZero final BigInteger value) {
    return this.addUintArg(256, value);
  }

  public FunctionCallBuilder addUintArg(@PositiveOrZero final long value) {
    return this.addUintArg(256, BigInteger.valueOf(value));
  }


  public FunctionCallBuilder addUintArrayArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final BigInteger ... value) {

    this.validateIntBitSize(bitSize);
    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    // @TODO Maybe it can be necessary to check each element of array should be non-negative

    Class<? extends Uint> clazz = classes.get("uint" + bitSize).asSubclass(Uint.class);
    this.appendArrayArg(clazz, BigInteger.class, value);
    return this;

  }

  public FunctionCallBuilder addUintArrayArg(@Min(8) @Max(256) final int bitSize,
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

  public FunctionCallBuilder addUintArrayArg(@Nonnull @NotEmpty final BigInteger ... value) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<Uint256> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) val.add(new Uint256(value[i]));

    this.args.add(new DynamicArray<Uint256>(Uint256.class, val));
    return this;
  }

  public FunctionCallBuilder addUintArrayArg(@Nonnull @NotEmpty final long ... value) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<Uint256> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) val.add(new Uint256(value[i]));

    this.args.add(new DynamicArray<Uint256>(Uint256.class, val));
    return this;
  }


  public FunctionCallBuilder addIntArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger value) {

    this.validateIntBitSize(bitSize);

    this.appendTypedArg(classes.get("int" + bitSize)
        .asSubclass(Int.class), BigInteger.class, value);
    return this;
  }

  public FunctionCallBuilder addIntArg(@Min(8) @Max(256) final int bitSize,
      @PositiveOrZero final long value) {

    return this.addIntArg(bitSize, BigInteger.valueOf(value));
  }

  public FunctionCallBuilder addIntArg(@Nonnull @PositiveOrZero final BigInteger value) {
    return this.addIntArg(256, value);
  }

  public FunctionCallBuilder addIntArg(@PositiveOrZero final long value) {
    return this.addIntArg(256, BigInteger.valueOf(value));
  }

  public FunctionCallBuilder addIntArrayArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final BigInteger ... value) {

    this.validateIntBitSize(bitSize);
    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");

    Class<? extends Int> clazz = classes.get("int" + bitSize).asSubclass(Int.class);
    this.appendArrayArg(clazz, BigInteger.class, value);
    return this;
  }

  public FunctionCallBuilder addIntArrayArg(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final long ... value) {

    this.validateIntBitSize(bitSize);
    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");

    final BigInteger[] val = new BigInteger[value.length];
    for(int i = 0, n = value.length; i < n; i++) {
      val[i] = BigInteger.valueOf(value[i]);
      Validate.isTrue(BigInteger.ZERO.compareTo(val[i]) <= 0, "Elements of value should be non-negative.");
    }

    Class<? extends Int> clazz = classes.get("int" + bitSize).asSubclass(Int.class);
    this.appendArrayArg(clazz, BigInteger.class, val);
    return this;
  }

  public FunctionCallBuilder addIntArrayArg(@Nonnull @NotEmpty final BigInteger ... value) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<Int256> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) val.add(new Int256(value[i]));

    this.args.add(new DynamicArray<Int256>(Int256.class, val));
    return this;
  }

  public FunctionCallBuilder addIntArrayArg(@Nonnull @NotEmpty final long ... value) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<Int256> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) val.add(new Int256(value[i]));

    this.args.add(new DynamicArray<Int256>(Int256.class, val));
    return this;
  }


  public FunctionCallBuilder addAddressArg(
      @NotBlank @Pattern(regexp = "0x[0-9a-fA-F]{40}") final String value) {

    this.validateAddressString(value);

    this.args.add(new Address(value));
    return this;
  }

  public FunctionCallBuilder addAddressArrayArg(@NotEmpty final String ... value ) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<Address> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) val.add(new Address(value[i]));

    this.args.add(new DynamicArray<Address>(Address.class, val));
    return this;
  }

  public FunctionCallBuilder addBoolArg(final boolean value) {
    this.args.add(new Bool(value));
    return this;
  }


  public FunctionCallBuilder addBoolArrayArg(final boolean ...value) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<Bool> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) val.add(new Bool(value[i]));

    this.args.add(new DynamicArray<Bool>(Bool.class, val));
    return this;
  }

  public FunctionCallBuilder addStaticBytesArg(@Min(1) @Max(8) final int size, final byte[] value) {

    this.validateStaticBytesLength(size);

    Class<? extends Bytes> clazz = classes.get("bytes" + size).asSubclass(Bytes.class);
    this.appendTypedArg(clazz, byte[].class, value);

    return this;
  }


  public FunctionCallBuilder addStaticBytesArrayArg(
      @Min(1) @Max(8) final int size, @NotEmpty final byte[] ...value) {

    this.validateStaticBytesLength(size);
    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");

    Class<? extends Bytes> clazz = classes.get("bytes" + size).asSubclass(Bytes.class);
    this.appendArrayArg(clazz, byte[].class, value);
    return this;
  }


  /**
   * Append a {@code bytes} ABI type argument
   *
   * @param value
   * @return
   *
   * @see #addBytesArg(String)
   * @see #addBytesArg(String, Charset)
   */
  public FunctionCallBuilder addBytesArg(final byte[] value) {

    this.args.add(new DynamicBytes(value));
    return this;
  }

  /**
   * Append a {@code bytes} ABI type argument.
   * <p>
   * Specified string would be turn to byte array on behalf of {@code UTF-8} encoding.
   *
   * @param value
   * @return
   *
   * @see #addBytesArg(byte[])
   * @see #addBytesArg(String, Charset)
   */
  public FunctionCallBuilder addBytesArg(final String value) {

    return this.addBytesArg(value, StandardCharsets.UTF_8);
  }

  /**
   * Append a {@code bytes} ABI type argument.
   * <p>
   * Specified string would be turn to byte array on behalf of the specified character set
   *
   * @param value
   * @param charset
   * @return
   *
   * @see java.lang.String#getBytes(Charset)
   * @see #addBytesArg(byte[])
   * @see #addBytesArg(String)
   */
  public FunctionCallBuilder addBytesArg(final String value, @Nonnull final Charset charset) {

    Validate.isTrue(charset != null, "Character set should be specified.");

    final byte[] bytes = (value != null) ? value.getBytes(charset) : null;
    return this.addBytesArg(bytes);
  }


  public FunctionCallBuilder addBytesArrayArg(@NotEmpty final byte[] ...value) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<DynamicBytes> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) val.add(new DynamicBytes(value[i]));

    this.args.add(new DynamicArray<DynamicBytes>(DynamicBytes.class, val));
    return this;
  }

  public FunctionCallBuilder addBytesArrayArg(@NotEmpty final String ... value) {
    return this.addBytesArrayArg(StandardCharsets.UTF_8, value);
  }

  public FunctionCallBuilder addBytesArrayArg(@Nonnull final Charset charset,
      @NotEmpty final String ... value) {

    Validate.isTrue(charset != null, "Character set should be specified.");
    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");

    final List<DynamicBytes> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) {
      val.add(new DynamicBytes(
          (value[i] != null) ? value[i].getBytes(StandardCharsets.UTF_8) : null));
    }

    this.args.add(new DynamicArray<DynamicBytes>(DynamicBytes.class, val));
    return this;
  }


  public FunctionCallBuilder addStringArg(final String value) {

    this.args.add(new Utf8String(value));
    return this;
  }

  public FunctionCallBuilder addStringArrayArg(@NotEmpty final String ...value) {

    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");
    final List<Utf8String> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) val.add(new Utf8String(value[i]));

    this.args.add(new DynamicArray<Utf8String>(Utf8String.class, val));
    return this;
  }

  //@TODO What about `<T extends StructType> FunctionCallBuilder addStructArg(final T value)`
  public FunctionCallBuilder addStructArg(@Nonnull final StructType value) {

    if(value instanceof StaticStruct) {
      this.args.add((StaticStruct)value);
    }else if(value instanceof DynamicStruct) {
      this.args.add((DynamicStruct)value);
    }else {
      throw new IllegalArgumentException("Unsupported type : " + value.getClass());
    }

    return this;
  }

  public FunctionCallBuilder addStructArrayArg(@NotEmpty final StructType ... value) {

    if(value[0] instanceof StaticStruct) {
      appendStructArrayArg(StaticStruct.class, (StaticStruct[])value);
    }else if(value[0] instanceof DynamicStruct) {
      appendStructArrayArg(DynamicStruct.class, (DynamicStruct[])value);
    }else {
      throw new IllegalArgumentException("Unsupported type : " + value[0].getClass());
    }

    return this;
  }

  protected <T extends StructType & Type> void appendStructArrayArg(
      @Nonnull Class<T> clazz, @NotEmpty final T ...value) {
    Validate.isTrue(value != null && value.length > 0, "Empty array is unacceptable.");

    final List<T> val = new ArrayList<>();
    for(int i = 0; i < value.length; i++) val.add((value[i]));

    this.args.add(new DynamicArray<T>(clazz , val));
  }

}

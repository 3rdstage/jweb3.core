package jweb3.base.func;

import java.math.BigInteger;
import java.nio.charset.Charset;
import javax.annotation.Nonnull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import org.web3j.abi.datatypes.StructType;

public class NeatFunctionCallBuilder{


  private final FunctionCallBuilder bldr = new FunctionCallBuilder();

  public FunctionCall build() {
    return this.bldr.build();
  }

  public String name() { return this.name(); }

  public NeatFunctionCallBuilder name(final String name) {

    this.bldr.setName(name);
    return this;
  }


  public NeatFunctionCallBuilder uint(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger value) {

    this.bldr.addUintArg(bitSize, value);
    return this;
  }

  public NeatFunctionCallBuilder uint(@Min(8) @Max(256) final int bitSize,
      @PositiveOrZero final long value) {
    this.bldr.addUintArg(bitSize, value);
    return this;
  }

  public NeatFunctionCallBuilder uint(@Nonnull @PositiveOrZero final BigInteger value) {
    this.bldr.addUintArg(value);
    return this;
  }

  public NeatFunctionCallBuilder uint(@PositiveOrZero final long value) {
    this.bldr.addUintArg(value);
    return this;
  }


  public NeatFunctionCallBuilder uintArray(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final BigInteger ... value) {

    this.bldr.addUintArrayArg(bitSize, value);
    return this;
  }


  public NeatFunctionCallBuilder uintArray(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final long ... value) {
    this.bldr.addUintArrayArg(bitSize, value);
    return this;
  }

  public NeatFunctionCallBuilder uintArray(@Nonnull @NotEmpty final BigInteger ... value) {
    this.bldr.addUintArrayArg(value);
    return this;
  }

  public NeatFunctionCallBuilder uintArray(@Nonnull @NotEmpty final long ... value) {
    this.bldr.addUintArrayArg(value);
    return this;
  }


  public NeatFunctionCallBuilder sint(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger value) {
    this.bldr.addIntArg(bitSize, value);
    return this;
  }

  public NeatFunctionCallBuilder sint(@Min(8) @Max(256) final int bitSize,
      @PositiveOrZero final long value) {
    this.bldr.addIntArg(bitSize, value);
    return this;
  }

  public NeatFunctionCallBuilder sint(@Nonnull @PositiveOrZero final BigInteger value) {
    this.bldr.addIntArg(value);
    return this;
  }

  public NeatFunctionCallBuilder sint(@PositiveOrZero final long value) {
    this.bldr.addIntArg(value);
    return this;
  }


  public NeatFunctionCallBuilder sintArray(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final BigInteger ... value) {
    this.bldr.addIntArrayArg(bitSize, value);
    return this;
  }

  public NeatFunctionCallBuilder sintArray(@Min(8) @Max(256) final int bitSize,
      @Nonnull @NotEmpty final long ... value) {
    this.bldr.addIntArrayArg(bitSize, value);
    return this;
  }

  public NeatFunctionCallBuilder sintArray(@Nonnull @NotEmpty final BigInteger ... value) {
    this.bldr.addIntArrayArg(value);
    return this;
  }

  public NeatFunctionCallBuilder sintArray(@NotEmpty final long ... value) {
    this.bldr.addIntArrayArg(value);
    return this;
  }

  public NeatFunctionCallBuilder address(
      @Pattern(regexp = "0x[0-9a-fA-F]{40}") @NotNull final String value) {
    this.bldr.addAddressArg(value);
    return this;
  }

  public NeatFunctionCallBuilder addressArray(@NotEmpty final String ... value ) {
    this.bldr.addAddressArrayArg(value);
    return this;
  }

  public NeatFunctionCallBuilder bool(final boolean value) {
    this.bldr.addBoolArg(value);
    return this;
  }

  public NeatFunctionCallBuilder boolArray(final boolean ...value) {
    this.bldr.addBoolArrayArg(value);
    return this;
  }


  public NeatFunctionCallBuilder staticBytes(@Min(1) @Max(8) final int size, final byte[] value) {
    this.bldr.addStaticBytesArg(size, value);
    return this;
  }

  public NeatFunctionCallBuilder staticBytesArray(@Min(1) @Max(8) final int size, @NotEmpty final byte[] ...value) {
    this.bldr.addStaticBytesArrayArg(size, value);
    return this;
  }


  public NeatFunctionCallBuilder bytes(final byte[] value) {
    this.bldr.addBytesArg(value);
    return this;
  }

  public NeatFunctionCallBuilder bytes(final String value) {
    this.bldr.addBytesArg(value);
    return this;
  }

  public NeatFunctionCallBuilder bytes(final String value, @Nonnull final Charset charset) {
    this.bldr.addBytesArg(value, charset);
    return this;
  }


  public NeatFunctionCallBuilder bytesArray(@NotEmpty final byte[] ...value) {
    this.bldr.addBytesArrayArg(value);
    return this;
  }

  public NeatFunctionCallBuilder bytesArray(@NotEmpty final String ... value) {
    this.bldr.addBytesArrayArg(value);
    return this;
  }

  public NeatFunctionCallBuilder bytesArray(@Nonnull final Charset charset,
      @NotEmpty final String ... value) {
    this.bldr.addBytesArrayArg(charset, value);
    return this;
  }

  public NeatFunctionCallBuilder string(final String value) {
    this.bldr.addStringArg(value);
    return this;
  }

  public NeatFunctionCallBuilder stringArray(@NotEmpty final String ...value) {
    this.bldr.addStringArrayArg(value);
    return this;
  }


  public NeatFunctionCallBuilder struct(@Nonnull final StructType value) {
    this.bldr.addStructArg(value);
    return this;
  }

  public NeatFunctionCallBuilder structArray(@NotEmpty final StructType ... value) {
    this.bldr.addStructArrayArg(value);
    return this;
  }

}

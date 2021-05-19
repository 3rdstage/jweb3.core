package jweb3.core.func;

import java.math.BigInteger;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@NotThreadSafe
public class NeatInputBuilder{

  final InputBuilder builder;

  public NeatInputBuilder(@Nonnull final InputBuilder builder) {
    if(builder == null) {
      throw new IllegalArgumentException("Null builder is specified.");
    }

    this.builder = builder;
  }

  public String name() { return this.builder.getName(); }

  public NeatInputBuilder name(final String name) {
    this.builder.setName(name);
    return this;
  }

  public NeatInputBuilder uint(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger value) {

    this.builder.addUintArg(bitSize, value);
    return this;
  }

  public NeatInputBuilder uint(@Min(8) @Max(256) final int bitSize,
      @PositiveOrZero final long value) {
    this.builder.addUintArg(bitSize, value);
    return this;
  }


  public NeatInputBuilder uint(@Nonnull @PositiveOrZero final BigInteger value) {
    this.builder.addUintArg(value);
    return this;
  }

  public NeatInputBuilder uint(@PositiveOrZero final long value) {
    this.builder.addUintArg(value);
    return this;
  }

  public NeatInputBuilder uintArray(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger ... value) {
    this.builder.addUintArrayArg(bitSize, value);
    return this;
  }

  public NeatInputBuilder uintArray(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final long ... value) {
    this.builder.addUintArrayArg(bitSize, value);
    return this;
  }

  public NeatInputBuilder sint(@Min(8) @Max(256) final int bitSize, final BigInteger value) {

    this.builder.addIntArg(bitSize, value);
    return this;
  }


  public NeatInputBuilder sint(@Nonnull final BigInteger value) {

    this.builder.addIntArg(value);
    return this;
  }


  public NeatInputBuilder sint(@Nonnull final long value) {

    this.builder.addIntArg(value);
    return this;
  }


  public NeatInputBuilder sintArray(@Min(8) @Max(256) final int bitSize,
      @Nonnull final BigInteger ... value) {

    this.builder.addIntArrayArg(bitSize, value);
    return this;
  }

}

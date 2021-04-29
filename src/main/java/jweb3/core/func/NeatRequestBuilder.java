package jweb3.core.func;

import java.math.BigInteger;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@NotThreadSafe
public class NeatRequestBuilder{

  final RequestBuilder builder;

  public NeatRequestBuilder(@Nonnull final RequestBuilder builder) {
    if(builder == null) {
      throw new IllegalArgumentException("Null builder is specified.");
    }

    this.builder = builder;
  }

  public String name() { return this.builder.getName(); }

  public NeatRequestBuilder name(final String name) {
    this.builder.setName(name);
    return this;
  }

  public NeatRequestBuilder uint(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger value) {

    this.builder.addUintArg(bitSize, value);
    return this;
  }

  public NeatRequestBuilder uint(@Min(8) @Max(256) final int bitSize,
      @PositiveOrZero final long value) {
    this.builder.addUintArg(bitSize, value);
    return this;
  }


  public NeatRequestBuilder uint(@Nonnull @PositiveOrZero final BigInteger value) {
    this.builder.addUintArg(value);
    return this;
  }

  public NeatRequestBuilder uint(@PositiveOrZero final long value) {
    this.builder.addUintArg(value);
    return this;
  }

  public NeatRequestBuilder uintArray(@Min(8) @Max(256) final int bitSize,
      @Nonnull @PositiveOrZero final BigInteger ... value) {
    this.builder.addUintArrayArg(bitSize, value);
    return this;
  }


}

package jweb3.base.tx;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Immutable
public class LegacyTransaction implements Transaction{

  // nonce of signer
  private final BigInteger nonce;

  public BigInteger getNonce() { return this.nonce; }

  private final BigInteger gasPrice;

  public BigInteger getGasPrice() { return this.gasPrice; }

  private final BigInteger gasLimit;

  public BigInteger getGasLimit() { return this.gasLimit; }

  private final String to;

  public String getTo() { return this.to; }

  private final BigInteger value;

  public BigInteger getValue() { return this.value; }

  private final String data;

  public String getData() { return this.data; }

  private final byte[] v;

  public byte[] getV() { return this.v; }

  private final byte[] r;

  /**
   * @return <strong>r</strong> value of sinature or empty array if signature is not yet specified.
   */
  @Nonnull public byte[] getR() { return this.r; }

  private final byte[] s;

  /**
   * @return <strong>s</strong> value of sinature or empty array if signature is not yet specified.
   */
  @Nonnull public byte[] getS() { return this.s; }


  public LegacyTransaction(final BigInteger nonce,
      @PositiveOrZero final BigInteger gasPrice,
      @Positive final BigInteger gasLimit,
      @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") @Nullable String to,
      final BigInteger value,
      @Pattern(regexp = "0x[0-9A-Fa-f]*") String data,
      final byte[] v,
      final byte[] r,
      final byte[] s) {

    // TODO input valiation
    // TODO What if `to` is null
    // TODO What if `data` is null

    this.nonce = nonce;
    this.gasPrice = gasPrice;
    this.gasLimit = gasLimit;
    this.to = to;
    this.value = value;
    this.data = data;
    this.v = v;
    this.r = (r != null) ? r : new byte[]{};
    this.s = (s != null) ? s : new byte[]{};

  }

  public LegacyTransaction(final BigInteger nonce,
      @PositiveOrZero final BigInteger gasPrice,
      @Positive final BigInteger gasLimit,
      @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") @Nullable String to,
      final BigInteger value,
      @Pattern(regexp = "0x[0-9A-Fa-f]*") final String data,
      final long chainId) {

    this(nonce, gasPrice, gasLimit, to, value, data,
        ByteBuffer.allocate(Long.BYTES).putLong(chainId).array(), null, null);

  }
}

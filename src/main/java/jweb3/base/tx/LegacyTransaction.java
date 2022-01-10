package jweb3.base.tx;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.StringUtils;

/**
 * Before signing, transaction <tt>T</tt> is
 *
 * <pre>
 *     T = (nonce, gas price, gas limit, to, value, data, chain identifier, 0, 0)
 *     or
 *     T = (nonce, gas price, gas limit, 0, value, init, chain identifier, 0, 0)
 * </pre>
 *
 * After signing, transaction <tt>T</tt> is
 *
 * <pre>
 *     T =  (nonce, gas price, gas limit, to, value, data, v, r, s)
 *     or
 *     T = (nonce, gas price, gas limit, 0, value, init, v, r, s)
 * </pre>
 *
 *
 * @author Sangmoon Oh
 *
 * @see Appendix F of <a href='https://ethereum.github.io/yellowpaper/paper.pdf'>Ethereum Yellow Paper</a>
 */
@Immutable
public class LegacyTransaction implements Transaction{

  // nonce of signer
  private final BigInteger nonce;

  public BigInteger getNonce() { return this.nonce; }

  private final BigInteger gasPrice;

  public BigInteger getGasPrice() { return this.gasPrice; }

  private final BigInteger gasLimit;

  public BigInteger getGasLimit() { return this.gasLimit; }

  @Nullable
  private final String to;

  @Nullable
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


  /**
   * @param nonce the number of transactions sent by the sender (T<sub>n</sub>)
   * @param gasPrice the number of wei to be paid per unit of gas (T<sub>p</sub>)
   * @param gasLimit the max. amount of gas that can be used in executing this tx (T<sub>g</sub>)
   * @param to the message call's recipient address (T<sub>t</sub>)
   * @param value the number of wei to be transfered to the recipient or,
   *              in the case of contract creation, to the newly created account (T<sub>v</sub>)
   * @param data input data of message call (T<sub>d</sub>) or,
   *             in the case of contract creation, byte array of EVM code (T<sub>i</sub>)
   * @param v (T<sub>w</sub>)
   * @param r T<sub>r</sub>
   * @param s T<sub>s</sub>
   *
   * @see section 4.2 of <a href='https://ethereum.github.io/yellowpaper/paper.pdf'>Ethereum Yellow Paper</a>
   */
  protected LegacyTransaction(final BigInteger nonce,
      @PositiveOrZero final BigInteger gasPrice,
      @Positive final BigInteger gasLimit,
      @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") @Nullable String to,
      final BigInteger value,
      @Pattern(regexp = "0x[0-9A-Fa-f]*") String data,
      final byte[] v,
      final byte[] r,
      final byte[] s) {

    // TODO input valiation
    // TODO(Done) What if `to` is null - `to` would be null when the tx is for contract creation.
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
      @Nonnull @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") String to,
      final BigInteger value,
      @Pattern(regexp = "0x[0-9A-Fa-f]*") final String data,
      final long chainId) {

    this(nonce, gasPrice, gasLimit, to, value, data,
        ByteBuffer.allocate(Long.BYTES).putLong(chainId).array(), null, null);

  }

  public LegacyTransaction(final BigInteger nonce,
      @PositiveOrZero final BigInteger gasPrice,
      @Positive final BigInteger gasLimit,
      final BigInteger value,
      @Pattern(regexp = "0x[0-9A-Fa-f]*") final String init,
      final long chainId) {

    this(nonce, gasPrice, gasLimit, null, value, init,
        ByteBuffer.allocate(Long.BYTES).putLong(chainId).array(), null, null);

  }


//  public LegacyTransaction(final BigInteger nonce,
//      @PositiveOrZero final BigInteger gasPrice,
//      @Positive final BigInteger gasLimit,
//      @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") @Nullable String to,
//      final BigInteger value,
//      @Pattern(regexp = "0x[0-9A-Fa-f]*") final String data,
//      final long chainId,
//      @NotEmpty final byte[] r,
//      @NotEmpty final byte[] s) {
//
//
//  }

  @Override
  public LegacyTransaction withSignature(@Nonnull final BigInteger r, @Nonnull final BigInteger s) {
    return null;
  }

  @Override
  public String toShortString() {
    return String.format("nonce: %d, to: %s", this.nonce, this.to);
  }

  @Override
  public String toString() {
    return String.format("none: %d, to: %s, gas price: %d, gas limit: %d, value: %d, data/init: %s...",
        this.nonce, this.to, this.gasPrice, this.gasLimit, this.value, StringUtils.substring(this.data, 0, 10));
  }


}

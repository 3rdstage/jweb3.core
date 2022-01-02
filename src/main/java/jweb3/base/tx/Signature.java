package jweb3.base.tx;

import javax.annotation.concurrent.Immutable;

/**
 * Refer ths 'Appendix F. Signing Transactions' of Etherum Yellow Paper.
 *
 * @author Sangmoon Oh
 *
 */
@Immutable
public class Signature{

  // either the recovery identifier or ‘chain identifier β doubled plus 35 or 36’
  private final byte[] v;

  public byte[] getV() { return v; }

  private final byte[] r;

  public byte[] getR() { return r; }

  private final byte[] s;

  public byte[] getS() { return s; }


  public Signature(final byte[] v, final byte[] r, final byte[] s) {
    this.v = v;
    this.r = r;
    this.s = s;
  }


  public static Signature of(final byte[] v, final byte[] r, final byte[] s) {
    return new Signature(v, r, s);
  }

}

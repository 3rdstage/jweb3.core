package jweb3.core.func2;

import javax.validation.constraints.Positive;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint256;

public class SampleStructs{

  public static class PositionStruct extends StaticStruct{

    private long x;
    private long y;

    public PositionStruct(final long x, final long y) {
      super(new Int256(x), new Int256(y));
      this.x = x;
      this.y = y;
    }
  }

  public static class DimensionsStruct extends StaticStruct{

    private long w; // width
    private long h; // height
    private long d; // depth

    public DimensionsStruct(@Positive final long w,
        @Positive final long h, @Positive final long d) {

      super(new Uint256(w), new Uint256(h), new Uint256(d));
      this.w = w;
      this.h = h;
      this.d = d;
    }
  }

}

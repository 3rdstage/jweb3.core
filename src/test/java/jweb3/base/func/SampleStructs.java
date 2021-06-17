package jweb3.base.func;

import java.lang.reflect.Array;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.StructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Int32;
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


  public static class BookStruct extends DynamicStruct{

    private String title;
    private String author;
    private int year;

    public BookStruct(@NotBlank final String title,
        @NotBlank final String author, int year) {
      super(new Utf8String(title), new Utf8String(author), new Int32(year));
    }

  }


  public static <T extends StructType & Type> T random(@Nonnull Class<T> clazz) throws Exception{

    T val = null;
    switch(clazz.getSimpleName()) {

      case "PositionStruct": {
        final long x = RandomUtils.nextLong() * (RandomUtils.nextBoolean() ? 1L : -1L);
        final long y = RandomUtils.nextLong() * (RandomUtils.nextBoolean() ? 1L : -1L);

        val = clazz.getConstructor(long.class, long.class).newInstance(x, y);
        break;
      }
      case "DimensionsStruct": {

        final long x = RandomUtils.nextLong() + 1;
        final long y = RandomUtils.nextLong() + 1;
        final long z = RandomUtils.nextLong() + 1;

        val = clazz.getConstructor(long.class, long.class, long.class)
            .newInstance(x, y, z);
        break;
      }
      case "BookStruct":
        final String ttl = RandomStringUtils.randomAlphabetic(20);
        final String auth = RandomStringUtils.randomAlphabetic(30);
        final int yr = RandomUtils.nextInt(1000, 2021);

        val = clazz.getConstructor(String.class, String.class, int.class)
            .newInstance(ttl, auth, yr);
        break;

      default:
        throw new IllegalArgumentException("Unsupported type : " + clazz.getTypeName());
    }

    return val;
  }

  public static <T extends StructType & Type> T[] randomArray(
      @Nonnull Class<T> clazz, @Positive int arrSize) throws Exception {

    if(arrSize < 1) throw new IllegalArgumentException("Invalid array size.");

    T[] arr = (T[]) Array.newInstance(clazz, arrSize);
    for(int i = 0; i < arrSize; i++) arr[i] = random(clazz);

    return arr;
  }



}

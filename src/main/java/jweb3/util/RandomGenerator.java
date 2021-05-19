package jweb3.util;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class RandomGenerator{

  private static char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  private static void validateIntBitSize(final int size) {
    if(size < 8 || size > 256 || size%8 != 0) {
      throw new IllegalArgumentException("Bit size should be multiple of 8 between 8 and 256.");
    }
  }

  private static void validateStaticBytesLength(final int len) {
    if(len < 1 || len > 32) {
      throw new IllegalArgumentException(
          "Length of static(fixed-size) bytes should be between 1 and 32.");
    }
  }

  @Nonnull
  public static BigInteger uintVal(@Min(8) @Max(256) int bitSize) {

    validateIntBitSize(bitSize);
    return new BigInteger(bitSize, ThreadLocalRandom.current());
  }

  /**
   * Generates array of {@code BigInteger} type values which can be bound to {@code uint<M>} ABI type.
   * Each element of generated array is a randoum number between {@code 0} and {@code 2^n - 1}
   *
   * @param bitSize
   * @param arrSize
   * @return
   */
  public static BigInteger[] uintArray(@Min(8) @Max(256) int bitSize, @Positive final int arrSize) {

    validateIntBitSize(bitSize);
    if(arrSize < 1) { throw new IllegalArgumentException("Array size should be positive."); }

    final Random rand = ThreadLocalRandom.current();
    final BigInteger[] arr = new BigInteger[arrSize];
    for(int i = 0; i < arrSize; i++) arr[i] = new BigInteger(bitSize, rand);

    return arr;
  }


  public static BigInteger[] uintArray(@Positive final int arrSize) {
    return uintArray(256, arrSize);
  }

  @Nonnull
  public static BigInteger intVal(@Min(8) @Max(256) int bitSize) {

    validateIntBitSize(bitSize);

    final Random rand = ThreadLocalRandom.current();
    return new BigInteger(bitSize - 1, rand).multiply(BigInteger.valueOf(rand.nextBoolean() ? 1 : -1));
  }

  /**
   * Generates array of {@code BigInteger} type values which can be bound to {@code int<M>} ABI type.
   * Each element of generated array is a randoum number between {@code -2^(n-1) + 1} and {@code 2^(n-1) - 1}
   *
   * @param bitSize
   * @param arrSize
   * @return
   */
  public static BigInteger[] intArray(@Min(8) @Max(256) int bitSize, @Positive final int arrSize) {

    validateIntBitSize(bitSize);
    if(arrSize < 1) { throw new IllegalArgumentException("Array size should be positive."); }

    final Random rand = ThreadLocalRandom.current();
    final BigInteger[] arr = new BigInteger[arrSize];
    for(int i = 0; i < arrSize; i++) {
      arr[i] = new BigInteger(bitSize - 1, rand);
      if(rand.nextBoolean()) arr[i] = arr[i].negate();
    }

    return arr;
  }

  public static BigInteger[] intArray(@Positive final int arrSize) {
    return intArray(256, arrSize);
  }

  @Nonnull @NotBlank
  public static String address() {
    return "0x" + RandomStringUtils.random(40, hexChars);
  }

  public static String[] addressArray(@Positive final int arrSize) {
    if(arrSize < 1) { throw new IllegalArgumentException("Array size should be positive."); }

    final String[] arr = new String[arrSize];
    for(int i = 0; i < arrSize; i++) {
      arr[i] = "0x" + RandomStringUtils.random(40, hexChars);
    }

    return arr;
  }


  public static byte[] staticBytes(@Min(1) @Max(32) final int len) {

    validateStaticBytesLength(len);
    return RandomUtils.nextBytes(len);
  }


  public static byte[][] staticBytesArray(@Min(1) @Max(32) final int len, @Positive final int arrSize){

    validateStaticBytesLength(len);
    if(arrSize < 1) { throw new IllegalArgumentException("Array size should be positive."); }

    final byte[][] arr = new byte[arrSize][];
    for(int i = 0; i < arrSize; i++) {
      arr[i] = RandomUtils.nextBytes(len);
    }

    return arr;
  }

}

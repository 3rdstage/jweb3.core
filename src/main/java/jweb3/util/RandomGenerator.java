package jweb3.util;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

public class RandomGenerator{

  private static void validateIntBitSize(final int size) {
    if(size < 8 || size > 256 || size%8 != 0) {
      throw new IllegalArgumentException("Bit size should be multiple of 8 between 8 and 256.");
    }
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





}

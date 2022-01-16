package jweb3.util;

import org.bouncycastle.jcajce.provider.digest.Keccak;

/**
 * @author Sangmoon Oh
 * @since 0.8
 */
public class CryptoUtils{

  /**
   * Keccak256 hash size in bytes
   */
  public static final int KECCAK_HASH_SIZE = 32;


  public static byte[] keccak(final byte[] input) {

    // https://github.com/bcgit/bc-java/blob/master/prov/src/main/java/org/bouncycastle/jcajce/provider/digest/Keccak.java
    Keccak.DigestKeccak kcc = new Keccak.Digest256();
    kcc.update(input, 0, input.length);
    return kcc.digest();
  }

}

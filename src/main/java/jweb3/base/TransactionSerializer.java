package jweb3.base;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

@ThreadSafe
public class TransactionSerializer{

  /**
   * Serialize legacy transaction with RLP before sigining
   *
   * @return
   */
  public byte[] serialize(final BigInteger nonce,
      @PositiveOrZero final BigInteger gasPrice, @Positive final BigInteger gasLimit,
      @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") @Nullable String to,
      final BigInteger value, final String data, final long chainId) {

    // TODO - input validation (strict validation is necessary for signing !!)
    // TODO - replaces `Numeric.hexStringToByteArray` with `Hex.decodeHex`

    //https://github.com/web3j/web3j/blob/v4.8.8/crypto/src/main/java/org/web3j/crypto/transaction/type/LegacyTransaction.java#L70
    final List<RlpType> items = new ArrayList<>();
    items.add(RlpString.create(nonce));
    items.add(RlpString.create(gasPrice));
    items.add(RlpString.create(gasLimit));

    if(to == null || to.length() == 0) {
      items.add(RlpString.create(""));
    }else {
      items.add(RlpString.create(Numeric.hexStringToByteArray(to)));
    }

    items.add(RlpString.create(value));
    items.add(RlpString.create(Numeric.hexStringToByteArray(data)));

    // signature before signing
    // signatrue.v in byte array
    final byte[] v = ByteBuffer.allocate(Long.BYTES).putLong(chainId).array();
    items.add(RlpString.create(Bytes.trimLeadingZeroes(v)));
    items.add(RlpString.create(new byte[] {})); // signature.r
    items.add(RlpString.create(new byte[] {})); // signature.s

    // https://github.com/web3j/web3j/blob/v4.8.8/crypto/src/main/java/org/web3j/crypto/TransactionEncoder.java#L94
    return RlpEncoder.encode(new RlpList(items));
  }

}

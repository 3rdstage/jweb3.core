package jweb3.base.tx;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import org.apache.commons.lang3.Validate;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

@ThreadSafe
public class LegacyTransactionSerializer implements TransactionSerializer<LegacyTransaction>{

  @Override
  public byte[] serialize(@Nonnull final LegacyTransaction tx){
    Validate.isTrue(tx != null, "No transaction instance is provided.");

    // TODO What if `data` is null

    //https://github.com/web3j/web3j/blob/v4.8.8/crypto/src/main/java/org/web3j/crypto/transaction/type/LegacyTransaction.java#L70
    final List<RlpType> items = new ArrayList<>();
    items.add(RlpString.create(tx.getNonce()));
    items.add(RlpString.create(tx.getGasPrice()));
    items.add(RlpString.create(tx.getGasLimit()));

    if(tx.getTo() == null) {
      items.add(RlpString.create(""));
    }else {
      items.add(RlpString.create(Numeric.hexStringToByteArray(tx.getTo())));
    }

    items.add(RlpString.create(tx.getValue()));
    items.add(RlpString.create(Numeric.hexStringToByteArray(tx.getData())));

    // signature before signing
    items.add(RlpString.create(Bytes.trimLeadingZeroes(tx.getV())));
    items.add(RlpString.create(tx.getR())); // signature.r
    items.add(RlpString.create(tx.getS())); // signature.s

    // https://github.com/web3j/web3j/blob/v4.8.8/crypto/src/main/java/org/web3j/crypto/TransactionEncoder.java#L94
    return RlpEncoder.encode(new RlpList(items));

  }

}

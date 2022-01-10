package jweb3.base.tx;

import java.math.BigInteger;
import javax.annotation.Nonnull;

public interface Transaction{


  <T extends Transaction> T withSignature(@Nonnull final BigInteger s, @Nonnull final BigInteger v);

  String toShortString();

}

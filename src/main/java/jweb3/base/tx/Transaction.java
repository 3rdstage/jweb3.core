package jweb3.base.tx;

import java.math.BigInteger;
import javax.validation.constraints.NotNull;

/**
 * @author Sangmoon Oh
 * @since 0.8
 */
public interface Transaction{


  <T extends Transaction> T withSignature(@NotNull final BigInteger r, @NotNull final BigInteger s);

  String toShortString();

}

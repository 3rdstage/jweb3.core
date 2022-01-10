package jweb3.base.tx;

import javax.annotation.Nonnull;
import javax.validation.constraints.Pattern;

public interface TransactionSigner{


  <T extends Transaction> byte[] sign(@Nonnull final T tx,
      @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") @Nonnull final String signerAddr);

}

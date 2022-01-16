package jweb3.base.tx;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Sangmoon Oh
 * @since 0.8
 */
public interface TransactionSigner{

  <T extends Transaction> byte[] sign(@NotNull final T tx,
      @Pattern(regexp = "0x[0-9A-Fa-f]{40}") @NotNull final String signerAddr);

}

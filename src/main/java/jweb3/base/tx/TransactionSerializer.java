package jweb3.base.tx;

import javax.validation.constraints.NotNull;

/**
 *
 * @param <T>
 *
 * @author Sangmoon Oh
 * @since 0.8
 */
public interface TransactionSerializer<T extends Transaction>{


  public byte[] serialize(@NotNull final T tx);

}

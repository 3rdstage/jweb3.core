package jweb3.base.tx;

import javax.annotation.Nonnull;

public interface TransactionSerializer<T extends Transaction>{


  public byte[] serialize(@Nonnull final T tx);

}

package jweb3.base.tx;

public interface TransactionSerializer<T extends Transaction>{


  public byte[] serialize(T tx);

}

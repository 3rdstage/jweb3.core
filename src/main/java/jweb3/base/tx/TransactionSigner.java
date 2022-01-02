package jweb3.base.tx;

public interface TransactionSigner{


  byte[] sign(Transaction tx);

}

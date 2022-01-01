package jweb3.base.sign;

public interface Signer{


  Signature sign(final byte[] message, final String address);


}

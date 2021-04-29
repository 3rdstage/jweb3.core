package jweb3.core.func;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;

/**
 * Just a skinny wrapper class of web3j's {@link Function} class, to localize
 * depedency on it and make it easy to replace or extend it later.
 *
 * @author Sangmoon Oh
 */
@Immutable
public class Function extends org.web3j.abi.datatypes.Function{

  private String encoded = null;

  public Function(@Nonnull final String name,
      java.util.List<Type> inputArgs, java.util.List<TypeReference<?>> outputParams) {
    super(name, inputArgs, outputParams);

  }

  public String encode() {
    if(encoded == null)  this.encoded = FunctionEncoder.encode(this);
    return this.encoded;
  }


}

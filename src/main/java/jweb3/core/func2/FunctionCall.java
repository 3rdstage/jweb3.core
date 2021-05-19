package jweb3.core.func2;

import java.util.Collections;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;

@Immutable
public class FunctionCall{

  private String name;

  private java.util.List<Type> args; // input arguments

  private String encoded = null;

  /**
   * @param name function name
   * @param args input arguments
   */
  public FunctionCall(@Nonnull final String name, final java.util.List<Type> args) {

    this.name = name;
    this.args = args;
  }

  public String getName() { return this.name; }

  public java.util.List<Type> getArguments(){ return this.args; }

  public String getEncoded() {

    //@TODO Need double-checked locking
    if(encoded == null) {
      this.encoded = FunctionEncoder.encode(new org.web3j.abi.datatypes.Function(name, args, Collections.emptyList()));
    }

    return this.encoded;
  }

}

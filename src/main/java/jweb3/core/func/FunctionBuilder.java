package jweb3.core.func;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import org.web3j.abi.TypeReference;

public class FunctionBuilder {

  private String name = null;

  final private InputBuilder inputBuilder = new InputBuilder();

  @Nonnull
  public InputBuilder getInputBuilder() { return this.inputBuilder; }

  @Nonnull
  public NeatInputBuilder getNeatInputBuilder() {
    return new NeatInputBuilder(this.inputBuilder);
  }

  public Function build() {

    final String name = this.inputBuilder.getName();

    //@TODO How to deal with blank name?

    // @TODO Temporary
    return new Function(name, this.inputBuilder.getArgs(),
       new ArrayList<TypeReference<?>>());

  }

  public String buildAndEncode() {

    return this.build().encode();
  }







}

package jweb3.core.func;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import org.web3j.abi.TypeReference;

public class FunctionBuilder {

  private String name = null;

  final private RequestBuilder reqBuilder = new RequestBuilder();

  @Nonnull
  public RequestBuilder getRequestBuilder() { return this.reqBuilder; }

  @Nonnull
  public NeatRequestBuilder getNeatRequestBuilder() {
    return new NeatRequestBuilder(this.reqBuilder);
  }

  public Function build() {

    final String name = this.reqBuilder.getName();

    //@TODO How to deal with blank name?

    // @TODO Temporary
    return new Function(name, this.reqBuilder.getArgs(),
       new ArrayList<TypeReference<?>>());

  }

  public String buildAndEncode() {

    return this.build().encode();
  }







}

package jweb3.base.func;

import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.concurrent.Immutable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;

@Immutable
public class OutputSignature{

  @SuppressWarnings("rawtypes")
  private java.util.List<TypeReference<Type>> params;

  @SuppressWarnings("rawtypes")
  public OutputSignature(final java.util.List<TypeReference<? extends Type>> params) {

    this.params = new ArrayList<>(params.size());

    for(int i = 0; i < params.size(); i++) {
      this.params.add((TypeReference<Type>)(params.get(i)));
    }

    this.params = Collections.unmodifiableList(this.params);
  }

  /**
   * @return Unmodifiable list of output parameters(types)
   */
  @SuppressWarnings("rawtypes")
  public java.util.List<TypeReference<Type>> getParams(){ return this.params; }


}

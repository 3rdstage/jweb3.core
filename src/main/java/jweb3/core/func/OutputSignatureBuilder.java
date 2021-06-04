package jweb3.core.func;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import org.web3j.abi.TypeReference;

@NotThreadSafe
public class OutputSignatureBuilder{

  private final List<TypeReference<?>> params = new ArrayList<>();

  public void appendParam(@Nonnull @NotBlank final String solidityType) {

    try {
      this.params.add(TypeReference.makeTypeReference(solidityType));
    }catch(RuntimeException ex) {
      throw ex;
    }catch(Throwable th) {
      throw new RuntimeException(th);
    }
  }

  public OutputSignatureBuilder addUintParam(@Min(8) @Max(256) final int n) {

    this.appendParam("uint" + n);
    return this;
  }


}

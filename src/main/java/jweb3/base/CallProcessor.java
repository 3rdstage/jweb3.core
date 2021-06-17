package jweb3.base;

import javax.annotation.Nonnull;
import javax.validation.constraints.Pattern;
import jweb3.base.func.FunctionCall;
import jweb3.base.func.Output;
import jweb3.base.func.OutputSignature;

public interface CallProcessor{


  Output process(
      @Pattern(regexp="0x[0-9A-Fa-f]{1,40}") final String senderAddress,
      @Pattern(regexp="0x[0-9A-Fa-f]{1,40}") final String contractAddress,
      @Nonnull final FunctionCall funcCall,
      @Nonnull final OutputSignature outputSignature);


}

package jweb3.base;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import jweb3.base.func.FunctionCall;
import jweb3.base.func.Output;
import jweb3.base.func.OutputSignature;

public interface CallProcessor{


  Output process(
      @Pattern(regexp="0x[0-9A-Fa-f]{40}") @NotNull final String senderAddress,
      @Pattern(regexp="0x[0-9A-Fa-f]{40}") @NotNull final String contractAddress,
      @NotNull final FunctionCall funcCall,
      @NotNull final OutputSignature outputSignature);


}

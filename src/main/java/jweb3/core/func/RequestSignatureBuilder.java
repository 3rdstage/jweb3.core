package jweb3.core.func;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.concurrent.NotThreadSafe;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import org.web3j.abi.TypeReference;

@NotThreadSafe
public class RequestSignatureBuilder {

  private String name = null;

	final private List<TypeReference<?>> params = new ArrayList<>();

	public RequestSignatureBuilder setName(@NotBlank final String name) {
	  if(name == null || name.trim().length() == 0) {
	    throw new IllegalArgumentException("The provided name is undefined, empty or blank");
	  }
	  this.name = name;
	  return this;
	}


	public RequestSignatureBuilder addUintParam(@Min(8) @Max(256) final int bitSize) {

	  return null;

	}


}

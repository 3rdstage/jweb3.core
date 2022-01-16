package jweb3.base;

import java.math.BigInteger;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public interface DeployProcessor{

  /**
   * @param senderAddress
   * @param byteCode
   * @return
   *
   * @see https://web3js.readthedocs.io/en/v1.3.6/web3-eth.html#sendtransaction
   */
  @Pattern(regexp="0x[0-9A-Fa-f]{40}")
  @NotNull
  String process(
    @Pattern(regexp="0x[0-9A-Fa-f]{40}") @NotNull final String senderAddress,
    @NotBlank final String byteCode,
    @PositiveOrZero @NotNull final BigInteger gasPrice,
    @Positive @NotNull final BigInteger gasLimit
    );

  @Pattern(regexp="0x[0-9A-Fa-f]{40}")
  @NotNull
  String process(
    @Pattern(regexp="0x[0-9A-Fa-f]{40}") @NotNull final String senderAddress,
    @NotBlank final String byteCode);



}

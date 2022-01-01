package jweb3.base;

import java.math.BigInteger;
import javax.validation.constraints.NotBlank;
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
  @Pattern(regexp="0x[0-9A-Fa-f]{1,40}")
  String process(
    @Pattern(regexp="0x[0-9A-Fa-f]{1,40}") final String senderAddress,
    @NotBlank final String byteCode,
    @PositiveOrZero final BigInteger gasPrice,
    @Positive final BigInteger gasLimit
    );

  @Pattern(regexp="0x[0-9A-Fa-f]{1,40}")
  String process(
    @Pattern(regexp="0x[0-9A-Fa-f]{1,40}") final String senderAddress,
    @NotBlank final String byteCode);



}

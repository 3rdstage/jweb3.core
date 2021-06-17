package jweb3.base;

import java.math.BigInteger;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class TestEthClientProvider{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public Web3j localClient() {

    return client("http", "127.0.0.1", 8545, "*", true);

  }

  protected Web3j client(final String protocol, final String host,
      final int port, final String netVersion, final boolean includesRawResponse) {
    final String endpoint = String.format("%s://%s:%d/", protocol, host, port);
    Web3j web3j = null;
    BigInteger latest = null;
    try {
      HttpService httpService = new HttpService(endpoint, includesRawResponse);

      web3j = Web3j.build(httpService);
      latest = web3j.ethBlockNumber().send().getBlockNumber();
    }catch(Exception ex) {
      logger.error(String.format("Fail to connect Ethereum node for %s://%s:%d/", protocol, host, port));

      ExceptionUtils.wrapAndThrow(ex);
    }

    if(StringUtils.isNotBlank(netVersion) && !"*".equals(netVersion)) {
      final String id = this.getNetVersion(web3j);
      if(!StringUtils.equals(netVersion, id)) {
        throw new IllegalStateException(
            String.format("The network ID of the Ethereum network to access is different from the expected one. - %s, %s", id, netVersion));
      }
    }

    logger.info("Successfully connected to Ethereum JSON RPC ervice for {}://{}:{}/", protocol, host, port);
    logger.info("The latest block number of the Ethereum is : {}", latest);
    return web3j;
  }


  protected String getNetVersion(@Nonnull @WillNotClose final Web3j web3j) {

    if(web3j == null) throw new IllegalArgumentException("The Ethereum session is 'null' which is never expected.");

    String ver = "";
    try {
      ver = web3j.netVersion().send().getNetVersion();
    }catch(Throwable ex) {
      logger.error("Fail to get network ID of the specified Ethereum network.");

      ExceptionUtils.wrapAndThrow(ex);
    }

    return ver;
  }



}

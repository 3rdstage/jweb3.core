package jweb3.support.web3j;

import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

/**
 * @author Sangmoon Oh
 *
 */
@Singleton
public class OkHttpSlf4jBridge{


  private final Logger logger = LoggerFactory.getLogger(OkHttpClient.class);

  private static final OkHttpSlf4jBridge sigleton = new OkHttpSlf4jBridge();

  private OkHttpSlf4jBridge() {
    final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor((msg) -> {
      this.logger.debug(msg);
    });

    interceptor.setLevel(Level.BODY);
  }

  public static void enable() {}

}

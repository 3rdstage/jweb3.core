package org.web3j.utils;

import java.nio.charset.Charset;
import java.util.Arrays;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumericTest{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  @Test
  public void testHexStringToByteArrayWithApacheCodecLib() throws Exception{

    logger.info(Charset.defaultCharset().displayName());

    final String[] strs = {
        "", "00", "a2",
        "0xb009cd53957c0D991CAbE184e884258a1D7b77D9",
        "0x05f9301Be8F3C133fC474F8d538fD732CaCa274c",
        "0x3DC9b4063a130535913137E40Bed546Ff93b1131",
        "0x770b1A8d293d152B8Cc9fC01531B1baB3469AF05",
        "0xAB3ca295454D4A4de79aE32474d2C82f2D0836b1",
        "0x876e0cab3dfC5d2EA19f9A9e6029E8C1b90452Ed",
        "0xBb9Bb87EfE2Cf5E024f32c5943311FdA471848Ca",
        "0x950ea7798112705f487F657E93Fe5D64557CD138",
        "0xA797051B95915F31464440A590332e3360bfEDb9",
        "0x9Ebe0ec1f5f2c0f6BA7d9f7187d6f5c540F2b5fa",
        "0x052fdb8f5af8f2e4ef5c935bcacf1338ad0d8abe30f45f0137943ac72f1bba1e",
        "0x6006fc64218112913e638a2aec5bd25199178cfaf9335a83b75c0e264e7d9cee"
    };

    byte[] bytes1;
    byte[] bytes2;
    for(String str : strs) {

      if(str != null && str.startsWith("0x")) str = str.substring(2);

      bytes1 = Numeric.hexStringToByteArray(str);
      bytes2 = Hex.decodeHex(str);
      Assertions.assertArrayEquals(bytes1, bytes2);

      logger.info(Arrays.toString(bytes1));
      logger.info(Arrays.toString(bytes2));

    }



  }

}

package org.web3j.abi;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Utf8String;

public class TypeReferenceTest{

  private Logger logger = LoggerFactory.getLogger(this.getClass());


  @Test
  public void testWithDynamicArray() throws Exception{

    TypeReference ref = TypeReference.makeTypeReference("string[]");
    Class<?> clazz = ref.getClass();
    this.logger.info(clazz.getName());


    this.logger.info(DynamicArray.empty("string").getTypeAsString());

    ref = TypeReference.create(DynamicArray.empty("string").getClass());
    this.logger.info(ref.getClassType().toString());

    ref = TypeReference.create(
        new DynamicArray(Utf8String.class, Collections.emptyList()).getClass());
    this.logger.info(ref.getClassType().toString());

  }

}

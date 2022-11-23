package com.xzc.utils

import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Properties

/**
  * Created by Xu on 2022/3/5.
  */
object MyProUtil {

  def loadProperties(propertiesName: String = "config.properties"): Properties = {
    //Properties -> hashtable -> map
    val properties = new Properties()
    properties.load(new InputStreamReader(Thread.currentThread().getContextClassLoader.getResourceAsStream(propertiesName), StandardCharsets.UTF_8))
    properties
  }

}

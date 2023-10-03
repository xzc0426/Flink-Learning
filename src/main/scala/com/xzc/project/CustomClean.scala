package com.xzc.project

import com.creaway.operator.DataCleaning.Cleaning

import java.util

/**
 * Created by Xu on 2022/11/27.
 * describe: 
 */

class CustomClean(conf: util.Map[String, String]) extends Cleaning(conf) {
  override def cleaning(map: util.Map[String, String]): Boolean = {
    val columns: Array[String] = this.column.split(",")
    val var3: Array[String] = columns
    val var4: Int = columns.length
    for (var5 <- 0 until var4) {
      val key: String = var3(var5)
      val value: String = map.get(key).asInstanceOf[String]
      if (value == null || value.trim == "" || value == "null") {
        return false
      }
    }
    true
  }

  @throws[Exception]
  override def filter(map: util.Map[String, String]): Boolean = this.cleaning(map)
}
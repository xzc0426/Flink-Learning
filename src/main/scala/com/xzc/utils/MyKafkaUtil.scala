package com.xzc.utils

import org.apache.flink.api.common.serialization._
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka._
import org.slf4j.LoggerFactory

import java.util.Properties

/**
 * Created by Xu on 2022/11/23.
 * describe: 
 */
class MyKafkaUtil {
  private val log = LoggerFactory.getLogger(classOf[MyKafkaUtil])
  private val properties: Properties = MyProUtil.loadProperties()
  private val env: StreamExecutionEnvironment = null

  /*  def KafkaUtil(): Unit = {
      try {
        val ism: InputStream = this.getClass.getClassLoader.getResourceAsStream("main/resources/config.properties")
        this.prop.load(ism)
        initProducer()
        KafkaUtil.log.info("KafkaUtil() successed")
      } catch {
        case e: Exception =>
          KafkaUtil.log.error("KafkaUtil() error:", e)
          e.printStackTrace()
      }
    }*/

  private val produceProp = new Properties()

  private def initProducer(): Unit = {
    val krbenable: String = properties.getProperty("Security_Krb5_Enable")
    val krb5path: String = properties.getProperty("Security_Krb5_Path")
    val kafkajasspath: String = properties.getProperty("Security_JaasFile_Path")
    val serviceName: String = properties.getProperty("Kerberos_Service_Name")
    val domainName: String = properties.getProperty("Kerberos_Domain_Name")
    val brokerList: String = properties.getProperty("BrokerList")
    log.info("发送程序成功读取配置 BrokerList:" + brokerList)
    produceProp.setProperty("bootstrap.servers", brokerList) //brokerList

    produceProp.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    produceProp.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    if (("true").equalsIgnoreCase(krbenable)) {
      produceProp.setProperty("sasl.kerberos.service.name", serviceName)
      produceProp.setProperty("security.protocol", "SASL_PLAINTEXT")
      produceProp.setProperty("kerberos.domain.name", domainName)
      System.setProperty("java.security.krb5.conf", krb5path)
      System.setProperty("java.security.auth.login.config", kafkajasspath)
    }
  }



  private def initPro(): Unit = {
  val KAFKA_BROKER: String = properties.getProperty("broker-list")
  val KAFKA_TOPICS: String = properties.getProperty("topic-name")
  val KAFKA_SCHEMA: String = properties.getProperty("kafka-deserialization-schema")
  val GROUP_ID: String = properties.getProperty("group-id")
  val FROM_BEGINNING: String = properties.getProperty("form-beginning")
  val IS_KRB: String = properties.getProperty("kerberos-enable")
  val KRB_DOMAIN: String = properties.getProperty("kerberos-domainName")
  val KRB_KAFKA_SERVICE_NAME: String = properties.getProperty("kafka-serviceName")
  val props: Properties = new Properties

  props.setProperty("bootstrap.servers", KAFKA_BROKER)
  props.setProperty("group.id", GROUP_ID)
  props.setProperty("topic", KAFKA_TOPICS)
  props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")
  props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")
  log.info("读取KafkaSource信息 TopicName[" + KAFKA_TOPICS + "] IS_KRB[" + IS_KRB + "] Schema[" + KAFKA_SCHEMA + "] GroupId[" + GROUP_ID + "] ")


  props.setProperty("auto.offset.reset", "latest")

  if (IS_KRB == "true") {
    props.setProperty("security.protocol", "SASL_PLAINTEXT")
    props.setProperty("sasl.kerberos.service.name", KRB_KAFKA_SERVICE_NAME)
    props.setProperty("kerberos.domain.name", KRB_DOMAIN)
    log.info(s"设置kafka.kerberos.service.name$KRB_KAFKA_SERVICE_NAME")
    log.info(s"设置kerberos.domain.name$KRB_KAFKA_SERVICE_NAME")
  }
  //    val dataStream = this.connectKafkaSource(KAFKA_TOPICS, KAFKA_SCHEMA, props)
}


  /*  def connectKafkaSource(topic: String, schemaClass: String, props: Properties, para: Int) = {
      val topics = topic.split(",").toList
      val dataStreamSources: Array[DataStreamSource[_]] = new Array[DataStreamSource[_]](topics.length - 1)

      new FlinkKafkaConsumer[String](topics, new SimpleStringSchema, props)
      env.addSource(FlinkKafkaConsumer).setParallelism(para)
     val env1 = StreamExecutionEnvironment.getExecutionEnvironment
      env1.addSource(FlinkKafkaConsumer)
    }*/
}



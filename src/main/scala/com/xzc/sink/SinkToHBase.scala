package com.xzc.sink

import com.xzc.caseclass.Event
import com.xzc.source.CustomSource
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.apache.flink.streaming.api.scala._
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Put, Table}
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}

/**
 * Created by Xu on 2022/11/29.
 * describe: 
 */
object SinkToHBase {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val dataStream: DataStream[Event] = env.addSource(new CustomSource)
    dataStream.addSink(new HBaseSink)
  }

  class HBaseSink extends RichSinkFunction[Event] {
    var connection: Connection = null
    var configuration: org.apache.hadoop.conf.Configuration = null
    var table: Table = null

    override def open(parameters: Configuration): Unit = {
      super.open(parameters)
      configuration = HBaseConfiguration.create()
      configuration.set("hbase.zookeeper.quorum", "hadoop102:2181")
      connection = ConnectionFactory.createConnection(configuration)
      table = connection.getTable(TableName.valueOf("table-name"))

    }

    override def invoke(value: Event): Unit = {
      val put = new Put("RowKey".getBytes()) //指定ROWKEY
      put.addColumn("columnFamily".getBytes(), "qualifier".getBytes, value.asInstanceOf[String].getBytes)
      table.put(put)
      table.close()
    }

    override def close(): Unit = {
      super.close()
      if (connection != null) connection.close()
    }
  }
}

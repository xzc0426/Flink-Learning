package com.xzc.sink

import com.xzc.caseclass.Event
import com.xzc.source.CustomSource
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.elasticsearch.{ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests

import java.util

/**
 * Created by Xu on 2022/11/29.
 * describe: 
 */
object SinkToES {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val dataStream: DataStream[Event] = env.addSource(new CustomSource)
    val httpHost = new util.ArrayList[HttpHost]()
    httpHost.add(new HttpHost("hadoop102", 9200))
    dataStream.addSink(new ElasticsearchSink.Builder[Event](httpHost, new elasticsearchSinkFuction).build())
    env.execute()
  }


  class elasticsearchSinkFuction extends ElasticsearchSinkFunction[Event] {

    override def process(t: Event, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
      //创建保存数据的map
      val stringToInt = new util.HashMap[String, Int]()
      stringToInt.put(t.name, t.age)
      //构建数据发送http请求
      val indexRequest: IndexRequest = Requests.indexRequest().index("Event").source(stringToInt).`type`("event")
      //发送请求
      requestIndexer.add(indexRequest)

    }
  }
}

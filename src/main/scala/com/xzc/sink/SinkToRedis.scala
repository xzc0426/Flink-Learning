package com.xzc.sink

import com.xzc.caseclass.EventData
import com.xzc.source.CustomSource
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

/**
 * Created by Xu on 2022/11/29.
 * describe: 
 */
object SinkToRedis {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val dataStream: DataStream[EventData] = env.addSource(new CustomSource)
    //创建连接配置
    val jedisPoolConfig: FlinkJedisPoolConfig = new FlinkJedisPoolConfig.Builder().setHost("hadoop102").build()

    dataStream.addSink(new RedisSink[EventData](jedisPoolConfig, new CustomRedisMapper))

    env.execute()

  }


  class CustomRedisMapper extends RedisMapper[EventData] {
    override def getCommandDescription: RedisCommandDescription = {
      new RedisCommandDescription(RedisCommand.HSET, "Event")
    }

    override def getKeyFromData(t: EventData): String = t.name

    override def getValueFromData(t: EventData): String = t.age.toString
  }
}

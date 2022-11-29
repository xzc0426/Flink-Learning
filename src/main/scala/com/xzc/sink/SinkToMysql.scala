package com.xzc.sink

import com.xzc.caseclass.Event
import com.xzc.source.CustomSource
import org.apache.flink.connector.jdbc.{JdbcConnectionOptions, JdbcSink, JdbcStatementBuilder}
import org.apache.flink.streaming.api.scala._

import java.sql.PreparedStatement

/**
 * Created by Xu on 2022/11/29.
 * describe: 
 */
object SinkToMysql {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val dataStream: DataStream[Event] = env.addSource(new CustomSource)

    dataStream.addSink(JdbcSink.sink(
      "INSERT INTO event (name,age) VALUES(?,?)",
      new JdbcStatementBuilder[Event] {
        override def accept(t: PreparedStatement, u: Event): Unit = {
          t.setString(1, u.name)
          t.setInt(2, u.age)
        }
      },
      new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
        .withUrl("jdbc:mysql://localhost:3306/test")
        .withDriverName("com.mysql.jdbc.Driver")
        .withUsername("root")
        .withPassword("")
        .build()
    ))

    env.execute()
  }
}

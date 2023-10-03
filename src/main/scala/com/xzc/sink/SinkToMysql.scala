package com.xzc.sink

import com.xzc.caseclass.Event
import com.xzc.source.CustomSource
import org.apache.flink.configuration.Configuration
import org.apache.flink.connector.jdbc.{JdbcConnectionOptions, JdbcSink, JdbcStatementBuilder}
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.apache.flink.streaming.api.scala._

import java.sql.{Connection, DriverManager, PreparedStatement}

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
        .withPassword("123456")
        .build()
    ))

    dataStream.addSink(new MySQLSink)

    env.execute()
  }

  class MySQLSink extends RichSinkFunction[Event] {
    var conn: Connection = _
    var ps: PreparedStatement = _
    val INSERT_CASE: String =
      """
        |INSERT INTO event (name,age) VALUES(?,?)
        |""".stripMargin

    override def open(parameters: Configuration): Unit = {
      // 加载驱动
      Class.forName("com.mysql.jdbc.Driver")
      // 数据库连接
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useSSL=false", "root", "123456")
      ps = conn.prepareStatement(INSERT_CASE)
    }

    override def invoke(value: Event): Unit = {
      try {
        ps.setString(1, value.name)
        ps.setLong(2, value.age)
        ps.addBatch()
        ps.executeBatch()
      } catch {
        case _: Exception =>
      }
    }

    override def close(): Unit = {
      super.close()
      if (ps != null) ps.close()
      if (conn != null) conn.close()

    }
  }
}

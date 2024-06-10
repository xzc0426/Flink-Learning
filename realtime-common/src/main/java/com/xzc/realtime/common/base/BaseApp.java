package com.xzc.realtime.common.base;

/**
 * Created by Xu on 2024/6/3.
 * Describe:
 */

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import static org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION;

public abstract class BaseApp {
    public abstract void handle(StreamExecutionEnvironment env, DataStreamSource<String> stream);

    public void start(int port, int parallelism, String ckAndGroupId, String topicName) {
        //   环境准备
        //   设置操作 Hadoop 的用户名为 Hadoop
        System.setProperty("HADOOP_USER_NAME", "xuzc");

        //   获取流处理环境，并指定本地测试时启动 WebUI 所绑定的端口
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", port);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);

        //  设置并行度
        env.setParallelism(parallelism);

        // 状态后端及检查点相关配置
        //   设置状态后端
        env.setStateBackend(new HashMapStateBackend());

        //   开启 checkpoint
        env.enableCheckpointing(5000);
        //   设置 checkpoint 模式: 精准一次
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //  checkpoint 存储
        env.getCheckpointConfig().setCheckpointStorage("hdfs://hadoop102:8020/flink/" + ckAndGroupId);
        //  checkpoint 并发数
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //  checkpoint 之间的最小间隔
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(5000);
        //  checkpoint  的超时时间
        env.getCheckpointConfig().setCheckpointTimeout(10000);
        //  job 取消时 checkpoint 保留策略
        env.getCheckpointConfig().setExternalizedCheckpointCleanup(RETAIN_ON_CANCELLATION);

        //  从 Kafka 目标主题读取数据，封装为流
        DataStreamSource<String> source = env.fromSource(
                KafkaSource.<String>builder()
                        .setBootstrapServers("hadoop102:9092")
                        .setTopics(topicName)
                        .setGroupId(ckAndGroupId)
                        .setStartingOffsets(OffsetsInitializer.latest())
                        .setValueOnlyDeserializer(new SimpleStringSchema())
                        .build()
                , WatermarkStrategy.noWatermarks()
                , topicName);

        //  执行具体处理逻辑
        handle(env, source);

        //  执行 Job
        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

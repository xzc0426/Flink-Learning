# Flink-Learning
## Flink 学习之路

### Yarn 模式
1. 安装包准备

- Flink 1.8 之前想要运行在yarn上需要下载带有hadoop支持的安装包

- Flink 1.8 之后，单独下载hadoop依赖，放在lib目录下

- Flink 1.11 之后，配置环境变量即可
2. 会话模式

    Flink1.11.0 版本不再使用-n 参数和-s 参数分别指定 TaskManager 数量和 slot 数量， YARN 会按照需求动态分配 TaskManager 和 slot。所以从这个意义上讲， YARN 的会话模式也
不会把集群资源固定，同样是动态分配的。


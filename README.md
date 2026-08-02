# 安行代驾服务系统

基于 Spring Cloud 微服务架构的在线代驾服务平台。

## 技术栈

| 技术 | 版本 |
|---|---|
| JDK | 17 |
| Spring Boot | 3.0.5 |
| Spring Cloud | 2022.0.2 |
| Spring Cloud Alibaba | 2022.0.0.0-RC2 |
| Nacos | 注册中心 + 配置中心 |
| Gateway | 服务网关 |
| OpenFeign | 远程调用 |
| Sentinel | 流量控制 |
| MyBatis-Plus | 3.5.3.1（持久层） |
| MySQL | 8.0 |
| Redis + Redisson | 缓存 + 分布式锁 |
| RabbitMQ | 延迟消息（x-delayed-message 插件） |
| Seata | 分布式事务 |
| XXL-Job | 分布式任务调度 |
| Drools | 规则引擎 |
| MongoDB | 日志存储 |
| Knife4j | 接口文档 |
| 微信支付 V3 | 在线支付 |
| 腾讯云 COS / OCR / CI / 人脸识别 | 对象存储 / 文字识别 / 内容审核 / 司机人脸核验 |

## 项目结构

```
daijia-parent
├── common/                        # 公共模块
│   ├── common-util/              # 通用工具类、异常、返回码
│   ├── common-log/               # 日志切面
│   ├── rabbit-util/              # RabbitMQ 工具
│   ├── service-util/             # 服务公共配置（Nacos、Redis、Redisson 等）
│   └── spring-security/          # 安全认证（Token 过滤）
├── model/                         # 实体/VO/枚举/表单
├── service/                       # 业务微服务
│   ├── service-coupon/           # 优惠券服务
│   ├── service-customer/         # 乘客服务
│   ├── service-dispatch/         # 派单服务
│   ├── service-driver/           # 司机服务
│   ├── service-map/              # 地图服务
│   ├── service-mq/               # 消息队列服务
│   ├── service-order/            # 订单服务
│   ├── service-payment/          # 支付服务
│   ├── service-rules/            # 规则引擎服务（Drools）
│   └── service-system/           # 系统服务
├── service-client/                # Feign 远程调用客户端
├── server-gateway/                # Spring Cloud Gateway 网关
├── web/                           # 对外接口聚合层
│   ├── web-customer/             # 乘客端
│   ├── web-driver/               # 司机端
│   └── web-mgr/                  # 管理后台
└── nacos-config/                  # Nacos 配置文件（DEFAULT_GROUP）
```

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0
- Redis
- RabbitMQ（需安装 **rabbitmq_delayed_message_exchange** 插件）
- Nacos
- MongoDB（订单日志）
- XXL-Job（可选，派单任务调度）

## 核心业务

- 乘客端：下单、支付、优惠券领取/使用、订单查询
- 司机端：抢单、开始/结束代驾、订单查询
- 派单系统：基于规则引擎自动派单
- 订单超时取消：RabbitMQ 延迟消息（15 分钟自动取消）
- 微信支付：支付回调、分账处理

## 快速开始

1. 配置 Nacos（注册中心 + 配置中心），将 `nacos-config/DEFAULT_GROUP/` 中的 yaml 配置导入
2. 启动各基础设施（MySQL、Redis、RabbitMQ、Nacos、MongoDB）
3. 启动业务服务（按需）：
   ```bash
   # 网关
   mvn -pl server-gateway spring-boot:run
   # 订单服务
   mvn -pl service/service-order spring-boot:run
   # ... 其他服务同理
   ```
4. 启动 web 聚合层（乘客/司机/管理后台）

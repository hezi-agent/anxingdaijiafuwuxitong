package com.atguigu.daijia.order.config;

import com.atguigu.daijia.common.constant.MqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 延迟插件配置（x-delayed-message 交换机）
 * 用于实现订单 15 分钟超时自动取消
 */
@Configuration
public class CancelOrderRabbitConfig {

    /**
     * 延迟交换机：使用 RabbitMQ 延迟消息插件，交换机类型为 x-delayed-message
     * 通过 x-delayed-type 声明内部路由类型为 direct
     */
    @Bean
    public CustomExchange cancelOrderExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MqConst.EXCHANGE_CANCEL_ORDER, "x-delayed-message", true, false, args);
    }

    /**
     * 取消订单队列：持久化
     */
    @Bean
    public Queue cancelOrderQueue() {
        return new Queue(MqConst.QUEUE_CANCEL_ORDER, true);
    }

    /**
     * 队列绑定到延迟交换机
     */
    @Bean
    public Binding cancelOrderBinding() {
        return BindingBuilder.bind(cancelOrderQueue())
                .to(cancelOrderExchange())
                .with(MqConst.ROUTING_CANCEL_ORDER)
                .noargs();
    }
}

package com.atguigu.daijia.order.receiver;

import com.atguigu.daijia.common.constant.MqConst;
import com.atguigu.daijia.order.service.OrderInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单超时取消消息消费者
 * 监听 RabbitMQ 延迟队列，实现订单 15 分钟超时自动取消
 */
@Slf4j
@Component
public class CancelOrderReceiver {

    @Autowired
    private OrderInfoService orderInfoService;

    @RabbitListener(queues = MqConst.QUEUE_CANCEL_ORDER)
    public void cancelOrder(String orderId, Message message, Channel channel) {
        try {
            log.info("订单超时取消消息，orderId: {}", orderId);
            //取消订单
            orderInfoService.orderCancel(Long.parseLong(orderId));
        } catch (Exception e) {
            log.error("订单超时取消失败，orderId: {}", orderId, e);
        }
    }
}

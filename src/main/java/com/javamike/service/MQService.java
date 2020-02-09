package com.javamike.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MQService {

    /**
     * 监听消息队列来接收数据
     *
     * @param message
     */
    @RabbitListener(queuesToDeclare=@Queue("fanout.queue"))
    public void receive(Message message) {
        System.out.println("收到fanout.queue消息 : " + new String(message.getBody()));
    }

}

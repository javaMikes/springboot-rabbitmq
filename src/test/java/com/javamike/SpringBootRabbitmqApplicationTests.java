package com.javamike;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRabbitmqApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;

    /**
     * 创建rabbitmq发送消息需要的交换器，队列和绑定
     */
    @Test
    public void create() {
        //创建direct类型Exchange，名称为exchange.direct
        amqpAdmin.declareExchange(new DirectExchange("exchange.direct"));
        //创建fanout类型Exchange，名称为exchange.fanout
        amqpAdmin.declareExchange(new FanoutExchange("exchange.fanout"));
        //创建topic类型Exchange，名称为exchange.topic
        amqpAdmin.declareExchange(new TopicExchange("exchange.topic"));

        //创建名称为direct Queue的队列，durable为true表示持久化
        amqpAdmin.declareQueue(new Queue("direct.queue", true));
        //创建名称为fanout Queue的队列，durable为true表示持久化
        amqpAdmin.declareQueue(new Queue("fanout.queue", true));

        //绑定Queue
        amqpAdmin.declareBinding(new Binding("direct.queue", Binding.DestinationType.QUEUE, "exchange.direct", "direct.queue", null));
        amqpAdmin.declareBinding(new Binding("fanout.queue", Binding.DestinationType.QUEUE, "exchange.direct", "fanout.queue", null));
        amqpAdmin.declareBinding(new Binding("direct.queue", Binding.DestinationType.QUEUE, "exchange.fanout", "", null));
        amqpAdmin.declareBinding(new Binding("fanout.queue", Binding.DestinationType.QUEUE, "exchange.fanout", "", null));
        amqpAdmin.declareBinding(new Binding("direct.queue", Binding.DestinationType.QUEUE, "exchange.topic", "direct.#", null));
        amqpAdmin.declareBinding(new Binding("fanout.queue", Binding.DestinationType.QUEUE, "exchange.topic", "direct.*", null));

//        //删除Queue
//        amqpAdmin.removeBinding(new Binding("direct.queue", Binding.DestinationType.QUEUE, "exchange.direct", "direct.queue", null));
//        amqpAdmin.removeBinding(new Binding("fanout.queue", Binding.DestinationType.QUEUE, "exchange.direct", "fanout.queue", null));
//        amqpAdmin.removeBinding(new Binding("direct.queue", Binding.DestinationType.QUEUE, "exchange.fanout", "", null));
//        amqpAdmin.removeBinding(new Binding("fanout.queue", Binding.DestinationType.QUEUE, "exchange.fanout", "", null));
//        amqpAdmin.removeBinding(new Binding("direct.queue", Binding.DestinationType.QUEUE, "exchange.topic", "direct.#", null));
//        amqpAdmin.removeBinding(new Binding("fanout.queue", Binding.DestinationType.QUEUE, "exchange.topic", "direct.*", null));
//
//        //删除Exchange
//        amqpAdmin.deleteExchange("exchange.direct");
//        amqpAdmin.deleteExchange("exchange.fanout");
//        amqpAdmin.deleteExchange("exchange.topic");
//
//        //删除Queue
//        amqpAdmin.deleteQueue("direct.queue");
//        amqpAdmin.deleteQueue("fanout.queue");

    }

    /**
     * 测试direct类型的exchange
     */
    @Test
    public void send2Direct() {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "这是一条direct消息");
        map.put("data", Arrays.asList("Hello World", 123456, true));

        //对象被序列化以后发送出去
        rabbitTemplate.convertAndSend("exchange.direct", "direct.queue", map);

    }

    /**
     * 测试fanout类型的exchange
     */
    @Test
    public void send2Fanout() {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "这是一条fanout消息");
        map.put("data", Arrays.asList("Hello World", 123456, true));

        //对象被序列化以后发送出去
        rabbitTemplate.convertAndSend("exchange.fanout", "", map);

    }

    /**
     * 测试topic类型的exchange
     */
    @Test
    public void send2Topic() {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "这是一条topic消息");
        map.put("data", Arrays.asList("Hello World", 123456, true));

        //对象被序列化以后发送出去
        rabbitTemplate.convertAndSend("exchange.topic", "direct.test", map);

    }

    /**
     * 接收队列的消息
     */
    @Test
    public void receive() {
        //根据队列名称获取消息
        Object o = rabbitTemplate.receiveAndConvert("direct.queue");
        o.getClass();
        System.out.println(o.getClass());
        System.out.println(o);
    }

}
package com.example.miaosha.rabbitmp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class MQConfig {
    public static final String MIAOSHA_QUEUE = "miaosha_queue";
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "top.queue1";
    public static final String TOPIC_QUEUE2 = "top.queue2";
    public static final String TOPIC_EXCHANGE = "topic.exchange";
    /**
     * Direct模式
     * */
    @Bean
    public Queue queue() {
        return new Queue(MIAOSHA_QUEUE,true);
    }
//    /**
//     * Topic模式
//     * */
//    @Bean
//    public Queue topicQueue1() {
//        return new Queue(TOPIC_QUEUE1,true);
//    }
//    @Bean
//    public Queue topicQueue2() {
//        return new Queue(TOPIC_QUEUE2,true);
//    }
//    @Bean
//    public TopicExchange topicExchange() {
//        return new TopicExchange(TOPIC_EXCHANGE);
//    }
//    @Bean
//    public Binding topicBinding1(){
//        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
//    }
//    @Bean
//    public Binding topicBinding2(){
//        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
//    }
}

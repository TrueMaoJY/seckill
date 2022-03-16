package com.maomao.seckill.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author MaoJY
 * @create 2022-03-08 14:09
 * @Description:
 */
@Configuration
public class RabbitMQConfig {
    private static final String QUEUE= "seckillQueue";
    private static final String EXCHANGE="seckillExchange";
    @Bean
    public Queue queue(){
        return new Queue(QUEUE,true);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }
}
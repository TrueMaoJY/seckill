package com.maomao.seckill.rabbitMQ;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MaoJY
 * @create 2022-03-08 14:42
 * @Description:
 */
@Service
public class MQSender {
    @Autowired
    RabbitTemplate rabbitTemplate;
    public void sendSeckillMessage(String message){
        rabbitTemplate.convertAndSend("seckillExchange","seckill.message",message);
    }
}
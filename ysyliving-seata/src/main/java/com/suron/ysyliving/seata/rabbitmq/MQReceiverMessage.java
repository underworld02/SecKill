package com.suron.ysyliving.seata.rabbitmq;

import cn.hutool.json.JSONUtil;
import com.suron.ysyliving.seata.pojo.SeckillMessage;
import com.suron.ysyliving.seata.pojo.User;
import com.suron.ysyliving.seata.service.GoodsService;
import com.suron.ysyliving.seata.service.OrderService;
import com.suron.ysyliving.seata.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ysy
 * @version 1.0
 * MQReceiverMessage: 消息的接收者/消费者, 这里调用seckill方法()
 */
@Service
@Slf4j
public class MQReceiverMessage {

    //装配需要的组件/对象
    @Resource
    private GoodsService goodsService;
    @Resource
    private OrderService orderService;


    //接收消息，并完成下单
    @RabbitListener(queues = "seckillQueue")
    public void queue(String message) {

        log.info("接收到的消息是-->" + message);

        //老师解读，这里我们从队列中取出的是string
        //但是我们需要的是SeckillMessage, 因此需要一个工具类JSONUtil
        //在hutool依赖
        SeckillMessage seckillMessage =
                JSONUtil.toBean(message, SeckillMessage.class);
        //秒杀用户对象
        User user = seckillMessage.getUser();
        //秒杀的商品id
        Long goodsId = seckillMessage.getGoodsId();
        //通过商品id,得到对应的GoodsVo
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);

        //下单操作
        orderService.seckill(user, goodsVo);
    }

}

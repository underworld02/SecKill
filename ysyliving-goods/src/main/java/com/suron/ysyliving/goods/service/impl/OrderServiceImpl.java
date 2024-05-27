package com.suron.ysyliving.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.ysyliving.goods.mapper.OrderMapper;
import com.suron.ysyliving.goods.service.OrderService;
import com.suron.ysyliving.goods.service.SeckillGoodsService;
import com.suron.ysyliving.goods.service.SeckillOrderService;
import com.suron.ysyliving.goods.pojo.Order;
import com.suron.ysyliving.goods.pojo.SeckillGoods;
import com.suron.ysyliving.goods.pojo.SeckillOrder;
import com.suron.ysyliving.goods.pojo.User;
import com.suron.ysyliving.goods.util.MD5Util;
import com.suron.ysyliving.goods.util.UUIDUtil;
import com.suron.ysyliving.goods.vo.GoodsVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author ysy
 * @version 1.0
 */
@Service
public class OrderServiceImpl
        extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

    //装配我们需要的组件/对象
    @Resource
    private SeckillGoodsService seckillGoodsService;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SeckillOrderService seckillOrderService;

    @Resource
    private RedisTemplate redisTemplate;

    //完成秒杀
    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goodsVo) {

        //查询秒杀商品的库存,并-1
        SeckillGoods seckillGoods = seckillGoodsService.
                getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsVo.getId()));

        //完成一个基本的秒杀操作[这块不具原子性],后面在高并发的情况下,还会优化, 不用急
        //seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        //seckillGoodsService.updateById(seckillGoods);

        //老师分析
        //1. Mysql在默认的事务隔离级[REPEATABLE-READ]别下
        //2. 执行update语句时,会在事务中锁定要更新的行
        //3. 这样可以防止其它会话在同一行执行update,delete

        System.out.println("执行update==>" + user.getId());
        //老师说明：只要在更新成功时,返回true,否则返回false, 即更新后，受影响的行数>=1为T
        boolean update = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count=stock_count-1")
                .eq("goods_id", goodsVo.getId()).gt("stock_count", 0));

        if (!update) {//如果更新失败,说明已经没有库存了
            //把这个秒杀失败的信息-记录到Redis
            return null;
        }

        //生成普通订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);//老师就设置一个初始值
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);//老师就设置一个初始值
        order.setStatus(0);//老师就设置一个初始值-未支付
        order.setCreateDate(new Date());//设置成now

        //保存order
        orderMapper.insert(order);


        //生成秒杀商品订单-
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsVo.getId());
        //这里秒杀商品订单对应的order_id 是从上面你添加 order后获取到的
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());

        //保存seckillOrder
        seckillOrderService.save(seckillOrder);

        //将生成的秒杀订单，存入到Redis, 这样在查询某个用户是否已经秒杀了这个商品时，
        //直接到Redis中查询，起到优化效果
        //设计秒杀订单的key => order:用户id:商品id
        redisTemplate.opsForValue()
                .set("order:" + user.getId() + ":" + goodsVo.getId(), seckillOrder);

        return order;
    }

    //方法: 生成秒杀路径/值(唯一)
    @Override
    public String createPath(User user, Long goodsId) {
        //生成秒杀路径/值
        String path = MD5Util.md5(UUIDUtil.uuid());
        //将随机生成的路径保存到Redis, 设置一个超时时间60s
        //key的设计: seckillPath:userId:goodsId
        redisTemplate.opsForValue().set("seckillPath:"
                + user.getId() + ":" + goodsId, path, 60, TimeUnit.SECONDS);
        return path;
    }

    //方法: 对秒杀路径进行校验
    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user == null || goodsId < 0 || !StringUtils.hasText(path)) {
            return false;
        }

        //取出该用户秒杀该商品的路径
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    //方法: 验证用户输入的验证码是否正确
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (user == null || goodsId < 0 || !StringUtils.hasText(captcha)) {
            return false;
        }

        //从Redis取出验证码
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);

        return captcha.equals(redisCaptcha);
    }
}

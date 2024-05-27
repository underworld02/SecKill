package com.suron.ysyliving.user.controller;

import cn.hutool.json.JSONUtil;
//import com.seckill.config.AccessLimit;
import com.suron.ysyliving.user.config.AccessLimit;
import com.suron.ysyliving.user.rabbitmq.MQSenderMessage;
import com.suron.ysyliving.user.service.OrderService;
import com.suron.ysyliving.user.pojo.SeckillMessage;
import com.suron.ysyliving.user.pojo.SeckillOrder;
import com.suron.ysyliving.user.pojo.User;
import com.suron.ysyliving.user.service.GoodsService;
import com.suron.ysyliving.user.service.SeckillOrderService;
import com.suron.ysyliving.user.vo.GoodsVo;
import com.suron.ysyliving.user.vo.RespBean;
import com.suron.ysyliving.user.vo.RespBeanEnum;
import com.ramostear.captcha.HappyCaptcha;
import com.ramostear.captcha.common.Fonts;
import com.ramostear.captcha.support.CaptchaStyle;
import com.ramostear.captcha.support.CaptchaType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ysy
 * @version 1.0
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    //装配需要的组件/对象
    @Resource
    private GoodsService goodsService;

    @Resource
    private SeckillOrderService seckillOrderService;

    @Resource
    private OrderService orderService;

    @Resource
    private RedisTemplate redisTemplate;

    //定义map-记录秒杀商品是否还有库存
    private HashMap<Long, Boolean> entryStockMap = new HashMap<>();

    //装配消息的生产者/发送者
    @Resource
    private MQSenderMessage mqSenderMessage;

    //装配RedisScript
    @Resource
    private RedisScript<Long> script;



//    //方法: 处理用户抢购请求/秒杀
//    //说明：我们先完成一个V1.0版本，后面在高并发的情况下, 还要做优化
//    @RequestMapping("/doSeckill")
//    public String doSeckill(Model model, User user, Long goodsId) {
//
//        System.out.println("------秒杀V1.0-------");
//
//        if (user == null) {//用户没有登录
//            return "login";
//        }
//        //将user放入到model, 下一个模板可以使用
//        model.addAttribute("user", user);
//
//        //获取到goodsVo
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//
//        //判断库存
//        if (goodsVo.getStockCount() < 1) {//没有库存
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //判断用户是否复购-判断当前购买用户id和购买商品id是否已经在 商品秒杀表存在了
//        SeckillOrder seckillOrder = seckillOrderService.getOne
//                (new QueryWrapper<SeckillOrder>()
//                        .eq("user_id", user.getId()).eq("goods_id", goodsId));
//
//        if(seckillOrder != null) {
//            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //抢购
//        Order order = orderService.seckill(user, goodsVo);
//        if(order == null) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //进入到订单页
//        model.addAttribute("order",order);
//        model.addAttribute("goods",goodsVo);
//
//        System.out.println("------秒杀V1.0-------");
//
//        return "orderDetail"; //进入到订单详情页
//    }

//    ////方法: 处理用户抢购请求/秒杀
//    ////说明：我们先完成一个V2.0版本，后面在高并发的情况下, 还要做优化
//    @RequestMapping("/doSeckill")
//    public String doSeckill(Model model, User user, Long goodsId) {
//
//
//        if (user == null) {//用户没有登录
//            return "login";
//        }
//        //将user放入到model, 下一个模板可以使用
//        model.addAttribute("user", user);
//
//        //获取到goodsVo
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//
//        //判断库存
//        if (goodsVo.getStockCount() < 1) {//没有库存
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //判断用户是否复购-直接到Redis中,获取对应的秒杀订单,如果有,则说明已经抢购了
//        SeckillOrder o = (SeckillOrder) redisTemplate.opsForValue()
//                .get("order:" + user.getId() + ":" + goodsVo.getId());
//        if (null != o) { //说明该用户已经抢购了该商品
//            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //抢购
//        Order order = orderService.seckill(user, goodsVo);
//        if (order == null) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //进入到订单页
//        model.addAttribute("order", order);
//        model.addAttribute("goods", goodsVo);
//
//
//        return "orderDetail"; //进入到订单详情页
//    }

////    方法: 处理用户抢购请求/秒杀
////    说明：我们先完成一个V3.0版本，Redis库存预减
////    @RequestMapping("/doSeckill")
//    public String doSeckill(Model model, User user, Long goodsId) {
//
//
//        if (user == null) {//用户没有登录
//            return "login";
//        }
//        //将user放入到model, 下一个模板可以使用
//        model.addAttribute("user", user);
//
//        //获取到goodsVo
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//
//        //判断库存
//        if (goodsVo.getStockCount() < 1) {//没有库存
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//
//        //判断用户是否复购-直接到Redis中,获取对应的秒杀订单,如果有,则说明已经抢购了
//        SeckillOrder o = (SeckillOrder) redisTemplate.opsForValue()
//                .get("order:" + user.getId() + ":" + goodsVo.getId());
//        if (null != o) { //说明该用户已经抢购了该商品
//            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //请求
//
//        //库存预减, 如果在Redis中预减库存，发现秒杀商品已经没有了，就直接返回
//        //从而减少去执行 orderService.seckill() 请求,防止线程堆积, 优化秒杀/高并发
//        //老师提示: decrement()是具有原子性[!!]
//        Long decrement = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
//        if (decrement < 0) {//说明这个商品已经没有库存
//            //恢复库存为0
//            redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//
//        //抢购
//        Order order = orderService.seckill(user, goodsVo);
//        if (order == null) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //进入到订单页
//        model.addAttribute("order", order);
//        model.addAttribute("goods", goodsVo);
//
//
//        return "orderDetail"; //进入到订单详情页
//    }

//    ////方法: 处理用户抢购请求/秒杀
//    ////说明：我们先完成一个V4.0版本，加入内存标记优化秒杀
//    @RequestMapping("/doSeckill")
//    public String doSeckill(Model model, User user, Long goodsId) {
//
//
//        if (user == null) {//用户没有登录
//            return "login";
//        }
//        //将user放入到model, 下一个模板可以使用
//        model.addAttribute("user", user);
//
//        //获取到goodsVo
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//
//        //判断库存
//        if (goodsVo.getStockCount() < 1) {//没有库存
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//
//        //判断用户是否复购-直接到Redis中,获取对应的秒杀订单,如果有,则说明已经抢购了
//        SeckillOrder o = (SeckillOrder) redisTemplate.opsForValue()
//                .get("order:" + user.getId() + ":" + goodsVo.getId());
//        if (null != o) { //说明该用户已经抢购了该商品
//            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //对map进行判断[内存标记],如果商品在map已经标记为没有库存，则直接返回，无需进行Redis预减
//        if(entryStockMap.get(goodsId)) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //库存预减, 如果在Redis中预减库存，发现秒杀商品已经没有了，就直接返回
//        //从而减少去执行 orderService.seckill() 请求,防止线程堆积, 优化秒杀/高并发
//        //老师提示: decrement()是具有原子性[!!]
//        Long decrement = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
//        if (decrement < 0) {//说明这个商品已经没有库存
//
//            //说明当前秒杀的商品，已经没有库存
//            entryStockMap.put(goodsId, true);
//
//            //恢复库存为0
//            redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//
//        //抢购
//        Order order = orderService.seckill(user, goodsVo);
//        if (order == null) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //进入到订单页
//        model.addAttribute("order", order);
//        model.addAttribute("goods", goodsVo);
//
//
//        return "orderDetail"; //进入到订单详情页
//    }

    //方法: 处理用户抢购请求/秒杀
    //说明：我们先完成一个V5.0版本，加入消息队列,实现秒杀的异步请求
//    @RequestMapping("/doSeckill")
//    public String doSeckill(Model model, User user, Long goodsId) {
//        if (user == null) {//用户没有登录
//            return "login";
//        }
//        //将user放入到model, 下一个模板可以使用
//        model.addAttribute("user", user);
//
//        //获取到goodsVo
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//
//        //判断库存
//        if (goodsVo.getStockCount() < 1) {//没有库存
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//
//        //判断用户是否复购-直接到Redis中,获取对应的秒杀订单,如果有,则说明已经抢购了
//        SeckillOrder o = (SeckillOrder) redisTemplate.opsForValue()
//                .get("order:" + user.getId() + ":" + goodsVo.getId());
//        if (null != o) { //说明该用户已经抢购了该商品
//            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
//            return "secKillFail";//错误页面
//        }
//
//        //对map进行判断[内存标记],如果商品在map已经标记为没有库存，则直接返回，无需进行Redis预减
//        if (entryStockMap.get(goodsId)) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
////        库存预减, 如果在Redis中预减库存，发现秒杀商品已经没有了，就直接返回
////        从而减少去执行 orderService.seckill() 请求,防止线程堆积, 优化秒杀/高并发
////        老师提示: decrement()是具有原子性[!!]
//        Long decrement = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
//        if (decrement < 0) {//说明这个商品已经没有库存
//
//            //说明当前秒杀的商品，已经没有库存
//            entryStockMap.put(goodsId, true);
//
//            //恢复库存为0
//            redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";//错误页面
//        }
//
////        //=====使用Redis分布式锁====
////        //老师说明
////        //1. 对于当前项目而言，使用redisTemplate.opsForValue().decrement() 就可以控制抢购,因为该方法具有原子性和隔离性
////        //2. 考虑到如果有比较多的操作，需要保证隔离性，也就是说，不是简单的-1,而是有多个操作
////        //   这样就需要扩大隔离性的范围，部分操作还需要原子性, 我们给小伙伴演示一下Redis分布式锁的使用
////        //3. 我们看看以前是如何使用Redis分布式锁的
////
////
////        //1 获取锁，setnx
////        //得到一个 uuid 值，作为锁的值
////        String uuid = UUID.randomUUID().toString();
////
////
////        Boolean lock =
////                redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS);
////        //2 获取锁成功
////        if (lock) {
////
////            //准备删除锁脚本
////            //String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
////            //DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
////            //redisScript.setScriptText(script);
////            //redisScript.setResultType(Long.class);
////
////            //写自己的业务-就可以有多个操作了
////            Long decrement = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
////            if (decrement < 0) {//说明这个商品已经没有库存
////                //说明当前秒杀的商品，已经没有库存
////                entryStockMap.put(goodsId, true);
////                //恢复库存为0
////                redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
////                //释放锁.-lua为什么使用redis+lua脚本释放锁前面讲过
////                redisTemplate.execute(script, Arrays.asList("lock"), uuid);
////                model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
////                return "secKillFail";//错误页面
////            }
////
////            //释放分布式锁
////            redisTemplate.execute(script, Arrays.asList("lock"), uuid);
////
////        } else {
////            //3 获取锁失败,返回个信息[本次抢购失败，请再次抢购...]
////            model.addAttribute("errmsg", RespBeanEnum.SEC_KILL_RETRY.getMessage());
////            return "secKillFail";//错误页面
////        }
//
//        //抢购,向消息队列发送秒杀请求,实现了秒杀异步请求
//        //这里我们发送秒杀消息后，立即快速返回结果[临时结果] - "比如排队中.."
//        //客户端可以通过轮询，获取到最终结果
//        //创建SeckillMessage
//        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
//        mqSenderMessage.sendSeckillMessage(JSONUtil.toJsonStr(seckillMessage));
//        model.addAttribute("errmsg", "排队中...");
//        return "secKillFail";
//    }

    //方法: 处理用户抢购请求/秒杀
    //说明：我们先完成一个V6.0版本，加入消秒杀安全,直接返回RespBean
    @RequestMapping("/{path}/doSeckill")
    @ResponseBody
    public RespBean doSeckill(@PathVariable String path, Model model, User user, Long goodsId) {


        if (user == null) {//用户没有登录
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        //这里我们增加一个判断逻辑，校验用户携带的path是否正确
        boolean b = orderService.checkPath(user, goodsId, path);
        if (!b) {//校验失败
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }

        //获取到goodsVo
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);

        //判断库存
        if (goodsVo.getStockCount() < 1) {//没有库存
            return RespBean.error(RespBeanEnum.ENTRY_STOCK);
        }


        //判断用户是否复购-直接到Redis中,获取对应的秒杀订单,如果有,则说明已经抢购了
        SeckillOrder o = (SeckillOrder) redisTemplate.opsForValue()
                .get("order:" + user.getId() + ":" + goodsVo.getId());
        if (null != o) { //说明该用户已经抢购了该商品
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        //对map进行判断[内存标记],如果商品在map已经标记为没有库存，则直接返回，无需进行Redis预减
        if (entryStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.ENTRY_STOCK);
        }

//        //库存预减, 如果在Redis中预减库存，发现秒杀商品已经没有了，就直接返回
//        //从而减少去执行 orderService.seckill() 请求,防止线程堆积, 优化秒杀/高并发
//        //老师提示: decrement()是具有原子性和隔离性的[!!]
//        Long decrement = redisTemplate.opsForValue().
//                decrement("seckillGoods:" + goodsId);
//        if (decrement < 0) {//说明这个商品已经没有库存
//            //说明当前秒杀的商品，已经没有库存
//            entryStockMap.put(goodsId, true);
//            //恢复库存为0
//            redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
//            return RespBean.error(RespBeanEnum.ENTRY_STOCK);
//        }
        //=====使用Redis分布式锁====
        //老师说明
        //1. 对于当前项目而言，使用redisTemplate.opsForValue().decrement() 就可以控制抢购,因为该方法具有原子性和隔离性
        //2. 考虑到如果有比较多的操作，需要保证隔离性，也就是说，不是简单的-1,而是有多个操作
        //   这样就需要扩大隔离性的范围，部分操作还需要原子性, 我们给小伙伴演示一下Redis分布式锁的使用
        //3. 我们看看以前是如何使用Redis分布式锁的


        //1 获取锁，setnx
        //得到一个 uuid 值，作为锁的值
        String uuid = UUID.randomUUID().toString();


        Boolean lock =
                redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS);
        //2 获取锁成功
        if (lock) {

            //准备删除锁脚本
            //String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            //DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            //redisScript.setScriptText(script);
            //redisScript.setResultType(Long.class);

            //写自己的业务-就可以有多个操作了
            Long decrement = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
            if (decrement < 0) {//说明这个商品已经没有库存
                //说明当前秒杀的商品，已经没有库存
                entryStockMap.put(goodsId, true);
                //恢复库存为0
                redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
                //释放锁.-lua为什么使用redis+lua脚本释放锁前面讲过
                redisTemplate.execute(script, Arrays.asList("lock"), uuid);
                model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
                return RespBean.error(RespBeanEnum.ENTRY_STOCK);//错误页面
            }

            //释放分布式锁
            redisTemplate.execute(script, Arrays.asList("lock"), uuid);

        } else {
            //3 获取锁失败,返回个信息[本次抢购失败，请再次抢购...]
            model.addAttribute("errmsg", RespBeanEnum.SEC_KILL_RETRY.getMessage());
            return RespBean.error(RespBeanEnum.SEC_KILL_RETRY);//错误页面
        }
        //抢购,向消息队列发送秒杀请求,实现了秒杀异步请求
        //这里我们发送秒杀消息后，立即快速返回结果[临时结果] - "比如排队中.."
        //客户端可以通过轮询，获取到最终结果
        //创建SeckillMessage
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSenderMessage.sendSeckillMessage(JSONUtil.toJsonStr(seckillMessage));
        return RespBean.error(RespBeanEnum.SEK_KILL_WAIT);

    }

    //方法: 获取秒杀路径
    @RequestMapping("/path")
    @ResponseBody
    /**
     * 老师解读 @AccessLimit(second = 5,maxCount = 5,needLogin = true)
     * 1. 使用注解的方式完成对用户的限流防刷-通用性和灵活性提高
     * 2. second = 5,maxCount = 5 说明是在5秒内可以访问的最大次数是5次
     * 3. needLogin = true 表示用户是否需要登录
     */
    @AccessLimit(second = 5, maxCount = 5, needLogin = true)
    public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request) {
        if (user == null || goodsId < 0 || !StringUtils.hasText(captcha)) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        //增加业务逻辑: 加入Redis计数器, 完成对用户的限流防刷
        //比如:5秒内访问次数超过了5次, 我们就认为是刷接口
        //这里老师先把代码写在方法中，后面我们使用注解提高使用的通用性
        //uri就是 localhost:8080/seckill/path 的 /seckill/path
        //String uri = request.getRequestURI();
        //ValueOperations valueOperations = redisTemplate.opsForValue();
        //String key = uri + ":" + user.getId();
        //Integer count = (Integer) valueOperations.get(key);
        //if (count == null) {//说明还没有key,就初始化，值为1, 过期时间为5秒
        //    valueOperations.set(key, 1, 5, TimeUnit.SECONDS);
        //} else if (count < 5) {//说明正常访问
        //    valueOperations.increment(key);
        //} else {//说明用户在刷接口
        //    return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REACHED);
        //}

        //增加一个业务逻辑-校验用户输入的验证码是否正确
        boolean check = orderService.checkCaptcha(user, goodsId, captcha);
        if (!check) {//如果校验失败
            return RespBean.error(RespBeanEnum.CAPTCHA_ERROR);
        }

        String path = orderService.createPath(user, goodsId);
        return RespBean.success(path);
    }


    //生成验证码-happyCaptcha
    @RequestMapping("/captcha")
    public void happyCaptcha(HttpServletRequest request, HttpServletResponse response, User user, Long goodsId) {
        //生成验证码，并输出
        //注意，该验证码，默认就保存到session中, key是 happy-captcha
        HappyCaptcha.require(request, response)
                .style(CaptchaStyle.ANIM)               //设置展现样式为动画
                .type(CaptchaType.NUMBER)               //设置验证码内容为数字
                .length(6)                              //设置字符长度为6
                .width(220)                             //设置动画宽度为220
                .height(80)                             //设置动画高度为80
                .font(Fonts.getInstance().zhFont())     //设置汉字的字体
                .build().finish();                      //生成并输出验证码

        //把验证码的值，保存Redis [考虑项目分布式], 设置了验证码的失效时间100s
        //key: captcha:userId:goodsId
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId
                , (String) request.getSession().getAttribute("happy-captcha"), 100, TimeUnit.SECONDS);
    }

    //该方法是在SeckillController类/对象的所有属性，都是初始化后，自动执行的
    //这里我们就可以将所有秒杀商品的库存量，加载到Redis
    @Override
    public void afterPropertiesSet() throws Exception {

        //查询所有的秒杀商品
        List<GoodsVo> list = goodsService.findGoodsVo();
        //先判断是否为空
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //遍历List,然后将秒杀商品的库存量，放入到Redis
        //秒杀商品库存量对应key : seckillGoods:商品id
        list.forEach(goodsVo -> {

            redisTemplate.opsForValue()
                    .set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());

            //初始化map
            //如果goodsId : false 表示有库存
            //如果goodsId : true 表示没有库存
            entryStockMap.put(goodsVo.getId(), false);

        });

    }
}

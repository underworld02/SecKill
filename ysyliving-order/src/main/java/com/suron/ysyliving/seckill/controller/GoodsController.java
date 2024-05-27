package com.suron.ysyliving.seckill.controller;

import com.suron.ysyliving.seckill.feignClient.GoodsClient;
import com.suron.ysyliving.seckill.pojo.User;
import com.suron.ysyliving.seckill.service.GoodsService;
import com.suron.ysyliving.seckill.service.UserService;
import com.suron.ysyliving.seckill.vo.GoodsVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author ysy
 * @version 1.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsClient goodsClient; // 注入 service 中 通过 @FeignClient 定义好的接口

    //装配UserService
    @Resource
    private UserService userService;

    //装配GoodsService
    @Resource
    private GoodsService goodsService;

    //装配RedisTemplate
    @Resource
    private RedisTemplate redisTemplate;

    //手动渲染需要的模板解析器
    @Resource
    private ThymeleafViewResolver thymeleafViewResolver;

    ////进入到商品列表页-到DB查询
    ////提示： HttpSession ,Model, @CookieValue("userTicket")
    ////在讲解Springboot详解过，小伙伴自己回顾
//    @RequestMapping("/toList")
//    ////public String toList(HttpSession session, Model model,
//    ////                     @CookieValue("userTicket") String ticket) { //1
//    ////public String toList(Model model,
//    ////                     @CookieValue("userTicket") String ticket,
//    ////                     HttpServletRequest request,
//    ////                     HttpServletResponse response) {  //2
//    public String toList(Model model,
//                         User user) {
//
//        ////如果cookie没有生成
//        //if (!StringUtils.hasText(ticket)) {
//        //    return "login";
//        //}
//        //
//        ////通过ticket获取session中存放的user
//        ////User user = (User)session.getAttribute(ticket); //1
//        //
//        ////从Redis来获取用户
//        //User user = userService.getUserByCookie(ticket, request, response);
//        if (null == user) { //用户没有成功登录
//            return "login";
//        }
//
//        //将user放入到model, 携带该下一个模板使用
//        model.addAttribute("user", user);
//        //将商品列表信息,放入到model,携带该下一个模板使用
//        model.addAttribute("goodsList", goodsService.findGoodsVo());
//        return "goodsList";
//    }


    //进入到商品列表页-使用Redis优化
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")///《《《《《《《《《《《《《《
    @ResponseBody///《《《《《《《《《《《《《《
    public String toList(Model model,
                         User user,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        if (null == user) { //用户没有成功登录
            try {
                response.sendRedirect("http://www.suron.com.cn/api/seckill/login/toLogin");  // 重定向到/login URL
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null; // 由于已经发起重定向，这里返回null或者任何字符串都不会被处理
        }

        //先到redis获取页面-如果有,直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (StringUtils.hasText(html)) {
            return html;
        }

        //将user放入到model, 携带该下一个模板使用
        model.addAttribute("user", user);
        //将商品列表信息,放入到model,携带该下一个模板使用
        try {
            model.addAttribute("goodsList", goodsClient.findGoodsVo());//替换为FeignClient
        }
        catch (Exception e){
            System.out.println(e);
        }



        //如果从Redis没有获取到页面, 手动渲染页面，并存入到redis
        WebContext webContext =
                new WebContext(request, response, request.getServletContext(),
                        request.getLocale(), model.asMap());

        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);//对应哪个html模板
        if (StringUtils.hasText(html)) {
            //将页面保存到redis, 设置每60s更新一次, 该页面60s失效,Redis会清除该页面
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS); //redis的key名称
        }
        return html;
    }


    //方法： 进入到商品详情页面-根据goodsId-到DB查询
    //说明: User user 是通过我们自定义的参数解析器处理，返回
    //@PathVariable Long goodsId 路径变量，是用户点击详情时，给携带过来的
//    @RequestMapping("/toDetail/{goodsId}")
//    public String toDetail(Model model, User user, @PathVariable Long goodsId) {
//
//        //判断user
//        if (user == null) {//说明没有登录
//            return "login";
//        }
//
//        //将user放入到model
//        model.addAttribute("user", user);
//        //通过goodsId, 获取指定的秒杀商品信息
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        //将查询到的goodsVo放入到model,携带给下一个模板页使用
//        model.addAttribute("goods", goodsVo);
//
//        //说明:返回秒杀商品详情时，同时返回该商品的秒杀状态和秒杀的剩余时间
//        //为了配合前端展示秒杀商品的状态 - 这里依然有一个业务设计
//        //1. 变量 secKillStatus 秒杀状态 0:秒杀未开始 1: 秒杀进行中 2: 秒杀已经结束
//        //2. 变量 remainSeconds 剩余秒数: >0: 表示还有多久开始秒杀: 0: 秒杀进行中 -1: 表示秒杀已经结束
//
//        //秒杀开始时间
//        Date startDate = goodsVo.getStartDate();
//        //秒杀结束时间
//        Date endDate = goodsVo.getEndDate();
//        //当前时间
//        Date nowDate = new Date();
//
//        //秒杀状态
//        int secKillStatus = 0;
//        //秒杀剩余时间-秒
//        int remainSeconds = 0;
//
//        //如果nowDate 在 startDate 前, 说明还没有开始秒杀
//        if (nowDate.before(startDate)) {
//            //得到还有多少秒开始秒杀
//            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
//        } else if (nowDate.after(endDate)) {//说明秒杀已经结束
//            secKillStatus = 2;
//            remainSeconds = -1;
//        } else { //秒杀进行中
//            secKillStatus = 1;
//            remainSeconds = 0;
//        }
//
//        //将secKillStatus 和 remainSeconds放入到model ,携带给模板页使用
//        model.addAttribute("secKillStatus", secKillStatus);
//        model.addAttribute("remainSeconds", remainSeconds);
//
//        return "goodsDetail";
//    }

    //方法： 进入到商品详情页面-根据goodsId-使用Redis进行优化
    @RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(Model model,
                           User user,
                           @PathVariable Long goodsId,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        //判断user
        if (user == null) {//说明没有登录
            return "login";
        }

        //先到redis获取页面-如果有,直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if (StringUtils.hasText(html)) {
            return html;
        }

        //将user放入到model
        model.addAttribute("user", user);
        //通过goodsId, 获取指定的秒杀商品信息
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        //将查询到的goodsVo放入到model,携带给下一个模板页使用
        model.addAttribute("goods", goodsVo);

        //说明:返回秒杀商品详情时，同时返回该商品的秒杀状态和秒杀的剩余时间
        //为了配合前端展示秒杀商品的状态 - 这里依然有一个业务设计
        //1. 变量 secKillStatus 秒杀状态 0:秒杀未开始 1: 秒杀进行中 2: 秒杀已经结束
        //2. 变量 remainSeconds 剩余秒数: >0: 表示还有多久开始秒杀: 0: 秒杀进行中 -1: 表示秒杀已经结束

        //秒杀开始时间
        Date startDate = goodsVo.getStartDate();
        //秒杀结束时间
        Date endDate = goodsVo.getEndDate();
        //当前时间
        Date nowDate = new Date();

        //秒杀状态
        int secKillStatus = 0;
        //秒杀剩余时间-秒
        int remainSeconds = 0;

        //如果nowDate 在 startDate 前, 说明还没有开始秒杀
        if (nowDate.before(startDate)) {
            //得到还有多少秒开始秒杀
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {//说明秒杀已经结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else { //秒杀进行中
            secKillStatus = 1;
            remainSeconds = 0;
        }

        //将secKillStatus 和 remainSeconds放入到model ,携带给模板页使用
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        //如果从Redis没有获取到页面, 手动渲染页面，并存入到redis
        WebContext webContext =
                new WebContext(request, response, request.getServletContext(),
                        request.getLocale(), model.asMap());

        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
        if (StringUtils.hasText(html)) {
            //将页面保存到redis, 设置每60s更新一次, 该页面60s失效,Redis会清除该页面
            valueOperations.set("goodsDetail:" + goodsId, html, 60, TimeUnit.SECONDS);
        }

        return html;
    }

}

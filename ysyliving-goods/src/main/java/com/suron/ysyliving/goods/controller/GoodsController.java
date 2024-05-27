package com.suron.ysyliving.goods.controller;


import com.suron.ysyliving.goods.pojo.Goods;
import com.suron.ysyliving.goods.pojo.User;
import com.suron.ysyliving.goods.service.GoodsService;
import com.suron.ysyliving.goods.service.SearchService;
import com.suron.ysyliving.goods.vo.*;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ysy
 * @version 1.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {



//    //装配UserService
//    @Resource
//    private UserService userService;

    //装配GoodsService
    @Resource
    private GoodsService goodsService;

    //装配RedisTemplate
    @Resource
    private RedisTemplate redisTemplate;

    //手动渲染需要的模板解析器
    @Resource
    private ThymeleafViewResolver thymeleafViewResolver;

    @Resource
    private SearchService searchService;

    @PostMapping(value = "/search/page")///《《《《《《《《《《《《《《
    @ResponseBody
    public ResponseEntity<PageResult<Product>> search(@RequestBody SearchRequest request) { //
        PageResult<Product> products = searchService.searchProducts(request);
//        Map<String, Object> response = new HashMap<>();
////        response.put("total", page.getTotalElements());
////        response.put("totalPage", page.getTotalPages());
////        response.put("items", page.getContent());
////        // 可以添加更多的数据，如分类和品牌信息
////        List<Product> items = new ArrayList<>();
////        for (int i = 0; i < 100; i++) {
////            Product p = new Product(1l,"水壶","dio水壶","/api/seckill/imgs/kitchen.jpg","det",22.34,22);
////            items.add(p);
////        }
//
//        response.put("total",100);
//        response.put("totalPage",5);
//        response.put("items",5);
        System.out.println(products);
        return ResponseEntity.ok(products); //ResponseEntity.ok(new PageResult<>());//
    }

    @RequestMapping(value = "/showGoods")///《《《《《《《《《《《《《《
    public String showGoodsList(@RequestParam(name = "searchText", required = false) String searchText,Model model,
                                         User user,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        if (null == user) { //用户没有成功登录
            try {
                response.sendRedirect("/login");  // 重定向到/login URL
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null; // 由于已经发起重定向，这里返回null或者任何字符串都不会被处理
        }

        model.addAttribute("searchText",searchText);
        return "search";
    }


    @RequestMapping(value = "/goodsvo")///《《《《《《《《《《《《《《
    @ResponseBody
    public List<GoodsVo> getSecGoodsList(Model model,
                                         User user,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        if (null == user) { //用户没有成功登录
            try {
                response.sendRedirect("/login");  // 重定向到/login URL
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null; // 由于已经发起重定向，这里返回null或者任何字符串都不会被处理
        }

        return goodsService.findGoodsVo();
    }

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
                response.sendRedirect("/login");  // 重定向到/login URL
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

package com.suron.ysyliving.seckill.controller;

import com.suron.ysyliving.seckill.pojo.Goods;
import com.suron.ysyliving.seckill.pojo.SeckillGoods;
import com.suron.ysyliving.seckill.pojo.User;
import com.suron.ysyliving.seckill.service.SeckillGoodsService;
import com.suron.ysyliving.seckill.service.UserService;
import com.suron.ysyliving.seckill.vo.LoginVo;
import com.suron.ysyliving.seckill.vo.RespBean;
import com.suron.ysyliving.seckill.vo.SecGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ysy
 * @version 1.0
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    //装配UserService
    @Resource
    private UserService userService;

    @Resource
    private SeckillGoodsService seckillGoodsService;

    //编写方法,可以进入到登录页面
    @RequestMapping("/toLogin") //到templates/login.html
    public String toLogin() {
        return "login";
    }

    //方法: 处理用户登录请求
    //回顾springboot的相关知识
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        return userService.doLogin(loginVo, request, response);
    }

    @RequestMapping("/secIndex")
    public String toIndex(Model model,
                            User user,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        //log.info("{}", loginVo);//观察数据
        List<SecGoodsVo> randomProducts1 = seckillGoodsService.getRandomProducts(5);
        List<SecGoodsVo> randomProducts2 = seckillGoodsService.getRandomProducts(5);

        model.addAttribute("randomProducts1",randomProducts1);
        model.addAttribute("randomProducts2",randomProducts2);
        model.addAttribute("mobile",user.getNickname());
        model.addAttribute("user",user);

        return "secIndex";
    }

}

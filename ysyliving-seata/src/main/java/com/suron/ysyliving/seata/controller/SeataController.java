package com.suron.ysyliving.seata.controller;

import com.suron.ysyliving.seata.feignClient.GoodsClient;
import com.suron.ysyliving.seata.pojo.User;
import com.suron.ysyliving.seata.service.GoodsService;
import com.suron.ysyliving.seata.service.SeataService;
import com.suron.ysyliving.seata.service.UserService;
import com.suron.ysyliving.seata.vo.GoodsVo;
import com.suron.ysyliving.seata.vo.RespBean;
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
@RequestMapping("/seata")
public class SeataController {

    @Resource
    private GoodsClient goodsClient; // 注入 service 中 通过 @FeignClient 定义好的接口

    //装配UserService
    @Resource
    private SeataService seataService;




    //进入到商品列表页-使用Redis优化
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")///《《《《《《《《《《《《《《
    @ResponseBody///《《《《《《《《《《《《《《
    public String save(GoodsVo order) {
        seataService.save(order);
        return "success";
    }

}

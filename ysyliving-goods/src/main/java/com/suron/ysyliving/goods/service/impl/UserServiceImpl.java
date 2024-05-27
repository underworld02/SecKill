package com.suron.ysyliving.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.ysyliving.goods.exception.GlobalException;
import com.suron.ysyliving.goods.mapper.UserMapper;
import com.suron.ysyliving.goods.service.UserService;
import com.suron.ysyliving.goods.pojo.User;
//import com.seckill.util.CookieUtil;
//import com.seckill.util.MD5Util;
//import com.seckill.util.UUIDUtil;
import com.suron.ysyliving.goods.util.CookieUtil;
import com.suron.ysyliving.goods.util.MD5Util;
import com.suron.ysyliving.goods.util.UUIDUtil;
import com.suron.ysyliving.goods.vo.LoginVo;
import com.suron.ysyliving.goods.vo.RespBean;
import com.suron.ysyliving.goods.vo.RespBeanEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ysy
 * @version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    //配置RedisTemplate,操作Redis
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {

        //接收到mobile/id和password[midPass]
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //判断手机号/id 和密码是否为空  参数校验通过 1.业务代码 2.也可通过注解校验机制
        //if (!StringUtils.hasText(mobile) || !StringUtils.hasText(password)) {
        //    return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        //}

        //校验手机号码是否合格
        //if (!ValidatorUtil.isMobile(mobile)) {
        //    return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        //}

        //查询DB, 看看用户是否存在
        User user = userMapper.selectById(mobile);     //同一个用户的不同请求可能被转发到不同服务器 分布式session问题
        if (null == user) {//说明用户不存在
            //return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);//统一由ExceptionHandlers处理
        }

        //如果用户存在，则比对密码!!
        //注意，我们从loginVo取出的密码是中间密码(即客户端经过一次加密加盐处理的密码)
        if (!MD5Util.midPassToDBPass(password, user.getSlat()).equals(user.getPassword())) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        //用户登录成功

//        //给每个用户生成ticket-唯一
        String ticket = UUIDUtil.uuid();
//        //将登录成功的用户保存到session
//        //request.getSession().setAttribute(ticket, user);
//
//        //为了实现分布式Session, 把登录的用户存放到Redis
//        System.out.println("使用的 redisTemplate->" + redisTemplate.hashCode());
        redisTemplate.opsForValue().set("user:" + ticket, user);
//        //将ticket保存到cookie
        CookieUtil.setCookie(request, response, "userTicket", ticket);
//        //这里我们需要返回userTicket


        return RespBean.success(ticket);
    }

    //根据Cookie-ticket 获取用户
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {

        if (!StringUtils.hasText(userTicket)) {
            return null;
        }

        //根据userTicket到Redis获取user
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        //如果用户不为null, 就重新设置cookie,刷新, 这里是根据你的业务需要来的
        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }

        return user;
    }

    //更新用户密码
    @Override
    public RespBean updatePassword(String userTicket,
                                   String password,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        User user =
                getUserByCookie(userTicket, request, response);
        if (user == null) {
            //抛出异常
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        //设置新密码
        user.setPassword(MD5Util.inputPassToDBPass(password, user.getSlat()));
        int i = userMapper.updateById(user);
        if (i == 1) {//更新成功
            //删除该用户在Redis数据
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWROD_UPDATE_FAIL);
    }
}

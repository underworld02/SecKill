package com.suron.ysyliving.seata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suron.ysyliving.seata.pojo.User;
import com.suron.ysyliving.seata.vo.LoginVo;
import com.suron.ysyliving.seata.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ysy
 * @version 1.0
 *
 * * 老韩解读
 *  * 1. 传统方式 在接口中 定义方法/声明方法, 然后在实现类中进行实现
 *  * 2. 在mybatis-plus中，我们可以继承父接口 IService
 *  * 3. 这个IService接口声明很多方法, 比如crud
 *  * 4. 如果默认提供方法不能满足需求，我们可以再声明需要的方法, 然后在实现类中进行实现即可
 */
public interface UserService extends IService<User> {

    //方法-完成用户的登录校验
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request,
                     HttpServletResponse response);

    //根据Cookie-ticket 获取用户
    User getUserByCookie(String userTicket,
                         HttpServletRequest request,
                         HttpServletResponse response);

    //方法: 更新密码
    RespBean updatePassword(String userTicket,
                            String password,
                            HttpServletRequest request,
                            HttpServletResponse response);
}

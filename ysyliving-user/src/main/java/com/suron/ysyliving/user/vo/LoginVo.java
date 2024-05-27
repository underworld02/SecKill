package com.suron.ysyliving.user.vo;

//import com.seckill.validator.IsMobile;
import com.suron.ysyliving.user.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author ysy
 * @version 1.0
 * LoginVo: 接收用户登录时，发送的信息(mobile,password)
 */
@Data
public class LoginVo {
    //对LoginVo的属性值进行，约束
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}

package com.suron.ysyliving.seckill.exception;

import com.suron.ysyliving.seckill.vo.RespBeanEnum;
import com.suron.ysyliving.seckill.vo.RespBean;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ysy
 * @version 1.0
 * GlobalExceptionHandler: 这是springboot的全局异常处理器
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    //处理所有的异常
    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e) {
        //如果是全局异常，正常处理
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return RespBean.error(ex.getRespBeanEnum());
        } else if (e instanceof BindException) {//绑定异常
            //如果是绑定异常  ：
            // 由于我们自定义的注解只会在控制台打印错误信息，想让该信息传给前端。
            //需要获取改异常BindException，进行打印
            BindException ex = (BindException) e;
            RespBean respBean = RespBean.error(RespBeanEnum.BING_ERROR);
            respBean.setMessage("参数校验异常~：" + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            //String message() default "手机号码格式错误";
            return respBean;
        }
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
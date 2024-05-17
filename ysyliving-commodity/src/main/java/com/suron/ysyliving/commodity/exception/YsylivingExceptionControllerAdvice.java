package com.suron.ysyliving.commodity.exception;

import com.suron.common.exception.HsplivingCodeEnum;
import com.suron.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ysy
 * @version 1.0

 * 1. @ResponseBody: 表示以json格式返回数据
 * 2. @Slf4j : 可以输出日志, 便于观察
 * 3. @ControllerAdvice(basePackages = "com.hspedu.hspliving.commodity.controller")
 *    表示统一接管 com.hspedu.hspliving.commodity.controller包下抛出异常
 */
@Slf4j
@ResponseBody
@ControllerAdvice(basePackages = "com.suron.ysyliving.commodity.controller")
public class YsylivingExceptionControllerAdvice {

    /**
     * 1. @ExceptionHandler({MethodArgumentNotValidException.class})
     *    表示如果 com.hspedu.hspliving.commodity.controller包抛出了数据校验异常
     *    , 就由handleValidException()处理
     * 2. 数据校验异常就是 MethodArgumentNotValidException.class
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public R handleValidException(MethodArgumentNotValidException e) {
        //输出日志,观察异常的信息
        //log.error("数据校验出现问题{} 异常类型是{}", e.getMessage(),e.getClass());

        //得到BindingResult
        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> errorMap = new HashMap<>();

        //遍历bindingResult 将校验错误信息收集map
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(HsplivingCodeEnum.INVALID_EXCEPTION.getCode(),
                HsplivingCodeEnum.INVALID_EXCEPTION.getMsg()).put("data",errorMap);
    }

    //说明:如果按照业务逻辑，需要去精确匹配处理异常/错误，可以再单独写方法
    //.....

    /**
     * 编写方法, 处理没有精确匹配到的异常/错误
     * 返回一个统一的信息,方便前端处理
     */

    @ExceptionHandler({Throwable.class})
    public R handleException(Throwable throwable) {
        return R.error(HsplivingCodeEnum.UNKNOWN_EXCEPTION.getCode(),
                HsplivingCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }

}

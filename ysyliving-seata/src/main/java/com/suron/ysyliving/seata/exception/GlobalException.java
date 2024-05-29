package com.suron.ysyliving.seata.exception;

import com.suron.ysyliving.seata.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ysy
 * @version 1.0
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalException extends RuntimeException{

    private RespBeanEnum respBeanEnum;
}

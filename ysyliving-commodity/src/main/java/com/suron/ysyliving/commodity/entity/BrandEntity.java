package com.suron.ysyliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.suron.common.valid.EnumValidate;
import com.suron.common.valid.SaveGroup;
import com.suron.common.valid.UpdateGroup;
import com.suron.common.valid.UpdateIsShowGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 家居品牌
 *
 * @author ysy
 */
@Data
@TableName("commodity_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     * 1. @NotNull(message = "修改要求指定id",groups = {UpdateGroup.class})
     * 表示@NotNull 在UpdateGroup校验组生效
     * <p>
     * 2. @Null(message = "添加不能指定id",groups = {SaveGroup.class})
     * 表示@Null 在SaveGroup校验组生效
     */
    @TableId
    @NotNull(message = "修改要求指定id", groups = {UpdateGroup.class, UpdateIsShowGroup.class})
    @Null(message = "添加不能指定id", groups = {SaveGroup.class})
    private Long id;
    /**
     * 品牌名
     * 1. @NotBlank 表示name必须包括一个非空字符
     * 2. message = "品牌名不能为空" 是老师指定的一个校验消息
     * 3. 如果没有指定 message = "品牌名不能为空" ,就会返回默认的校验消息  key = javax.validation.constraints.NotBlank.message
     * 4. 这个默认的消息是在  ValidationMessages_zh_CN.properties 文件配置 javax.validation.constraints.NotBlank.message        = \u4e0d\u80fd\u4e3a\u7a7a
     * 5. @NotBlank 可以用于  CharSequence
     * 6. groups = {SaveGroup.class, UpdateGroup.class}
     * 就是@NotBlank 在 SaveGroup 和 UpdateGroup都生效
     */
    @NotBlank(message = "品牌名不能为空", groups = {SaveGroup.class, UpdateGroup.class})
    private String name;
    /**
     * logo 因为这个logo对应的是图片的url
     * 1. @NotBlank 和 @URL 组合去校验  logo
     */
    @NotBlank(message = "logo不能为空", groups = {SaveGroup.class})
    @URL(message = "logo不是一个合法的URL", groups = {SaveGroup.class, UpdateGroup.class})
    private String logo;
    /**
     * 说明
     */
    private String description;
    /**
     * 显示, isshow 后面的s 是小写
     * 1. 这里我们使用的注解是@NotNull, 他可以接收任意类型
     * 2. 如果这里使用 @NotBlank , 会报错 ,因为 @NotBlank 不支持Integer
     * 3. 同学们在开发时，需要知道注解可以用在哪些类型上，可以查看注解源码
     * 4. 老师说明，假如 isshow 规定是 0/1 , 这时我们后面通过自定义校验器来解决..
     * 5. 如果是String 类型，可以直接使用@Pattern来进一步校验
     */
    @NotNull(message = "显示状态不能为空", groups = {SaveGroup.class, UpdateGroup.class, UpdateIsShowGroup.class})
    //@Pattern(regexp = "^[0-1]$", message = "显示状态必须是0或者1", groups = {SaveGroup.class, UpdateGroup.class})
    @EnumValidate(values = {0, 1}, message = "显示状态的值需要是0或者1~", groups = {SaveGroup.class, UpdateGroup.class, UpdateIsShowGroup.class})
    //@EnumValidate(regexp = "^[0-9]$", message = "显示状态的值需要是0或者1~", groups = {SaveGroup.class, UpdateGroup.class})
    private Integer isshow;
    /**
     * 检索首字母 a-z A-Z
     * 因为 firstLetter 是String 可以直接使用@Pattern
     */
    @NotBlank(message = "检索首字母不能为空", groups = {SaveGroup.class})
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是a-z或者A-Z", groups = {SaveGroup.class, UpdateGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(message = "排序值不能为空", groups = {SaveGroup.class})
    @Min(value = 0, message = "排序值要求大于等于0", groups = {SaveGroup.class, UpdateGroup.class})
    private Integer sort;
}

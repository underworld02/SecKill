package com.suron.ysyliving.commodity.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.suron.common.valid.SaveGroup;
import com.suron.common.valid.UpdateGroup;
import com.suron.common.valid.UpdateIsShowGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suron.ysyliving.commodity.entity.BrandEntity;
import com.suron.ysyliving.commodity.service.BrandService;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.R;


/**
 * 家居品牌
 *
 * @author ysy
 */
@RestController
@RequestMapping("commodity/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("commodity:brand:list")
    public R list(@RequestParam Map<String, Object> params) {
        System.out.println("params= " + params);
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("commodity:brand:info")
    public R info(@PathVariable("id") Long id) {
        BrandEntity brand = brandService.getById(id);

        return R.ok().put("brand", brand);
    }

    /**
     * 老韩说明
     * 1. @Validated 注解的作用就是启用 BrandEntity 字段校验
     * 2. 注解如果没有写 @Validated 这个校验规则不生效
     * 3. BindingResult result: springboot 会将校验的错误放入到 result
     * 4. 程序员可以通过 BindingResult result 将校验得到错误取出,然后进行相应处理
     * 5. 因为我们要将数据校验异常/错误 交给全局异常处理器 HsplivingExceptionControllerAdvice
     *    ,所以这里save方法就注销相关的校验代码.
     * 6. @Validated({SaveGroup.class}) 表示调用save时，进行参数校验, 使用的是SaveGroup的校验规则
     */
    @RequestMapping("/save")
    //@RequiresPermissions("commodity:brand:save")
    public R save(@Validated({SaveGroup.class}) @RequestBody BrandEntity brand/*, BindingResult result*/) {

        //先创建一个Map ,用于收集校验错误
        //Map<String, String> map = new HashMap<>();
        //
        //if (result.hasErrors()) { //如果有校验错误
        //    //遍历 result ,将错误信息收集到map
        //    result.getFieldErrors().forEach((item) -> {
        //        //得到field
        //        String field = item.getField();
        //        //得到校验错误信息
        //        String message = item.getDefaultMessage();
        //        //放入map
        //        map.put(field, message);
        //    });
        //    return R.error(400, "品牌表单数据不合法").put("data", map);
        //} else { //如果没有校验错误,入库
        //
        //    brandService.save(brand);
        //    return R.ok();
        //}
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     * 说明:
     * 1. @Validated({UpdateGroup.class})表示进行修改/调用update方法时,会进行参数校验,使用的是
     *    UpdateGroup组的校验规则
     */
    @RequestMapping("/update")
    //@RequiresPermissions("commodity:brand:update")
    public R update(@Validated({UpdateGroup.class}) @RequestBody BrandEntity brand) {
        brandService.updateById(brand);

        return R.ok();
    }

    @RequestMapping("/update/isshow")
    //@RequiresPermissions("commodity:brand:update")
    public R updateIsShow(@Validated({UpdateIsShowGroup.class}) @RequestBody BrandEntity brand) {
        brandService.updateById(brand);

        return R.ok();
    }

    @RequestMapping("/save2")
    //@RequiresPermissions("commodity:brand:update")
    public R save2(@RequestBody BrandEntity brand) {

        int num = 10 / 0;

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("commodity:brand:delete")
    public R delete(@RequestBody Long[] ids) {
        brandService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

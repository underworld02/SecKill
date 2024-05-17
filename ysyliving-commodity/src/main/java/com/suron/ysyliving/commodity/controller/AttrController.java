package com.suron.ysyliving.commodity.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suron.ysyliving.commodity.entity.AttrEntity;
import com.suron.ysyliving.commodity.service.AttrService;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.R;


/**
 * 商品属性表
 *
 * @author ysy
 */
@RestController
@RequestMapping("commodity/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("commodity:attr:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 1. 提供一个新的api接口/方法
     * 2. 列表- 根据categoryId + 查询条件key完成分页检索基本属性
     */
    @RequestMapping("/base/list/{categoryId}")
    //@RequiresPermissions("commodity:attr:list")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("categoryId") Long categoryId) {
        //PageUtils page = attrService.queryPage(params);
        PageUtils page = attrService.queryBaseAttrPage(params, categoryId);

        return R.ok().put("page", page);
    }

    /**
     * 1. 提供一个新的api接口/方法
     * 2. 列表- 根据categoryId + 查询条件key完成分页检索销售属性
     */
    @RequestMapping("/sale/list/{categoryId}")
    //@RequiresPermissions("commodity:attr:list")
    public R saleAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("categoryId") Long categoryId) {
        PageUtils page = attrService.querySaleAttrPage(params, categoryId);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("commodity:attr:info")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrEntity attr = attrService.getById(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("commodity:attr:save")
    public R save(@RequestBody AttrEntity attr) {
        System.out.println("attr->" + attr);
        //attrService.save(attr); 这个方法不适应,我们增加相应方法
        attrService.saveAttrAndRelation(attr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("commodity:attr:update")
    public R update(@RequestBody AttrEntity attr) {
        attrService.updateById(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("commodity:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}

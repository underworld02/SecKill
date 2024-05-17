package com.suron.ysyliving.commodity.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.suron.ysyliving.commodity.entity.AttrAttrgroupRelationEntity;
import com.suron.ysyliving.commodity.entity.AttrEntity;
import com.suron.ysyliving.commodity.service.AttrAttrgroupRelationService;
import com.suron.ysyliving.commodity.service.AttrService;
import com.suron.ysyliving.commodity.service.CategoryService;
import com.suron.ysyliving.commodity.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suron.ysyliving.commodity.entity.AttrgroupEntity;
import com.suron.ysyliving.commodity.service.AttrgroupService;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.R;

import javax.annotation.Resource;


/**
 * 家居商品属性分组
 *
 * @author ysy
 */
@RestController
@RequestMapping("commodity/attrgroup")
public class AttrgroupController {
    @Autowired
    private AttrgroupService attrgroupService;


    // 装配AttrAttrgroupRelationService
    @Resource
    private AttrAttrgroupRelationService attrAttrgroupRelationService;


    /**
     * 获取某个分类的所有属性组及其关联的基本属性
     */
    @RequestMapping("/{catalogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catalogId") Long categoryId) {

        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos =
                attrgroupService.getAttrGroupWithAttrsByCategoryId(categoryId);

        return R.ok().put("data",attrGroupWithAttrsVos);
    }

    /**
     * 批量添加属性组和属性(基本属性)的关联关系
     */
    @RequestMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities) {

        attrAttrgroupRelationService.saveBatch(attrAttrgroupRelationEntities);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("commodity:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrgroupService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 老韩说明
     * 1. 根据业务需求，增加根据分类(第3级分类) + 查询条件+ 分页的API接口/方法
     */
    @RequestMapping("/list/{categoryId}")
    //@RequiresPermissions("commodity:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("categoryId") Long categoryId) {
        PageUtils page = attrgroupService.queryPage(params, categoryId);

        return R.ok().put("page", page);
    }

    // 装配CategoryService
    @Resource
    private CategoryService categoryService;

    /**
     * 信息, 根据id返回对应的属性组的信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("commodity:attrgroup:info")
    public R info(@PathVariable("id") Long id) {
        AttrgroupEntity attrgroup = attrgroupService.getById(id);


        //获取到该属性组对应的categoryId
        Long categoryId = attrgroup.getCategoryId();
        //获取到getCategoryId 对应的 cascadedCategoryId 形式[1,21,301]
        Long[] cascadedCategoryId = categoryService.getCascadedCategoryId(categoryId);
        //把cascadedCategoryId 设置到 attrgroup对象的 cascadedCategoryId属性
        attrgroup.setCascadedCategoryId(cascadedCategoryId);
        return R.ok().put("attrgroup", attrgroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("commodity:attrgroup:save")
    public R save(@RequestBody AttrgroupEntity attrgroup) {
        attrgroupService.save(attrgroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("commodity:attrgroup:update")
    public R update(@RequestBody AttrgroupEntity attrgroup) {
        attrgroupService.updateById(attrgroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("commodity:attrgroup:delete")
    public R delete(@RequestBody Long[] ids) {
        attrgroupService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    // 装配AttrService对象
    @Resource
    private AttrService attrService;

    /**
     * 根据属性组id , 返回该属性组关联的基本属性
     */
    @RequestMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId) {

        List<AttrEntity> attrEntities = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", attrEntities);
    }

    /**
     * 响应批量删除属性组和属性的关联关系
     */
    @RequestMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrAttrgroupRelationEntity[] attrAttrgroupRelationEntities) {
        attrService.deleteRelation(attrAttrgroupRelationEntities);
        return R.ok();
    }

    /**
     * 根据attrgroupId返回可以关联的基本属性
     */
    @RequestMapping("/{attrgroupId}/attr/allowrelation")
    public R attrAllowRelation(@PathVariable("attrgroupId") Long attrgroupId,
                               @RequestParam Map<String, Object> params) {
        PageUtils page =
                attrService.getAllowRelationAttr(params, attrgroupId);
        return R.ok().put("page", page);
    }
}

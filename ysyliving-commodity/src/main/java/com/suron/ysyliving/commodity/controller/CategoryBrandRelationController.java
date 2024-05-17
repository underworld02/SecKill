package com.suron.ysyliving.commodity.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.suron.ysyliving.commodity.entity.BrandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suron.ysyliving.commodity.entity.CategoryBrandRelationEntity;
import com.suron.ysyliving.commodity.service.CategoryBrandRelationService;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.R;


/**
 * 品牌分类关联表
 *
 * @author ysy
 */
@RestController
@RequestMapping("commodity/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;


    /**
     * 根据categoryId返回管理的品牌信息
     */
    @RequestMapping("/brands/list")
    public R relationBrandsList(@RequestParam(value = "catId",required = true) Long categoryId) {

        List<BrandEntity> brandEntities =
                categoryBrandRelationService.getBrandsByCategoryId(categoryId);
        return R.ok().put("data",brandEntities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("commodity:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表-根据brandId 返回 品牌和分类关联的记录
     */
    @RequestMapping("/brand/list")
    //@RequiresPermissions("commodity:categorybrandrelation:list")
    public R categoryBrandListByBrandId(@RequestParam("brandId") Long brandId) {

        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));

        return R.ok().put("data", data);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("commodity:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("commodity:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        //categoryBrandRelationService.save(categoryBrandRelation);

        //老师说明，因为我们要保存brandName 和 categoryName ,所以我们使用自定义的方法saveAll
        categoryBrandRelationService.saveAll(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("commodity:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("commodity:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

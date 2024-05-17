package com.suron.ysyliving.commodity.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suron.ysyliving.commodity.entity.CategoryEntity;
import com.suron.ysyliving.commodity.service.CategoryService;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.R;



/**
 * 商品分类表
 *
 * @author ysy
 */
@RestController
@RequestMapping("commodity/category")
public class CategoryController {
    //装配Service
    @Autowired
    private CategoryService categoryService;

    /**
     * 提供/编写 方法/接口 查出所有分类及其子类,(并要求带有层级关系)
     * {
     *     "msg": "success",
     *     "code": 0,
     *     "data": [
     *         {
     *             "id": 1,
     *             "name": "家用电器",
     *             "parentId": 0,
     *             "catLevel": 1,
     *             "isShow": 1,
     *             "sort": 0,
     *             "icon": null,
     *             "proUnit": null,
     *             "proCount": 0,
     *             "childrenCategories": [
     *                 {
     *                     "id": 21,
     *                     "name": "大 家 电",
     *                     "parentId": 1,
     *                     "catLevel": 2,
     *                     "isShow": 1,
     *                     "sort": 0,
     *                     "icon": null,
     *                     "proUnit": null,
     *                     "proCount": 0,
     *                     "childrenCategories": [
     *                         {
     *                             "id": 301,
     *                             "name": "平板电视",
     *                             "parentId": 21,
     *                             "catLevel": 3,
     *                             "isShow": 1,
     *                             "sort": 0,
     *                             "icon": null,
     *                             "proUnit": null,
     *                             "proCount": 0,
     *                             "childrenCategories": []
     *                         }
     *                     ]
     *                 }
     *             ]
     *         }
     *     ]
     * }
     */

    @RequestMapping("/list/tree")
    public R listTree() {
        List<CategoryEntity> entities = categoryService.listTree();
        return R.ok().put("data",entities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions 是shiro的注解,我们不使用, 先注销
    //@RequiresPermissions("commodity:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("commodity:category:info")
    public R info(@PathVariable("id") Long id){
		CategoryEntity category = categoryService.getById(id);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("commodity:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("commodity:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("commodity:category:delete")
    public R delete(@RequestBody Long[] ids){
		categoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

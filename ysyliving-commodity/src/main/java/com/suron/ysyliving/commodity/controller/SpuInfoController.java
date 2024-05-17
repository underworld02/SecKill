package com.suron.ysyliving.commodity.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.suron.ysyliving.commodity.vo.SpuSaveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suron.ysyliving.commodity.entity.SpuInfoEntity;
import com.suron.ysyliving.commodity.service.SpuInfoService;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.R;


/**
 * 商品spu信息
 *
 * @author ysy
 */
@RestController
@RequestMapping("commodity/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;


    /**
     * 商品SPU上架
     */
    @RequestMapping("/{spuId}/up")
    public R spuUp(@PathVariable("spuId") Long spuId) {
        spuInfoService.up(spuId);
        return R.ok();
    }

    /**
     * 商品SPU下架
     */
    @RequestMapping("/{spuId}/down")
    public R spuDown(@PathVariable("spuId") Long spuId) {
        spuInfoService.down(spuId);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("commodity:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        //PageUtils page = spuInfoService.queryPage(params);//不带检索条件
        PageUtils page = spuInfoService.queryPageByCondition(params);//带检索条件
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("commodity:spuinfo:info")
    public R info(@PathVariable("id") Long id) {
        SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     * 老韩解读:
     * 1. 因为我们保存的商品信息, 涉及到的表非常多, 不是 SpuInfoEntity 实体类可以全部包含的
     * 2. 将 SpuInfoEntity 改成我们前面生成的 SpuSaveVo 对象，将前端提交的json数据全部封装到 SpuSaveVo
     * 3. 然后在下面的业务中，进行相应的保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("commodity:spuinfo:save")
    public R save(@RequestBody SpuSaveVO spuSaveVO) {
        //spuInfoService.save(spuInfo);
        spuInfoService.saveSpuInfo(spuSaveVO);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("commodity:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo) {
        spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("commodity:spuinfo:delete")
    public R delete(@RequestBody Long[] ids) {
        spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

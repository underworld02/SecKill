package com.suron.ysyliving.commodity.service.impl;

import com.suron.ysyliving.commodity.dao.BrandDao;
import com.suron.ysyliving.commodity.dao.CategoryDao;
import com.suron.ysyliving.commodity.entity.BrandEntity;
import com.suron.ysyliving.commodity.entity.CategoryEntity;
import com.suron.ysyliving.commodity.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.Query;

import com.suron.ysyliving.commodity.dao.CategoryBrandRelationDao;
import com.suron.ysyliving.commodity.entity.CategoryBrandRelationEntity;
import com.suron.ysyliving.commodity.service.CategoryBrandRelationService;

import javax.annotation.Resource;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    // 装配Dao
    @Resource
    BrandDao brandDao;
    @Resource
    CategoryDao categoryDao;

    @Override
    public void saveAll(CategoryBrandRelationEntity categoryBrandRelationEntity) {

        //1. 获取到前端提交的 brandId 和 categoryId
        Long brandId = categoryBrandRelationEntity.getBrandId();
        Long categoryId = categoryBrandRelationEntity.getCategoryId();

        //2. 通过 brandId 和 categoryId 得到对应的brandName 和 categoryName
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(categoryId);

        //3. 将brandName 和 categoryName 设置到 categoryBrandRelationEntity
        categoryBrandRelationEntity.setBrandName(brandEntity.getName());
        categoryBrandRelationEntity.setCategoryName(categoryEntity.getName());

        //4. 调用保存
        this.save(categoryBrandRelationEntity);
    }


    // 装配 CategoryBrandRelationDao
    @Resource
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Resource
    private BrandService brandService;

    // 根据categoryId 返回该分类关联的品牌信息-集合
    @Override
    public List<BrandEntity> getBrandsByCategoryId(Long categoryId) {

        //1. 先得到categoryId 关联的所有品牌-到commodity_category_brand_relation表
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities =
                categoryBrandRelationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("category_id", categoryId));


        //2. 根据前面得到的categoryBrandRelationEntities 收集到对应的brand_id
        //   并直接查询到对应的BrandEntity对象-使用stream API 流式计算
        List<BrandEntity> brandEntities =
                categoryBrandRelationEntities.stream().map((item)->{
                    Long brandId = item.getBrandId();
                    return brandService.getById(brandId);
                }).collect(Collectors.toList());

        //2. 根据前面得到的categoryBrandRelationEntities 收集到对应的brand_id 并放入到一个集合-使用stream API 流式计算
        //List<Long> brandIds =
        //        categoryBrandRelationEntities.stream().map((item)->{
        //    return item.getBrandId();
        //}).collect(Collectors.toList());


        //3. 再根据得到到 brandIds 到 commodity_brand 表获取品牌的信息并收集到集合中, 依然使用stream API
        //List<BrandEntity> brandEntities = brandDao.selectList(new QueryWrapper<BrandEntity>().in("id", brandIds));

        return brandEntities;
    }

}
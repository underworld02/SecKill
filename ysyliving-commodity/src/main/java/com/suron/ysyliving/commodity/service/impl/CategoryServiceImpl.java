package com.suron.ysyliving.commodity.service.impl;

import com.suron.ysyliving.commodity.vo.Catalog2Vo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.Query;

import com.suron.ysyliving.commodity.dao.CategoryDao;
import com.suron.ysyliving.commodity.entity.CategoryEntity;
import com.suron.ysyliving.commodity.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {


    //核心方法 返回所有分类及其子分类(带有层级关系-即树形!)
    //这里我们会使用到java8的 流式计算(stream api) + 递归操作(有一定难度)
    //学会一会，小伙伴们可以将其作为一个方案 在以后的工作中使用..
    @Override
    public List<CategoryEntity> listTree() {
        //老韩思路分析-步骤
        //1. 查出所有的分类数据
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2. 组装成层级树形结构[使用到 java8的 stream api + 递归操作]
        //思路
        List<CategoryEntity> categoryTree = entities.stream().filter(categoryEntity -> {
            //2.1 过滤filter, 返回 1级分类
            return categoryEntity.getParentId() == 0;
        }).map(category -> {
            //2.2 进行map映射操作, 给每个一级分类设置对应的子分类 (这个过程会使用到递归)
            category.setChildrenCategories(getChildrenCategories(category, entities));
            return category;
        }).sorted((category1, category2) -> {
            //2.3 进行排序sorted操作, 按照sort的升序排列
            return (category1.getSort() == null ? 0 : category1.getSort()) -
                    (category2.getSort() == null ? 0 : category2.getSort());
        }).collect(Collectors.toList());//2.4 将处理好的数据收集collect/转换到集合
        //3. 返回 带有层级关系数据-即树形
        return categoryTree;
    }

    //递归查询所有分类的子分类
    //1. 该方法的任务就是把 root 下的所有子分类的层级关系组织好(有多少级，就处理多少级)，并返回
    //2. all 就是所有的分类信息[即上个方法的entities]
    private List<CategoryEntity> getChildrenCategories(CategoryEntity root, List<CategoryEntity> all) {

        //过滤
        List<CategoryEntity> children =
                all.stream().filter(categoryEntity -> {
                    //这里有问题, categoryEntity.getParentId() root.getId()
                    //返回的是 Long 包装类型 == 表示是对象比较
                    //return categoryEntity.getParentId() == root.getId();
                    //解决方法1
                    //return categoryEntity.getParentId().longValue() == root.getId().longValue();
                    /**
                     *  public boolean equals(Object obj) {
                     *         if (obj instanceof Long) {
                     *             return value == ((Long)obj).longValue();
                     *         }
                     *         return false;
                     *  }
                     */
                    //解决方案2 转成对数值的比较
                    return categoryEntity.getParentId().equals(root.getId());
                }).map(categoryEntity -> {
                    //1. 找到子分类, 并设置,递归
                    categoryEntity.setChildrenCategories(getChildrenCategories(categoryEntity, all));
                    return categoryEntity;
                }).sorted((category1, category2) -> {
                    //按照sort排序-升序
                    return (category1.getSort() == null ? 0 : category1.getSort()) -
                            (category2.getSort() == null ? 0 : category2.getSort());
                }).collect(Collectors.toList());

        return children;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 分析
     * 1. 该方法返回 cascadedCategoryId, 数据形式是 [1,21,301]
     * 2. 这里我们需要使用到递归， 会递归的查找parentId
     *
     * @param categoryId
     * @return
     */
    @Override
    public Long[] getCascadedCategoryId(Long categoryId) {

        //1. 先创建一个集合, 把层级关系收集到集合
        List<Long> cascadedCategoryId = new ArrayList<>();

        //2. 调用方法进行处理-递归方法
        //   cascadedCategoryId是引用传递, 所以直接就影响到 本方法的 cascadedCategoryId
        //   返回的数据 如 [301,21,1]
        getParentCategoryId(categoryId, cascadedCategoryId);

        //3 将cascadedCategoryId集合进行翻转 [301,21,1]=>[1,21,301]
        //  项目的逻辑+java基础
        Collections.reverse(cascadedCategoryId);
        return cascadedCategoryId.toArray(new Long[cascadedCategoryId.size()]);
    }

    //返回当前所有的一级分类
    @Override
    public List<CategoryEntity> getLevel1Categories() {

        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", 0);
        List<CategoryEntity> categoryEntities =
                this.baseMapper.selectList(queryWrapper);

        return categoryEntities;

        //上面其实一条语句也可以完成

        //return this.baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_id",0));


    }

    /**
     * @param selectList 就是所有的分类数据
     * @param parentCId  根据父级的分类Id,返回对应的分类数据
     * @return
     */
    private List<CategoryEntity> getParent_cid
    (List<CategoryEntity> selectList, Long parentCId) {

        //流式计算-filter
        List<CategoryEntity> categoryEntities = selectList.stream().filter(item -> {
            return item.getParentId().equals(parentCId);
        }).collect(Collectors.toList());
        return categoryEntities;
    }


    /**
     * 返回二级分类(包含三级分类)的数据-按照规定的格式Map<String, List<Catalog2Vo>>
     * 这里我们会使用到流式计算的 集合->map
     * 有一定难度-有层级关系
     * 老师分析: 我们需要一个辅助方法, 就是通过parentId获取对应的下一级的分类数据
     *
     * @return
     */
    @Override
    public Map<String, List<Catalog2Vo>> getCatalogJson() {

        //-先得到所有的分类数据[到数据表查一次]-> 在程序中进行业务处理 -> Map<String, List<Catalog2Vo>>

        //- 得到所有的分类数据[到数据表查一次]
        List<CategoryEntity> selectList =
                this.baseMapper.selectList(null);

        //- 从一级分类开始 -》 二级分类 -》 三级分类 ->

        //- 得到所有的一级分类
        List<CategoryEntity> level1Categories =
                getParent_cid(selectList, 0L);

        //- 遍历一级分类 ---> 最终得到 --》 Map<String, List<Catalog2Vo>>
        //- > 直接使用前面老师讲解的 Collectors.toMap

        Map<String, List<Catalog2Vo>> categoryMap =
                level1Categories.stream().collect(
                        Collectors.toMap(k -> {
                            return k.getId().toString();
                        }, v -> {
                            //这里就需要业务处理 List<Catalog2Vo>
                            List<Catalog2Vo> catalog2Vos = new ArrayList<>();

                            //-得到当前一级分类对应的所有二级分类
                            List<CategoryEntity> level2Categories =
                                    getParent_cid(selectList, v.getId());

                            //-遍历二级分类 - 使用流式计算
                            if (level2Categories != null && level2Categories.size() > 0) {
                                catalog2Vos = level2Categories.stream().map(l2 -> {

                                    //构建Catalog2Vo
                                    Catalog2Vo catalog2Vo =
                                            new Catalog2Vo(v.getId().toString(), null, l2.getId().toString(), l2.getName());

                                    //遍历l2对应的三级分类
                                    List<CategoryEntity> level3Categories = getParent_cid(selectList, l2.getId());
                                    if (level3Categories != null && level3Categories.size() > 0) {
                                        List<Catalog2Vo.Category3Vo> category3Vos = level3Categories.stream().map(l3 -> {
                                            //构建当前二级分类对应的三级分类对象
                                            Catalog2Vo.Category3Vo category3Vo =
                                                    new Catalog2Vo.Category3Vo(l2.getId().toString(), l3.getId().toString(), l3.getName());
                                            return category3Vo;
                                        }).collect(Collectors.toList());
                                        catalog2Vo.setCatalog3List(category3Vos);
                                    }
                                    return catalog2Vo;
                                }).collect(Collectors.toList());
                            }

                            return catalog2Vos;
                        }));

        return categoryMap;
    }

    /**
     * 编写方法，更加categoryId 递归的查找层级关系
     * ，比如我们接收到 categoryId 301->parentId->....直到parentId=0
     * 老师说明：这里有点麻烦
     * <p>
     * 这里老师做简单实现分析
     * cascadedCategoryId =>[301]
     */

    private List<Long> getParentCategoryId(Long categoryId, List<Long> cascadedCategoryId) {

        //1. 先把categoryId放入到
        cascadedCategoryId.add(categoryId);
        //2. 根据categoryId得到他的CategoryEntity
        CategoryEntity categoryEntity = this.getById(categoryId);
        //3. 判断categoryEntity的parentId是否为0, 如果不为0，说明他有上级分类
        //   就递归处理
        if (!categoryEntity.getParentId().equals(0L)) {
            //21,cascadedCategoryId=> cascadedCategoryId[301,21,1]
            getParentCategoryId(categoryEntity.getParentId(), cascadedCategoryId);
        }

        return cascadedCategoryId;
    }

}
package com.suron.ysyliving.commodity.service.impl;

import com.suron.ysyliving.commodity.dao.AttrAttrgroupRelationDao;
import com.suron.ysyliving.commodity.dao.AttrgroupDao;
import com.suron.ysyliving.commodity.entity.AttrAttrgroupRelationEntity;
import com.suron.ysyliving.commodity.entity.AttrgroupEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.Query;

import com.suron.ysyliving.commodity.dao.AttrDao;
import com.suron.ysyliving.commodity.entity.AttrEntity;
import com.suron.ysyliving.commodity.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );
        return new PageUtils(page);
    }

    // 装配AttrAttrgroupRelationDao
    @Resource
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    /**
     * 实现方法，完成保存商品属性(基本属性) 的同时，
     * 要需要保存该基本属性和属性组的关联关系
     * <p>
     * 因为该方法涉及到对多表操作[insert], 因此需要进行事务控制
     */
    @Transactional
    @Override
    public void saveAttrAndRelation(AttrEntity attrEntity) {

        //1. 先保存基本数据
        this.save(attrEntity);//insert

        //2. 保存商品属性(基本属性)和属性组的关联关系
        if (attrEntity.getAttrType() == 1 && attrEntity.getAttrGroupId() != null) {

            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity =
                    new AttrAttrgroupRelationEntity();

            attrAttrgroupRelationEntity.setAttrGroupId(attrEntity.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());

            //insert
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }

    }

    /**
     * 1. 编写根据分类categoryId+查询条件[key] 进行分页检索[基本属性]的方法
     * 2. 根据自己的业务逻辑，进行实现
     */
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId) {

        //1. 先创建QueryWrapper
        QueryWrapper<AttrEntity> queryWrapper =
                new QueryWrapper<AttrEntity>().eq("attr_type", 1);

        //2. 考虑categoryId
        if (categoryId != 0) {
            queryWrapper.eq("category_id", categoryId);
        }

        //3. 考虑查询用户是否携带key
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and((wrapper) -> {
                // 我们规定[业务逻辑] 查询条件是针对attr_id 或者 attr_name, attr_id 就是相等条件,
                // attr_name 就是模糊查询
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);

    }

    /**
     * 1. 编写根据分类categoryId+查询条件[key] 进行分页检索[销售属性]的方法
     * 2. 根据自己的业务逻辑，进行实现
     */
    @Override
    public PageUtils querySaleAttrPage(Map<String, Object> params, Long categoryId) {
        //1. 先创建QueryWrapper
        QueryWrapper<AttrEntity> queryWrapper =
                new QueryWrapper<AttrEntity>().eq("attr_type", 0);

        //2. 考虑categoryId
        if (categoryId != 0) {
            queryWrapper.eq("category_id", categoryId);
        }

        //3. 考虑查询用户是否携带key
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and((wrapper) -> {
                // 我们规定[业务逻辑] 查询条件是针对attr_id 或者 attr_name, attr_id 就是相等条件,
                // attr_name 就是模糊查询
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 根据属性组id , 返回该属性组关联的商品属性(基本属性)
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {

        //1. 根据attrgroupId属性组id,到commodity_attr_attrgroup_relation表,查询关联的属性(基本属性)
        List<AttrAttrgroupRelationEntity> entities =
                attrAttrgroupRelationDao.selectList(
                        new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        //2. 将entities的attrId 收集到集合中-> 使用前面我们讲过的jdk8 流式计算 stream api , 如果忘记了，自己回顾

        List<Long> attrIds = entities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //3. 根据得到attrIds 获取对应的attrEntity
        //   老师说明: 如果当前attrgroupId没有关联任何的基本属性 , 前面的attrIds就是[]
        // 如果没有关联任何的基本属性，返回null
        if (attrIds == null || attrIds.size() == 0) {
            return null;
        }

        // 根据attrIds 返回对应AttrEntity 集合
        // 比如 attrIds = [1,2,3] => 对应的 AttrEntity对象集合

        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);

        // 进行转换
        return (List<AttrEntity>) attrEntities;
    }

    /**
     * 实现批量删除属性组和属性的关联关系.
     *
     * @param attrAttrgroupRelationEntities
     */
    @Override
    public void deleteRelation(AttrAttrgroupRelationEntity[] attrAttrgroupRelationEntities) {

        attrAttrgroupRelationDao.deleteBatchRelation(Arrays.asList(attrAttrgroupRelationEntities));
    }

    /**
     * 获取某个属性组可以关联的基本属性
     * 1. 如果某个基本属性已经和某个属性组关联了, 就不能再关联
     * 2. 某个属性组可以关联的基本属性，必须是同一个分类
     */

    //装配 AttrGroupDao
    @Resource
    private AttrgroupDao attrgroupDao;

    @Override
    public PageUtils getAllowRelationAttr(Map<String, Object> params, Long attrgroupId) {

        // 小伙伴注意： 这里就是涉及多表检索的解决方案.. 通过流式计算stream API

        //1. 通过接收的 属性组id attrgroupId, 得到对应的categoryId
        AttrgroupEntity attrgroupEntity = attrgroupDao.selectById(attrgroupId);
        Long categoryId = attrgroupEntity.getCategoryId();

        // ----- 增加业务需求，排除已经关联的基本属性------

        //(1) 先得到当前categoryId 的所有分组 - commodity_attrgroup表
        List<AttrgroupEntity> group =
                attrgroupDao.selectList(new QueryWrapper<AttrgroupEntity>().eq("category_id", categoryId));
        //收集到上面得到group的对应的属性组id->jdk8 流式计算 stream API
        List<Long> attrGroupIds = group.stream().map((item) -> {
            return item.getId();
        }).collect(Collectors.toList());


        //(2) 再到commodity_attr_attrgroup_relation中，去检索有哪些基本属性已经和上一步得到的属性组关联上
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities =
                attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupIds));
        //收集从上面得到 attrAttrgroupRelationEntities对应attr_id,放入到集合->jdk8 流式计算
        List<Long> attrIds = attrAttrgroupRelationEntities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //2. 通过得到 categoryId, 获取到对应的基本属性
        QueryWrapper<AttrEntity> wrapper =
                new QueryWrapper<AttrEntity>().eq("category_id", categoryId).eq("attr_type", 1);

        //(3) 增加一个排除的前面已经关联过的基本属性即可.

        if (attrIds != null && attrIds.size() != 0) {
            wrapper.notIn("attr_id", attrIds);
        }

        //3. 因为还有支持条件查询，所以考虑携带的检索条件.
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) { //如果key有内容
            wrapper.and((obj) -> {
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        //进行分页查询
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);

    }

}
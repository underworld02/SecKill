package com.suron.ysyliving.commodity.service.impl;

import com.suron.ysyliving.commodity.entity.AttrEntity;
import com.suron.ysyliving.commodity.service.AttrService;
import com.suron.ysyliving.commodity.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.Query;

import com.suron.ysyliving.commodity.dao.AttrgroupDao;
import com.suron.ysyliving.commodity.entity.AttrgroupEntity;
import com.suron.ysyliving.commodity.service.AttrgroupService;

import javax.annotation.Resource;


@Service("attrgroupService")
public class AttrgroupServiceImpl extends ServiceImpl<AttrgroupDao, AttrgroupEntity> implements AttrgroupService {

    //这里没有带检索条件，因此默认是返回所有的商品属性组的信息
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrgroupEntity> page = this.page(
                new Query<AttrgroupEntity>().getPage(params),
                new QueryWrapper<AttrgroupEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 实现增加根据分类(第3级分类)+查询条件[key和categoryId]+分页的API接口
     *
     * @param params
     * @param categoryId
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {
        //先获检索传入的关键字
        String key = (String) params.get("key");

        //根据实际情况,封装查询条件到 QueryWrapper对象
        QueryWrapper<AttrgroupEntity> wrapper =
                new QueryWrapper<>();
        //判断key是否携带的有查询条件-希望他是一组独立检索条件
        if (StringUtils.isNotBlank(key)) {
            //id 就是相等条件, name 就是 模糊查询
            //wrapper.eq("id",key).or().like("name",key);
            wrapper.and((obj) -> {
                obj.eq("id", key).or().like("name", key);
            });
        }
        //下面我们在处理是否需要封装categoryId检索条件
        //我们这里先设置一个业务规则:categoryId==0 表示在查询属性组的时候, 不加入categoryId
        //否则就加入 And categoryId=xx, 这里逻辑需求前端代码配合
        if (categoryId != 0) {
            wrapper.eq("category_id", categoryId);
        }
        IPage<AttrgroupEntity> page = this.page(
                new Query<AttrgroupEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    // 装配AttrService
    @Resource
    private AttrService attrService;

    //根据分类categoryId返回该分类关联的属性组和这些属性组关联的基本属性
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCategoryId(Long categoryId) {

        //1. 根据categoryId 得到该分类关联的所有属性组
        List<AttrgroupEntity> attrgroupEntities =
                this.list(new QueryWrapper<AttrgroupEntity>().eq("category_id", categoryId));

        //2. 根据前面查询到的 attrgroupEntities ，对其进行遍历，将各个属性组关联的
        //   基本属性查询到，并封装到 AttrGroupWithAttrsVo 集合中，返回-> 依然使用流式计算 stream API
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos = attrgroupEntities.stream().map(attrgroupEntity -> {

            //(1) 先创建一个AttrGroupWithAttrsVo对象
            AttrGroupWithAttrsVo attrGroupWithAttrsVo =
                    new AttrGroupWithAttrsVo();
            //(2) 将attrgroupEntity 对象属性拷贝到 attrGroupWithAttrsVo
            BeanUtils.copyProperties(attrgroupEntity, attrGroupWithAttrsVo);

            //(3) 通过属性组的id,获取到该属性组关联的所有基本属性
            List<AttrEntity> attrEntities =
                    attrService.getRelationAttr(attrGroupWithAttrsVo.getId());

            //(4) 将前面得到attrEntities设置到 attrGroupWithAttrsVo的attrs属性
            attrGroupWithAttrsVo.setAttrs(attrEntities);

            return attrGroupWithAttrsVo;

        }).collect(Collectors.toList());

        return attrGroupWithAttrsVos;
    }
}
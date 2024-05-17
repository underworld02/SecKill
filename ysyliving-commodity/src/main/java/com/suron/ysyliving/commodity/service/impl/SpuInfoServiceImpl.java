package com.suron.ysyliving.commodity.service.impl;

import com.suron.ysyliving.commodity.entity.*;
import com.suron.ysyliving.commodity.service.*;
import com.suron.ysyliving.commodity.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suron.common.utils.PageUtils;
import com.suron.common.utils.Query;

import com.suron.ysyliving.commodity.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }


    // 装配 SpuInfoDescService
    @Resource
    private SpuInfoDescService spuInfoDescService;


    // 装配SpuImagesService
    @Resource
    private SpuImagesService spuImagesService;


    // 装配 AttrService
    @Resource
    private AttrService attrService;

    // 装配ProductAttrValueService
    @Resource
    private ProductAttrValueService productAttrValueService;

    // 装配 SkuInfoService
    @Resource
    private SkuInfoService skuInfoService;

    // 装配 SkuImagesService
    @Resource
    private SkuImagesService skuImagesService;

    // 装配 SkuSaleAttrValueService
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    /**
     * 1. 保存SpuSaveVO 对象/数据到表[会根据业务，将数据分别保存对应表]
     * 2. 因为saveSpuInfo 涉及到对多表的添加，因此需要进行事务管理, 所以标识 @Transactional
     * 3.
     *
     * @param spuSaveVO
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVO spuSaveVO) {

        //1. 保存spu基本信息到 表commodity_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        //2. 将spuSaveVO 对象的属性值对拷到 spuInfoEntity对象[属性名需要有对应关系]
        BeanUtils.copyProperties(spuSaveVO, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());

        //3. 将spuInfoEntity 保存到 commodity_spu_info, 这里我们为了可读性和扩展性
        //   单独的写一个方法。
        this.saveBaseSpuInfo(spuInfoEntity);

        //保存spu 的介绍图片的信息 - 对应的表 commodity_spu_info_desc

        List<String> decript = spuSaveVO.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        //这里我们设置给 spuInfoDescEntity对象的spuId就是上面添加的spuInfoEntity对应id
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());

        //判断decript中有几个图片路径-元素
        if (decript.size() == 0) {//spu没有对应的介绍图片-设置一个默认图片
            spuInfoDescEntity.setDecript("default.jpg");
        } else { // spu有对应的介绍图片, 就进行遍历，如果有多个图片url,就使用,间隔 1.jpg, 2.jpg
            spuInfoDescEntity.setDecript(String.join(",", decript)); // "1.jpg,2.jpg"
        }

        //保存spuInfoDescEntity
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        //保存spu对应的图片集
        //1. 先得到images
        List<String> images = spuSaveVO.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);


        //--保存SPU对应的基本属性[一个SPU可以有多个基本属性]-将基本属性保存到
        //  commodity_product_attr_value

        //1. 从spuSaveVO对象取出前端通过json发送的 基本属性数据

        List<BaseAttrs> baseAttrs = spuSaveVO.getBaseAttrs();

        //2. 遍历baseAttrs, 将数据封装到ProductAttrValueEntity[根据相应的业务要求完成], 并将其收集成集合
        //   , 通常情况下，对于有综合业务需求处理的遍历，使用流式计算 stream

        List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(attr -> {
            //创建ProductAttrValueEntity
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setAttrSort(0);// 默认值0
            //这里我们发现前端没有通过json把基本属性名携带到vo对象, 需要我们二次处理
            //通过attrId 获取到 属性
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            productAttrValueEntity.setAttrId(attr.getAttrId());
            return productAttrValueEntity;
        }).collect(Collectors.toList());

        //3. 将收集到的 productAttrValueEntities 批量保存到对应的表 commodity_product_attr_value
        productAttrValueService.saveProductAttr(productAttrValueEntities);

        //-- 保存SKU的基本信息 - commodity_sku_info

        //1. 从spuSaveVO对象取出前端通过json发送的 sku基本信息数据
        //  前端发送的sku信息是多个-对应集合
        List<Skus> skus = spuSaveVO.getSkus();

        //2. 遍历skus , 将sku的基本信息封装到 SkuInfoEntity[根据相应的业务要求处理即可]
        //   这里会涉及到业务处理，我们使用遍历完成

        if (skus != null && skus.size() > 0) {

            skus.forEach(item -> {

                //先创建 SkuInfoEntity
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                skuInfoEntity.setSpuId(spuInfoEntity.getId());

                //处理默认图片
                String defaultImg = "default.jpg";

                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }

                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoEntity.setSaleCount(0l);//销量默认)
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setPrice(item.getPrice());
                skuInfoEntity.setSkuName(item.getSkuName());
                skuInfoEntity.setSkuTitle(item.getSkuTitle());
                skuInfoEntity.setSkuSubtitle(item.getSkuSubtitle());

                //保存sku的基本信息到表中 commodity_sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                //-保存sku的图片集信息到 commodity_sku_images
                //- 一个sku可以对应多个图片

                //1. 从item(sku)取出图片集集合-> 进行遍历 -> 把数据封装到 SkuImagesEntity
                //   -> 进行保存
                //2. 在收集skuImagesEntitie时，要过滤到imgUrl为空的情况
                List<SkuImagesEntity> skuImagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setImgSort(0);
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                    return skuImagesEntity;
                }).filter(img -> {
                    //过滤到img对象的imgUrl为空,""的对象
                    return StringUtils.isNotBlank(img.getImgUrl());
                }).collect(Collectors.toList());

                //2. 批量添加
                skuImagesService.saveBatch(skuImagesEntities);

                //- 保存sku的销售属性-对应的表是commodity_sku_sale_attr_value
                //- 一个sku可以对应多个销售属性

                //1. 得到前端发送的sku的销售属性-集合
                List<Attr> attrs = item.getAttr();
                //2. 遍历 attrs 将数据封装到  SkuSaleAttrValueEntity 对象中[根据具体的业务需求完成即可]
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map(attr -> { // attr就是一个销售属性
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                    skuSaleAttrValueEntity.setAttrSort(0); //默认排序0
                    skuSaleAttrValueEntity.setAttrId(attr.getAttrId());
                    skuSaleAttrValueEntity.setAttrName(attr.getAttrName());
                    skuSaleAttrValueEntity.setAttrValue(attr.getAttrValue());
                    return skuSaleAttrValueEntity;

                }).collect(Collectors.toList());

                //3. 批量保存 skuSaleAttrValueEntities
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

            });
        }

    }

    /**
     * 保存spu的基本信息
     *
     * @param spuInfoEntity
     */
    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {

        this.baseMapper.insert(spuInfoEntity);
    }

    //根据前端请求携带的检索条件，进行分页查询
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        //检索条件-key
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {

            //我们定一个业务需求, key如果视为id ,就是等于, 如果视为名称就是模糊查询
            wrapper.and(w -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }

        //检索条件-状态
        String status = (String) params.get("status");
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq("publish_status", status);
        }

        //检索条件-品牌id
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotBlank(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }

        //检索条件-分类id
        String catalogId = (String) params.get("catalogId");
        if (StringUtils.isNotBlank(catalogId) && !"0".equalsIgnoreCase(catalogId)) {
            wrapper.eq("catalog_id", catalogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    //上架
    @Override
    public void up(Long spuId) {
        this.baseMapper.updateSpuStatus(spuId, 1);
    }

    //下架
    @Override
    public void down(Long spuId) {
        this.baseMapper.updateSpuStatus(spuId, 2);
    }

}
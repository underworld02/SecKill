package com.suron.ysyliving.goods.service.impl;


import com.suron.ysyliving.goods.mapper.BrandMapper;
import com.suron.ysyliving.goods.mapper.CategoryMapper;

import com.suron.ysyliving.goods.mapper.ProductMapper;
import com.suron.ysyliving.goods.service.SearchService;
import com.suron.ysyliving.goods.vo.*;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Suron
 * @version 1.0
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

//    @Autowired
//    private ProductRepository productRepository; // Assuming you have a JPA repository
//    @Autowired
//    private ElasticsearchTemplate template;
    @Override
    public PageResult<Product> searchProducts(SearchRequest request) {//SearchRequest
//        //        因为es的page从0开始，永远有一页显示不出来，所以-1
////        page=第几页       size=每页显示多少条数据(已经在类中固定，不能改了)
        int page = request.getPage() - 1;
        int size = request.getSize();
        int offset = page * size;
        String key = request.getKey();
        System.out.println(key);
        if(key == null || key == "" || key.equals("null")){
            key = "";
        }
        List<Product> productList = productMapper.search(key, offset, size);
        int totalElements = productMapper.countSearch(key);
        int totalPages = (int) Math.ceil((double) totalElements / size);

//        List<Category> categories = productMapper.aggregateCategories(request.getKey());
//        List<Brand> brands = productMapper.aggregateBrands(request.getKey());


////      创建查询构造器，可以分页和过滤
//        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
////        结果过滤，里面第一个参数是包含什么，这里用数组，第二个参数是不包含什么，这里是mull
//        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subtitle", "skus"}, null));
////        分页
//        queryBuilder.withPageable(PageRequest.of(page, size));
////        过滤
//        queryBuilder.withQuery(QueryBuilders.matchQuery("all", request.getKey()));
////        聚合分类和品牌（二次功能实现添加）
//        String categoryAggName = "category_agg";
//        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3")); //根据cid进行聚合
////        聚合品牌
//        String brandAggName = "brand_agg";
//        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
////        查询(二次查询的结果不是分页结果，而是聚合结果，不能再用普通查询，需要template查询)
////        Page<Product> search = goodsRepository.search(queryBuilder.build());
//        AggregatedPage<Product> search = template.queryForPage(queryBuilder.build(), Product.class);
////        解析结果
////        解析分页结果
//        long totalElements = search.getTotalElements();//总条数
//        int totalPages = search.getTotalPages();//总页数
//        List<Product> productList = search.getContent(); //当前页面数据
////        解析聚合结果
//        Aggregations aggs = search.getAggregations();
//        List<Category> categories = parseCategoryAgg(aggs.get(categoryAggName));
//        List<Brand> brands = parsebrandAgg(aggs.get(brandAggName));


//        long totalElements = 50;
//        int totalPages = 5;
//        List<Product> productList = new ArrayList<>();
////        productList.add(new Product(1l, "Product 1", "Subtitle 1", StringUtil.sku));
////        productList.add(new Product(2l, "Product 2", "Subtitle 2", StringUtil.sku2));
////        productList.add(new Product(3l, "Product 3", "Subtitle 3", StringUtil.sku));
////        productList.add(new Product(4l, "Product 4", "Subtitle 4", StringUtil.sku2));
////        productList.add(new Product(5l, "Product 5", "Subtitle 5", StringUtil.sku));
//        for (long i = 1; i <= 5; i++) {
//            float i1 = new Random().nextFloat();
//            if (i1>=0.5){
//                productList.add(new Product(i, "Product "+i, "Subtitle "+i, StringUtil.sku));
//            }
//            else {
//                productList.add(new Product(i, "Product "+i, "Subtitle "+i, StringUtil.sku2));
//            }
//
//        }
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Electronics"));
        categories.add(new Category(2, "Clothing"));

        List<Brand> brands = new ArrayList<>();
        brands.add(new Brand(1, "Brand A"));
        brands.add(new Brand(2, "Brand B"));
        return new SearchResult<>((long) totalElements, totalPages, productList, categories, brands);
    }
    private List<Brand> parsebrandAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Brand> brands = brandMapper.selectByIdList(ids);
            return brands;
        } catch (Exception e) {
            return null;
        }
    }
    private List<Category> parseCategoryAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Category> categories = categoryMapper.selectByIdList(ids);
            return categories;
        } catch (Exception e) {
            return null;
        }
    }
}
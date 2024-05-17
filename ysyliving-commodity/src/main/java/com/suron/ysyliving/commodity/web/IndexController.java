package com.suron.ysyliving.commodity.web;

import com.suron.ysyliving.commodity.entity.CategoryEntity;
import com.suron.ysyliving.commodity.service.CategoryService;
import com.suron.ysyliving.commodity.vo.Catalog2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author ysy
 * @version 1.0
 */
@Controller
public class IndexController {

    //装配CategoryService
    @Resource
    private CategoryService categoryService;

    //响应用户请求首页 http://localhost:9090 或者 http://localhost:9090/index.html
    @GetMapping(value = {"/", "/index.html"})
    private String indexPage(Model model) {

        //调用方法，获取到一级分类信息[集合]
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categories();
        //将 categoryEntities放入到 model
        model.addAttribute("categories", categoryEntities);

        //老师说明: 默认找到就是 "classpath\templates\" + "index" + ".html"
        return "index";
    }

    //测试-返回json数据
    @GetMapping(value = "/index/catalog.json")
    @ResponseBody
    public Map<String,List<Catalog2Vo>> getCatalogJson() {

        Map<String, List<Catalog2Vo>> catalogJson =
                categoryService.getCatalogJson();

        return catalogJson;
    }
}

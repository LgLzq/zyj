package cn.cdqf.redisdemo.controller;

import cn.cdqf.redisdemo.common.ResultResponse;
import cn.cdqf.redisdemo.pojo.Product;
import cn.cdqf.redisdemo.service.CategoryService;
import cn.cdqf.redisdemo.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    //根据id查询分类的信息
    @GetMapping("category/{id}")
    public ResultResponse queryById(@PathVariable("id") Integer id) throws JsonProcessingException {
        return productService.queryById(id);
    }
    @GetMapping("category/products/{id}")
    public ResultResponse queryProductsById(@PathVariable("id") Integer id){
        return categoryService.queryProductsById(id);
    }
}

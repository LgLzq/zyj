package cn.cdqf.redisdemo.controller;

import cn.cdqf.redisdemo.common.ResultResponse;
import cn.cdqf.redisdemo.pojo.Product;
import cn.cdqf.redisdemo.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("product/{id}")
    public ResultResponse queryById(@PathVariable("id") Integer id) throws JsonProcessingException {
        return productService.queryById(id);
    }
    @PutMapping("product")
    public ResultResponse updateById(@RequestBody Product product){
        return productService.updateById(product);
    }
}

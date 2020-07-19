package cn.cdqf.redisdemo.service.impl;

import cn.cdqf.redisdemo.common.Constant;
import cn.cdqf.redisdemo.common.ResultResponse;
import cn.cdqf.redisdemo.dao.CategoryDao;
import cn.cdqf.redisdemo.pojo.Product;
import cn.cdqf.redisdemo.service.CategoryService;
import cn.cdqf.redisdemo.service.ProductService;
import cn.cdqf.redisdemo.util.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private ProductService productService;

    @Override
    public ResultResponse queryProductsById(Integer id) {
        //先从redis查询
        Set<Object> objects = redisUtils.sgetAll(Constant.RedisKey.REDIS_SET_CATEGORY_ + id);
        List<Product> products;
        if(objects==null||objects.size()==0){
            //从数据库查询
            products =  productService.queryProductsByCid(id);
           if(products!=null&& products.size()>0){
               Object[] object = new Object[products.size()];
               final AtomicInteger atomicInteger = new AtomicInteger(0);
               products.stream().forEach(s->object[atomicInteger.getAndIncrement()]=s.getId());
               redisUtils.sset(Constant.RedisKey.REDIS_SET_CATEGORY_ + id,object);
           }
        }else {
            Set<Integer> ids = objects.stream().map(p -> (Integer) p).collect(Collectors.toSet());
            products = productService.queryByIds(ids);
        }

        return ResultResponse.success(products);
    }
}

package cn.cdqf.redisdemo.service.impl;

import cn.cdqf.redisdemo.common.Constant;
import cn.cdqf.redisdemo.common.ResultResponse;
import cn.cdqf.redisdemo.dao.ProductDao;
import cn.cdqf.redisdemo.pojo.Product;
import cn.cdqf.redisdemo.service.ProductService;
import cn.cdqf.redisdemo.util.RedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductService productService;
    @Override
    public ResultResponse queryById(Integer id) throws JsonProcessingException {
       // 判断一下 todo

        //先从redis查询
       String key = Constant.RedisKey.REDIS_HASH_PRODUCT_ + id;
        Map<Object, Object> hmget = redisUtils.hmget(key);
        if(hmget==null||hmget.size()==0){//走数据库查询
           Product product =  productDao.queryById(id);
            String s = objectMapper.writeValueAsString(product);
            Map<Object, Object> map = objectMapper.readValue(s, HashMap.class);
            if(product!=null){//放入redis
               redisUtils.hmset(key,map);
           }
            return ResultResponse.success(product);
        }

        return ResultResponse.success(hmget);
    }

    @Override
    @Transactional
    public ResultResponse updateById(Product product) {
        //正好 redis缓存当前数据失效
        //1.线程B 查询redis  因为失效 走数据库查询
        // 2.线程A 更新product
        //3. 线程A 删除redis
        //4. 线程B 写入redis
        //出现双写一致性问题,当然概率很小

        productDao.updateById(product);
        //删除当前商品对应缓存信息
        String key = Constant.RedisKey.REDIS_HASH_PRODUCT_ + product.getId();

            redisUtils.del(key);
            //消息中间件 rabbitmq
            CompletableFuture.runAsync(()->{
                try {
                    Thread.sleep(3000);
                    redisUtils.del(key);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });


        return null;
    }

    @Override
    public List<Product> queryProductsByCid(Integer id) {

        return productDao.queryProductsByCid(id);
    }

    @Override
    public List<Product> queryByIds(Set<Integer> ids) {
        List<ResultResponse> collect = ids.stream().map(id -> {
            try {
                return productService.queryById(id);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        List<Object> collect1 = collect.stream().map(resultResponse -> resultResponse.getData()).collect(Collectors.toList());
        productDao.queryByIds(ids);
        return collect1.stream().map(p->(Product)p).collect(Collectors.toList());
    }
}

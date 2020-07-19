package cn.cdqf.redisdemo.service;

import cn.cdqf.redisdemo.common.ResultResponse;
import cn.cdqf.redisdemo.pojo.Product;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Set;

public interface ProductService {
    ResultResponse queryById(Integer id) throws JsonProcessingException;

    ResultResponse updateById(Product product);

    List<Product> queryProductsByCid(Integer id);

    List<Product> queryByIds(Set<Integer> ids);
}

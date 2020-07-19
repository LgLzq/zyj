package cn.cdqf.redisdemo.dao;

import cn.cdqf.redisdemo.pojo.Product;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Set;

public interface ProductDao {
    Product queryById(@Param("id") Integer id);

    int updateById(Product id);

    List<Product> queryProductsByCid(@Param("id") Integer id);

    List<Product> queryByIds(@Param("ids") Set<Integer> ids);

}

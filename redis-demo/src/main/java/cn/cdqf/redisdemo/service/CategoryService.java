package cn.cdqf.redisdemo.service;

import cn.cdqf.redisdemo.common.ResultResponse;

public interface CategoryService {
    ResultResponse queryProductsById(Integer id);
}

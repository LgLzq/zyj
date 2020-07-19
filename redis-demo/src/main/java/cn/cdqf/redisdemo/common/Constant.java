package java.cn.cdqf.redisdemo.common;

public interface Constant {

    interface RedisKey{
        //存储 商品信息 hash
        String REDIS_HASH_PRODUCT_ = "REDIS_HASH_PRODUCT_";

        String REDIS_SET_CATEGORY_ = "REDIS_SET_CATEGORY_";
    }
}

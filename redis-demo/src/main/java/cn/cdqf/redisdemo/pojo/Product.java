package cn.cdqf.redisdemo.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Product implements Serializable {

    private Integer id;

    private String pname;

    private Integer cid;

    private Integer quantity;
}

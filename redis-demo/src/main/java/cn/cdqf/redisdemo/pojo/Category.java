package cn.cdqf.redisdemo.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Category implements Serializable {

    private Integer cid;

    private String cname;
}

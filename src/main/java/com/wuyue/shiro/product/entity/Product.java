package com.wuyue.shiro.product.entity;

import com.wuyue.shiro.shiro.PermissionResolver;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "product")
public class Product implements PermissionResolver {

    @Override
    public String resolve() {
        return merchantId + ":" + brandId + ":" + id;
    }

    @Id
    @GenericGenerator(name = "idGen", strategy = "uuid")
    @GeneratedValue(generator = "idGen")
    private String id;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "brand_id")
    private String brandId;

    @Column(name = "name")
    private String name;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}

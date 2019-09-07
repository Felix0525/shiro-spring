package com.wuyue.shiro.merchant.entity;

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
@Table(name = "merchant")
public class Merchant implements PermissionResolver {

    @Id
    @GenericGenerator(name = "idGen", strategy = "uuid")
    @GeneratedValue(generator = "idGen")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Override
    public String resolve() {
        return id;
    }

}

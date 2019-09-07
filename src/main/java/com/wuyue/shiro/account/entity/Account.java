package com.wuyue.shiro.account.entity;

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
@Table(name = "account")
public class Account {

    @Id
    @GenericGenerator(name = "idGen", strategy = "uuid")
    @GeneratedValue(generator = "idGen")
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "pwd")
    private String pwd;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}

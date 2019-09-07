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
@Table(name = "account_merchant_link")
public class AccountMerchantLink {

    @Id
    @GenericGenerator(name = "idGen", strategy = "uuid")
    @GeneratedValue(generator = "idGen")
    private String id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}

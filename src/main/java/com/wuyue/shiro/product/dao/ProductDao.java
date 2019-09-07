package com.wuyue.shiro.product.dao;

import com.wuyue.shiro.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, String> {

    List<Product> findByBrandId(String brandId);

}

package com.wuyue.shiro.product.service;

import com.wuyue.shiro.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> findById(String id);

    List<Product> findByBrandId(String brandId);

}

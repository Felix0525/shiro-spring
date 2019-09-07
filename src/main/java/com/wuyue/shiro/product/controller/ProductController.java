package com.wuyue.shiro.product.controller;

import com.wuyue.shiro.DataResp;
import com.wuyue.shiro.product.service.ProductService;
import com.wuyue.shiro.product.entity.Product;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/product")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequiresAuthentication
    @GetMapping("/{id}")
    public DataResp<Product> findById(@PathVariable("id") String id) {
        return DataResp.build(productService.findById(id));
    }

    @RequiresAuthentication
    @GetMapping("/brand/{brandId}")
    public DataResp<List<Product>> findByBrandId(@PathVariable("brandId") String brandId) {
        return DataResp.build(productService.findByBrandId(brandId));
    }

}

package com.wuyue.shiro.product.service;

import com.wuyue.shiro.product.dao.ProductDao;
import com.wuyue.shiro.product.entity.Product;
import com.wuyue.shiro.shiro.Permitable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Permitable(post = "#result?.get().resolve()")
    @Override
    public Optional<Product> findById(String id) {
        if (StringUtils.isBlank(id)) {
            return Optional.empty();
        }
        return productDao.findById(id);
    }

    @Permitable(post = "#resolve(#result)")
    @Override
    public List<Product> findByBrandId(String brandId) {
        if (StringUtils.isBlank(brandId)) {
            return Collections.emptyList();
        }
        return productDao.findByBrandId(brandId);
    }

}

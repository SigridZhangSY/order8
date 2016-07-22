package com.thoughtworks.ketsu.infrastructure.repositories;


import com.thoughtworks.ketsu.domain.product.*;
import com.thoughtworks.ketsu.support.DatabaseTestRunner;
import com.thoughtworks.ketsu.support.TestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(DatabaseTestRunner.class)
public class ProductRepositoryTest {

    @Inject
    ProductRepository productRepository;

    @Test
    public void should_save_and_find_product(){
        Optional<Product> product = productRepository.createProduct(TestHelper.productMap("apple"));
        assertThat(product.isPresent(), is(true));
    }

    @Test
    public void should_list_all_products(){
        Product product = productRepository.createProduct(TestHelper.productMap("name")).get();
        List<Product> list = productRepository.listProducts();
        assertThat(list.size(), is(1));
    }

    @Test
    public void should_find_product(){
        Product product = productRepository.createProduct(TestHelper.productMap("name")).get();
        Optional<Product> product_res = productRepository.findById(product.getId());
        assertThat(product_res.isPresent(), is(true));
    }
}

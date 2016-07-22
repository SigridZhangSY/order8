package com.thoughtworks.ketsu.infrastructure.repositories;

import com.thoughtworks.ketsu.domain.order.Order;
import com.thoughtworks.ketsu.domain.payment.Payment;
import com.thoughtworks.ketsu.domain.product.Product;
import com.thoughtworks.ketsu.domain.product.ProductRepository;
import com.thoughtworks.ketsu.domain.user.User;
import com.thoughtworks.ketsu.support.DatabaseTestRunner;
import com.thoughtworks.ketsu.support.TestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Or;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(DatabaseTestRunner.class)
public class UserTest {

    @Inject
    UserRepository userRepository;
    @Inject
    ProductRepository productRepository;

    @Test
    public void should_save_and_find_order(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Optional<Order> order = user.createOrder(TestHelper.orderMap(product.getId()));
        assertThat(order.isPresent(), is(true));
    }

    @Test
    public void should_list_orders(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Order order = user.createOrder(TestHelper.orderMap(product.getId())).get();
        List<Order> orderList = user.listOrders();
        assertThat(orderList.size(), is(1));
    }

    @Test
    public void should_find_order(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Order order = user.createOrder(TestHelper.orderMap(product.getId())).get();
        Optional<Order> order_res = user.findOrder(order.getId());
        assertThat(order_res.isPresent(), is(true));
    }

    @Test
    public void should_save_and_find_payment(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Order order = user.createOrder(TestHelper.orderMap(product.getId())).get();
        Optional<Payment> payment = order.createPayment(TestHelper.paymentMap());
        assertThat(payment.isPresent(), is(true));
    }

    @Test
    public void should_find_payment(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Order order = user.createOrder(TestHelper.orderMap(product.getId())).get();
        Payment payment = order.createPayment(TestHelper.paymentMap()).get();
        Optional<Payment> payment_res = order.findPayment();
        assertThat(payment_res.isPresent(), is(true));
    }
}

package com.thoughtworks.ketsu.web;


import com.thoughtworks.ketsu.domain.order.Order;
import com.thoughtworks.ketsu.domain.payment.Payment;
import com.thoughtworks.ketsu.domain.product.Product;
import com.thoughtworks.ketsu.domain.product.ProductRepository;
import com.thoughtworks.ketsu.domain.user.User;
import com.thoughtworks.ketsu.domain.user.UserRepository;
import com.thoughtworks.ketsu.support.ApiSupport;
import com.thoughtworks.ketsu.support.ApiTestRunner;
import com.thoughtworks.ketsu.support.TestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(ApiTestRunner.class)
public class UsersApiTest extends ApiSupport{

    @Inject
    UserRepository userRepository;
    @Inject
    ProductRepository productRepository;

    @Test
    public void should_return_201_when_post(){
        Response post = post("users", TestHelper.userMap("xxx"));
        assertThat(post.getStatus(), is(201));
        assertThat(Pattern.matches(".*/users/[0-9-]*", post.getLocation().toASCIIString()), is(true));
    }

    @Test
    public void should_return_400_when_name_is_empty(){
        Response post = post("users", new HashMap<String, Object>());
        assertThat(post.getStatus(), is(400));
        final List<Map<String, Object>> list = post.readEntity(List.class);
        assertThat(list.size(), is(1));
    }

    @Test
    public void should_return_detail_when_find_user(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Response get = get("users/" + user.getId());
        assertThat(get.getStatus(), is(200));
        final Map<String, Object> map = get.readEntity(Map.class);
        assertThat(map.get("uri"), is("/users/" + user.getId()));
    }

    @Test
    public void should_return_404_when_user_not_found(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Response get = get("users/" + (user.getId()+1));
        assertThat(get.getStatus(), is(404));
    }

    @Test
    public void should_return_201_when_create_order(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Response post = post("users/" + user.getId() + "/orders", TestHelper.orderMap(product.getId()));
        assertThat(post.getStatus(), is(201));
        assertThat(Pattern.matches(".*/orders/[0-9-]*", post.getLocation().toASCIIString()), is(true));
    }

    @Test
    public void should_return_400_when_create_order_with_name_is_empty(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Map<String, Object> map = TestHelper.orderMap(product.getId());
        map.remove("name");
        Response post = post("users/" + user.getId() + "/orders", map);
        assertThat(post.getStatus(), is(400));
        final List<Map<String, Object>> list = post.readEntity(List.class);
        assertThat(list.size(), is(1));
    }

    @Test
    public void should_return_detail_when_list_orders(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Order order = user.createOrder(TestHelper.orderMap(product.getId())).get();
        Response get = get("users/" + user.getId() + "/orders");
        assertThat(get.getStatus(), is(200));
        final List<Map<String, Object>> list = get.readEntity(List.class);
        assertThat(list.size(), is(1));
        assertThat(list.get(0).get("uri"), is("/users/" + user.getId() + "/orders/" + order.getId()));
    }

    @Test
    public void should_return_detail_when_find_order(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Order order = user.createOrder(TestHelper.orderMap(product.getId())).get();
        Response get = get("users/" + user.getId() + "/orders/" + order.getId());
        assertThat(get.getStatus(), is(200));
        final Map<String, Object> map = get.readEntity(Map.class);
        assertThat(map.get("uri"), is("/users/" + user.getId() + "/orders/" + order.getId()));
        final List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("order_items");
        assertThat(list.get(0).get("uri"), is("/products/" + product.getId()));

    }

    @Test
    public void should_return_404_when_order_not_exist(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Order order = user.createOrder(TestHelper.orderMap(product.getId())).get();
        Response get = get("users/" + user.getId() + "/orders/" + (order.getId()+1));
        assertThat(get.getStatus(), is(404));
    }

    @Test
    public void should_return_201_when_post_payment(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Order order = user.createOrder(TestHelper.orderMap(product.getId())).get();
        Response post = post("users/" + user.getId() + "/orders/" + order.getId() + "/payment", TestHelper.paymentMap());
        assertThat(post.getStatus(), is(201));
        assertThat(Pattern.matches(".*/orders/[0-9-]*/payment", post.getLocation().toASCIIString()), is(true));
    }

    @Test
    public void should_return_400_when_pay_type_is_empty(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Order order = user.createOrder(TestHelper.orderMap(product.getId())).get();
        Map<String, Object> map = TestHelper.paymentMap();
        map.remove("pay_type");
        Response post = post("users/" + user.getId() + "/orders/" + order.getId() + "/payment", map);
        assertThat(post.getStatus(), is(400));
        final List<Map<String, Object>> list = post.readEntity(List.class);
        assertThat(list.size(), is(1));
    }

    @Test
    public void should_return_detail_when_find_payment(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Order order = user.createOrder(TestHelper.orderMap(product.getId())).get();
        Payment payment = order.createPayment(TestHelper.paymentMap()).get();

        Response get = get("users/" + user.getId() + "/orders/" + order.getId() + "/payment");
        assertThat(get.getStatus(), is(200));
        final Map<String, Object> map = get.readEntity(Map.class);
        assertThat(map.get("uri"), is("/users/" + user.getId() + "/orders/" + order.getId() + "/payment"));
    }

    @Test
    public void should_return_404_when_payment_not_exists(){
        User user = userRepository.createUser(TestHelper.userMap("xxx")).get();
        Product product = productRepository.createProduct(TestHelper.productMap("apple")).get();
        Order order = user.createOrder(TestHelper.orderMap(product.getId())).get();

        Response get = get("users/" + user.getId() + "/orders/" + order.getId() + "/payment");
        assertThat(get.getStatus(), is(404));
    }
}

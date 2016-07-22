package com.thoughtworks.ketsu.web;


import com.thoughtworks.ketsu.support.ApiSupport;
import com.thoughtworks.ketsu.support.ApiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.util.HashMap;

@RunWith(ApiTestRunner.class)
public class ProductApiTest extends ApiSupport{

    @Test
    public void should_return_201_when_post(){
        Response post = post("products", new HashMap<String, Object>());
    }
}

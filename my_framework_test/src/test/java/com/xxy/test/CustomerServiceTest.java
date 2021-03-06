package com.xxy.test;

import com.xxy.model.Customer;
import com.xxy.service.CustomerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceTest {
    private final CustomerService customerService;

    public CustomerServiceTest(){
        customerService = new CustomerService();
    }
    @Before
    public void init(){
        //初始化数据库
    }
    @Test
    public void getCustomerListTest() throws Exception{
        String keyWord = "xxy";
        List<Customer> customerList = customerService.getCustomerList(keyWord);
        Assert.assertEquals(2, customerList.size());
    }

    @Test
    public void getCustomerTest() throws Exception{
        Long id = Long.valueOf(1);
        Customer customer = customerService.getCustomer(id);
        Assert.assertNotNull(customer);
    }

    @Test
    public void createCustomerTest() throws Exception{
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("name","customer100");
        fieldMap.put("contact","John");
        fieldMap.put("telephone","11111111111");
        boolean result = customerService.createCustomer(fieldMap);
        Assert.assertTrue(result);
    }
    @Test
    public void updateCustomerTest() throws Exception{
        Long id = Long.valueOf(1);
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("contact","Eric");
        boolean result = customerService.updateCustomer(id,fieldMap);
        Assert.assertTrue(result);
    }

    @Test
    public void deleteCustomerTest() throws  Exception{
        Long id = Long.valueOf(1);
        boolean result = customerService.deleteCustomer(id);
        Assert.assertTrue(result);

    }

}

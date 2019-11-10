package com.xxy.service;

import com.xxy.model.Customer;
import com.xxy.util.DatabaseHelper;
import com.xxy.util.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    /**
     * 获取客户列表
     */
    public List<Customer> getCustomerList(String keyWord){
       String sql = "SELECT *  FROM customer";
       return DatabaseHelper.queryEntityList(Customer.class,sql);
    }
    /**
     * 获取客户
     */
    public Customer getCustomer(Long id){
        String sql = "SELECT *  FROM customer WHERE id = " + id;
        List<Customer> list = DatabaseHelper.queryEntityList(Customer.class,sql);
        Customer customer = new Customer();
        if(list != null && list.size() > 0){
            customer = list.get(0);
        }
        return customer;
    }
    /**
     * 创建客户
     */
    public boolean createCustomer(Map<String, Object> fieldMap){
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    /**
     * 更新客户
     */
    public boolean updateCustomer(Long id, Map<String, Object> fieldMap){
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    /**
     * 删除客户
     */
    public  boolean deleteCustomer(Long id){
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }
}

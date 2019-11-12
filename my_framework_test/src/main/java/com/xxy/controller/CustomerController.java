package com.xxy.controller;

import com.xxy.annotation.Action;
import com.xxy.annotation.Controller;
import com.xxy.annotation.Inject;
import com.xxy.bean.Param;
import com.xxy.bean.View;
import com.xxy.model.Customer;
import com.xxy.service.CustomerService;

import java.util.List;


@Controller
public class CustomerController {
    @Inject
    private CustomerService customerService;
    /**
     * 进入客户列表界面
     */
    @Action("get:/customer")
    public View index(Param param){
        List<Customer> customerList = customerService.getCustomerList("");
        return new View("customer.jsp").addModel("customerList",customerList);
    }

}

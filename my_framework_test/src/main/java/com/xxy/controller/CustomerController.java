package com.xxy.controller;

import com.sun.glass.ui.View;
import com.xxy.model.Customer;
import com.xxy.service.CustomerService;

import javax.xml.ws.Action;
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

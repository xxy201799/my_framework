package com.xxy.model;

import com.xxy.annotation.Table;
import lombok.Data;

@Data
@Table(value = "customer")
public class Customer {
    /**
     * Id
     */
    private Long id;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 备注
     */
    private String remark;
}

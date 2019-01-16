package com.dcq.service;


import com.dcq.common.anno.Service;

/**
 * @日期: 2019-01-12 22:56
 * @作者: 杜超群
 * @描述:
 */
@Service
public class UserService {

    public void test(String name){
        System.out.println("Service invoke----------------"+name);
    }
}

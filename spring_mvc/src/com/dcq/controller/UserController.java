package com.dcq.controller;


import com.dcq.common.anno.Autowired;
import com.dcq.common.anno.Controller;
import com.dcq.common.anno.RequestMapping;
import com.dcq.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @日期: 2019-01-12 22:55
 * @作者: 杜超群
 * @描述:
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired("userService")
    private UserService userService;

    @RequestMapping("/index")
    public void index(HttpServletRequest request, HttpServletResponse response){
        String name = request.getParameter("name");
        userService.test(name);
    }
}

package com.dcq.test;

import java.net.URL;

/**
 * @日期: 2019-01-12 23:05
 * @作者: 杜超群
 * @描述:
 */
public  class Test {
    public static void main(String[] args) throws Exception {
        String packageName = "com.dcq";
        if(packageName ==null||"".equals(packageName)){
            throw new NullPointerException();
        }
        packageName = packageName.replaceAll("\\.","/");
        URL url = Test.class.getClassLoader().getResource("/"+packageName);
        System.out.println(Test.class.getClassLoader().getResource("/"));
    }
}

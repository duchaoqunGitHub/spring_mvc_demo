package com.dcq.common.servlet;

import com.dcq.common.anno.Autowired;
import com.dcq.common.anno.Controller;
import com.dcq.common.anno.RequestMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @日期: 2019-01-12 23:00
 * @作者: 杜超群
 * @描述:
 */
public class DispatcherServlet extends HttpServlet {
    /**
     * 存放扫描包下所有类路径
     */
    private Map<String,String> classPackageMap = new HashMap<>();
    /**
     * 存放方法和实例化后的Controller对象
     */
    private Map<String,Object> handleMapping = new HashMap<>();

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            initHandleMapping();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求路径
        String uri = request.getRequestURI();
        //获取项目路径
        String projectName = request.getContextPath();
        //获取指定Controller请求路径
        String path = uri.replace(projectName,"");
        //获取执行的方法
        Method method = (Method) handleMapping.get(path);
        PrintWriter printWriter = response.getWriter();

        if(method == null){
            printWriter.write("未找到该地址");
        }
        //获取Controller对象
        RequestMapping mappingAnno = method.getAnnotation(RequestMapping.class);
        String controllerKey = path.replace(mappingAnno.value(),"");
        Object controller = handleMapping.get(controllerKey);
        //执行对象具体方法
        try {
            method.invoke(controller,new Object[]{request,response});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * 初始化handleMapping
     */
    private void initHandleMapping() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        //1.扫描包获取class包路径
        scanPackage("com.dcq");
        //初始化实例
        initInstance();
    }

    /**
     * 递归算法扫描包下所有class
     * @param basePackage
     */
    private void scanPackage(String basePackage){
        URL url = this.getClass().getClassLoader().getResource("/"+replaceTo(basePackage));
        String path = url.getFile();
        //获取所有class类
        File file = new File(path);
        String[] files = file.list();
        for(String filePath:files){
            File file1 = new File(path+"/"+filePath);
            if(file1.isDirectory()){
                scanPackage(basePackage+"."+file1.getName());
            }else if(file1.getName().contains(".class")){
                String packName = file1.getName().replace(".class","");
                classPackageMap.put(packName,basePackage+"."+packName);
            }
        }
    }

    /**
     * 1.实例化Controller注解的类
     * 2.判断该类下是否存在 Service注解，存在则实例化该类，并set到controller中
     * 3.判断Controller层是否存在RequestMapping注解,存在则获取该注解的value值,将value值作为key，实例化后的controller作为value存在 map中
     * 4.遍历controller类中的method方法，如果存在方法则将 Controller的RequestMapping地址+方法的RequestMapping 作为key,method作为value存在map中
     * @throws ClassNotFoundException
     */
    private void initInstance() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //遍历所有class类
        for(String packageName:classPackageMap.keySet()){
            //获取指定class
            Class clazz = Class.forName(classPackageMap.get(packageName));
            //判断是否存在@Controller注解
            if(clazz.isAnnotationPresent(Controller.class)){
                //获取RequestMapping中的value
                RequestMapping annotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                String basePath = annotation.value();
                //实例化该controller
                Object object = clazz.newInstance();
                //获取该类下所有成员字段值
                Field[] fields = clazz.getDeclaredFields();
                for(Field field:fields){
                    //判断是否存在依赖注入的注解@Autowired
                    if(field.isAnnotationPresent(Autowired.class)){
                        //存在则实例化该类
                        Object serviceObject = field.getType().newInstance();
                        //如果设置的是private私有变量则通过该方法进行访问
                        field.setAccessible(true);
                        //将Service方法set到Controller中
                        field.set(object,serviceObject);
                    }
                }
                //遍历Controller下所有方法
                Method[] methods = clazz.getMethods();
                for(Method method:methods){
                    //判断方法下是否存在RequestMapping注解
                    if(method.isAnnotationPresent(RequestMapping.class)){
                        RequestMapping requestMappingAnno = method.getAnnotation(RequestMapping.class);
                        handleMapping.put(basePath+requestMappingAnno.value(),method);
                    }
                }
                handleMapping.put(basePath,object);
            }
        }
    }
    private String replaceTo(String basePackage){
        if(basePackage == null||"".equals(basePackage)){
            throw new NullPointerException();
        }
        basePackage = basePackage.replaceAll("\\.","/");
        return basePackage;
    }
}

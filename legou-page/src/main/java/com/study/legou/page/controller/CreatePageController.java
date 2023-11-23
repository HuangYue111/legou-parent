package com.study.legou.page.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/create")
@RestController
public class CreatePageController {

    @Value("${pagepath}")
    private String pagepath;

    @Autowired
    private TemplateEngine templateEngine;
    @GetMapping("/demo")
    public String demo() throws Exception{
        Context context = new Context();
        Map<String,Object> map = new HashMap<>();
        map.put("username","张三");
        context.setVariables(map);// model.addAttribute()


        File file = new File(pagepath);
        if(!file.exists()){
            file.mkdirs();
        }
        File dest = new File(file,"12345.html");

        PrintWriter writer = new PrintWriter(dest, "UTF-8");
        //生成页面
        templateEngine.process("demo",context,writer);
        return "ok";
    }

//    @GetMapping("/test")
//    public String test(Model model){
//        model.addAttribute("username","张三");
//        return "demo";
//    }
}

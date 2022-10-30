package com.wang.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
        try{
            Thread.sleep(10*1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}

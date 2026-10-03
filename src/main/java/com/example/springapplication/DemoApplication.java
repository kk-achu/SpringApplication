package com.example.springapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    @Bean
    public SampleBean sampleBean() {
        return new SampleBean();
    }

    @Bean
    public ChildBean childBean() {
        return new ChildBean();
    }

    public static void main(String[] args) {
        var context = SpringApplication.run(DemoApplication.class, args);

        // SampleBean sampleBean = new SampleBean();
        SampleBean sampleBean = context.getBean(SampleBean.class);
        System.out.println(sampleBean.getMessage());
        System.out.println(sampleBean.getChildMessage());
    }
}

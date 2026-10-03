package com.example.springapplication;

import org.springframework.beans.factory.annotation.Autowired;

public class SampleBean {

    @Autowired 
    private final ChildBean childBean = null;//new ChildBean();

    public String getMessage() {
        return "I am a Spring bean";
    }

    public String getChildMessage() {
        return childBean.getMessage();
    }
}
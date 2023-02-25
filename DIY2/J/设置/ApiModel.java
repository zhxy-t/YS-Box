package com.github.tvbox.osc.bean;

import java.io.Serializable;

public class ApiModel implements Serializable {
    private String name;
    private String url;

    public ApiModel() {
        String str = "";
        this.name = str;
        this.url = str;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setUrl(String str) {
        this.url = str;
    }
   
}

package com.azure.spring.cloud.feature.manager.testobjects;

public class DiscountBannerSize {

    private Integer size;

    private String color;

    public Integer getSize() {
        return size;
    }

    public DiscountBannerSize setSize(Integer size) {
        this.size = size;
        return this;
    }

    public String getColor() {
        return color;
    }

    public DiscountBannerSize setColor(String color) {
        this.color = color;
        return this;
    }
    
    @Override
    public String toString() {
        return "DiscountBannder: Size " + size + " Color " + color;
    }
}

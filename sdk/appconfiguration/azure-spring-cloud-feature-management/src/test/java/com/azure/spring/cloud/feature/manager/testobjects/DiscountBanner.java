package com.azure.spring.cloud.feature.manager.testobjects;

public class DiscountBanner {

    private DiscountBannerSize big;

    private DiscountBannerSize small;

    /**
     * @return the big
     */
    public DiscountBannerSize getBig() {
        return big;
    }

    /**
     * @param big the big to set
     */
    public void setBig(DiscountBannerSize big) {
        this.big = big;
    }

    /**
     * @return the small
     */
    public DiscountBannerSize getSmall() {
        return small;
    }

    /**
     * @param small the small to set
     */
    public void setSmall(DiscountBannerSize small) {
        this.small = small;
    }

}

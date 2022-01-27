package com.azure.spring.cloud.feature.manager.testobjects;

import com.azure.spring.cloud.feature.manager.IDynamicFeatureProperties;

public class MockableProperties implements IDynamicFeatureProperties {

    private DiscountBanner discountBanner;

    /**
     * @return the discountBanner
     */
    public DiscountBanner getDiscountBanner() {
        return discountBanner;
    }

    /**
     * @param discountBanner the discountBanner to set
     */
    public void setDiscountBanner(DiscountBanner discountBanner) {
        this.discountBanner = discountBanner;
    }

}

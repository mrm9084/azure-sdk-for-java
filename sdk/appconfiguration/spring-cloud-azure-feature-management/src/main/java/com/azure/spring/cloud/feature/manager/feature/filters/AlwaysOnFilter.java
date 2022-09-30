package com.azure.spring.cloud.feature.manager.feature.filters;

import com.azure.spring.cloud.feature.manager.models.FeatureFilterEvaluationContext;
import com.azure.spring.cloud.feature.manager.models.IFeatureFilter;

/**
 * A filter that always returns true
 */
public class AlwaysOnFilter implements IFeatureFilter {

    @Override
    public boolean evaluate(FeatureFilterEvaluationContext context) {
        return true;
    }

}

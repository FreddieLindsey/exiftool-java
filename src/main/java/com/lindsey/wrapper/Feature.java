package com.lindsey.wrapper;

public enum Feature {

    STAY_OPEN(8.36),

    ;

    private final Double requiredVersion;

    Feature(Double requiredVersion) {
        this.requiredVersion = requiredVersion;
    }

    public static boolean isCompatible(Feature feature, Double toolVersion) {
        return feature.requiredVersion <= toolVersion;
    }

}

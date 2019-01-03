package com.lindsey.wrapper;

public enum Feature {

    STAY_OPEN(8.36, "-stay_open"),

    ;

    private final Double requiredVersion;
    private final String flag;

    Feature(Double requiredVersion) {
        this(requiredVersion, null);
    }

    Feature(Double requiredVersion, String flag) {
        this.requiredVersion = requiredVersion;
        this.flag = flag;
    }

    public static boolean isCompatible(Feature feature, Double toolVersion) {
        return feature.requiredVersion <= toolVersion;
    }

    public static boolean hasFlag(Feature feature) {
        return feature.flag != null;
    }

    public static String getFlag(Feature feature) {
        return feature.flag;
    }

}

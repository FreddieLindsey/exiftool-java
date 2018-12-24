package com.lindsey.wrapper;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExifTool {

    private final Set<Feature> features;
    private final Logger logger;

    private Process longRunningProcess;

    private ExifTool(Set<Feature> features, Logger logger) {
        this.features = features;
        this.logger = logger;

        assert(features.stream().allMatch(feature -> {
            try {
                return Feature.isCompatible(feature, getInstalledVersion());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public void startLongRunningProcess() {

    }

    public void cancelLongRunningProcess() {
        longRunningProcess.destroy();
        assert(!longRunningProcess.isAlive());
    }

    public static Double getInstalledVersion() throws IOException, InterruptedException {
        List<String> argsList = new ArrayList<>();
        argsList.add("exiftool");
        argsList.add("-ver");
        CommandRunner.run(argsList.toArray(new String[]{}));
        return 0.0;
    }

    public static class Builder {

        private Logger logger;
        private Set<Feature> features = new HashSet<>();

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder features(Set<Feature> features) {
            this.features = features;
            return this;
        }

        public ExifTool build() {
            return new ExifTool(features, logger);
        }

    }

}

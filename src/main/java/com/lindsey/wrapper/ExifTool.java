package com.lindsey.wrapper;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static java.lang.Double.parseDouble;

public class ExifTool {

    private static Double INSTALLED_VERSION;

    private final Set<Feature> features;
    private final Logger logger;

    private Process longRunningProcess;

    private ExifTool(Set<Feature> features, Logger logger) {
        this.features = features;
        this.logger = logger != null ? logger : LoggerFactory.getLogger(ExifTool.class);

        assert(features.stream().allMatch(feature -> {
            try {
                return Feature.isCompatible(feature, getInstalledVersion());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public void startLongRunningProcess() throws IOException, InterruptedException {
        if (features.contains(Feature.STAY_OPEN)) {
            List<String> argsList = new ArrayList<>();
            argsList.add("exiftool");
            if (Feature.hasFlag(Feature.STAY_OPEN)) {
                argsList.add(Feature.getFlag(Feature.STAY_OPEN));
            }
            longRunningProcess = CommandRunner.run(argsList);
        }
    }

    public void cancelLongRunningProcess() {
        longRunningProcess.destroy();
        assert !longRunningProcess.isAlive() : "Long running process is still alive.";
    }

    public Double getInstalledVersion() throws IOException, InterruptedException {
        if (INSTALLED_VERSION == null) {
            List<String> argsList = new ArrayList<>();
            argsList.add("exiftool");
            argsList.add("-ver");
            logger.debug("Running command with args: {}", argsList);
            Pair<List<String>, List<String>> result = CommandRunner.runAndFinish(argsList);
            if (result.getKey().size() == 0) {
                throw new RuntimeException("Could not get installed version of ExifTool. Is ExifTool installed?");
            }
            logger.info("Installed ExifTool Version: {}", result.getKey());
            INSTALLED_VERSION = parseDouble(result.getKey().get(0));
        }
        return INSTALLED_VERSION;
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

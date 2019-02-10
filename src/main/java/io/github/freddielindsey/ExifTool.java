package com.lindsey.wrapper;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;

public class ExifTool {

    private static Double INSTALLED_VERSION;

    private final Set<Feature> features;
    private final Logger logger;

    private Process longRunningProcess;

    private ExifTool(Set<Feature> features, Logger logger) throws IOException, InterruptedException {
        this.features = features;
        this.logger = logger != null ? logger : LoggerFactory.getLogger(ExifTool.class);

        Double installedVersion = getInstalledVersion();
        features.forEach(feature -> {
            if (!Feature.isCompatible(feature, installedVersion)) {
                throw new UnsupportedOperationException(String.format(
                        "Feature %s not supported by ExifTool version %s", feature, installedVersion
                ));
            }
        });
    }

    public void startLongRunningProcess() throws IOException, InterruptedException {
        if (features.contains(Feature.STAY_OPEN)) {
            List<String> argsList = new ArrayList<>();
            argsList.add("exiftool");
            argsList.add(Feature.getFlag(Feature.STAY_OPEN));
            argsList.add("True");
            argsList.add("-@");
            argsList.add("-");
            longRunningProcess = CommandRunner.run(argsList);
        }
    }

    public void cancelLongRunningProcess() throws InterruptedException {
        longRunningProcess.destroyForcibly().waitFor();
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

    public <T> Map<Key, T> query(File file, Set<Key> keys) throws IOException, InterruptedException {
        return (longRunningProcess != null && longRunningProcess.isAlive())
                ? queryLongRunning(file, keys)
                : queryShortLived(file, keys);
    }

    private <T> Map<Key, T> queryLongRunning(File file, Set<Key> keys) throws IOException, InterruptedException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(longRunningProcess.getOutputStream());
        List<String> argsList = new ArrayList<>();
        argsList.add("-S");
        for (Key key : keys) {
            argsList.add(String.format("-%s", Key.getName(key)));
        }
        argsList.add(file.getAbsolutePath());
        argsList.add("-execute\n");
        outputStreamWriter.write(String.join("\n", argsList));
        outputStreamWriter.flush();

        String line;
        BufferedReader stdOutStreamReader = new BufferedReader(new InputStreamReader(longRunningProcess.getInputStream()));
        List<String> stdOut = new ArrayList<>();
        while ((line = stdOutStreamReader.readLine()) != null) {
            if (line.equals("{ready}")) {
                break;
            }
            stdOut.add(line);
        }

        BufferedReader stdErrStreamReader = new BufferedReader(new InputStreamReader(longRunningProcess.getErrorStream()));
        List<String> stdErr = new ArrayList<>();
        while (stdErrStreamReader.ready() && (line = stdErrStreamReader.readLine()) != null) {
            stdErr.add(line);
        }

        return processQueryResult(stdOut, stdErr);
    }

    private <T> Map<Key, T> queryShortLived(File file, Set<Key> keys) throws IOException, InterruptedException {
        List<String> argsList = new ArrayList<>();
        argsList.add("exiftool");
        argsList.add("-S");
        for (Key key : keys) {
            argsList.add(String.format("-%s", Key.getName(key)));
        }
        argsList.add(file.getAbsolutePath());
        Pair<List<String>, List<String>> result = CommandRunner.runAndFinish(argsList);
        List<String> stdOut = result.getKey();
        List<String> stdErr = result.getValue();

        return processQueryResult(stdOut, stdErr);
    }

    private <T> Map<Key, T> processQueryResult(List<String> stdOut, List<String> stdErr) {
        Map<Key, T> queryResult = new HashMap<>();
        if (stdErr.size() > 0) {
            throw new RuntimeException(String.join("\n", stdErr));
        }

        for (String line : stdOut) {
            List<String> lineSeparated = Arrays.asList(line.split(":"));
            if (lineSeparated.size() < 2) {
                continue;
            }
            String name = lineSeparated.get(0).trim();
            String value = String.join(":", lineSeparated.subList(1, lineSeparated.size())).trim();
            Optional<Key> maybeKey = Key.findKeyWithName(name);
            maybeKey.ifPresent(key -> queryResult.put(key, Key.parse(key, value)));
        }
        return queryResult;
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

        public ExifTool build() throws IOException, InterruptedException {
            return new ExifTool(features, logger);
        }

    }

}

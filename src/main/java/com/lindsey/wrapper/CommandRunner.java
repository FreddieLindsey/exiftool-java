package com.lindsey.wrapper;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public final class CommandRunner {

    public static void run(String[] args) throws IOException, InterruptedException {
        run(args, FileSystems.getDefault().getPath("."));
    }

    public static void run(String[] args, Path workingDir) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(args);
        processBuilder.directory(workingDir.toFile());

        Process process = processBuilder.start();

        process.waitFor();
    }

}

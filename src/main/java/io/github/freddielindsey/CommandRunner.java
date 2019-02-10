package io.github.freddielindsey;

import javafx.util.Pair;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class CommandRunner {

    public static Process run(List<String> args) throws IOException, InterruptedException {
        return run(args, FileSystems.getDefault().getPath("."));
    }

    public static Process run(List<String> args, Path workingDir) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(args);
        processBuilder.directory(workingDir.toFile());

        return processBuilder.start();
    }

    public static Pair<List<String>, List<String>> runAndFinish(List<String> args) throws IOException, InterruptedException {
        return runAndFinish(args, FileSystems.getDefault().getPath("."));
    }

    public static Pair<List<String>, List<String>> runAndFinish(List<String> args, Path workingDir) throws IOException, InterruptedException {
        Process process = run(args, workingDir);
        process.waitFor();
        return new Pair<>(getStreamContent(process.getInputStream()), getStreamContent(process.getErrorStream()));
    }
    
    private static List<String> getStreamContent(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        List<String> result = new ArrayList<>();
        while (scanner.hasNextLine()) {
            result.add(scanner.nextLine());
        }
        return result;
    }

}

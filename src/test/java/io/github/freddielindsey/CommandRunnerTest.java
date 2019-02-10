package io.github.freddielindsey;

import javafx.util.Pair;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class CommandRunnerTest {

    @Test
    public void run_with_args() throws IOException, InterruptedException {
        Process process = CommandRunner.run(singletonList("pwd"));
        assertTrue(process.isAlive());
        process.waitFor();
        assertFalse(process.isAlive());
    }

    @Test
    public void run_with_args_and_path() throws IOException, InterruptedException {
        Process process = CommandRunner.run(singletonList("pwd"), Paths.get("/"));
        assertTrue(process.isAlive());
        process.waitFor();
        assertFalse(process.isAlive());
    }

    @Test
    public void run_and_finish_with_args() throws IOException, InterruptedException {
        Pair<List<String>, List<String>> outputs = CommandRunner.runAndFinish(singletonList("pwd"));
        assertThat(outputs.getValue().size(), CoreMatchers.is(0));

        List<String> cwd = singletonList(Paths.get("").toAbsolutePath().toString());
        assertThat(outputs.getKey(), CoreMatchers.is(cwd));
    }

    @Test
    public void run_and_finish_with_args_and_path() throws IOException, InterruptedException {
        Pair<List<String>, List<String>> outputs = CommandRunner.runAndFinish(singletonList("pwd"), Paths.get("/"));
        assertThat(outputs.getValue().size(), CoreMatchers.is(0));

        List<String> cwd = singletonList(Paths.get("/").toAbsolutePath().toString());
        assertThat(outputs.getKey(), CoreMatchers.is(cwd));
    }
}
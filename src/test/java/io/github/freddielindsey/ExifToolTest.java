package io.github.freddielindsey;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExifToolTest {

    @Test
    public void getInstalledVersionReturnsVersionGreaterThan0() throws IOException, InterruptedException {
        Double version = new ExifTool.Builder().build().getInstalledVersion();

        assertTrue(version > 0);
    }

    @Test
    public void canStartAndStopLongRunningProcess() throws IOException, InterruptedException {
        ExifTool exifTool = new ExifTool.Builder().features(singleton(Feature.STAY_OPEN)).build();

        exifTool.startLongRunningProcess();
        exifTool.cancelLongRunningProcess();
    }

    @Test
    public void canQueryFileUsingShortLivedProcess() throws IOException, InterruptedException {
        ExifTool exifTool = new ExifTool.Builder().features(singleton(Feature.STAY_OPEN)).build();

        Map<Key, ?> queryResult = exifTool.query(
                new File("./src/test/resources/sample_files/datetimeoriginal.jpg"),
                singleton(Key.DATETIMEORIGINAL)
        );

        assertEquals("2019:01:01 00:00:00", queryResult.get(Key.DATETIMEORIGINAL));
    }

    @Test
    public void canQueryFileUsingLongRunningProcess() throws IOException, InterruptedException {
        ExifTool exifTool = new ExifTool.Builder().features(singleton(Feature.STAY_OPEN)).build();

        exifTool.startLongRunningProcess();

        Map<Key, ?> queryResult = exifTool.query(
                new File("./src/test/resources/sample_files/datetimeoriginal.jpg"),
                singleton(Key.DATETIMEORIGINAL)
        );

        exifTool.cancelLongRunningProcess();

        assertEquals("2019:01:01 00:00:00", queryResult.get(Key.DATETIMEORIGINAL));
    }

    @Test
    public void longRunningProcessTakesLessTimeThanShortLived() throws IOException, InterruptedException {
        ExifTool exifTool = new ExifTool.Builder().features(singleton(Feature.STAY_OPEN)).build();

        exifTool.startLongRunningProcess();
        Thread.sleep(100); // Give process time to start

        long longRunningTime = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            exifTool.query(
                    new File("./src/test/resources/sample_files/datetimeoriginal.jpg"),
                    singleton(Key.DATETIMEORIGINAL)
            );
        }
        longRunningTime = System.currentTimeMillis() - longRunningTime;
        exifTool.cancelLongRunningProcess();

        long shortLivedTime = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            exifTool.query(
                    new File("./src/test/resources/sample_files/datetimeoriginal.jpg"),
                    singleton(Key.DATETIMEORIGINAL)
            );
        }
        shortLivedTime = System.currentTimeMillis() - shortLivedTime;

        assertTrue(longRunningTime < shortLivedTime);
    }

}
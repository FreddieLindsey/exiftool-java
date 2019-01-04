package com.lindsey.wrapper;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.util.Collections.singleton;
import static org.junit.Assert.*;

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
    public void canQueryFile() throws IOException, InterruptedException {
        ExifTool exifTool = new ExifTool.Builder().features(singleton(Feature.STAY_OPEN)).build();

        Map<Key, ?> queryResult = exifTool.query(
                new File("./src/test/resources/sample_files/datetimeoriginal.jpg"),
                singleton(Key.DATETIMEORIGINAL)
        );

        assertEquals("2009:08:11 10:39:42", queryResult.get(Key.DATETIMEORIGINAL));
    }

}
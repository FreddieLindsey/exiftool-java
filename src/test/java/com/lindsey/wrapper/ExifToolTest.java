package com.lindsey.wrapper;

import org.junit.Test;

import java.io.IOException;

import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExifToolTest {

    @Test
    public void getInstalledVersionReturnsDouble() throws IOException, InterruptedException {
        Double version = new ExifTool.Builder().build().getInstalledVersion();

        assertEquals(version, 11.11, 0.0001);
    }

    @Test
    public void canStartAndStopLongRunningProcess() throws IOException, InterruptedException {
        ExifTool exifTool = new ExifTool.Builder().features(singleton(Feature.STAY_OPEN)).build();

        exifTool.startLongRunningProcess();
        exifTool.cancelLongRunningProcess();
    }

}
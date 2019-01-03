package com.lindsey.wrapper;

import org.junit.Test;

import java.io.IOException;

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

}
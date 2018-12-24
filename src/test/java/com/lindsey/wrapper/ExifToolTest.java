package com.lindsey.wrapper;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ExifToolTest {

    @Test
    public void getInstalledVersionReturnsDouble() throws IOException, InterruptedException {
        Double version = ExifTool.getInstalledVersion();

        assertNotNull(version);
    }

}
package com.g3.seapp;

import com.g3.seapp.server.DataManager;
import org.junit.Assert;
import org.junit.Test;

/**
 * Defines tests for the DataManager class
 */
public class DataManagerTest {

    /**
     * Tests if the datamanager can load its data
     */
    @Test
    public void testDataManagerCanLoadData() {
        DataManager.loadData();

        Assert.assertNotNull(DataManager.getMeasurements());
        Assert.assertEquals(DataManager.getMeasurements().size(), 228175);
    }
}

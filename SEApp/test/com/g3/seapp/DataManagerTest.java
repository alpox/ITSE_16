package com.g3.seapp;

import com.g3.seapp.server.DataManager;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Elias on 24.11.16.
 */
public class DataManagerTest {

    @Test
    public void testDataManagerCanLoadData() {
        DataManager.loadData();

        Assert.assertNotNull(DataManager.getMeasurements());
        Assert.assertNotNull(DataManager.getCountryCollection());
        Assert.assertEquals(DataManager.getMeasurements().size(), 228175);
    }
}

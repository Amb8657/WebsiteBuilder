package com.amb8657.websitebuilder;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class V4UiTest {
    @Test public void dashboardShowsCanonicalWorkflow() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), WebsiteBuilderV4Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        try (ActivityScenario<WebsiteBuilderV4Activity> ignored =
                     ActivityScenario.launch(intent)) {
            assertTrue("Website Builder heading not visible", device.wait(Until.hasObject(By.text("Website Builder")), 10000));
            assertTrue("V4 hero heading not visible", device.wait(Until.hasObject(By.text("Build something beautiful")), 10000));
            assertTrue("Create action not visible", device.wait(Until.hasObject(By.textContains("Create a website")), 5000));
            assertTrue("Quick add section not visible", device.wait(Until.hasObject(By.text("Quick add")), 5000));
            assertNotNull("V4 activity did not create a window", device.getCurrentPackage());
        }
    }
}

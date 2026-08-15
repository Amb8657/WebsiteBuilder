package com.amb8657.websitebuilder;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BuilderV3SmokeTest {
    @Test
    public void builderLaunches() {
        try (ActivityScenario<BuilderV3Activity> ignored =
                     ActivityScenario.launch(BuilderV3Activity.class)) {
            // Successful ActivityScenario.launch means the activity could be created.
        }
    }
}

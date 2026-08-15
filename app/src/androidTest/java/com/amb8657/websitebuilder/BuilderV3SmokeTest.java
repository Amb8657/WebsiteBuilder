package com.amb8657.websitebuilder;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BuilderV3SmokeTest {
    @Rule
    public ActivityScenarioRule<BuilderV3Activity> activityRule =
            new ActivityScenarioRule<>(BuilderV3Activity.class);

    @Test
    public void builderLaunches() {
        onView(withText("Website Builder")).check(isDisplayed());
    }
}

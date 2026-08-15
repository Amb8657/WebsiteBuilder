package com.amb8657.websitebuilder;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class V4UiTest {
    @Rule public ActivityScenarioRule<WebsiteBuilderV4Activity> activity =
            new ActivityScenarioRule<>(WebsiteBuilderV4Activity.class);

    @Test public void dashboardShowsCanonicalWorkflow() throws Exception {
        Thread.sleep(2600);
        onView(withText("Website Builder")).check(matches(isDisplayed()));
        onView(withText("Build something beautiful")).check(matches(isDisplayed()));
        onView(withText("Create a website")).check(matches(isDisplayed()));
        onView(withText("Quick add")).check(matches(isDisplayed()));
    }
}

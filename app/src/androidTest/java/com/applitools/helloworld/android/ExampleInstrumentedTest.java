package com.applitools.helloworld.android;

import android.support.test.rule.ActivityTestRule;

import com.applitools.eyes.android.common.TestResults;
import com.applitools.eyes.android.espresso.Eyes;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void simpleTest() {

        // Initialize the eyes SDK and set your private API key.
        Eyes eyes = new Eyes();
        eyes.setApiKey("YOUR_API_KEY");

        try {
            // Start the test
            eyes.open("Hello World!", "My first Espresso Android test!");

            // Visual checkpoint #1.
            eyes.checkWindow("Hello!");

            onView(withId(R.id.click_me_btn)).perform(click());

            // Visual checkpoint #2.
            eyes.checkWindow("Click!");

            // End the test.
            TestResults results = eyes.close();
            assertTrue(results.isPassed());
        } finally {
            // If the test was aborted before eyes.close was called, ends the test as aborted.
            eyes.abortIfNotClosed();
        }
    }
}

package com.applitools.helloworld.android;

import android.support.test.rule.ActivityTestRule;

import com.applitools.eyes.android.common.BatchInfo;
import com.applitools.eyes.android.common.EyesRunner;
import com.applitools.eyes.android.common.TestResultContainer;
import com.applitools.eyes.android.common.TestResultsSummary;
import com.applitools.eyes.android.espresso.ClassicRunner;
import com.applitools.eyes.android.espresso.Eyes;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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

    private static EyesRunner runner = null;
    private static Eyes eyes = null;
    private static BatchInfo batch = null;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @BeforeClass
    public static void batchInitialization(){
        batch = new BatchInfo("Espresso Batch");
        batch.setId(BuildConfig.APPLITOOLS_BATCH_ID);
        batch.setSequenceName("Espresso");
        batch.setNotifyOnCompletion(true);
    }

    @Test
    public void simpleTest() {

        runner = new ClassicRunner();
        // Initialize the eyes SDK and set your private API key.
        eyes = new Eyes(runner);
        eyes.setApiKey(BuildConfig.APPLITOOLS_API_KEY);
        eyes.setBatch(batch);

        System.out.println("\n******* MY BATCH ID: " + System.getenv("APPLITOOLS_BATCH_ID"));

        try {
            // Start the test
            eyes.open("Hello World!", "My first Espresso Android test!");

            // Visual checkpoint #1.
            eyes.checkWindow("Hello!");

            onView(withId(R.id.click_me_btn)).perform(click());

            // Visual checkpoint #2.
            eyes.checkWindow("Click!");

            // End the test.
            eyes.close();
        } finally {
            // If the test was aborted before eyes.close was called, ends the test as aborted.
            eyes.abortIfNotClosed();
        }
    }

    @AfterClass
    public static void afterTestSuite() {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        for (TestResultContainer result : allTestResults) {
            assertTrue(result.getTestResults().isPassed());
        }
    }
}

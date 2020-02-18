package com.applitools.helloworld.android;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.applitools.eyes.android.common.BatchInfo;
import com.applitools.eyes.android.common.EyesRunner;
import com.applitools.eyes.android.common.TestResultContainer;
import com.applitools.eyes.android.common.TestResults;
import com.applitools.eyes.android.common.TestResultsSummary;
import com.applitools.eyes.android.common.config.Configuration;
import com.applitools.eyes.android.espresso.ClassicRunner;
import com.applitools.eyes.android.espresso.Eyes;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DocumentationExampleTest {

    private static final String TAG = "DocumentationExampleTest";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    private static String eyesServerUrl = "https://eyesapi.applitools.com";
    private static String appName = "EKB Example : classic app";
    private static String batchName = "EKB Example : classic";
    private static String apiKey = "YOUR_API_KEY";
    private static EyesRunner runner = null;
    private static Configuration suiteConfig;
    private Eyes eyes;

    @BeforeClass
    public static void beforeTestSuite() {
        runner = new ClassicRunner();
        suiteConfig = new Configuration();
        suiteConfig.setHideCaret(true)
                .setAppName(appName)
                .setApiKey(apiKey)
                .setServerUrl(eyesServerUrl)
                .setBatch(new BatchInfo(batchName));
    }

    @Before
    public void beforeEachTest() {
        eyes = new Eyes(runner);
        eyes.setConfiguration(suiteConfig);
    }

    @Test
    public void testStartScreen() {
        eyes.open("Hello World test");

        eyes.checkWindow("Before button click");

        onView(withId(R.id.click_me_btn)).perform(click());

        eyes.checkWindow("After button click");
    }

    @After
    public void afterEachTest() {
        try {
            eyes.close();
        } finally {
            eyes.abortIfNotClosed();
        }
    }

    @AfterClass
    public static void afterTestSuite() {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        for (TestResultContainer result : allTestResults) {
            handleTestResults(result);
        }
    }

    private static void handleTestResults(TestResultContainer summary) {
        Throwable ex = summary.getException();
        if (ex != null) {
            Log.e(TAG, "System error occurred while checking target.");
        }
        TestResults result = summary.getTestResults();
        if (result == null) {
            Log.e(TAG, "No test results information available.");
        } else {
            Log.d(TAG, String.format("URL = %s, AppName = %s, testName = %s, matched = %d, mismatched = %d, missing = %d, aborted = %s",
                    result.getUrl(),
                    result.getAppName(),
                    result.getName(),
                    result.getMatches(),
                    result.getMismatches(),
                    result.getMissing(),
                    (result.isAborted() ? "aborted" : "no")));
        }
    }
}

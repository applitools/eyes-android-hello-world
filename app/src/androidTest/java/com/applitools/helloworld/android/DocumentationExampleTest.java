package com.applitools.helloworld.android;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;

import com.applitools.eyes.android.common.BatchInfo;
import com.applitools.eyes.android.common.EyesRunner;
import com.applitools.eyes.android.common.Region;
import com.applitools.eyes.android.common.TestResultContainer;
import com.applitools.eyes.android.common.TestResults;
import com.applitools.eyes.android.common.TestResultsSummary;
import com.applitools.eyes.android.common.config.Configuration;
import com.applitools.eyes.android.espresso.ClassicRunner;
import com.applitools.eyes.android.espresso.Eyes;
import com.applitools.eyes.android.espresso.fluent.EspressoCheckSettings;
import com.applitools.eyes.android.espresso.fluent.Target;

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
                //Add the following line to force use of Android PixelCopy to obtain screenshots 
                //This can improve the quality of the screenshot, for example to ensure rendering of the shadow layer.
                .setFeatures(Feature.PIXEL_COPY_SCREENSHOT) 
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

        eyes.checkRegion(ViewMatchers.withId(R.id.click_me_btn), "Click me button");
        eyes.check(Target.region(ViewMatchers.withId(R.id.click_me_btn)).withName("Click me button"));

        eyes.checkRegion(ViewMatchers.withId(R.id.hello_text_view), "HelloWorld label");
        View helloLabel = mActivityRule.getActivity().findViewById(R.id.hello_text_view);
        eyes.check(Target.region(helloLabel).withName("HelloWorld label"));

        Region region = new Region(200, 300, 0, 0);
        eyes.checkRegion(region, "Region");
        eyes.check(Target.region(region).withName("Region"));

        eyes.checkWindow("Before button click");
        eyes.check(Target.window().withName("Before button click"));

        onView(withId(R.id.click_me_btn)).perform(click());

        eyes.checkWindow("After button click");
        eyes.check(Target.window().withName("After button click"));
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

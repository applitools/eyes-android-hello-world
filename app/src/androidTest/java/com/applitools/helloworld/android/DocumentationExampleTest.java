package com.applitools.helloworld.android;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.util.Log;
import android.view.View;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.applitools.eyes.android.common.BatchInfo;
import com.applitools.eyes.android.common.EyesRunner;
import com.applitools.eyes.android.common.Region;
import com.applitools.eyes.android.common.ScreenshotMode;
import com.applitools.eyes.android.common.TestResultContainer;
import com.applitools.eyes.android.common.TestResults;
import com.applitools.eyes.android.common.TestResultsSummary;
import com.applitools.eyes.android.common.config.Configuration;
import com.applitools.eyes.android.espresso.ClassicRunner;
import com.applitools.eyes.android.espresso.Eyes;
import com.applitools.eyes.android.espresso.fluent.Target;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DocumentationExampleTest {

    private static final String TAG = "DocumentationExampleTest";

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityRule = new ActivityTestRule(MainActivity.class, false, false);

    @Rule
    public ActivityTestRule<DialogActivity> mDialogActivityRule = new ActivityTestRule(DialogActivity.class, false, false);

    @Rule
    public ActivityTestRule<GoogleMapsActivity> mGoogleMapActivityRule = new ActivityTestRule(GoogleMapsActivity.class, false, false);

    private static final String eyesServerUrl = "https://eyesapi.applitools.com";
    private static final String appName = "EKB Example : classic app";
    private static final String batchName = "EKB Example : classic";
    private static final String apiKey = "YOUR_API_KEY";
    private static final String testName = "Hello World test";
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
                .setEnvironmentName("PreProd")
                .setScreenshotMode(ScreenshotMode.SCREEN_CAPTURE)
                .setBatch(new BatchInfo(batchName));
    }

    @Before
    public void beforeEachTest() {
        eyes = new Eyes(runner);
        /*
           Uncomment the call to 'eyes.setComponentsProvider' if you use AndroidX components such as
           NestedScrollView, RecyclerView and ViewPager2
        */
        //eyes.setComponentsProvider(new AndroidXComponentsProvider());
        eyes.setConfiguration(suiteConfig);
    }

    @Test
    public void testStartScreen() {
        mMainActivityRule.launchActivity(null);

        eyes.open(testName);

        eyes.check("Click me button",Target.region(ViewMatchers.withId(R.id.click_me_btn)));

        View helloLabel = mMainActivityRule.getActivity().findViewById(R.id.hello_text_view);
        eyes.check("HelloWorld label", Target.region(helloLabel));

        Region region = new Region(500, 700, 0, 0);
        eyes.check("Region",Target.region(region));

        eyes.check("Before button click", Target.window());

        onView(withId(R.id.click_me_btn)).perform(click());
        eyes.check("After button click", Target.window().layout(withId(R.id.click_me_btn)));
    }

    @Test
    public void testDialog() {
        mDialogActivityRule.launchActivity(null);

        eyes.open("Dialog test");

        //TBD - can we add examples with a popup or dialog and then show the 3 possibilities of
        eyes.check("main viewport only",Target.window());
        eyes.check("dialog only",Target.window().dialog());
        eyes.check("Both main viewport and dialog",Target.window().includeAllLayers());
    }

    @Test
    public void testGoogleMap() {
        mGoogleMapActivityRule.launchActivity(null);

        eyes.open("GoogleMaps test");

        eyes.check("A googleMap", Target.googleMap().id(R.id.map));
//        eyes.check("Not a SupportMapFragment", Target.googleMap().id(R.id.map).isNotSupportGoogleMap());
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
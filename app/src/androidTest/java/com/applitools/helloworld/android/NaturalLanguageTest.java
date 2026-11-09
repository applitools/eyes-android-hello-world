package com.applitools.helloworld.android;

import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.applitools.eyes.android.common.BatchInfo;
import com.applitools.eyes.android.common.EyesRunner;
import com.applitools.eyes.android.common.TestResultContainer;
import com.applitools.eyes.android.common.TestResults;
import com.applitools.eyes.android.common.TestResultsSummary;
import com.applitools.eyes.android.common.config.Configuration;
import com.applitools.eyes.android.espresso.ClassicRunner;
import com.applitools.eyes.android.espresso.Eyes;
import com.applitools.eyes.android.espresso.automation.AutomationResult;
import com.applitools.eyes.android.espresso.automation.AutomationStepException;
import com.applitools.eyes.android.espresso.fluent.Target;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Drives the app with natural-language steps ({@code eyes.run("tap 'Click me!'")}) and takes visual
 * checkpoints around them. Needs Android 8.0 (API 26) or newer and an Applitools account with NLP enabled.
 * The API key comes from the {@code APPLITOOLS_API_KEY} environment variable at build time.
 */
@RunWith(AndroidJUnit4.class)
public class NaturalLanguageTest {

    private static final String TAG = "NaturalLanguageTest";
    private static final String appName = "Hello World : natural language";
    private static final String batchName = "Hello World : natural language";

    private static EyesRunner runner;
    private static Configuration suiteConfig;

    private Eyes eyes;
    private ActivityScenario<?> scenario;

    @BeforeClass
    public static void beforeTestSuite() {
        String apiKey = BuildConfig.APPLITOOLS_API_KEY;
        Assert.assertFalse("Export APPLITOOLS_API_KEY before building the tests", apiKey.isEmpty());

        runner = new ClassicRunner();
        suiteConfig = new Configuration();
        suiteConfig.setAppName(appName);
        suiteConfig.setApiKey(apiKey);
        suiteConfig.setBatch(new BatchInfo(batchName));
        // Natural-language steps work with either screenshot provider; NML is the modern one.
        suiteConfig.setNMLScreenshotProvider(true);
        // Log the full report of every eyes.run() call (element, location, timing), not only failures.
        suiteConfig.setNlpVerbose(true);
    }

    @Before
    public void beforeEachTest() {
        eyes = new Eyes(runner);
        eyes.setConfiguration(suiteConfig);
    }

    /** One instruction per call: tap a button by its label, then assert on what appeared. */
    @Test
    public void tapByLabel() {
        scenario = ActivityScenario.launch(MainActivity.class);
        eyes.open("Tap by label");

        eyes.check("Before tap", Target.window());

        AutomationResult tap = eyes.run("tap 'Click me!'");
        Log.i(TAG, "Tapped " + tap.info.selector + " at " + tap.info.location + " in " + tap.info.durationMs + "ms");

        eyes.run("verify that 'You successfully clicked the button!' is visible");

        eyes.check("After tap", Target.window());
    }

    /** Several actions in one phrase; the result carries one entry per step. */
    @Test
    public void multiStepPhrase() {
        scenario = ActivityScenario.launch(MainActivity.class);
        eyes.open("Multi-step phrase");

        AutomationResult result = eyes.run("check 'Press to simulate additional differences' and tap 'Click me!'");
        for (AutomationResult step : result.steps) {
            Log.i(TAG, (step.stepSuccessful ? "ok     " : "failed ") + step.testStep);
        }
        Assert.assertEquals(2, result.steps.size());

        eyes.check("Differences simulated", Target.window());
    }

    /** Dismiss a dialog by the text of its button. */
    @Test
    public void dismissDialog() {
        scenario = ActivityScenario.launch(DialogActivity.class);
        eyes.open("Dismiss dialog");

        eyes.check("Dialog open", Target.window().includeAllLayers());

        eyes.run("tap 'OK'");

        eyes.check("Dialog dismissed", Target.window());
    }

    /** The target is far below the fold: the step scrolls it into view before acting on it. */
    @Test
    public void scrollsToElementBelowTheFold() {
        scenario = ActivityScenario.launch(ScrollActivity.class);
        eyes.open("Scroll to element");

        eyes.check("Top of the list", Target.window());

        AutomationResult tap = eyes.run("tap 'Reach the bottom'");
        Log.i(TAG, "Found the button at " + tap.info.location + " after scrolling");

        eyes.run("verify that 'You reached the bottom!' is visible");

        eyes.check("Bottom of the list", Target.window());
    }

    /** A step that cannot run throws; the exception carries the structured result. */
    @Test
    public void failedStepExplainsWhy() {
        scenario = ActivityScenario.launch(MainActivity.class);
        eyes.open("Failed step");

        try {
            eyes.run("tap 'Button that does not exist'");
            Assert.fail("the step should have failed");
        } catch (AutomationStepException e) {
            AutomationResult result = e.getResult();
            Log.i(TAG, "Step failed as expected: code=" + result.code + ", reason=" + result.failureReason);
            Assert.assertFalse(result.stepSuccessful);
            Assert.assertEquals("NO_ELEMENT", result.code);
        }

        eyes.check("Still on the main screen", Target.window());
    }

    @After
    public void afterEachTest() {
        try {
            eyes.close();
        } finally {
            eyes.abortIfNotClosed();
            if (scenario != null) {
                scenario.close();
            }
        }
    }

    @AfterClass
    public static void afterTestSuite() {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        for (TestResultContainer container : allTestResults) {
            TestResults result = container.getTestResults();
            if (result == null) {
                Log.e(TAG, "No results: " + container.getException());
                continue;
            }
            Log.i(TAG, String.format("%s: matched=%d mismatched=%d missing=%d url=%s",
                    result.getName(), result.getMatches(), result.getMismatches(), result.getMissing(), result.getUrl()));
        }
    }
}

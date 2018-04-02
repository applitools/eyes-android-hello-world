package helloworld.applitools.com.applitoolshelloworld;

import android.support.test.rule.ActivityTestRule;

import com.applitools.eyes.android.espresso.Eyes;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import com.microsoft.appcenter.espresso.Factory;
import com.microsoft.appcenter.espresso.ReportHelper;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Rule
    public ReportHelper reportHelper = Factory.getReportHelper();

    @Before
    public void before() {
        reportHelper.label("Start test App");
    }

    @Test
    public void simpleTest() {
        reportHelper.label("Start simple test");
        // Initialize the eyes SDK and set your private API key.
        Eyes eyes = new Eyes();
        eyes.setApiKey("YJBanzkYVJtfj1lqKzyjdjK96nKrfmc2xBeh95yfVSA110");

        try {
            // Start the test
            eyes.open("Hello World!", "My first Espresso Android test!");

            // Visual checkpoint #1.
            eyes.checkWindow("Hello!");
            reportHelper.label("Check window method");

            onView(withId(R.id.click_me_btn)).perform(click());
            reportHelper.label("Click on ClickMe btn");

            // Visual checkpoint #2.
            eyes.checkWindow("Click!");

            // End the test.
            eyes.close();
        } finally {
            // If the test was aborted before eyes.close was called, ends the test as aborted.
            eyes.abortIfNotClosed();
            reportHelper.label("Stop simple test");
        }
    }

    @After
    public void after() {
        reportHelper.label("Stopping App");
    }
}

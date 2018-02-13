package helloworld.applitools.com.applitoolshelloworld;

import android.support.test.rule.ActivityTestRule;

import com.applitools.eyes.android.espresso.Eyes;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
            eyes.open("Hello World!", "My first Selenium Java test!");

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
}

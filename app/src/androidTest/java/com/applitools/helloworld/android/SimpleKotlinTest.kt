package com.applitools.helloworld.android

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.applitools.eyes.android.common.MatchLevel
import com.applitools.eyes.android.espresso.Eyes
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class SimpleKotlinTest {
    @get: Rule
    var intentRule = ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun kotlinTest() {

        // Initialize the eyes SDK and set your private API key.
        val eyes = Eyes()
        eyes.apiKey = "YOUR_API_KEY"
        eyes.matchLevel = MatchLevel.STRICT
        eyes.forceFullPageScreenshot = true
        eyes.branchName = "main"
        eyes.addProperty("username", System.getProperty("user.dir"))
        try {
            // Start the test
            eyes.open("Hello World!", "My first Espresso Kotlin Android test!")

            // Visual checkpoint #1.
            eyes.checkWindow("Hello!")
            Espresso.onView(withId(R.id.click_me_btn)).perform(ViewActions.click())

            // Visual checkpoint #2.
            eyes.checkWindow("Click!")

            // End the test.
            eyes.close()
        } finally {
            // If the test was aborted before eyes.close was called, ends the test as aborted.
            eyes.abortIfNotClosed()
        }
    }
}
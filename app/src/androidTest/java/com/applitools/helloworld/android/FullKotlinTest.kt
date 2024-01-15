package com.applitools.helloworld.android

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.applitools.eyes.android.espresso.Eyes
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)

For before and after class, see:
https://stackoverflow.com/questions/35554076/how-do-i-manage-unit-test-resources-in-kotlin-such-as-starting-stopping-a-datab
 */
@RunWith(AndroidJUnit4::class)
class FullKotlinTest {
    companion object {
        lateinit var eyes: Eyes

        @BeforeClass
        @JvmStatic
        fun setup() {
            // things to execute once and keep around for the class
            eyes = Eyes()
            eyes.apiKey = "YOUR_API_KEY"
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            // clean up after this class, leave nothing dirty behind
            eyes.abortIfNotClosed()
        }
    }

    @get: Rule
    var intentRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun prepareTest() {
        // things to do before each test
        eyes.open("Hello World!", "My first Espresso Kotlin Android test!")
    }

    @After
    fun cleanupTest() {
        // things to do before each test
        try {
            eyes.close()
        } finally {
            eyes.abortIfNotClosed()
        }
    }

    @Test
    fun kotlinTest() {
        // Visual checkpoint #1.
        eyes.checkWindow("Hello!")
        Espresso.onView(withId(R.id.click_me_btn)).perform(ViewActions.click())

        // Visual checkpoint #2.
        eyes.checkWindow("Click!")
    }

}
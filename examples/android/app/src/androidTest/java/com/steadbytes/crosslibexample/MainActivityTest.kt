package com.steadbytes.crosslibexample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun generatesIdsOnButtonPressAndResetsOnGeneratorExhaustion() {
        (0..1).forEach { _ ->
            for (i in 1..MAX_IDS) {
                onView(withId(R.id.buttonGenerate)).perform(click())
                val numIds = activityRule.activity.listViewIds.adapter.count
                assertEquals(i, numIds)
            }
        }
    }
}


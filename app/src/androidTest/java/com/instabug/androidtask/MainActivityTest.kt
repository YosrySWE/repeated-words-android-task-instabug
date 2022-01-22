package com.instabug.androidtask

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import com.instabug.androidtask.ui.main.MainActivity
import org.hamcrest.Matcher
import org.junit.Test


class MainActivityTest {
    @get:Rule
    public var mActivityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

//    @Test
//    fun testReloadScreen(){
//        onView(withId(R.id.swipeRefresher)).perform(withCustomConstraints(ViewActions.swipeDown(), ViewMatchers.isDisplayingAtLeast(85)))
//    }

    @Test
    fun testSortButton(){
        onView(withId(R.id.sort)).perform(ViewActions.click())
    }
}


fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {
    return object :ViewAction{
        override fun getConstraints(): Matcher<View> {
            return constraints
        }

        override fun getDescription(): String {
            return action.description
        }
        override fun perform(uiController: UiController?, view: View?) {
            action.perform(uiController, view)
        }
    }
}
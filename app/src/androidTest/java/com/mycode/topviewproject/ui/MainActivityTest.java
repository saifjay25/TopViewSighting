package com.mycode.topviewproject.ui;

import android.os.SystemClock;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.mycode.topviewproject.R;
import com.mycode.topviewproject.persistence.WeatherDatabaseTest;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class MainActivityTest extends WeatherDatabaseTest {

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void clickTodaysWeather(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
        Espresso.onView(ViewMatchers.withId(R.id.clicker)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("More Information")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void clickTomorrowsWeather(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
        Espresso.onView(ViewMatchers.withId(R.id.clicker1)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("More Information")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void click2daysAfterWeather(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
        Espresso.onView(ViewMatchers.withId(R.id.clicker2)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("More Information")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void click3daysAfterWeather(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
        Espresso.onView(ViewMatchers.withId(R.id.clicker3)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("More Information")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void click4daysAfterWeather(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
        Espresso.onView(ViewMatchers.withId(R.id.clicker4)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("More Information")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void click5daysAfterWeather(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
        Espresso.onView(ViewMatchers.withId(R.id.clicker5)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("More Information")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void click6daysAfterWeather(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
        Espresso.onView(ViewMatchers.withId(R.id.clicker6)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("More Information")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

}
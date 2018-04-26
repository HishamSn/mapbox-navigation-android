package com.mapbox.services.android.navigation.testapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class WaypointNavigationActivityTest {

  private static final int ONE_SECOND = 1000;

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

  @Rule
  public GrantPermissionRule mRuntimePermissionRule =
    GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

  @Test
  public void waypointNavigationActivityTest() {

    sleep(ONE_SECOND);

    checkTestModeEnabled();

    ViewInteraction recyclerView = onView(
      allOf(withId(R.id.recycler_view),
        childAtPosition(
          withClassName(is("android.support.constraint.ConstraintLayout")),
          0)));
    recyclerView.perform(actionOnItemAtPosition(4, click()));

    sleep(20000);

    ViewInteraction appCompatButton = onView(
      allOf(withId(android.R.id.button1), withText("Start next"),
        childAtPosition(
          childAtPosition(
            withId(R.id.buttonPanel),
            0),
          3)));
    appCompatButton.perform(scrollTo(), click());

    sleep(20000);

    ViewInteraction appCompatButton2 = onView(
      allOf(withId(android.R.id.button1), withText("Start next"),
        childAtPosition(
          childAtPosition(
            withId(R.id.buttonPanel),
            0),
          3)));
    appCompatButton2.perform(scrollTo(), click());

    sleep(20000);

    ViewInteraction appCompatButton3 = onView(
      allOf(withId(android.R.id.button1), withText("Start next"),
        childAtPosition(
          childAtPosition(
            withId(R.id.buttonPanel),
            0),
          3)));
    appCompatButton3.perform(scrollTo(), click());

    sleep(20000);
  }

  private void checkTestModeEnabled() {
    if (shouldEnableTestMode()) {
      ViewInteraction actionMenuItemView = onView(
        allOf(withId(R.id.settings), withContentDescription("Settings"),
          childAtPosition(
            childAtPosition(
              withId(R.id.action_bar),
              1),
            0),
          isDisplayed()));
      actionMenuItemView.perform(click());

      sleep(ONE_SECOND);

      DataInteraction linearLayout = onData(anything())
        .inAdapterView(allOf(withId(android.R.id.list),
          childAtPosition(
            withId(android.R.id.list_container),
            0)))
        .atPosition(4);
      linearLayout.perform(click());

      pressBack();

      sleep(ONE_SECOND);
    }
  }

  private boolean shouldEnableTestMode() {
    Context context = mActivityTestRule.getActivity();
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    return !preferences.getBoolean(context.getString(R.string.test_mode_key), false);
  }

  private void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static Matcher<View> childAtPosition(
    final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
          && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
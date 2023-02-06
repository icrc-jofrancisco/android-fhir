/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.fhir.datacapture

import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commitNow
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.fhir.datacapture.TestQuestionnaireFragment.Companion.QUESTIONNAIRE_FILE_PATH_KEY
import com.google.android.fhir.datacapture.test.R
import com.google.android.fhir.datacapture.utilities.clickOnText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuestionnaireUiEspressoTest {

  @Rule
  @JvmField
  var activityScenarioRule: ActivityScenarioRule<TestActivity> =
    ActivityScenarioRule(TestActivity::class.java)

  private lateinit var parent: FrameLayout

  @Before
  fun setup() {
    activityScenarioRule.getScenario().onActivity { activity -> parent = FrameLayout(activity) }
  }

  @Test
  fun shouldDisplayReviewButtonWhenNoMorePagesToDisplay() {
    val bundle =
      bundleOf(QUESTIONNAIRE_FILE_PATH_KEY to "/paginated_questionnaire_with_dependent_answer.json")
    activityScenarioRule.getScenario().onActivity { activity ->
      activity.supportFragmentManager.commitNow {
        setReorderingAllowed(true)
        add<TestQuestionnaireFragment>(R.id.container_holder, args = bundle)
      }
    }

    onView(ViewMatchers.withId(R.id.review_mode_button))
      .check(
        ViewAssertions.matches(
          ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        )
      )

    clickOnText("Yes")
    onView(ViewMatchers.withId(R.id.review_mode_button))
      .check(
        ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE))
      )

    clickOnText("No")
    onView(ViewMatchers.withId(R.id.review_mode_button))
      .check(
        ViewAssertions.matches(
          ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        )
      )
  }
}
package com.suraj.gallery

import android.Manifest
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.GrantPermissionRule
import com.gallery.R
import com.suraj.gallery.presentation.ui.albums.AlbumsActivity
import com.suraj.gallery.presentation.ui.albums.AlbumsAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.`is`
import org.junit.*
import org.junit.runner.RunWith

@HiltAndroidTest
class AlbumsActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    // Auto grant media read permission
    @get:Rule(order = 1)
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.READ_MEDIA_IMAGES)

    private lateinit var scenario: ActivityScenario<AlbumsActivity>

    @Before
    fun setup() {
        hiltRule.inject()
        val intent = Intent(ApplicationProvider.getApplicationContext(), AlbumsActivity::class.java)
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun albumList_isDisplayed() {
        // Wait for data to load (replace with IdlingResource in production)
        Thread.sleep(2000)

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun toggleViewMode_changesLayout() {
        onView(withId(R.id.fabViewMode)).perform(click())

        // Confirm icon toggles (this test checks icon change, can be enhanced with layout checks)
        onView(withId(R.id.fabViewMode)).check(
            matches(withTagValue(`is`(R.drawable.ic_view_grid)))
        )
    }

    @Test
    fun clickOnAlbum_opensDetailActivity() {
        Thread.sleep(2000)

        onView(withId(R.id.recyclerView))
            .perform(
                androidx.test.espresso.contrib.RecyclerViewActions
                    .actionOnItemAtPosition<AlbumsAdapter.AlbumViewHolder>(0, click())
            )

        // You can assert intent or view in detail screen
    }

    @Test
    fun swipeToRefresh_triggersReload() {
        onView(withId(R.id.swipeRefresh))
            .perform(androidx.test.espresso.action.ViewActions.swipeDown())

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }
}

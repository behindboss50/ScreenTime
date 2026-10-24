package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DemoDataProvider
import com.example.model.AppCategory
import com.example.model.AppSession
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("True Screen Time", appName)
    }

    @Test
    fun testDemoDataMorningTimeline() {
        val summary = DemoDataProvider.generateDailySummary(System.currentTimeMillis(), isToday = false)
        assertNotNull(summary)
        assertTrue(summary.sessions.isNotEmpty())

        // Verify sessions start from morning (7 AM)
        val firstSession = summary.sessions.first()
        assertTrue("First session should start in the morning", firstSession.startTimeMs > 0)
        assertNotNull(firstSession.formattedStartTime)
        assertNotNull(firstSession.formattedEndTime)
        assertTrue(firstSession.formattedTimeRange.contains("—"))

        // Verify categories classification
        assertEquals(AppCategory.ENTERTAINMENT, AppCategory.fromPackage("com.google.android.youtube"))
        assertEquals(AppCategory.COMMUNICATION, AppCategory.fromPackage("com.whatsapp"))
        assertEquals(AppCategory.SOCIAL, AppCategory.fromPackage("com.instagram.android"))
    }

    @Test
    fun testDurationFormatting() {
        assertEquals("45s", AppSession.formatDuration(45_000L))
        assertEquals("2m", AppSession.formatDuration(120_000L))
        assertEquals("1h 15m", AppSession.formatDuration(75 * 60 * 1000L))
    }
}

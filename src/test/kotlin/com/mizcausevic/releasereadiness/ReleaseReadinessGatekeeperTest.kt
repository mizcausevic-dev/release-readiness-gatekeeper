package com.mizcausevic.releasereadiness

import io.javalin.testtools.JavalinTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReleaseReadinessGatekeeperTest {
    @Test
    fun `root route returns metadata`() = JavalinTest.test(ReleaseReadinessGatekeeperApp.create()) { _, client ->
        val response = client.get("/")
        assertEquals(200, response.code)
        assertTrue(response.body!!.string().contains("release-readiness-gatekeeper"))
    }

    @Test
    fun `sample route returns analysis`() = JavalinTest.test(ReleaseReadinessGatekeeperApp.create()) { _, client ->
        val response = client.get("/api/sample")
        assertEquals(200, response.code)
        assertTrue(response.body!!.string().contains("status"))
    }

    @Test
    fun `critical release escalates`() {
        val analysis = ReleaseReadinessEngine.analyze(
            ReleaseAnalysisRequest(
                id = "rel-test",
                title = "Critical release",
                service = "checkout-runtime",
                severity = "critical",
                releaseWindowHours = 1.0,
                errorBudgetRemaining = 0.14,
                blockedDependencies = 3,
                rollbackReady = false,
                freezeWindowActive = true,
                ownerLane = "platform-release",
                blockers = listOf("dependency drag"),
                nextSteps = listOf("pause")
            )
        )

        assertEquals("escalate", analysis.status)
        assertEquals("hold", analysis.releaseDecision)
        assertEquals("incident-command", analysis.ownerLane)
    }
}

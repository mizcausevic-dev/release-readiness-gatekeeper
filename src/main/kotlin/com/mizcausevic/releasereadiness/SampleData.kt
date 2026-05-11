package com.mizcausevic.releasereadiness

object SampleData {
    fun releases(): List<ReleaseThread> = listOf(
        ReleaseThread(
            id = "rel-9401",
            title = "Checkout runtime patch is entering a freeze window with two unresolved dependencies",
            service = "checkout-runtime",
            severity = "critical",
            releaseWindowHours = 1.5,
            errorBudgetRemaining = 0.18,
            blockedDependencies = 3,
            rollbackReady = false,
            freezeWindowActive = true,
            ownerLane = "platform-release",
            blockers = listOf(
                "Tax service retries are still above tolerance.",
                "Rollback artifact was not validated in the current region."
            ),
            nextSteps = listOf(
                "Hold the release lane and assign a rollback owner now.",
                "Clear dependency drag before the next freeze checkpoint."
            )
        ),
        ReleaseThread(
            id = "rel-9406",
            title = "Identity sync release has low error budget but a clean rollback posture",
            service = "identity-sync",
            severity = "high",
            releaseWindowHours = 6.0,
            errorBudgetRemaining = 0.31,
            blockedDependencies = 1,
            rollbackReady = true,
            freezeWindowActive = false,
            ownerLane = "identity-systems",
            blockers = listOf("Regional metadata propagation needs one final verification."),
            nextSteps = listOf("Keep the lane in watch mode and validate the final propagation check.")
        ),
        ReleaseThread(
            id = "rel-9411",
            title = "Experiment delivery release is clear enough to ship with normal monitoring",
            service = "feature-delivery",
            severity = "medium",
            releaseWindowHours = 14.0,
            errorBudgetRemaining = 0.72,
            blockedDependencies = 0,
            rollbackReady = true,
            freezeWindowActive = false,
            ownerLane = "growth-systems",
            blockers = emptyList(),
            nextSteps = listOf("Ship during the planned window and monitor the first 30 minutes.")
        )
    )
}

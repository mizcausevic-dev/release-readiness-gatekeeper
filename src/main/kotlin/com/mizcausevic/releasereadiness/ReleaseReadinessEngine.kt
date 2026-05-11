package com.mizcausevic.releasereadiness

object ReleaseReadinessEngine {
    fun summary(): DashboardSummary {
        val releases = SampleData.releases()
        val analyses = releases.map { analyze(it.toRequest()) }

        return DashboardSummary(
            service = "release-readiness-gatekeeper",
            pendingReleases = releases.size,
            escalateNow = analyses.count { it.status == "escalate" },
            freezeWindowThreads = releases.count { it.freezeWindowActive },
            dominantRisk = "Freeze-window release entering without rollback certainty",
            mostExposedLane = "platform-release",
        )
    }

    fun releases(): List<ReleaseThread> = SampleData.releases()

    fun release(id: String): ReleaseThread? = SampleData.releases().find { it.id == id }

    fun sampleAnalysis(): ReleaseAnalysis = analyze(SampleData.releases().first().toRequest())

    fun analyze(request: ReleaseAnalysisRequest): ReleaseAnalysis {
        var score = 10
        score += when (request.severity.lowercase()) {
            "critical" -> 34
            "high" -> 24
            "medium" -> 14
            else -> 8
        }

        if (request.releaseWindowHours <= 2.0) {
            score += 24
        } else if (request.releaseWindowHours <= 6.0) {
            score += 12
        }

        if (request.errorBudgetRemaining <= 0.2) {
            score += 20
        } else if (request.errorBudgetRemaining <= 0.4) {
            score += 10
        }

        score += request.blockedDependencies * 6
        if (!request.rollbackReady) score += 16
        if (request.freezeWindowActive) score += 14

        val status = when {
            score >= 72 -> "escalate"
            score >= 46 -> "watch"
            else -> "stable"
        }

        val releaseDecision = when (status) {
            "escalate" -> "hold"
            "watch" -> "conditional-ship"
            else -> "ship"
        }

        val recommendedAction = when (status) {
            "escalate" -> "Pause the release, assign a rollback owner, and clear dependency drag before the freeze window closes."
            "watch" -> "Keep the release in watch mode and complete the remaining dependency verification before the cutover."
            else -> "Proceed with the release and use standard monitoring during the first response window."
        }

        val risks = buildList {
            if (request.releaseWindowHours <= 2.0) add("The release is entering an immediate freeze window.")
            if (request.errorBudgetRemaining <= 0.2) add("The error budget is almost exhausted.")
            if (request.blockedDependencies >= 2) add("Dependency drag is still too high for a clean launch.")
            if (!request.rollbackReady) add("Rollback posture is incomplete.")
        }

        val stabilizers = buildList {
            if (request.rollbackReady) add("A rollback path already exists.")
            if (request.blockedDependencies <= 1) add("Dependency drag is still manageable.")
            if (!request.freezeWindowActive) add("There is still time before the next freeze window.")
            if (request.nextSteps.isNotEmpty()) add("A next-step sequence already exists for the owner lane.")
        }

        return ReleaseAnalysis(
            status = status,
            score = score,
            recommendedAction = recommendedAction,
            releaseDecision = releaseDecision,
            ownerLane = if (status == "escalate") "incident-command" else request.ownerLane,
            risks = risks,
            stabilizers = stabilizers,
        )
    }
}

private fun ReleaseThread.toRequest() = ReleaseAnalysisRequest(
    id = id,
    title = title,
    service = service,
    severity = severity,
    releaseWindowHours = releaseWindowHours,
    errorBudgetRemaining = errorBudgetRemaining,
    blockedDependencies = blockedDependencies,
    rollbackReady = rollbackReady,
    freezeWindowActive = freezeWindowActive,
    ownerLane = ownerLane,
    blockers = blockers,
    nextSteps = nextSteps,
)

package com.mizcausevic.releasereadiness

data class ReleaseThread(
    val id: String,
    val title: String,
    val service: String,
    val severity: String,
    val releaseWindowHours: Double,
    val errorBudgetRemaining: Double,
    val blockedDependencies: Int,
    val rollbackReady: Boolean,
    val freezeWindowActive: Boolean,
    val ownerLane: String,
    val blockers: List<String>,
    val nextSteps: List<String>,
)

data class DashboardSummary(
    val service: String,
    val pendingReleases: Int,
    val escalateNow: Int,
    val freezeWindowThreads: Int,
    val dominantRisk: String,
    val mostExposedLane: String,
)

data class ReleaseCollection(
    val releases: List<ReleaseThread>,
)

data class ReleaseAnalysisRequest(
    val id: String,
    val title: String,
    val service: String,
    val severity: String,
    val releaseWindowHours: Double,
    val errorBudgetRemaining: Double,
    val blockedDependencies: Int,
    val rollbackReady: Boolean,
    val freezeWindowActive: Boolean,
    val ownerLane: String,
    val blockers: List<String>,
    val nextSteps: List<String>,
)

data class ReleaseAnalysis(
    val status: String,
    val score: Int,
    val recommendedAction: String,
    val releaseDecision: String,
    val ownerLane: String,
    val risks: List<String>,
    val stabilizers: List<String>,
)

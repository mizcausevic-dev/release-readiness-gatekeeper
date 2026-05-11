# Release Readiness Gatekeeper Architecture

Release Readiness Gatekeeper is designed as a lightweight Kotlin control layer that sits between release prep and launch approval. The idea is simple: launch pressure should become an explicit decision, not a meeting vibe.

## Core Flow

1. A release thread arrives with severity, blocked dependencies, and rollback posture.
2. The Javalin route layer validates and forwards the payload to the readiness engine.
3. The engine scores severity, release-window compression, freeze exposure, and rollback readiness.
4. The result becomes a release decision:
   - `ship`
   - `conditional-ship`
   - `hold`
5. The service returns the owner lane and immediate next action.

## Why Kotlin Here

- it broadens the portfolio with another JVM language, but not just Java again
- the service stays lightweight and operator-focused
- it pairs naturally with reliability, workflow, and governance systems already in the portfolio

## Main Entities

- `ReleaseThread`
- `ReleaseAnalysisRequest`
- `ReleaseAnalysis`
- `DashboardSummary`

## Operational Framing

This repo is intentionally not a generic deployment dashboard. It acts more like a launch gate policy service:

- blocked dependencies are treated as risk
- freeze windows are first-class inputs
- rollback readiness is weighted heavily
- owner lanes are explicit

package com.mizcausevic.releasereadiness

import io.javalin.Javalin
import io.javalin.json.JavalinJackson

object ReleaseReadinessGatekeeperApp {
    fun create(): Javalin =
        Javalin.create { config ->
            config.http.defaultContentType = "application/json"
            config.jsonMapper(JavalinJackson())
        }.apply {
            get("/") { ctx ->
                ctx.json(
                    mapOf(
                        "service" to "release-readiness-gatekeeper",
                        "language" to "Kotlin",
                        "framework" to "Javalin",
                        "description" to "Release gate evaluation for freeze windows, dependency drag, and rollback posture.",
                        "endpoints" to listOf(
                            "/docs",
                            "/api/dashboard/summary",
                            "/api/releases",
                            "/api/releases/{id}",
                            "/api/sample",
                            "/api/analyze/release"
                        )
                    )
                )
            }

            get("/docs") { ctx ->
                ctx.contentType("text/html")
                ctx.result(
                    """
                    <!doctype html>
                    <html lang="en">
                      <head>
                        <meta charset="utf-8" />
                        <title>Release Readiness Gatekeeper Docs</title>
                        <style>
                          body { font-family: Segoe UI, sans-serif; background:#09121f; color:#f3efe1; margin:0; padding:32px; }
                          .shell { max-width:960px; margin:0 auto; background:#131d30; border:1px solid #294164; border-radius:20px; padding:28px; }
                          h1 { margin:0 0 8px; font-size:40px; line-height:1.08; }
                          p, li, code { color:#c6d0e2; }
                          code { background:#0d1728; padding:2px 6px; border-radius:6px; }
                        </style>
                      </head>
                      <body>
                        <div class="shell">
                          <p style="letter-spacing:0.25em;text-transform:uppercase;color:#86c4ff;">Release Readiness Gatekeeper</p>
                          <h1>Kotlin control surface for release gates, freeze pressure, and rollback-aware launch decisions.</h1>
                          <p>This service turns launch risk into explicit ship, watch, and hold guidance instead of leaving it trapped in release meetings.</p>
                          <ul>
                            <li><code>GET /api/dashboard/summary</code> returns queue posture.</li>
                            <li><code>GET /api/releases</code> returns modeled release threads.</li>
                            <li><code>GET /api/sample</code> returns a sample analysis.</li>
                            <li><code>POST /api/analyze/release</code> scores a payload and returns the next action.</li>
                          </ul>
                        </div>
                      </body>
                    </html>
                    """.trimIndent()
                )
            }

            get("/api/dashboard/summary") { ctx ->
                ctx.json(ReleaseReadinessEngine.summary())
            }

            get("/api/releases") { ctx ->
                ctx.json(ReleaseCollection(ReleaseReadinessEngine.releases()))
            }

            get("/api/releases/{id}") { ctx ->
                val releaseId = ctx.pathParam("id")
                val release = ReleaseReadinessEngine.release(releaseId)
                if (release == null) {
                    ctx.status(404).json(mapOf("error" to "release_not_found", "id" to releaseId))
                } else {
                    ctx.json(release)
                }
            }

            get("/api/sample") { ctx ->
                ctx.json(ReleaseReadinessEngine.sampleAnalysis())
            }

            post("/api/analyze/release") { ctx ->
                val payload = ctx.bodyAsClass(ReleaseAnalysisRequest::class.java)
                ctx.json(ReleaseReadinessEngine.analyze(payload))
            }
        }
}

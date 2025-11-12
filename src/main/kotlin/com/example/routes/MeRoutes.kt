package com.example.routes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
fun Route.meRoutes() {
    authenticate("auth-jwt") {
        get("/api/me") {
            val p = call.principal<JWTPrincipal>()!!
            call.respond(mapOf("email" to p.getClaim("email", String::class), "role" to p.getClaim("role", String::class)))
        }
    }
}

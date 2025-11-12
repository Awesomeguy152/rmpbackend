package com.example.plugins
import com.example.routes.adminRoutes
import com.example.routes.authRoutes
import com.example.routes.meRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
fun Application.configureRouting() {
    routing {
        authRoutes()
        meRoutes()
        adminRoutes()
    }
}

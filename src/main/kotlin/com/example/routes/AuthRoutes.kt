package com.example.routes
import com.auth0.jwt.JWT
import com.example.plugins.JwtConfig
import com.example.schema.Role
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable data class RegisterRq(val email: String, val password: String, val role: Role)
@Serializable data class LoginRq(val email: String, val password: String)

fun Route.authRoutes() {
    val service = UserService()
    route("/api/auth") {
        post("/register") {
            val rq = call.receive<RegisterRq>()
            val dto = service.create(rq.email, rq.password, rq.role)
            call.respond(HttpStatusCode.Created, dto)
        }
        post("/login") {
            val rq = call.receive<LoginRq>()
            val dto = service.verifyAndGet(rq.email, rq.password)
            if (dto == null) call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "invalid credentials"))
            else {
                val token = JWT.create()
                    .withIssuer(JwtConfig.issuer).withAudience(JwtConfig.audience)
                    .withClaim("sub", dto.id).withClaim("email", dto.email).withClaim("role", dto.role.name)
                    .withExpiresAt(Date(System.currentTimeMillis() + 1000L*60*60*12))
                    .sign(JwtConfig.algorithm)
                call.respond(mapOf("token" to token))
            }
        }
    }
}

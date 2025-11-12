package com.example.schema
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

enum class Role { ADMIN, USER }

object UserTable : UUIDTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val role = varchar("role", 16)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
}

@Serializable data class UserDTO(val id: String, val email: String, val role: Role)

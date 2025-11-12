package com.example.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.schema.Role
import com.example.schema.UserDTO
import com.example.schema.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserService {
    fun create(email: String, rawPassword: String, role: Role): UserDTO {
        val hash = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray())
        val id = transaction {
            val exists = UserTable.select { UserTable.email eq email }.any()
            require(!exists) { "User already exists" }

            UserTable.insertAndGetId {
                it[UserTable.email] = email
                it[UserTable.passwordHash] = hash
                it[UserTable.role] = role.name
            }.value
        }
        return UserDTO(id.toString(), email, role)
    }

    fun verifyAndGet(email: String, rawPassword: String): UserDTO? = transaction {
        UserTable.select { UserTable.email eq email }.singleOrNull()?.let { row ->
            val ok = BCrypt.verifyer()
                .verify(rawPassword.toCharArray(), row[UserTable.passwordHash])
                .verified
            if (ok) UserDTO(
                row[UserTable.id].value.toString(),
                row[UserTable.email],
                Role.valueOf(row[UserTable.role])
            ) else null
        }
    }

    fun list(): List<UserDTO> = transaction {
        UserTable.selectAll().map { row ->
            UserDTO(
                row[UserTable.id].value.toString(),
                row[UserTable.email],
                Role.valueOf(row[UserTable.role]))
        }
    }
}

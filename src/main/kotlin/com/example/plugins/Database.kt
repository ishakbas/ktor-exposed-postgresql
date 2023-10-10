package com.example.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database


fun Application.configureDatabase() {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5433/отельтест",
        user = "testLogin",
        driver = "org.postgresql.Driver",
        password = "test"
    )
}


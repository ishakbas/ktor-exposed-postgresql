package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5433/отельтест",
        user = "testLogin",
        driver = "org.postgresql.Driver",
        password = "test"
    )

    val hotelService = HotelService(database)

    routing {
        get("/roomTypes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val roomType = hotelService.read(id)
            if  (roomType != null) {
                call.respond(HttpStatusCode.OK, roomType)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
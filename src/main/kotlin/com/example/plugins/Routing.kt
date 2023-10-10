package com.example.plugins

import com.example.data.ExposedRoomTypes
import com.example.data.ExposedUsers
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.descriptors.serialDescriptor

fun Application.configureRouting() {
    routing {
        users()
        rooms()
    }
}

fun Route.users() {

    val userService = HotelService.UserRepository()

    route("/user/") {
        route("{id}") {

            get {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = userService.read(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = call.receive<ExposedUsers>()
                userService.update(id, user)
            }

            delete {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                userService.delete(id)
                call.respond(HttpStatusCode.OK, "user $id was deleted")
            }
        }

        post {
            val user = call.receive<ExposedUsers>()
            val id = userService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }
    }
}
fun Route.rooms() {
    val roomService = HotelService.HotelRoomsRepository()
    route("/room/") {
        post {
            val roomType = call.receive<ExposedRoomTypes>()
            val id = roomTypes.create(roomType)
            call.respond(HttpStatusCode.Created, id)
        }
        route("{id}") {

        }
    }
}
fun Route.roomTypes() {
    val roomTypesService = HotelService.RoomTypesRepository()
    route("/room_types") {
        route("{id}") {

        }
        post {
            val roomType = call.receive<ExposedRoomTypes>()
            val id = serialDescriptor<>().create(roomType)
            call.respond(HttpStatusCode.Created, id)
        }
    }
}
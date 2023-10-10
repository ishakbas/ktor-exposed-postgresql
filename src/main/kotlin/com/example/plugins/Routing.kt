package com.example.plugins

import com.example.data.ExposedBookings
import com.example.data.ExposedHotelRooms
import com.example.data.ExposedRoomTypes
import com.example.data.ExposedUsers
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        users()
        rooms()
        roomTypes()
        bookings()
    }
}

const val invalidId = "Invalid ID"
fun Route.users() {
    val userService = HotelService.UserRepository()
    route("/user/") {
        route("{id}") {

            get {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val user = userService.read(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val user = call.receive<ExposedUsers>()
                userService.update(id, user)
            }

            delete {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
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
            val room = call.receive<ExposedHotelRooms>()
            val id = roomService.create(room)
            call.respond(HttpStatusCode.Created, id)
        }
        route("{id}") {
            get {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val room = roomService.read(id)
                if (room != null) {
                    call.respond(HttpStatusCode.OK, room)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            put {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val room = call.receive<ExposedHotelRooms>()
                roomService.update(id, room)
            }
            delete {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                roomService.delete(id)
            }
        }
    }
}

fun Route.roomTypes() {
    val roomTypesService = HotelService.RoomTypesRepository()
    route("/room_types") {
        route("{id}") {
            get {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val roomType = roomTypesService.read(id)
                if (roomType != null) {
                    call.respond(HttpStatusCode.OK, roomType)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            put {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val roomType = call.receive<ExposedRoomTypes>()
                roomTypesService.update(id, roomType)
            }
            delete {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                roomTypesService.delete(id)
            }
        }
        post {
            val roomType = call.receive<ExposedRoomTypes>()
            val id = roomTypesService.create(roomType)
            call.respond(HttpStatusCode.Created, id)
        }
    }
}

fun Routing.bookings() {
    val bookingService = HotelService.BookingsRepository()
    route("/booking/") {
        route("{id}") {
            get {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val booking = bookingService.read(id)
                if (booking != null) {
                    call.respond(HttpStatusCode.OK, booking)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            put {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val booking = call.receive<ExposedBookings>()
                bookingService.update(id, booking)
            }
            delete {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                bookingService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
        post {
            val booking = call.receive<ExposedBookings>()
            val id = bookingService.create(booking)
            call.respond(HttpStatusCode.Created, id)
        }
    }
}

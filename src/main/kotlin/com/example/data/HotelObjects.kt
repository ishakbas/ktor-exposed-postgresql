package com.example.data

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date


@Serializable
data class ExposedRoomTypes(
    val name: String,
    val description: String?
)

@Serializable
data class ExposedHotelRooms(
    val name: String,
    val roomTypeId: Int,
    val room_image: ByteArray
)

@Serializable
data class ExposedUsers(
    val login: String,
    val password: String,
    val firstName: String,
    val lastName: String?,
    val email: String?
)

@Serializable
data class ExposedBookings(
    val user_id: Int,
    val room_id: Int,
    val check_in_date: LocalDate,
    val check_out_date: LocalDate,
    val status: String?
)
object Bookings : IntIdTable("bookings") {
    val user_id = integer("user_id")
    val room_id = integer("room_id")
    val check_in_date = date("check_in_date")
    val check_out_date = date("check_out_date")
    val status = varchar("status", 20).nullable()
}

object RoomTypes : IntIdTable("room_types") {
    val name = varchar("name", 50)
    val description = text("description").nullable()
}

object HotelRooms : IntIdTable("hotel_rooms") {
    val name = varchar("name", 50)
    val room_type_id = integer("room_type_id")
    val room_image = binary("room_image").nullable()
}

object Users : IntIdTable("users") {
    val login = varchar("login", 50)
    val password = varchar("password", 50)
    val first_name = varchar("first_name", 50)
    val last_name = varchar("last_name", 50).nullable()
    val email = varchar("email", 100).nullable()
}

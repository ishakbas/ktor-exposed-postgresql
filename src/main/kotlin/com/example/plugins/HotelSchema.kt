package com.example.plugins

import com.example.data.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }

class HotelService {
    init {
        transaction {
            addLogger(StdOutSqlLogger)
        }
    }

    class UserRepository {
        suspend fun read(id: Int): ExposedUsers? {
            return dbQuery {
                Users.select { Users.id eq id }.map {
                    ExposedUsers(
                        it[Users.login],
                        it[Users.password],
                        it[Users.first_name],
                        it[Users.last_name],
                        it[Users.email]
                    )
                }.singleOrNull()
            }
        }

        suspend fun create(user: ExposedUsers): Int = dbQuery {
            Users.insert {
                it[login] = user.login
                it[password] = user.password
                it[first_name] = user.firstName
                it[last_name] = user.lastName
                it[email] = user.email
            }[Users.id].value
        }

        suspend fun update(id: Int, user: ExposedUsers) {
            dbQuery {
                Users.update({ Users.id eq id }) {
                    it[login] = user.login
                    it[password] = user.password
                    it[first_name] = user.firstName
                    it[last_name] = user.lastName
                    it[email] = user.email
                }
            }
        }

        suspend fun delete(id: Int) {
            dbQuery {
                Users.deleteWhere { Users.id.eq(id) }
            }
        }
    }

    class BookingsRepository {
        suspend fun read(id: Int): ExposedBookings? {
            return dbQuery {
                Bookings.select { Bookings.id eq id }.map {
                    ExposedBookings(
                        it[Bookings.user_id],
                        it[Bookings.room_id],
                        it[Bookings.check_in_date],
                        it[Bookings.check_out_date],
                        it[Bookings.status]
                    )
                }.singleOrNull()
            }
        }

        suspend fun create() {

        }

        suspend fun update(id: Int, bookings: ExposedBookings) {
            dbQuery {
                Bookings.update({ Bookings.id eq id }) {
                    it[user_id] = bookings.userId
                    it[room_id] = bookings.roomId
                    it[check_in_date] = bookings.checkInDate
                    it[check_out_date] = bookings.checkOutDate
                    it[status] = bookings.status
                }
            }
        }

        suspend fun delete(id: Int) {
            dbQuery {
                Bookings.deleteWhere { Bookings.id eq id }
            }
        }
    }

    class HotelRoomsRepository {
        suspend fun read(id: Int): ExposedHotelRooms? {
            return dbQuery {
                HotelRooms.select { HotelRooms.id eq id }.map {
                    ExposedHotelRooms(
                        it[HotelRooms.name], it[HotelRooms.room_type_id], it[HotelRooms.room_image]
                    )
                }.singleOrNull()
            }
        }

        suspend fun create(hotelRooms: ExposedHotelRooms) = dbQuery {
            HotelRooms.insertAndGetId {
                it[name] = hotelRooms.name
                it[room_type_id] = hotelRooms.roomTypeId
                it[room_image] = hotelRooms.room_image
            }
        }

        suspend fun update(id: Int, hotelRooms: ExposedHotelRooms) {
            dbQuery {
                HotelRooms.update({ HotelRooms.id eq id }) {
                    it[name] = hotelRooms.name
                    it[room_type_id] = hotelRooms.roomTypeId
                    it[room_image] = hotelRooms.room_image
                }
            }
        }

        suspend fun delete(id: Int) {
            dbQuery {
                HotelRooms.deleteWhere { HotelRooms.id.eq(id) }
            }
        }
    }

    class RoomTypesRepository {
        suspend fun create(roomType: ExposedRoomTypes): EntityID<Int> = dbQuery {
            RoomTypes.insert {
                it[name] = roomType.name
                it[description] = roomType.description
            }[RoomTypes.id]
        }

        suspend fun read(id: Int): ExposedRoomTypes? {
            return dbQuery {
                RoomTypes.select { RoomTypes.id eq id }
                    .map { ExposedRoomTypes(it[RoomTypes.name], it[RoomTypes.description]) }.singleOrNull()
            }
        }

        suspend fun update(id: Int, roomType: ExposedRoomTypes) {
            dbQuery {
                RoomTypes.update({ RoomTypes.id eq id }) {
                    it[name] = roomType.name
                    it[description] = roomType.description
                }
            }
        }

        suspend fun delete(id: Int) {
            dbQuery {
                RoomTypes.deleteWhere { RoomTypes.id.eq(id) }
            }
        }
    }
}
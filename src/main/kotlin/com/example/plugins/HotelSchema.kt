package com.example.plugins

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ExposedRoomType(val name: String, val description: String)
class HotelService(private val database: Database) {
    object RoomTypes : IntIdTable("room_types") {
        val name = varchar("name", 50)
        val description = text("description")
    }

    init {
        transaction {
            addLogger(StdOutSqlLogger)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(roomType: ExposedRoomType): EntityID<Int> = dbQuery {
        RoomTypes.insert {
            it[name] = roomType.name
            it[description] = roomType.description
        }[RoomTypes.id]
    }

    suspend fun read(id: Int): ExposedRoomType? {
        return dbQuery {
            RoomTypes.select { RoomTypes.id eq id }
                .map { ExposedRoomType(it[RoomTypes.name], it[RoomTypes.description]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, roomType: ExposedRoomType) {
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
package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes

@Entity(tableName = "Movimento")
data class MovimentoEntity(
    @PrimaryKey
    val id: String,
    val faixaCorresponde: String,
    val tipo: String,
    val base: String,
    val nome: String,
    @TypeConverters(Convertes::class)
    val observacao: List<String>
)

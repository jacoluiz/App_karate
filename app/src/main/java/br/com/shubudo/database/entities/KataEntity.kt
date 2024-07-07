package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Movimento

@Entity(tableName = "Kata")
data class KataEntity(
    @PrimaryKey val id: String,
    val faixa: String,
    val ordem: Int,
    val quantidadeMovimentos: Int,
    @TypeConverters(Convertes::class)
    val movimentos: List<Movimento>
)



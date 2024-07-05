package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes

@Entity(tableName = "Kata")
data class KataEntity(
    @PrimaryKey val id: String,
    val faixa: String,
    val orden: Int,
    val quantidadeMovimentos: Int,
    @TypeConverters(Convertes::class)
    val movimentos: List<MovimentoEntity>
)



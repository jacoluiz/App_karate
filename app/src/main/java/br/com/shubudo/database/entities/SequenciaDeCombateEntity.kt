package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Movimento
import br.com.shubudo.model.SequenciaDeCombate

@Entity(tableName = "SequenciaDeCombate")
@TypeConverters(Convertes::class)
data class SequenciaDeCombateEntity(
    @PrimaryKey
    val _id: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>,
    val video: String

)

fun SequenciaDeCombateEntity.toSequenciaDeCombate() = SequenciaDeCombate(
    _id = _id,
    faixa = faixa,
    numeroOrdem = numeroOrdem,
    movimentos = movimentos,
    video = video

)
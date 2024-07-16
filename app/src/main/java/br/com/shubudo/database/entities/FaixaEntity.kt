package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.shubudo.model.Faixa

@Entity(tableName = "Faixa")
data class FaixaEntity(
    @PrimaryKey()
    val _id: String,
    val faixa: String,
    val ordem: Int,
    val dan: Int = 0
)

fun FaixaEntity.toFaixa() = Faixa(
    _id = _id,
    faixa = faixa,
    ordem = ordem,
    dan = dan,
)

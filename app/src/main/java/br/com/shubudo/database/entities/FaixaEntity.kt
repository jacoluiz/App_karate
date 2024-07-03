package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Faixa")
data class FaixaEntity(
    @PrimaryKey()
    val id: String,
    val faixa: String,
    val ordem: Int,
    val dan: Int
)

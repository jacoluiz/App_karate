package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Projecao

@Entity(tableName = "projecoes")
@TypeConverters(Convertes::class)

data class ProjecaoEntity(
    @PrimaryKey val _id: String,
    val nome: String,
    val nomeJapones: String,
    val descricao: String,
    val observacao: List<String>,
    val ordem: Int,
    val faixa: String
)

fun ProjecaoEntity.toProjecao() = Projecao(
    _id = _id,
    nome = nome,
    nomeJapones = nomeJapones,
    descricao = descricao,
    observacao = observacao,
    ordem = ordem,
    faixa = faixa
)


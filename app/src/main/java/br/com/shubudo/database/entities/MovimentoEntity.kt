package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Movimento

@Entity(tableName = "Movimento")
@TypeConverters(Convertes::class)
data class MovimentoEntity(
    @PrimaryKey
    val _id: String,
    val faixa: String,
    val tipoMovimento: String,
    val base: String,
    val nome: String,
    val ordem: Int,
    val descricao: String,
    val observacao: List<String>
)

fun MovimentoEntity.toMovimento() = Movimento(
    _id = _id,
    faixa = faixa,
    tipoMovimento = tipoMovimento,
    base = base,
    nome = nome,
    descricao = descricao,
    ordem = ordem,
    observacao = observacao
)
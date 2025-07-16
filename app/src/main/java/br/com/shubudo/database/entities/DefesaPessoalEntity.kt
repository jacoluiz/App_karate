package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.model.Faixa
import br.com.shubudo.model.Movimento

@Entity(tableName = "DefesasPessoais")
@TypeConverters(Convertes::class)
data class DefesaPessoalEntity(
    @PrimaryKey
    val _id: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>,
    val video: String
)

fun DefesaPessoalEntity.toDefesaPessoal() = DefesaPessoal(
    _id = _id,
    faixa = faixa,
    numeroOrdem = numeroOrdem,
    movimentos = movimentos,
    video = video

)

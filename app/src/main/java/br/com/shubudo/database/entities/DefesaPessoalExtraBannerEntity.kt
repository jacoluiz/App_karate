package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.DefesaPessoalExtraBanner
import br.com.shubudo.model.Movimento

@Entity(tableName = "defesas_pessoais_extra_banner")
@TypeConverters(Convertes::class)

data class DefesaPessoalExtraBannerEntity(
    @PrimaryKey
    val _id: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>,
    val video: String

)

fun DefesaPessoalExtraBannerEntity.toDefesaPessoalExtraBanner() = DefesaPessoalExtraBanner(
    _id = _id,
    faixa = faixa,
    numeroOrdem = numeroOrdem,
    movimentos = movimentos,
    video = video

)

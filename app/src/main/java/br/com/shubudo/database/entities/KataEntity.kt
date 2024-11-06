package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Movimento
import br.com.shubudo.model.TempoVideo
import br.com.shubudo.model.Video

@Entity(tableName = "Kata")
@TypeConverters(Convertes::class)
data class KataEntity(
    @PrimaryKey
    val _id: String,
    val faixa: String,
    val ordem: Int,
    val quantidadeMovimentos: Int,
    val movimentos: List<Movimento>,
    val video: List<Video> = emptyList(),
    val temposVideos: List<TempoVideo> = emptyList()
)

fun KataEntity.toKata() = Kata(
    _id = _id,
    faixa = faixa,
    ordem = ordem,
    quantidadeMovimentos = quantidadeMovimentos,
    movimentos = movimentos,
    video = video,
    temposVideos = temposVideos
)


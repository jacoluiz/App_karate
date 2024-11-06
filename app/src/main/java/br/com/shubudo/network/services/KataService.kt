package br.com.shubudo.network.services

import br.com.shubudo.database.entities.KataEntity
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Movimento
import br.com.shubudo.model.TempoVideo
import br.com.shubudo.model.Video
import retrofit2.http.GET

data class KataResponse(
    val _id: String,
    val faixa: String,
    val ordem: Int,
    val quantidadeMovimentos: Int,
    val movimentos: List<Movimento>,
    val video: List<Video>,
    val temposVideos: List<TempoVideo>
)

fun KataResponse.toKata(): Kata {
    return Kata(
        _id = _id,
        faixa = faixa,
        ordem = ordem,
        quantidadeMovimentos = quantidadeMovimentos,
        movimentos = movimentos,
        video = video,
        temposVideos = temposVideos
    )
}

fun KataResponse.toKataEntity(): KataEntity {
    return KataEntity(
        _id = _id,
        faixa = faixa,
        ordem = ordem,
        quantidadeMovimentos = quantidadeMovimentos,
        movimentos = movimentos,
        video = video,
        temposVideos = temposVideos
    )
}

interface KataServices {
    @GET("kata")
    suspend fun getKatas(): List<KataResponse>
}
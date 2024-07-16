package br.com.shubudo.network.services

import br.com.shubudo.database.entities.KataEntity
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Movimento
import retrofit2.http.GET

data class KataResponse(
    val _id: String,
    val faixa: String,
    val ordem: Int,
    val quantidadeMovimentos: Int,
    val movimentos: List<Movimento>
)

fun KataResponse.toKata(): Kata {
    return Kata(
        _id = _id,
        faixa = faixa,
        ordem = ordem,
        quantidadeMovimentos = quantidadeMovimentos,
        movimentos = movimentos,
    )
}

fun KataResponse.toKataEntity(): KataEntity {
    return KataEntity(
        _id = _id,
        faixa = faixa,
        ordem = ordem,
        quantidadeMovimentos = quantidadeMovimentos,
        movimentos = movimentos,
    )
}

interface KataServices {
    @GET("kata")
    suspend fun getKatas(): List<KataResponse>
}
package br.com.shubudo.network.services

import br.com.shubudo.database.entities.SequenciaDeCombateEntity
import br.com.shubudo.model.Movimento
import br.com.shubudo.model.SequenciaDeCombate
import retrofit2.http.GET

data class SequenciaDeCombateResponse(
    val _id: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>
)

fun SequenciaDeCombateResponse.toSequenciaDeCombate(): SequenciaDeCombate {
    return SequenciaDeCombate(
        _id = _id,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
    )
}

fun SequenciaDeCombateResponse.toSequenciaDeCombateEntity(): SequenciaDeCombateEntity {
    return SequenciaDeCombateEntity(
        _id = _id,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
    )
}

interface SequenciaDeCombateService {
    @GET("sequenciaDeCombate")
    suspend fun getSequenciaDeCombate(): List<SequenciaDeCombateResponse>
}
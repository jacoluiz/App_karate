package br.com.shubudo.network.services

import br.com.shubudo.database.entities.SequenciaDeCombateEntity
import br.com.shubudo.model.Movimento
import br.com.shubudo.model.SequenciaDeCombate
import retrofit2.http.GET

data class SequenciaDeCombateResponse(
    val id: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>
)

fun SequenciaDeCombateResponse.toSequenciaDeCombate(): SequenciaDeCombate {
    return SequenciaDeCombate(
        id = id,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
    )
}

fun SequenciaDeCombateResponse.toSequenciaDeCombateEntity(): SequenciaDeCombateEntity {
    return SequenciaDeCombateEntity(
        id = id,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
    )
}

interface SequenciaDeCombateServices {
    @GET("sequenciaDeCombate")
    suspend fun getSequenciaDeCombate(): List<SequenciaDeCombateResponse>
}
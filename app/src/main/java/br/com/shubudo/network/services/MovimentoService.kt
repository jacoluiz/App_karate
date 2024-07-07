package br.com.shubudo.network.services

import br.com.shubudo.database.entities.MovimentoEntity
import br.com.shubudo.model.Movimento
import retrofit2.http.GET

data class MovimentoResponse(
    val id: String,
    val faixaCorresponde: String,
    val tipoMovimento: String,
    val base: String,
    val nome: String,
    val observacao: List<String>
)

fun MovimentoResponse.toMovimento(): Movimento {
    return Movimento(
        id = id,
        faixaCorresponde = faixaCorresponde,
        tipoMovimento = tipoMovimento,
        base = base,
        nome = nome,
        observacao = observacao,
    )
}

fun MovimentoResponse.toMovimentoEntity(): MovimentoEntity {
    return MovimentoEntity(
        id = id,
        faixaCorresponde = faixaCorresponde,
        tipoMovimento = tipoMovimento,
        base = base,
        nome = nome,
        observacao = observacao,
    )
}

interface MovimentoServices {

    @GET("ataqueMao")
    suspend fun getAtaquesDeMao(): List<MovimentoResponse>
}
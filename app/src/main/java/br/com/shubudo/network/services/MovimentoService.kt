package br.com.shubudo.network.services

import br.com.shubudo.database.entities.MovimentoEntity
import br.com.shubudo.model.Movimento
import retrofit2.http.GET

data class MovimentoResponse(
    val _id: String,
    val faixa: String,
    val tipoMovimento: String,
    val base: String,
    val nome: String,
    val descricao: String,
    val ordem: Int,
    val observacao: List<String>,
    val video: String

)

fun MovimentoResponse.toMovimento(): Movimento {
    return Movimento(
        _id = _id,
        faixa = faixa,
        tipoMovimento = tipoMovimento,
        base = base,
        nome = nome,
        descricao = descricao,
        ordem = ordem,
        observacao = observacao,
        video = video

    )
}

fun MovimentoResponse.toMovimentoEntity(): MovimentoEntity {
    return MovimentoEntity(
        _id = _id,
        faixa = faixa,
        tipoMovimento = tipoMovimento,
        base = base,
        nome = nome,
        descricao = descricao,
        ordem = ordem,
        observacao = observacao,
        video = video

    )
}

interface MovimentoService {

    @GET("ataqueMao")
    suspend fun getAtaquesDeMao(): List<MovimentoResponse>

    @GET("chute")
    suspend fun getChutes(): List<MovimentoResponse>

    @GET("defesa")
    suspend fun getDefesas(): List<MovimentoResponse>
}
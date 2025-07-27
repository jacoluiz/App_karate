package br.com.shubudo.network.services

import br.com.shubudo.database.entities.ArmamentoEntity
import br.com.shubudo.model.Movimento
import retrofit2.http.GET

data class ArmamentoResponse(
    val _id: String,
    val arma: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>,
    val video: String
)

fun ArmamentoResponse.toArmamentoEntity(): ArmamentoEntity {
    return ArmamentoEntity(
        _id = _id,
        arma = arma,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
        video = video
    )
}

interface ArmamentoServices {
    @GET("armamentos")
    suspend fun getArmamentos(): List<ArmamentoResponse>
}

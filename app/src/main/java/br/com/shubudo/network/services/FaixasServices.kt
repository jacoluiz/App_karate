package br.com.shubudo.network.services

import br.com.shubudo.database.entities.FaixaEntity
import br.com.shubudo.model.Faixa
import retrofit2.http.GET

data class FaixaResponse(
    val _id: String,
    val faixa: String,
    val ordem: Int,
    val dan: Int
)

fun FaixaResponse.toFaixa() : Faixa {
    return Faixa(
        _id = _id,
        faixa = faixa,
        ordem = ordem,
        dan = dan
    )
}

fun FaixaResponse.toFaixaEntity() : FaixaEntity {
    return FaixaEntity(
        _id = _id,
        faixa = faixa,
        ordem = ordem,
        dan = dan
    )
}

interface FaixasServices {

    @GET("faixa")
    suspend fun getFaixas() : List<FaixaResponse>
}
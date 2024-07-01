package br.com.shubudo.network.services

import br.com.shubudo.model.Faixa
import retrofit2.http.GET

data class FaixaResponse(val id : String, val faixa : String, val ordem : Int, val dan : Int)

interface FaixasServices {
    fun FaixaResponse.toFaixa() : Faixa {
        return Faixa(
                id = id, faixa = faixa, ordem = ordem, dan = dan
        )
    }

    @GET("faixa")
    suspend fun getFaixas() : List<FaixaResponse>
}
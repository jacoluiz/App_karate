package br.com.shubudo.network.services

import br.com.shubudo.database.entities.DefesaPessoalExtraBannerEntity
import br.com.shubudo.model.DefesaPessoalExtraBanner
import br.com.shubudo.model.Movimento
import retrofit2.http.GET

data class DefesaPessoalExtraBannerResponse(
    val _id: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>,
    val video: String

)

fun DefesaPessoalExtraBannerResponse.toDefesaPessoalExtraBanner(): DefesaPessoalExtraBanner {
    return DefesaPessoalExtraBanner(
        _id = _id,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
        video = video

    )
}

fun DefesaPessoalExtraBannerResponse.toDefesaPessoalExtraBannerEntity(): DefesaPessoalExtraBannerEntity {
    return DefesaPessoalExtraBannerEntity(
        _id = _id,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
        video = video
    )
}

interface DefesaPessoalExtraBannerServices {
    @GET("defesasPessoaisExtraBanner")
    suspend fun getDefesasPessoaisExtraBanner(): List<DefesaPessoalExtraBannerResponse>
}

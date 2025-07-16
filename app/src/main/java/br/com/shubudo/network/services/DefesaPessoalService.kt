package br.com.shubudo.network.services

import br.com.shubudo.database.entities.DefesaPessoalEntity
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.model.Movimento
import retrofit2.http.GET

data class DefesaPessoalResponse(
    val _id: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>,
    val video: String

)

fun DefesaPessoalResponse.toDefesaPessoal(): DefesaPessoal {
    return DefesaPessoal(
        _id = _id,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
        video = video

    )
}

fun DefesaPessoalResponse.toDefesaPessoalEntity(): DefesaPessoalEntity {
    return DefesaPessoalEntity(
        _id = _id,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
        video = video

    )
}

interface DefesaPessoalServices {
    @GET("defesaPessoal")
    suspend fun getDefesasPessoais(): List<DefesaPessoalResponse>
}
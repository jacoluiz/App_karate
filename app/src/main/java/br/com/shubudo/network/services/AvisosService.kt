package br.com.shubudo.network.services

import br.com.shubudo.database.entities.AvisoEntity
import br.com.shubudo.model.Aviso
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Modelo da resposta vinda da API
data class AvisoResponse(
    val _id: String,
    val titulo: String,
    val conteudo: String,
    val dataHoraCriacao: String,
    val publicoAlvo: List<String>,
    val academia: String
)

data class NovoAvisoRequest(
    val titulo: String,
    val conteudo: String,
    val publicoAlvo: List<String>,
    val academia: String
)

// Conversão para Entity (para Room)
fun AvisoResponse.toAvisoEntity(): AvisoEntity {
    return AvisoEntity(
        id = _id,
        titulo = titulo,
        conteudo = conteudo,
        dataHoraCriacao = dataHoraCriacao,
        publicoAlvo = publicoAlvo,
        academia = academia
    )
}

// Interface Retrofit
interface AvisoService {
    @GET("aviso")
    suspend fun getAvisos(): List<AvisoResponse>

    @POST("aviso")
    suspend fun criarAviso(@Body aviso: NovoAvisoRequest): AvisoResponse

    @DELETE("aviso/{id}")
    suspend fun deletarAviso(@Path("id") id: String)

    @PUT("aviso/{id}")
    suspend fun editarAviso(
        @Path("id") id: String,
        @Body aviso: NovoAvisoRequest
    )

}

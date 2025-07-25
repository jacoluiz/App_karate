package br.com.shubudo.network.services

import br.com.shubudo.database.entities.AcademiaEntity
import br.com.shubudo.model.Academia
import br.com.shubudo.model.Filial
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class AcademiaResponse(
    val _id: String,
    val nome: String,
    val descricao: String?,
    val filiais: List<Filial>
)

data class NovaAcademiaRequest(
    val nome: String,
    val descricao: String?,
    val filiais: List<Filial>
)

fun AcademiaResponse.toAcademia(): Academia {
    return Academia(
        _id = _id,
        nome = nome,
        descricao = descricao ?: "",
        filiais = filiais
    )
}

fun AcademiaResponse.toAcademiaEntity(): AcademiaEntity {
    return AcademiaEntity(
        id = _id,
        nome = nome,
        descricao = descricao,
        filiais = filiais
    )
}

interface AcademiaService {
    @GET("academias")
    suspend fun getAcademias(): List<AcademiaResponse>

    @POST("academias")
    suspend fun criarAcademia(@Body academia: NovaAcademiaRequest): AcademiaResponse

    @PUT("academias/{id}")
    suspend fun editarAcademia(
        @Path("id") id: String,
        @Body academia: NovaAcademiaRequest
    ): AcademiaResponse

    @DELETE("academias/{id}")
    suspend fun deletarAcademia(@Path("id") id: String)
}

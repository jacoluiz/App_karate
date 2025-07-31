package br.com.shubudo.network.services

import br.com.shubudo.database.entities.ParceiroEntity
import br.com.shubudo.model.Parceiro
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

data class ParceiroResponse(
    val _id: String,
    val nome: String,
    val descricao: String,
    val localizacao: String?,
    val telefone: String,
    val site: String?,
    val logomarca: String,
    val imagens: List<String> = emptyList()
)

data class NovoParceiroRequest(
    val nome: String,
    val descricao: String,
    val localizacao: String?,
    val telefone: String,
    val site: String?,
    val logomarca: String,
    val imagens: List<String> = emptyList()
)

fun ParceiroResponse.toParceiro(): Parceiro {
    return Parceiro(
        _id = _id,
        nome = nome,
        descricao = descricao,
        localizacao = localizacao ?: "",
        telefone = telefone,
        site = site ?: "",
        logomarca = logomarca,
        imagens = imagens
    )
}

fun ParceiroResponse.toParceiroEntity(): ParceiroEntity {
    return ParceiroEntity(
        _id = _id,
        nome = nome,
        descricao = descricao,
        localizacao = localizacao ?: "",
        telefone = telefone,
        site = site ?: "",
        logomarca = logomarca,
        imagens = imagens
    )
}

interface ParceiroService {

    @GET("/parceiros")
    suspend fun listarParceiros(): List<ParceiroResponse>

    @POST("/parceiros")
    suspend fun criarParceiro(
        @Body parceiro: NovoParceiroRequest
    ): ParceiroResponse

    @PUT("/parceiros/{id}")
    suspend fun atualizarParceiro(
        @Path("id") parceiroId: String,
        @Body parceiro: NovoParceiroRequest
    ): ParceiroResponse

    @DELETE("/parceiros/{id}")
    suspend fun deletarParceiro(
        @Path("id") parceiroId: String
    ): Response<JsonObject>
}

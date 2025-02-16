package br.com.shubudo.network.services

import br.com.shubudo.database.entities.AvisoEntity
import br.com.shubudo.model.Aviso
import br.com.shubudo.model.AvisoResumido
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class AvisoCriadoResponse(
    val message: String,
    val aviso: Aviso
)

data class AvisoResponse(
    val _id: String,
    val titulo: String,
    val conteudo: String,
    val imagem: String?,
    val arquivos: List<String>,
    val ativo: Boolean,
    val dataCriacao: String,
    val exclusivoParaFaixas: List<String>
)

fun AvisoCriadoResponse.toAviso(): Aviso {
    return aviso
}

fun AvisoResponse.toAviso(): Aviso {
    return Aviso(
        _id = _id,
        titulo = titulo,
        conteudo = conteudo,
        imagem = imagem,
        arquivos = arquivos,
        ativo = ativo,
        dataCriacao = dataCriacao,
        exclusivoParaFaixas = exclusivoParaFaixas
    )
}

fun AvisoResponse.toAvisoEntity(): AvisoEntity {
    return AvisoEntity(
        _id = _id,
        titulo = titulo,
        conteudo = conteudo,
        imagem = imagem,
        arquivos = arquivos,
        ativo = ativo,
        dataCriacao = dataCriacao,
        exclusivoParaFaixas = exclusivoParaFaixas
    )
}

interface AvisoService {

    @POST("/aviso")
    suspend fun criarAviso(@Body aviso: AvisoResumido): AvisoCriadoResponse

    @PUT("/aviso/{id}")
    suspend fun atualizarAviso(
        @Path("id") id: String,
        @Body aviso: Aviso
    ): AvisoResponse?

    @GET("/aviso")
    suspend fun listarAvisos(): List<AvisoResponse>

    @GET("/aviso/{id}")
    suspend fun getAvisoById(@Path("id") id: String): AvisoResponse

    @DELETE("/aviso/{id}")
    suspend fun excluirAviso(@Path("id") id: String): AvisoResponse
}

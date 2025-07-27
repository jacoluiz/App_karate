package br.com.shubudo.network.services

import br.com.shubudo.database.entities.GaleriaFotoEntity
import br.com.shubudo.model.GaleriaFoto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

fun GaleriaFoto.toGaleriaFotoEntity(): GaleriaFotoEntity {
    return GaleriaFotoEntity(
        _id = this._id ?: "",
        eventoId = this.eventoId,
        url = this.url,
        uploadedBy = this.uploadedBy,
        nomeArquivo = this.nomeArquivo ?: "",
        createdAt = this.createdAt ?: ""
    )
}

interface GaleriaFotoService {

    @GET("/galeria/fotos/{eventoId}")
    suspend fun listarFotosPorEvento(@Path("eventoId") eventoId: String): List<GaleriaFoto>

    @Multipart
    @POST("/galeria/fotos/{eventoId}")
    suspend fun enviarFotos(
        @Path("eventoId") eventoId: String,
        @Part foto: List<MultipartBody.Part>,
        @Part("academiaId") academiaId: RequestBody,
        @Part("usuarioId") usuarioId: RequestBody
    ): GaleriaFoto

    @DELETE("/galeria/fotos/{id}")
    suspend fun deletarFoto(@Path("id") fotoId: String)
}

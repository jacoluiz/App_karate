package br.com.shubudo.network.services

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming

data class RelatorioDinamicoRequest(
    val filtros: Map<String, String> = emptyMap(),        // ex: {"faixa":"Branca"}
    val colunasExtras: List<String> = emptyList(),        // ex: ["telefone","registroAKSD"]
    val ordenarPor: List<String> = emptyList()            // ex: ["altura:asc","nome:asc"]
)

interface RelatorioService {

    @GET("/relatorios/organizacao")
    @Streaming
    suspend fun baixarRelatorioOrganizado(): Response<ResponseBody>

    @POST("/relatorios/dinamico")
    @Streaming
    suspend fun baixarRelatorioDinamico(
        @Body body: RelatorioDinamicoRequest
    ): Response<ResponseBody>
}

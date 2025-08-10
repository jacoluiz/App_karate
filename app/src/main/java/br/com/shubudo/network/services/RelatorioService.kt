package br.com.shubudo.network.services

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Query

interface RelatorioService {

    @GET("/relatorios/organizacao/{eventoId}")
    @Streaming
    suspend fun baixarRelatorioOrganizado(
        @Path("eventoId") eventoId: String,
        @Query("ateCone") conesMax: Int? = null,
        @Query("ateFila") filasMax: String? = null
    ): Response<ResponseBody>

    // Relatório "normal" (adultos/adolescentes)
    @GET("/relatorios/exame/{eventoId}")
    @Streaming
    suspend fun baixarRelatorioExamePorEvento(
        @Path("eventoId") eventoId: String,
        @Query("primeiraInfancia") primeiraInfancia: Boolean = false
    ): Response<ResponseBody>

    // Relatório de Primeira Infância (rota específica no backend)
    @GET("/relatorios/primeira-infancia/{eventoId}")
    @Streaming
    suspend fun baixarRelatorioPrimeiraInfanciaPorEvento(
        @Path("eventoId") eventoId: String
    ): Response<ResponseBody>
}

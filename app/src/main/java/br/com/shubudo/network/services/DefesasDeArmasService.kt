package br.com.shubudo.network.services

import br.com.shubudo.database.entities.DefesaArmaEntity
import br.com.shubudo.model.Armamento
import br.com.shubudo.model.Movimento
import retrofit2.http.GET

data class DefesaArmaResponse(
    val _id: String,
    val arma: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>,
    val video: String,
    val tipo: String // Propriedade para identificar que é defesa de arma, se necessário
)

// Converte a resposta para o modelo de domínio Armamento
fun DefesaArmaResponse.toArmamento(): Armamento {
    return Armamento(
        _id = _id,
        arma = arma,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
        video = video
    )
}

// Converte a resposta para a entidade específica de defesas de armas
fun DefesaArmaResponse.toArmamentoEntity(): DefesaArmaEntity {
    return DefesaArmaEntity(
        _id = _id,
        arma = arma,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
        video = video
    )
}

interface DefesaArmaServices {
    @GET("defesasDeArmas")
    suspend fun getDefesasArmas(): List<DefesaArmaResponse>
}

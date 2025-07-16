package br.com.shubudo.network.services

import br.com.shubudo.database.entities.ProjecaoEntity
import br.com.shubudo.model.Projecao
import retrofit2.http.GET

// Modelo de resposta da API
data class ProjecaoResponse(
    val _id: String,
    val nome: String,
    val nomeJapones: String,
    val descricao: String,
    val observacao: List<String> = emptyList(),
    val ordem: Int,
    val faixa: String,
    val video: String

)

// Função de conversão para o modelo Projecao
fun ProjecaoResponse.toProjecao(): Projecao {
    return Projecao(
        _id = _id,
        nome = nome,
        nomeJapones = nomeJapones,
        descricao = descricao,
        observacao = observacao,
        ordem = ordem,
        faixa = faixa,
        video = video

    )
}

// Função de conversão para a entidade ProjecaoEntity (para banco de dados)
fun ProjecaoResponse.toProjecaoEntity(): ProjecaoEntity {
    return ProjecaoEntity(
        _id = _id,
        nome = nome,
        nomeJapones = nomeJapones,
        descricao = descricao,
        observacao = observacao,
        ordem = ordem,
        faixa = faixa,
        video = video

    )
}

// Interface Retrofit
interface ProjecaoServices {
    @GET("projecao") // Endpoint para buscar as projeções
    suspend fun getProjecoes(): List<ProjecaoResponse>
}


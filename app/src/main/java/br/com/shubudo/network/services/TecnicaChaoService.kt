package br.com.shubudo.network.services

import br.com.shubudo.database.entities.TecnicaChaoEntity
import br.com.shubudo.model.TecnicaChao
import retrofit2.http.GET

data class TecnicaChaoResponse(
    val _id: String,
    val nome: String,
    val descricao: String,
    val ordem: Int,
    val observacao: List<String>,
    val faixa: String,
    val video: String,
    val tipo: String? = "tecnicaChao" // opcional, caso queira identificar o tipo
)

// Converte a resposta da API para o modelo de domínio
fun TecnicaChaoResponse.toModel(): TecnicaChao {
    return TecnicaChao(
        _id = _id,
        nome = nome,
        descricao = descricao,
        ordem = ordem,
        observacao = observacao,
        faixa = faixa,
        video = video
    )
}

// Converte a resposta da API para a entidade do Room
fun TecnicaChaoResponse.toEntity(): TecnicaChaoEntity {
    return TecnicaChaoEntity(
        _id = _id,
        nome = nome,
        descricao = descricao,
        ordem = ordem,
        observacao = observacao,
        faixa = faixa,
        video = video
    )
}

interface TecnicaChaoService {
    @GET("tecnicasChao")
    suspend fun getTecnicasChao(): List<TecnicaChaoResponse>
}

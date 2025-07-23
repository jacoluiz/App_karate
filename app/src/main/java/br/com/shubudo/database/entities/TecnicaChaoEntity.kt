package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.TecnicaChao

@Entity(tableName = "TecnicasChao")
@TypeConverters(Convertes::class)
data class TecnicaChaoEntity(
    @PrimaryKey
    val _id: String,
    val nome: String,
    val descricao: String,
    val ordem: Int,
    val observacao: List<String>,
    val faixa: String,
    val video: String
)

// Função de extensão para converter a entidade para o modelo de domínio
fun TecnicaChaoEntity.toTecnicaChao(): TecnicaChao {
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

// Função de extensão para converter o modelo de domínio em entidade
fun TecnicaChao.toTecnicaChaoEntity(): TecnicaChaoEntity {
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

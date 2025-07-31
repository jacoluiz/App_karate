package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Parceiro

@Entity(tableName = "Parceiro")
@TypeConverters(Convertes::class)
data class ParceiroEntity(
    @PrimaryKey val _id: String,
    val nome: String,
    val descricao: String,
    val localizacao: String? = null,
    val telefone: String,
    val site: String? = null,
    val logomarca: String,
    val imagens: List<String> = emptyList()
)

fun ParceiroEntity.toParceiro(): Parceiro {
    return Parceiro(
        _id = _id,
        nome = nome,
        descricao = descricao,
        localizacao = localizacao,
        telefone = telefone,
        site = site,
        logomarca = logomarca,
        imagens = imagens
    )
}
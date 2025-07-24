package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Aviso

@Entity(tableName = "avisos")
@TypeConverters(Convertes::class)
data class AvisoEntity(
    @PrimaryKey
    val id: String,
    val titulo: String,
    val conteudo: String,
    val dataHoraCriacao: String,
    val publicoAlvo: List<String>
)

// Conversão de Entity para Model
fun AvisoEntity.toAviso() = Aviso(
    id = id,
    titulo = titulo,
    conteudo = conteudo,
    dataHoraCriacao = dataHoraCriacao,
    publicoAlvo = publicoAlvo
)

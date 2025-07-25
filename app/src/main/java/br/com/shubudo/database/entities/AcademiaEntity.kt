package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Academia
import br.com.shubudo.model.Filial

@Entity(tableName = "academias")
@TypeConverters(Convertes::class)
data class AcademiaEntity(
    @PrimaryKey
    val id: String,
    val nome: String,
    val descricao: String?,
    val filiais: List<Filial>
)

// Conversão de Entity para Model
fun AcademiaEntity.toAcademia() = Academia(
    _id = id,
    nome = nome,
    descricao = descricao ?: "",
    filiais = filiais
)

// Conversão de Model para Entity
fun Academia.toAcademiaEntity() = AcademiaEntity(
    id = _id,
    nome = nome,
    descricao = descricao,
    filiais = filiais
)

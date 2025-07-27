package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.GaleriaEvento

@Entity(tableName = "GaleriaEvento")
@TypeConverters(Convertes::class)
data class GaleriaEventoEntity(
    @PrimaryKey
    val _id: String,
    val titulo: String,
    val descricao: String,
    val data: String,
    val criadoPor: String,
    val filialId: String,
    val academiaId: String,
    val createdAt: String
)

fun GaleriaEventoEntity.toGaleriaEvento(): GaleriaEvento {
    return GaleriaEvento(
        _id = this._id,
        titulo = this.titulo,
        descricao = this.descricao,
        data = this.data,
        criadoPor = this.criadoPor,
        filialId = this.filialId,
        academiaId = this.academiaId,
        createdAt = this.createdAt
    )
}

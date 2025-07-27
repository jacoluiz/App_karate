package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.GaleriaFoto

@Entity(tableName = "GaleriaFoto")
@TypeConverters(Convertes::class)
data class GaleriaFotoEntity(
    @PrimaryKey
    val _id: String,
    val eventoId: String,
    val url: String,
    val uploadedBy: String,
    val nomeArquivo: String?,
    val createdAt: String?
)

fun GaleriaFotoEntity.toGaleriaFoto(): GaleriaFoto {
    return GaleriaFoto(
        _id = this._id,
        eventoId = this.eventoId,
        url = this.url,
        uploadedBy = this.uploadedBy,
        nomeArquivo = this.nomeArquivo,
        createdAt = this.createdAt
    )
}


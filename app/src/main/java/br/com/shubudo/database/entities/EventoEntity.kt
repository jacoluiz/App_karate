package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Evento
import br.com.shubudo.model.Presenca

@Entity(tableName = "Evento")
@TypeConverters(Convertes::class)
data class EventoEntity(
    @PrimaryKey val _id: String,
    val titulo: String,
    val descricao: String,
    val dataInicio: String,
    val local: String,
    val confirmados: List<String> = emptyList(),
    val academia: String,
    val eventoOficial: Boolean = false,
    val presencas: List<Presenca> = emptyList()
)

fun EventoEntity.toEvento(): Evento {
    return Evento(
        _id = _id,
        titulo = titulo,
        descricao = descricao,
        dataInicio = dataInicio,
        local = local,
        confirmados = confirmados,
        academia = academia,
        eventoOficial = eventoOficial,
        presencas = presencas
    )
}

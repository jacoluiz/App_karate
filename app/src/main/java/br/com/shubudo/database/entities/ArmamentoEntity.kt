package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Armamento
import br.com.shubudo.model.Movimento

@Entity(tableName = "Armamento")
@TypeConverters(Convertes::class)
data class ArmamentoEntity(
    @PrimaryKey
    val _id: String,
    val arma: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>,
    val video: String
)

// Função de extensão para converter a entidade para o modelo de domínio
fun ArmamentoEntity.toArmamento(): Armamento {
    return Armamento(
        _id = _id,
        arma = arma,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
        video = video
    )
}

// Função de extensão para converter o modelo de domínio em entidade
fun Armamento.toArmamentoEntity(): ArmamentoEntity {
    return ArmamentoEntity(
        _id = _id,
        arma = arma,
        faixa = faixa,
        numeroOrdem = numeroOrdem,
        movimentos = movimentos,
        video = video
    )
}

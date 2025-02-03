package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Aviso

@Entity(tableName = "Aviso")
@TypeConverters(Convertes::class)
data class AvisoEntity(
    @PrimaryKey
    val _id: String,
    val titulo: String,
    val conteudo: String,
    val imagem: String?,              // Pode ser nulo caso não haja imagem
    val arquivos: List<String>,       // Lista de arquivos (ex: URLs ou caminhos)
    val ativo: Boolean,               // Indica se o aviso está ativo
    val dataCriacao: String,          // Data de criação em formato String
    val exclusivoParaFaixas: List<String> // Lista com as faixas exclusivas para o aviso
)

fun AvisoEntity.toAviso() = Aviso(
    _id = _id,
    titulo = titulo,
    conteudo = conteudo,
    imagem = imagem,
    arquivos = arquivos,
    ativo = ativo,
    dataCriacao = dataCriacao,
    exclusivoParaFaixas = exclusivoParaFaixas
)

package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Usuario

@Entity(tableName = "Usuario")
@TypeConverters(Convertes::class)
data class UsuarioEntity(
    @PrimaryKey
    val _id: String,
    val nome: String,
    val username: String,
    val email: String,
    val peso: String,
    val altura: String,
    val idade: String,
    val perfis: List<String> = listOf("aluno"),
    val corFaixa: String,
    val status: String = "ativo",
    val dan: Int = 0,
    val academia: String = "",
    val tamanhoFaixa: String = "",
    val lesaoOuLaudosMedicos: String = "",
    val registroAKSD: String = "",
    val fcmToken: String = ""
)

fun UsuarioEntity.toUsuario(): Usuario {
    return Usuario(
        _id = this._id,
        nome = this.nome,
        username = this.username,
        email = this.email,
        peso = this.peso,
        altura = this.altura,
        idade = this.idade,
        perfis = this.perfis,
        corFaixa = this.corFaixa,
        status = this.status,
        dan = this.dan,
        academia = this.academia,
        tamanhoFaixa = this.tamanhoFaixa,
        lesaoOuLaudosMedicos = this.lesaoOuLaudosMedicos,
        registroAKSD = this.registroAKSD,
        fcmToken = this.fcmToken.ifEmpty { null }
    )
}

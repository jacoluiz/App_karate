package br.com.shubudo.model

import br.com.shubudo.database.entities.UsuarioEntity

data class Usuario(
    val _id: String? = null,
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
    val fcmToken: String? = null,
    val professorEm: List<String> = emptyList()
)

fun Usuario.toUsuarioEntity(): UsuarioEntity? {
    return this._id?.let {
        UsuarioEntity(
            _id = it,
            nome = this.nome,
            email = this.email,
            corFaixa = this.corFaixa,
            peso = this.peso,
            altura = this.altura,
            username = this.username,
            idade = this.idade,
            perfis = this.perfis,
            status = this.status,
            dan = this.dan,
            academia = this.academia,
            tamanhoFaixa = this.tamanhoFaixa,
            lesaoOuLaudosMedicos = this.lesaoOuLaudosMedicos,
            registroAKSD = this.registroAKSD,
            fcmToken = this.fcmToken ?: "",
            professorEm = this.professorEm
        )
    }
}

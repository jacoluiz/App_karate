package br.com.shubudo.model

import br.com.shubudo.database.entities.UsuarioEntity

data class Usuario(
    val _id: String? = null,
    val nome: String,
    val username: String,
    val email: String,
    val senha: String = "",
    val peso: String,
    val altura: String,
    val idade: String,
    val perfil: String = "básico",
    val corFaixa: String,
    val faixas: List<Faixa>? = null,
    val status: String = "ativo",
    val dan: Int = 0,
    val academia: String = "",
    val tamanhoFaixa: String = "",
    val lesaoOuLaudosMedicos: String = "",
    val registroAKSD: String = ""
)

fun Usuario.toUsuarioEntity(): UsuarioEntity? {
    return this._id?.let {
        UsuarioEntity(
            _id = it,
            nome = this.nome,
            email = this.email,
            senha = this.senha,
            corFaixa = this.corFaixa,
            peso = this.peso,
            altura = this.altura,
            username = this.username,
            idade = this.idade,
            perfil = this.perfil,
            status = this.status,
            dan = this.dan,
            academia = this.academia,
            tamanhoFaixa = this.tamanhoFaixa,
            lesaoOuLaudosMedicos = this.lesaoOuLaudosMedicos,
            registroAKSD = this.registroAKSD
        )
    }
}

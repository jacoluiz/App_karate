package br.com.shubudo.model

import br.com.shubudo.database.entities.UsuarioEntity

data class Usuario(
    val _id: String? = null,
    val nome: String,
    val username: String,
    val email: String,
    val senha: String = "",// A senha armazenada deve ser criptografada
    val peso: String,
    val altura: String,
    val idade: String,
    val perfil: String = "básico", // Valor padrão
    val corFaixa: String,
    val faixas: List<Faixa>? = null // Lista de faixas associadas ao usuário
)

fun Usuario.toUsuarioEntity(): UsuarioEntity? {
    return this._id?.let {
        UsuarioEntity(
            _id = it, // Se o ID for gerado automaticamente, deixe como null
            nome = this.nome,
            email = this.email,
            senha = this.senha,
            corFaixa = this.corFaixa,
            peso = this.peso,
            altura = this.altura,
            username = this.username,
            idade = this.idade,
            perfil = this.perfil
        )
    }
}

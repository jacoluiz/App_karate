package br.com.shubudo.model

data class Usuario(
    val _id: String,
    val nome: String,
    val username: String,
    val email: String,
    val senha: String, // A senha armazenada deve ser criptografada
    val peso: String,
    val altura: String,
    val idade: String,
    val perfil: String = "básico", // Valor padrão
    val corFaixa: String,
    val faixas: List<Faixa>? = null // Lista de faixas associadas ao usuário
)

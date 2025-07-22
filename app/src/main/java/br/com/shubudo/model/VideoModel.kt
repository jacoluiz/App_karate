package br.com.shubudo.model

enum class Orientacao {
    FRENTE,
    ESQUERDA,
    DIREITA,
    COSTAS
}

data class Video(
    val _id: String,
    val orientacao: Orientacao,
    val url: String
)
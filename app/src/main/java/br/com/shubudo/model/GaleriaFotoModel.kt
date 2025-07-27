package br.com.shubudo.model

data class GaleriaFoto(
    val _id: String,
    val eventoId: String,
    val url: String,
    val nomeArquivo: String? = null,
    val uploadedBy: String,
    val createdAt: String? = null
)

package br.com.shubudo.model

data class GaleriaEvento(
    val _id: String,
    val titulo: String,
    val descricao: String? = "",
    val data: String, // ou LocalDate se for converter, depende do seu uso
    val academiaId: String,
    val filialId: String,
    val criadoPor: String,
    val createdAt: String? = null
)

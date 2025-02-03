package br.com.shubudo.model

data class Aviso(
    val _id: String,
    val titulo: String,
    val conteudo: String,
    val imagem: String?, // Pode ser nulo caso não haja imagem
    val arquivos: List<String>,
    val ativo: Boolean,
    val dataCriacao: String,
    val exclusivoParaFaixas: List<String>
)

package br.com.shubudo.model

data class Aviso(
    val _id: String = "",
    val titulo: String,
    val conteudo: String,
    val imagem: String? = null,
    val arquivos: List<String> = emptyList(),
    val ativo: Boolean = false,
    val dataCriacao: String = "",
    val exclusivoParaFaixas: List<String> = emptyList()
)

data class AvisoResumido(
    val titulo: String,
    val conteudo: String,
)


package br.com.shubudo.model

data class Programacao(
//    val id: String,
    val faixa: Faixa,
    val ataquesDeMao: List<Movimento>,
    val chutes: List<Movimento>,
    val defesas: List<Movimento>,
    var defesaPessoal: List<DefesaPessoal>,
    val katas: List<Kata>,
    val sequenciaDeCombate: List<SequenciaDeCombate>,
)
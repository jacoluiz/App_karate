package br.com.shubudo.model

data class Programacao(
//    val id: String,
    val faixa: Faixa,
    val ataquesDeMao: List<Movimento>,
    val chutes: List<Movimento>,
    val defesas: List<Movimento>,
    var defesaPessoal: List<DefesaPessoal>,
    val sequenciaDeCombate: List<SequenciaDeCombate>,
    val katas: List<Kata>,
    val defesaExtraBanner: List<DefesaPessoalExtraBanner>,
    val projecoes : List<Projecao>,
    val armamento: List<Armamento>,
    val defesasDeArma: List<Armamento>
) {
}
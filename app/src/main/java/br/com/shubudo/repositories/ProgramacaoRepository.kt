package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.model.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ProgramacaoRepository @Inject constructor(
    private val faixaRepository: FaixaRepository,
    private val defesaPessoalRepository: DefesaPessoalRepository,
    private val kataRepository: KataRepository,
    private val movimentoRepository: MovimentoRepository,
    private val sequenciaDeCombateRepository: SequenciaDeCombateRepository
) {

    suspend fun findAllProgramacoes(): Flow<List<Programacao>> {
        val programacoes = mutableListOf<Programacao>()
        val faixas = faixaRepository.findAll()

        faixas.first().
            forEach { faixa ->
               programacoes.add(findProgramacaoByFaixa(faixa).first())
            }
        Log.i("Conteudo", "findAllProgramacoes: programacoes: $programacoes")
        return flow { emit(programacoes) }
    }

    suspend fun findProgramacaoByCorFaixa(corFaixa: String): Flow<Programacao> {
        val faixa = faixaRepository.findByNome(corFaixa).first()
        return findProgramacaoByFaixa(faixa)
    }

    suspend fun findProgramacaoByFaixa(faixa: Faixa): Flow<Programacao> {
        val idFaixa = faixa._id
        val defesasPessoais = defesaPessoalRepository.findByFaixa(idFaixa)
        val katas = kataRepository.findByFaixa(idFaixa)
        val ataquesDeMao = movimentoRepository.findMovimentoByFaixa(idFaixa, "ataqueMao")
        val chutes = movimentoRepository.findMovimentoByFaixa(idFaixa, "chute")
        val defesas = movimentoRepository.findMovimentoByFaixa(idFaixa, "defesa")
        val sequenciasDeCombate = sequenciaDeCombateRepository.findByFaixa(idFaixa)

        val programacao = Programacao(
            faixa = faixa,
            defesaPessoal = defesasPessoais.first(),
            katas = katas.first(),
            ataquesDeMao = ataquesDeMao.first(),
            chutes = chutes.first(),
            defesas = defesas.first(),
            sequenciaDeCombate = sequenciasDeCombate.first(),
        )
        Log.i("Conteudo", "findProgramacaoByFaixa: programacao: $programacao")
        return flow { emit(programacao) }
    }
}

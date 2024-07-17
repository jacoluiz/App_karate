package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.model.Faixa
import br.com.shubudo.model.Programacao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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

        faixas.first().forEach { faixa ->
            programacoes.add(findProgramacaoByFaixa(faixa).first())
        }
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
        val ataquesDeMao = movimentoRepository.findMovimentoByFaixa(idFaixa, "Ataque de mão")
        val chutes = movimentoRepository.findMovimentoByFaixa(idFaixa, "Chute")
        val defesas = movimentoRepository.findMovimentoByFaixa(idFaixa, "Defesa")
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

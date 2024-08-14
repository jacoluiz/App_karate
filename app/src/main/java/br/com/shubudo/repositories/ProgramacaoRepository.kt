package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.model.Faixa
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Movimento
import br.com.shubudo.model.Programacao
import br.com.shubudo.model.SequenciaDeCombate
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

    suspend fun findMovimentoByFaixaETipo(faixa: String, tipo: String): Flow<List<Movimento>> {
        when (tipo) {
            "Ataques de Mão" -> return findAtaquesDeMaoByFaixa(
                faixaRepository.findByNome(faixa).first()
            )

            "Chutes" -> return findChutesByFaixa(faixaRepository.findByNome(faixa).first())
            "Defesas" -> return findDefesasByFaixa(faixaRepository.findByNome(faixa).first())
        }
        return flow { emit(emptyList()) }
    }

    suspend fun findFaixaByCor(cor: String): Flow<Faixa> {
        return faixaRepository.findByNome(cor)
    }

    suspend fun findProgramacaoByCorFaixa(corFaixa: String): Flow<Programacao> {
        val faixa = findFaixaByCor(corFaixa).first()
        return findProgramacaoByFaixa(faixa)
    }

    suspend fun findProgramacaoByFaixa(faixa: Faixa): Flow<Programacao> {
        val defesasPessoais = findDefesasPessoaisByFaixa(faixa).first()
        val katas = findKatasByFaixa(faixa).first()
        val ataquesDeMao = findAtaquesDeMaoByFaixa(faixa).first()
        val chutes = findChutesByFaixa(faixa).first()
        val defesas = findDefesasByFaixa(faixa).first()
        val sequenciasDeCombate = findSequenciasDeCombateByFaixa(faixa).first()

        val programacao = Programacao(
            faixa = faixa,
            defesaPessoal = defesasPessoais,
            katas = katas,
            ataquesDeMao = ataquesDeMao,
            chutes = chutes,
            defesas = defesas,
            sequenciaDeCombate = sequenciasDeCombate,
        )
        return flow { emit(programacao) }
    }

    suspend fun findAtaquesDeMaoByFaixa(faixa: Faixa): Flow<List<Movimento>> {
        return movimentoRepository.findMovimentoByFaixa(faixa._id, "Ataque de mão")
    }

    suspend fun findChutesByFaixa(faixa: Faixa): Flow<List<Movimento>> {
        return movimentoRepository.findMovimentoByFaixa(faixa._id, "Chute")
    }

    suspend fun findDefesasByFaixa(faixa: Faixa): Flow<List<Movimento>> {
        return movimentoRepository.findMovimentoByFaixa(faixa._id, "Defesa")
    }

    suspend fun findDefesasPessoaisByFaixa(
        faixa: Faixa? = null,
        idFaixa: String? = null
    ): Flow<List<DefesaPessoal>> {
        val faixaId = faixa?._id ?: idFaixa
        ?: throw IllegalArgumentException("É necessário fornecer uma Faixa ou um idFaixa")
        return defesaPessoalRepository.findByFaixa(faixaId)
    }

    // Função para buscar Katas por Faixa ou ID de Faixa
    suspend fun findKatasByFaixa(faixa: Faixa? = null, idFaixa: String? = null): Flow<List<Kata>> {
        val faixaId = faixa?._id ?: idFaixa?: throw IllegalArgumentException("É necessário fornecer uma Faixa ou um idFaixa")
        return kataRepository.findByFaixa(faixaId)
    }

    // Função para buscar Sequências de Combate por Faixa ou ID de Faixa
    suspend fun findSequenciasDeCombateByFaixa(
        faixa: Faixa? = null,
        idFaixa: String? = null
    ): Flow<List<SequenciaDeCombate>> {
        val faixaId = faixa?._id ?: idFaixa
        ?: throw IllegalArgumentException("É necessário fornecer uma Faixa ou um idFaixa")
        return sequenciaDeCombateRepository.findByFaixa(faixaId)
    }

}

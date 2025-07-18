package br.com.shubudo.repositories

import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.model.DefesaPessoalExtraBanner
import br.com.shubudo.model.Faixa
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Movimento
import br.com.shubudo.model.Programacao
import br.com.shubudo.model.Projecao
import br.com.shubudo.model.SequenciaDeCombate
import br.com.shubudo.model.Armamento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProgramacaoRepository @Inject constructor(
    private val faixaRepository: FaixaRepository,
    private val defesaPessoalRepository: DefesaPessoalRepository,
    private val kataRepository: KataRepository,
    private val movimentoRepository: MovimentoRepository,
    private val sequenciaDeCombateRepository: SequenciaDeCombateRepository,
    private val projecaoRepository: ProjecoesRepository,
    private val defesaPessoalExtraBannerRepository: DefesaPessoalExtraBannerRepository,
    private val armamentoRepository: ArmamentoRepository,
    private val defesaArmaRepository: DefesaDeArmaRepository // Novo repositório para defesas de armas
) {
    suspend fun findMovimentoByFaixaETipo(faixa: String, tipo: String): Flow<List<Movimento>> {
        val faixaResult = faixaRepository.findByNome(faixa).firstOrNull()
        if (faixaResult == null) {
            return flow { emit(emptyList()) } // Retorna uma lista vazia se não encontrar a faixa
        }
        return when (tipo) {
            "Ataques de Mão" -> findAtaquesDeMaoByFaixa(faixaResult)
            "Chutes" -> findChutesByFaixa(faixaResult)
            "Defesas" -> findDefesasByFaixa(faixaResult)
            else -> flow { emit(emptyList()) }
        }
    }

    suspend fun findFaixaByCor(cor: String): Flow<Faixa> {
        return faixaRepository.findByNome(cor).filterNotNull()
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
        val defesaPessoalExtraBanner = findDefesaPessoalExtraBannerByFaixa(faixa).first()
        val projecao = findProjecoesByFaixa(faixa).first()
        val armamentos = findArmamentosByFaixa(faixa).first()
        val defesasDeArma = findDefesasDeArmasByFaixa(faixa).first() // Nova chamada

        val programacao = Programacao(
            faixa = faixa,
            defesaPessoal = defesasPessoais,
            katas = katas,
            ataquesDeMao = ataquesDeMao,
            chutes = chutes,
            defesas = defesas,
            sequenciaDeCombate = sequenciasDeCombate,
            projecoes = projecao,
            defesaExtraBanner = defesaPessoalExtraBanner,
            armamento = armamentos,
            defesasDeArma = defesasDeArma // Atribuição das defesas de arma
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
        val faixaId = faixa?._id ?: idFaixa
        ?: throw IllegalArgumentException("É necessário fornecer uma Faixa ou um idFaixa")
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

    suspend fun findProjecoesByFaixa(
        faixa: Faixa? = null,
        idFaixa: String? = null
    ): Flow<List<Projecao>> {
        val faixaId = faixa?._id ?: idFaixa
        ?: throw IllegalArgumentException("É necessário fornecer uma Faixa ou um idFaixa")
        return projecaoRepository.findByFaixa(faixaId)
    }

    suspend fun findDefesaPessoalExtraBannerByFaixa(
        faixa: Faixa? = null,
        idFaixa: String? = null
    ): Flow<List<DefesaPessoalExtraBanner>> {
        val faixaId = faixa?._id ?: idFaixa
        ?: throw IllegalArgumentException("É necessário fornecer uma Faixa ou um idFaixa")
        return defesaPessoalExtraBannerRepository.findByFaixa(faixaId)
    }

    // Função para buscar Armamentos por Faixa ou ID de Faixa
    suspend fun findArmamentosByFaixa(
        faixa: Faixa? = null,
        idFaixa: String? = null
    ): Flow<List<Armamento>> {
        val faixaId = faixa?._id ?: idFaixa
        ?: throw IllegalArgumentException("É necessário fornecer uma Faixa ou um idFaixa")
        return armamentoRepository.findByFaixa(faixaId)
    }

    // Função para buscar Defesas de Armas por Faixa ou ID de Faixa
    suspend fun findDefesasDeArmasByFaixa(
        faixa: Faixa? = null,
        idFaixa: String? = null
    ): Flow<List<Armamento>> {
        val faixaId = faixa?._id ?: idFaixa
        ?: throw IllegalArgumentException("É necessário fornecer uma Faixa ou um idFaixa")
        return defesaArmaRepository.findByFaixa(faixaId)
    }
}

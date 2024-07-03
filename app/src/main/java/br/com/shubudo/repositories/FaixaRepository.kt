package br.com.shubudo.repositories

import kotlin.collections.List;

import kotlinx.coroutines.flow.Flow;
import br.com.shubudo.model.Faixa;
import br.com.shubudo.network.services.FaixasServices;
import javax.inject.Inject;

class FaixaRepository @Inject constructor(private val service: FaixasServices) {

//    suspend fun findAll(): Flow<List<Faixa>> {
//       try {
//           service.getFaixas()
//       } catch (e: Exception) {
//           throw e
//       }
//
//    }
}

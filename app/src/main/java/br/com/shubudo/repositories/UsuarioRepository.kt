package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.UsuarioDao
import br.com.shubudo.database.entities.toUsuario
import br.com.shubudo.model.Usuario
import br.com.shubudo.network.services.UsuarioService
import br.com.shubudo.network.services.toUsuarioEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject

import kotlin.coroutines.coroutineContext

class UsuarioRepository @Inject constructor(
    private val service: UsuarioService,
    private val dao: UsuarioDao
) {
    suspend fun getUsuario(): Flow<Usuario?> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getUsuario()
                val entity = response?.toUsuarioEntity()
                entity?.let { dao.salvarUsuario(it) }
            } catch (e: ConnectException) {
                Log.e("UsuarioRepository", "getUsuario: falha ao conectar na API", e)
            }
        }

        return dao.getUsuario().map { it?.toUsuario() }
    }

    suspend fun logout() {
        try {
            service.logout() // Envia requisição de logout para a API, se aplicável
            dao.clear() // Limpa o usuário do banco local
        } catch (e: ConnectException) {
            Log.e("UsuarioRepository", "logout: falha ao conectar na API", e)
        }
    }
}

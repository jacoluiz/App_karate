package br.com.shubudo

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import br.com.shubudo.model.Usuario
import br.com.shubudo.utils.getFcmToken

object SessionManager {
    var usuarioLogado by mutableStateOf<Usuario?>(null)
        private set

    var perfilAtivo by mutableStateOf("aluno")
    var idAcademiaVisualizacao by mutableStateOf("")

    fun alternarPerfil(novoPerfil: String, novaAcademiaId: String? = null) {
        if (usuarioLogado?.perfis?.contains(novoPerfil) == true) {
            perfilAtivo = novoPerfil
            idAcademiaVisualizacao = novaAcademiaId ?: when (novoPerfil) {
                "aluno" -> usuarioLogado?.academiaId.orEmpty()
                "professor" -> usuarioLogado?.professorEm?.firstOrNull().orEmpty()
                else -> ""
            }
            Log.d("Perfil", "$idAcademiaVisualizacao  -  $perfilAtivo")
        }
    }

    fun limparSessao(context: Context) {
        usuarioLogado = null
        idAcademiaVisualizacao = ""
        perfilAtivo = "aluno"
        val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        prefs.edit { clear() }
    }

    fun inicializar(context: Context) {
        Log.d("MainActivity", "Inicializando SessionManager")
        val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        Log.d("MainActivity", "Carregando dados do usuário logado")
        with(prefs) {
            val _id = getString("_id", null)
            val nome = getString("nome", null)
            val username = getString("username", null)
            val email = getString("email", null)
            val peso = getString("peso", "")
            val altura = getString("altura", "")
            val idade = getString("idade", "")
            val perfis = getString("perfis", "aluno")?.split(",") ?: listOf("aluno")
            val corFaixa = getString("corFaixa", null)
            val status = getString("status", "ativo")
            val dan = getInt("dan", 0)
            val academiaId = getString("academiaId", "")
            val filialId = getString("filialId", "")
            val tamanhoFaixa = getString("tamanhoFaixa", "")
            val lesaoOuLaudosMedicos = getString("lesaoOuLaudosMedicos", "")
            val registroAKSD = getString("registroAKSD", "")
            val fcmToken = getString("fcmToken", getFcmToken(context))
            val professorEm =
                getString("professorEm", "")?.split(",")?.filter { it.isNotBlank() } ?: emptyList()

            Log.d("MainActivity", "Dados carregados: _id=$_id, nome=$nome, email=$email")

            Log.d("MainActivity", "${!nome.isNullOrBlank() && !email.isNullOrBlank()}")
            if (!nome.isNullOrBlank() && !email.isNullOrBlank()) {
                Log.d("MainActivity", "Usuário logado: $nome")
                usuarioLogado = Usuario(
                    _id = _id,
                    nome = nome,
                    username = username ?: "",
                    email = email,
                    peso = peso ?: "",
                    altura = altura ?: "",
                    idade = idade ?: "",
                    perfis = perfis,
                    corFaixa = corFaixa ?: "",
                    status = status ?: "ativo",
                    dan = dan,
                    academiaId = academiaId ?: "",
                    filialId = filialId ?: "",
                    tamanhoFaixa = tamanhoFaixa ?: "",
                    lesaoOuLaudosMedicos = lesaoOuLaudosMedicos ?: "",
                    registroAKSD = registroAKSD ?: "",
                    fcmToken = fcmToken ?: getFcmToken(context),
                    professorEm = professorEm
                )
            }
            Log.d("MainActivity", "Usuário logado: $usuarioLogado")
        }
        idAcademiaVisualizacao = usuarioLogado?.academiaId ?: ""
        Log.d("MainActivity", "usuarioLogado?.academiaId: ${usuarioLogado?.academiaId}")

        Log.d("MainActivity", "idAcademiaVisualizacao: $idAcademiaVisualizacao")
    }

    fun salvarUsuario(context: Context, usuario: Usuario) {
        Log.d("MainActivity", "usuario login: $usuario")

        val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        prefs.edit {
            putString("_id", usuario._id)
            putString("nome", usuario.nome)
            putString("email", usuario.email)
            putString("username", usuario.username)
            putString("corFaixa", usuario.corFaixa)
            putString("status", usuario.status)
            putInt("dan", usuario.dan)
            putString("academiaId", usuario.academiaId)
            putString("filialId", usuario.filialId)
            putString("tamanhoFaixa", usuario.tamanhoFaixa)
            putString("lesaoOuLaudosMedicos", usuario.lesaoOuLaudosMedicos)
            putString("registroAKSD", usuario.registroAKSD)
            putString("fcmToken", usuario.fcmToken)
            putString("professorEm", usuario.professorEm.joinToString(","))
        }
        if (idAcademiaVisualizacao.isEmpty()) {
            idAcademiaVisualizacao = usuarioLogado?.academiaId ?: ""
        }

        usuarioLogado = usuario
        Log.d("MainActivity", "usuarioLogado: $usuarioLogado")

    }
}

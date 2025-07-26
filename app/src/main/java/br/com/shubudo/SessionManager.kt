package br.com.shubudo

import android.content.Context
import br.com.shubudo.model.Usuario
import br.com.shubudo.utils.getFcmToken

object SessionManager {
    var usuarioLogado: Usuario? = null

    fun inicializar(context: Context) {
        val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
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
            val academia = getString("academia", "")
            val tamanhoFaixa = getString("tamanhoFaixa", "")
            val lesaoOuLaudosMedicos = getString("lesaoOuLaudosMedicos", "")
            val registroAKSD = getString("registroAKSD", "")
            val fcmToken = getString("fcmToken", getFcmToken(context))

            if (!nome.isNullOrBlank() && !corFaixa.isNullOrBlank() && !username.isNullOrBlank()) {
                usuarioLogado = Usuario(
                    _id = _id,
                    nome = nome,
                    username = username,
                    email = email ?: "",
                    peso = peso ?: "",
                    altura = altura ?: "",
                    idade = idade ?: "",
                    perfis = perfis,
                    corFaixa = corFaixa,
                    status = status ?: "ativo",
                    dan = dan,
                    academia = academia ?: "",
                    tamanhoFaixa = tamanhoFaixa ?: "",
                    lesaoOuLaudosMedicos = lesaoOuLaudosMedicos ?: "",
                    registroAKSD = registroAKSD ?: "",
                    fcmToken = fcmToken ?: getFcmToken(context)
                )
            }
        }
    }

}

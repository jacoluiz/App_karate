package br.com.shubudo

import android.content.Context
import br.com.shubudo.model.Usuario

object SessionManager {
    var usuarioLogado: Usuario? = null

    fun inicializar(context: Context) {
        val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        val nome = prefs.getString("nome", null)
        val faixa = prefs.getString("corFaixa", null)
        val username = prefs.getString("username", null)
        val email = prefs.getString("email", null)
        val idade = prefs.getString("idade", null)
        val peso = prefs.getString("peso", null)
        val altura = prefs.getString("altura", null)

        if (nome != null && faixa != null && username != null) {
            usuarioLogado = Usuario(
                nome = nome,
                corFaixa = faixa,
                username = username,
                email = email ?: "",
                idade = idade ?: "",
                peso = peso ?: "",
                altura = altura ?: ""
            )
        }
    }
}


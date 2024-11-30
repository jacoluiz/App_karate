package br.com.shubudo.network.services

import br.com.shubudo.database.entities.UsuarioEntity
import br.com.shubudo.model.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class UsuarioResponse(
    val _id: String,
    val nome: String,
    val username: String,
    val email: String,
    val senha: String,
    val peso: String?,
    val altura: String?,
    val idade: String?,
    val perfil: String,
    val corFaixa: String
)

fun UsuarioResponse.toUsuario(): Usuario {
    return Usuario(
        _id = _id ?: "",
        nome = nome ?: "Nome não informado",
        username = username ?: "Usuário não informado",
        email = email ?: "Email não informado",
        senha = senha ?: "",
        peso = peso ?: "",
        altura = altura ?: "",
        idade = idade ?: "",
        perfil = perfil ?: "Básico",
        corFaixa = corFaixa ?: "Sem faixa"
    )
}


fun UsuarioResponse.toUsuarioEntity(): UsuarioEntity {
    return UsuarioEntity(
        _id = _id ?: "",
        nome = nome ?: "Nome não informado",
        username = username ?: "Usuário não informado",
        email = email ?: "Email não informado",
        senha = senha ?: "",
        peso = peso ?: "",
        altura = altura ?: "",
        idade = idade ?: "",
        perfil = perfil ?: "Básico",
        corFaixa = corFaixa ?: "Sem faixa"
    )
}

interface UsuarioService {

    @GET("usuarios") // Endpoint para obter o usuário logado
    suspend fun getUsuario(): List<UsuarioResponse>

    @POST("/usuarios/login") // Endpoint para login
    suspend fun login(@Body credentials: Map<String, String>): UsuarioResponse

    @POST("logout") // Endpoint para logout
    suspend fun logout()
}

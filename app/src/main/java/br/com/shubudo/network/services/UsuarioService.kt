package br.com.shubudo.network.services

import br.com.shubudo.database.entities.UsuarioEntity
import br.com.shubudo.model.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginResponse(
    val message: String,
    val token: String,
    val usuario: UsuarioResponse
)

data class UsuarioResponse(
    val _id: String,
    val nome: String,
    val username: String,
    val email: String,
    val peso: String?,
    val altura: String?,
    val idade: String?,
    val perfil: String,
    val corFaixa: String,
    val status: String?,
    val dan: Int?,
    val academia: String?,
    val tamanhoFaixa: String?,
    val lesaoOuLaudosMedicos: String? = null,
    val registroAKSD: String? = null,
)

fun UsuarioResponse.toUsuario(): Usuario {
    return Usuario(
        _id = _id,
        nome = nome,
        username = username,
        email = email,
        peso = peso ?: "",
        altura = altura ?: "",
        idade = idade ?: "",
        perfil = perfil,
        corFaixa = corFaixa,
        status = status ?: "ativo",
        dan = dan ?: 0,
        academia = academia ?: "",
        tamanhoFaixa = tamanhoFaixa ?: "",
        lesaoOuLaudosMedicos = lesaoOuLaudosMedicos ?: "",
        registroAKSD = registroAKSD ?: "",
    )
}


fun UsuarioResponse.toUsuarioEntity(): UsuarioEntity {
    return UsuarioEntity(
        _id = _id,
        nome = nome,
        username = username,
        email = email,
        peso = peso ?: "",
        altura = altura ?: "",
        idade = idade ?: "",
        perfil = perfil,
        corFaixa = corFaixa,
        status = status ?: "ativo",
        dan = dan ?: 0,
        academia = academia ?: "",
        tamanhoFaixa = tamanhoFaixa ?: "",
        lesaoOuLaudosMedicos = lesaoOuLaudosMedicos ?: "",
        registroAKSD = registroAKSD ?: "",
    )
}

interface UsuarioService {

    @GET("/usuarios")
    suspend fun getUsuarios(): List<UsuarioResponse>

    @POST("/usuarios") // Endpoint para obter o usuário logado
    suspend fun criarUsuarios(@Body usuario: Usuario): UsuarioResponse

    @POST("/usuarios/login") // Endpoint para login
    suspend fun login(@Body credentials: Map<String, String>): LoginResponse

    @PUT("usuarios/{id}")
    suspend fun atualizarUsuario(
        @Path("id") id: String,
        @Body usuario: Usuario
    ): UsuarioResponse?
}
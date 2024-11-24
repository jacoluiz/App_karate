package br.com.shubudo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.shubudo.database.Convertes
import br.com.shubudo.model.Usuario

@Entity(tableName = "Usuario")
@TypeConverters(Convertes::class)
data class UsuarioEntity(
    @PrimaryKey
    val _id: String,
    val nome: String,
    val username: String,
    val email: String,
    val senha: String,
    val peso: String,
    val altura: String,
    val idade: String,
    val perfil: String = "básico",
    val corFaixa: String
)

// Conversão de UsuarioEntity para Usuario
fun UsuarioEntity.toUsuario() = Usuario(
    _id = _id,
    nome = nome,
    username = username,
    email = email,
    senha = senha,
    peso = peso,
    altura = altura,
    idade = idade,
    perfil = perfil,
    corFaixa = corFaixa
)

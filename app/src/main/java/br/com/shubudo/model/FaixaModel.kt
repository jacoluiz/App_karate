package br.com.shubudo.model

import java.util.UUID

data class Faixa(val id : String = UUID.randomUUID().toString(),
                 val faixa : String,
                 val ordem : Int,
                 val dan : Int)

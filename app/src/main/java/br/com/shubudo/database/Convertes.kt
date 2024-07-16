package br.com.shubudo.database

import androidx.room.TypeConverter
import br.com.shubudo.model.Movimento
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Convertes {
    @TypeConverter
    fun fromObservacaoList(observacao: List<String>): String {
        return Gson().toJson(observacao)
    }

    @TypeConverter
    fun toObservacaoList(observacaoString: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(observacaoString, listType)
    }

    @TypeConverter
    fun fromMovimentoList(movimentos: List<Movimento>): String {
        return Gson().toJson(movimentos)
    }

    @TypeConverter
    fun toMovimentoList(movimentoString: String): List<Movimento> {
        val listType = object : TypeToken<List<Movimento>>() {}.type
        return Gson().fromJson(movimentoString, listType)
    }
}
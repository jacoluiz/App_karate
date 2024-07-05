package br.com.shubudo.database

import androidx.room.TypeConverter
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
}
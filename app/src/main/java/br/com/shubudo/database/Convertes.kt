package br.com.shubudo.database

import androidx.room.TypeConverter
import br.com.shubudo.model.Filial
import br.com.shubudo.model.Movimento
import br.com.shubudo.model.TempoVideo
import br.com.shubudo.model.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Convertes {

    @TypeConverter
    fun fromFilialList(value: List<Filial>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toFilialList(value: String): List<Filial> {
        val type = object : TypeToken<List<Filial>>() {}.type
        return Gson().fromJson(value, type)
    }

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

    @TypeConverter
    fun fromVideoList(videoList: List<Video>): String {
        return Gson().toJson(videoList)
    }

    @TypeConverter
    fun toVideoList(videoListString: String): List<Video> {
        val listType = object : TypeToken<List<Video>>() {}.type
        return Gson().fromJson(videoListString, listType)
    }

    @TypeConverter
    fun fromTempoVideoList(tempoVideoModelList: List<TempoVideo>): String {
        return Gson().toJson(tempoVideoModelList)
    }

    @TypeConverter
    fun toTempoVideoList(tempoVideoListString: String): List<TempoVideo> {
        val listType = object : TypeToken<List<TempoVideo>>() {}.type
        return Gson().fromJson(tempoVideoListString, listType)
    }

}
package br.com.shubudo.utils

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class DateValidation(
    val isValid: Boolean,
    val message: String,
    val age: Int? = null
)

fun String.toLocalDateTimeOrNull(): LocalDateTime? {
    return try {
        LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: Exception) {
        null
    }
}

// Função para aplicar máscara de altura no formato x,xx
fun applyHeightMask(input: String, previousValue: String = ""): Pair<String, Int> {
    // Remove qualquer caractere que não seja dígito ou vírgula
    val digitsOnly = input.filter { it.isDigit() || it == ',' }

    // Se estiver vazio, retorna vazio
    if (digitsOnly.isEmpty()) return Pair("", 0)

    // Remove vírgulas duplicadas ou no início
    val cleanInput = digitsOnly.replace(Regex(",+"), ",").removePrefix(",")

    // Se só tem vírgula, retorna vazio
    if (cleanInput == ",") return Pair("", 0)

    // Separa por vírgula
    val parts = cleanInput.split(",")

    val result = when {
        // Se não tem vírgula e tem 1 dígito
        parts.size == 1 && parts[0].length <= 1 -> parts[0]

        // Se não tem vírgula e tem mais de 1 dígito, adiciona vírgula automaticamente
        parts.size == 1 && parts[0].length > 1 -> {
            val digits = parts[0]
            if (digits.length >= 3) {
                // Limita a 3 dígitos e formata como x,xx
                "${digits[0]},${digits.substring(1, 3)}"
            } else {
                "${digits[0]},${digits.substring(1)}"
            }
        }

        // Se já tem vírgula
        parts.size == 2 -> {
            val beforeComma = parts[0].take(1) // Máximo 1 dígito antes da vírgula
            val afterComma = parts[1].take(2)  // Máximo 2 dígitos depois da vírgula

            if (beforeComma.isEmpty()) {
                if (afterComma.isEmpty()) "" else ",${afterComma}"
            } else {
                "${beforeComma},${afterComma}"
            }
        }

        // Casos com múltiplas vírgulas - pega apenas a primeira parte válida
        else -> {
            val firstPart = parts[0].take(1)
            val secondPart = if (parts.size > 1) parts[1].take(2) else ""
            if (firstPart.isEmpty()) secondPart else "${firstPart},${secondPart}"
        }
    }

    // Calcula a nova posição do cursor
    val cursorPosition = when {
        // Se a vírgula foi inserida automaticamente, posiciona o cursor no final
        result.length > previousValue.length && result.contains(",") && !previousValue.contains(",") -> {
            result.length
        }
        // Caso contrário, posiciona no final
        else -> result.length
    }

    return Pair(result, cursorPosition)
}

// Função para validar altura
fun validateHeight(height: String): Boolean {
    if (height.isEmpty()) return true // Campo vazio é válido (opcional)

    // Regex para validar formato x,xx onde x são dígitos
    val heightRegex = Regex("^[0-2]$|^[0-2],[0-9]{1,2}$")

    if (!heightRegex.matches(height)) return false

    // Converte para float para validação adicional
    val heightValue = height.replace(",", ".").toFloatOrNull() ?: return false

    // Valida range realístico (0.5m a 2.5m)
    return heightValue in 0.5f..2.5f
}

// Função para validar data de nascimento
fun validateBirthDate(dateString: String): DateValidation {
    if (dateString.isEmpty()) {
        return DateValidation(false, "Data de nascimento é obrigatória")
    }

    // Se for apenas um número (idade antiga do banco), considera como válido mas pede para atualizar
    if (dateString.matches(Regex("^\\d{1,3}$"))) {
        val age = dateString.toIntOrNull()
        return if (age != null && age in 5..120) {
            DateValidation(true, "Idade: $age anos - Atualize para data completa", age)
        } else {
            DateValidation(false, "Idade inválida - Digite uma data de nascimento")
        }
    }

    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        formatter.isLenient = false // Não permite datas inválidas como 31/02/2023
        val birthDate = formatter.parse(dateString) ?: return DateValidation(false, "Data inválida")

        val birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()

        // Verifica se a data não é futura
        if (birthLocalDate.isAfter(today)) {
            return DateValidation(false, "Data não pode ser futura")
        }

        // Calcula a idade
        val age = Period.between(birthLocalDate, today).years

        // Verifica limites de idade
        when {
            age < 5 -> DateValidation(false, "Idade mínima: 5 anos")
            age > 120 -> DateValidation(false, "Idade máxima: 120 anos")
            else -> DateValidation(true, "$age anos", age)
        }
    } catch (e: Exception) {
        DateValidation(false, "Formato de data inválido")
    }
}

// Função para verificar se deve mostrar o campo Dan
fun shouldShowDan(corFaixa: String): Boolean {
    return corFaixa in listOf("Preta", "Mestre", "Grão Mestre")
}

// Função para obter as opções de Dan baseado na faixa
fun getDanOptions(corFaixa: String): List<Int> {
    return when (corFaixa) {
        "Preta" -> (0..4).toList()
        "Mestre" -> (5..9).toList()
        "Grão Mestre" -> (10..12).toList()
        else -> emptyList()
    }
}

fun applyDateMask(input: TextFieldValue): TextFieldValue {
    val digits = input.text.filter { it.isDigit() }
    val limitedDigits = digits.take(8)

    // Validar e corrigir os dígitos conforme são inseridos
    val validatedDigits = when (limitedDigits.length) {
        0 -> ""
        1 -> {
            // Primeiro dígito do dia: máximo 3
            if (limitedDigits[0].digitToInt() > 3) "3" else limitedDigits
        }

        2 -> {
            // Dia completo: máximo 31
            val day = limitedDigits.toInt()
            when {
                day == 0 -> "01"
                day > 31 -> "31"
                else -> limitedDigits
            }
        }

        3 -> {
            // Primeiro dígito do mês: máximo 1
            val day = limitedDigits.substring(0, 2).toInt()
            val firstMonthDigit = limitedDigits[2].digitToInt()
            val validDay = when {
                day == 0 -> "01"
                day > 31 -> "31"
                else -> limitedDigits.substring(0, 2)
            }
            val validFirstMonthDigit = if (firstMonthDigit > 1) "1" else firstMonthDigit.toString()
            validDay + validFirstMonthDigit
        }

        4 -> {
            // Mês completo: máximo 12
            val day = limitedDigits.substring(0, 2).toInt()
            val month = limitedDigits.substring(2, 4).toInt()
            val validDay = when {
                day == 0 -> "01"
                day > 31 -> "31"
                else -> limitedDigits.substring(0, 2)
            }
            val validMonth = when {
                month == 0 -> "01"
                month > 12 -> "12"
                else -> limitedDigits.substring(2, 4)
            }
            validDay + validMonth
        }

        else -> {
            // 5-8 dígitos: validar dia e mês, manter ano
            val day = limitedDigits.substring(0, 2).toInt()
            val month = limitedDigits.substring(2, 4).toInt()
            val year = limitedDigits.substring(4)

            val validDay = when {
                day == 0 -> "01"
                day > 31 -> "31"
                else -> limitedDigits.substring(0, 2)
            }
            val validMonth = when {
                month == 0 -> "01"
                month > 12 -> "12"
                else -> limitedDigits.substring(2, 4)
            }
            validDay + validMonth + year
        }
    }

    val maskedText = when (validatedDigits.length) {
        0 -> ""
        1 -> validatedDigits
        2 -> validatedDigits
        3 -> "${validatedDigits.substring(0, 2)}/${validatedDigits[2]}"
        4 -> "${validatedDigits.substring(0, 2)}/${validatedDigits.substring(2, 4)}"
        5 -> "${validatedDigits.substring(0, 2)}/${
            validatedDigits.substring(
                2,
                4
            )
        }/${validatedDigits[4]}"

        6 -> "${validatedDigits.substring(0, 2)}/${
            validatedDigits.substring(
                2,
                4
            )
        }/${validatedDigits.substring(4, 6)}"

        7 -> "${validatedDigits.substring(0, 2)}/${
            validatedDigits.substring(
                2,
                4
            )
        }/${validatedDigits.substring(4, 7)}"

        8 -> "${validatedDigits.substring(0, 2)}/${
            validatedDigits.substring(
                2,
                4
            )
        }/${validatedDigits.substring(4, 8)}"

        else -> validatedDigits.substring(0, 2) + "/" + validatedDigits.substring(
            2,
            4
        ) + "/" + validatedDigits.substring(4, 8)
    }

    return TextFieldValue(maskedText, TextRange(maskedText.length))
}

fun isValidDate(dateString: String): Boolean {
    if (dateString.length != 10) return false

    val parts = dateString.split("/")
    if (parts.size != 3) return false

    return try {
        val day = parts[0].toInt()
        val month = parts[1].toInt()
        val year = parts[2].toInt()

        // Validações básicas
        if (day < 1 || day > 31) return false
        if (month < 1 || month > 12) return false
        if (year < 1900 || year > 2024) return false

        // Validação específica por mês
        val daysInMonth = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 0
        }

        day <= daysInMonth
    } catch (e: NumberFormatException) {
        false
    }
}

fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

fun applyShiftedMask(input: TextFieldValue): TextFieldValue {
    val digits = input.text.filter { it.isDigit() }
    val limitedDigits = digits.takeLast(3)

    val maskedText = when (limitedDigits.length) {
        1 -> "0,0${limitedDigits[0]}"
        2 -> "0,$limitedDigits"
        3 -> "${limitedDigits[0]},${limitedDigits.substring(1)}"
        else -> "0,00"
    }

    return TextFieldValue(maskedText, TextRange(maskedText.length))
}

fun calcularIdade(dataNascimento: String): Int {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("pt", "BR"))
        val nascimento = LocalDate.parse(dataNascimento, formatter)
        val hoje = LocalDate.now()
        Period.between(nascimento, hoje).years
    } catch (e: Exception) {
        -1 // idade inválida
    }
}

// Função para formatar data para exibição
fun formatDateForDisplay(dateString: String): String {
    if (dateString.isEmpty()) return ""

    // Se for apenas um número (idade), retorna vazio para permitir edição
    if (dateString.matches(Regex("^\\d{1,3}$"))) {
        return ""
    }

    // Se já está no formato dd/MM/yyyy, retorna como está
    if (dateString.matches(Regex("^\\d{2}/\\d{2}/\\d{4}$"))) {
        return dateString
    }

    // Se está no formato ISO (yyyy-MM-dd), converte para dd/MM/yyyy
    if (dateString.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))) {
        return try {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale("pt", "BR"))
            val outputFormatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
            val date = inputFormatter.parse(dateString)
            date?.let { outputFormatter.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    return dateString
}
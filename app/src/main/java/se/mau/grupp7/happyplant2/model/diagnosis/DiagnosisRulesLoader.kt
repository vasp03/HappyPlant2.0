package se.mau.grupp7.happyplant2.model.diagnosis

import android.content.Context
import com.google.gson.Gson
import java.io.IOException

class DiagnosisRulesLoader(
    private val context: Context,
    private val gson: Gson = Gson()
) {
    @Throws(IOException::class)
    fun load(): DiagnosisRulesFile {
        val json = context.assets.open(FILE_NAME)
            .bufferedReader()
            .use { it.readText() }

        return gson.fromJson(json, DiagnosisRulesFile::class.java)
    }

    private companion object {
        const val FILE_NAME = "diagnosis_rules.json"
    }
}
package se.mau.grupp7.happyplant2.model.diagnosis


class DiagnosisEngine {

    /**
     * Matches selected symptoms against all rules.
     * Returns results sorted by highest score first
     */
    fun diagnose(
        rules: List<DiagnosisRule>,
        selectedSymptoms: List<String>
    ): List<DiagnosisResult> {
        val selected = selectedSymptoms
            .map { normalize(it) }
            .toSet()

        return rules.mapNotNull { rule ->
            val ruleSymptoms = rule.symptoms.map { normalize(it) }
            val matched = ruleSymptoms.filter { it in selected }

            if (matched.isEmpty()) null
            else DiagnosisResult(
                rule = rule,
                score = matched.size,
                matchedSymptoms = matched
            )
        }.sortedByDescending { it.score }
    }

    private fun normalize(s: String): String =
        s.trim().lowercase()
}
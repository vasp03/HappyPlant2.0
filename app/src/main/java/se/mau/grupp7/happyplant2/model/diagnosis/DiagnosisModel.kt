package se.mau.grupp7.happyplant2.model.diagnosis

data class DiagnosisRule(
    val id: String,
    val name: String,
    val symptoms: List<String>,
    val tips: List<String>
)

data class DiagnosisRulesFile(
    val rules: List<DiagnosisRule>
)

/**
 * After matching selected symptoms that's "against the rules", a result is returned
 * score = number of matched symptoms for that rule.
 */
data class DiagnosisResult(
    val rule: DiagnosisRule,
    val score: Int,
    val matchedSymptoms: List<String>
)
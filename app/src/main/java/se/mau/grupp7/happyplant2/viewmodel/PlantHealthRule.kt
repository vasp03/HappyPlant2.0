package se.mau.grupp7.happyplant2.viewmodel

private const val MAX_HEALTH = 5

internal fun calculateNewHealth(healthImpact: Int): Int {
    return (MAX_HEALTH + healthImpact).coerceIn(0, MAX_HEALTH)
}
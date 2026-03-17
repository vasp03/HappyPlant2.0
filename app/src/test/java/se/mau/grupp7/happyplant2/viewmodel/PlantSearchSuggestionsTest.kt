package se.mau.grupp7.happyplant2.viewmodel

import org.junit.Assert
import org.junit.Test

class PlantSearchSuggestionsTest {
    private val candidates = listOf("rosa", "rose", "lavender", "monstera", "cactus", "orchid")

    @Test
    fun boundedLevenshtein_caseInsensitive_exactMatch_isZero() {
        Assert.assertEquals(0, boundedLevenshtein("ROSE", "rose", max = 2))
    }

    @Test
    fun boundedLevenshtein_lengthDiffGreaterThanMax_returnsMaxPlusOne() {
        Assert.assertEquals(3, boundedLevenshtein("a", "abcdefgh", max = 2))
    }

    @Test
    fun boundedLevenshtein_emptyA_returnsLengthOfB() {
        Assert.assertEquals(4, boundedLevenshtein("", "rose", max = 10))
    }

    @Test
    fun boundedLevenshtein_emptyB_returnsLengthOfA() {
        Assert.assertEquals(4, boundedLevenshtein("rose", "", max = 10))
    }

    @Test
    fun boundedLevenshtein_oneSubstitution_isOne() {
        Assert.assertEquals(1, boundedLevenshtein("rose", "r0se", max = 2))
    }

    @Test
    fun boundedLevenshtein_rowMinExceedsMax_returnsMaxPlusOne() {
        Assert.assertEquals(2, boundedLevenshtein("rose", "zzzz", max = 1))
    }



    @Test
    fun suggestQuery_tooShort_returnsEmpty() {
        Assert.assertTrue(suggestQuery("ro", candidates, maxDistance = 2).isEmpty())
    }

    @Test
    fun suggestQuery_trimsAndLowercases() {
        Assert.assertEquals(listOf("rose"), suggestQuery("    ROSE  ", candidates, maxDistance = 0))
    }

    @Test
    fun suggestQuery_returnsCloseMatches() {
        val result = suggestQuery("ros", candidates, maxDistance = 1)
        Assert.assertTrue(result.contains("rosa"))
        Assert.assertTrue(result.contains("rose"))
    }

    @Test
    fun suggestQuery_noCloseMatches_returnsEmpty() {
        Assert.assertTrue(suggestQuery("zzzzzzxxx", candidates, maxDistance = 2).isEmpty())
    }

    @Test
    fun suggestQuery_limitsToFive() {
        val many = listOf("rose", "rosa", "roes", "rsoe", "rse", "rosee", "roze", "rxxe")
        val result = suggestQuery("rose", many, maxDistance = 2)
        Assert.assertTrue(result.size <= 5)
    }

    @Test
    fun suggestQuery_bestMatchFirst() {
        val result = suggestQuery("rose", listOf("roze", "rose", "ruse"), maxDistance = 2)
        Assert.assertEquals("rose", result.first())
    }
}
package se.mau.grupp7.happyplant2

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import se.mau.grupp7.happyplant2.model.Defect
import se.mau.grupp7.happyplant2.model.DefectList

class DefectTest {

    @Test
    fun defectClassTest() {
        val defect = Defect("test_id", "Test Defect", "Category", "SubCategory", -1)
        assertEquals("test_id", defect.id)
        assertEquals("Test Defect", defect.displayName)
        assertEquals("Category", defect.category)
        assertEquals("SubCategory", defect.subCategory)
        assertEquals(-1, defect.healthImpact)
    }

    @Test
    fun defectList_categories_notEmpty() {
        val categories = DefectList.categories
        assert(categories.isNotEmpty())
    }

    @Test
    fun defectList_subCategories_notEmpty() {
        val subCategories = DefectList.subCategories("Leaf Symptoms")
        assert(subCategories.isNotEmpty())
    }

    @Test
    fun defectList_subCategories_emptyForInvalidCategory() {
        val subCategories = DefectList.subCategories("Invalid Category")
        assert(subCategories.isEmpty())
    }

    @Test
    fun defectList_defects_notEmpty() {
        val defects = DefectList.defects("Leaf Symptoms", "Spots or Blights")
        assert(defects.isNotEmpty())
    }

    @Test
    fun defectList_defects_emptyForInvalidSubCategory() {
        val defects = DefectList.defects("Leaf Symptoms", "Invalid SubCategory")
        assert(defects.isEmpty())
    }

    @Test
    fun defectList_findById_returnsCorrectDefect() {
        val defect = DefectList.findById("grease_spot")
        assertEquals("grease_spot", defect.id)
        assertEquals("Grease spot (Pythium blight)", defect.displayName)
    }

    @Test
    fun defectList_findById_returnsNoneForInvalidId() {
        val defect = DefectList.findById("invalid_id")
        assertEquals(DefectList.NONE, defect)
    }

    @Test
    fun defectList_noneAndDeadArePresent() {
        assertNotEquals(null, DefectList.NONE)
        assertNotEquals(null, DefectList.DEAD)
        assertEquals("none", DefectList.NONE.id)
        assertEquals("DEAD", DefectList.DEAD.id)
    }

    @Test
    fun defectList_allIsNotEmpty() {
        assert(DefectList.ALL.isNotEmpty())
    }
}

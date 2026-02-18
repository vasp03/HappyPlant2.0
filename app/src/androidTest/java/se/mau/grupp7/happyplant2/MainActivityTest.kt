package se.mau.grupp7.happyplant2

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun bonsaiScreen_settingsAndCalendarButtons_areDisplayed() {
        composeTestRule.onNodeWithContentDescription("Settings", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Calendar", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun navigateToSearch(){
        composeTestRule.onNodeWithContentDescription("PlantSearch", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithContentDescription("PlantSearch", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun navigateToLanding(){
        composeTestRule.onNodeWithContentDescription("Home", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithContentDescription("Settings", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun navigateToUserPlantList(){
        composeTestRule.onNodeWithContentDescription("Your Plants", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithContentDescription("Your Plants", useUnmergedTree = true).assertIsDisplayed()
    }
}

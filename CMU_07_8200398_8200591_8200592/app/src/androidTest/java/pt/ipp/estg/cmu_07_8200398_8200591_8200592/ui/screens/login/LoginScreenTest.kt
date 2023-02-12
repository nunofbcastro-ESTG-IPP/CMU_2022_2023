package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.login

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R

class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun EmptyEntries() {
        // Start the app
        composeTestRule.setContent {
            LoginScreen(
                onComposing = {},
                onNavigateRegister = {},
                onNavigateHouses = {}
            )
        }

        composeTestRule.onNodeWithTag("errorLogin").assertDoesNotExist()
        composeTestRule.onNodeWithTag("loginButton").performClick()
        composeTestRule.onNodeWithTag("errorLogin").assertExists()
    }

    @Test
    fun EmptyPassword() {
        // Start the app
        composeTestRule.setContent {
            LoginScreen(
                onComposing = {},
                onNavigateRegister = {},
                onNavigateHouses = {}
            )
        }

        composeTestRule.onNodeWithTag("errorLogin").assertDoesNotExist()
        composeTestRule.onNodeWithTag("emailInput").performTextInput("teste@gmail.com")
        composeTestRule.onNodeWithTag("loginButton").performClick()
        composeTestRule.onNodeWithTag("errorLogin").assertExists()
    }

    @Test
    fun EmptyEmail() {
        // Start the app
        composeTestRule.setContent {
            LoginScreen(
                onComposing = {},
                onNavigateRegister = {},
                onNavigateHouses = {}
            )
        }

        composeTestRule.onNodeWithTag("errorLogin").assertDoesNotExist()
        composeTestRule.onNodeWithTag("passwordInput").performTextInput("Teste123!")
        composeTestRule.onNodeWithTag("loginButton").performClick()
        composeTestRule.onNodeWithTag("errorLogin").assertExists()
    }

    @Test
    fun InvalidEmail() {
        // Start the app
        composeTestRule.setContent {
            LoginScreen(
                onComposing = {},
                onNavigateRegister = {},
                onNavigateHouses = {}
            )
        }

        composeTestRule.onNodeWithTag("errorLogin").assertDoesNotExist()
        composeTestRule.onNodeWithTag("emailInput").performTextInput("teste")
        composeTestRule.onNodeWithTag("passwordInput").performTextInput("Teste123!")
        composeTestRule.onNodeWithTag("loginButton").performClick()
        composeTestRule.onNodeWithTag("errorLogin").assertExists()
    }

    @Test
    fun InvalidPassword() {
        // Start the app
        composeTestRule.setContent {
            LoginScreen(
                onComposing = {},
                onNavigateRegister = {},
                onNavigateHouses = {}
            )
        }

        composeTestRule.onNodeWithTag("errorLogin").assertDoesNotExist()
        composeTestRule.onNodeWithTag("emailInput").performTextInput("teste@gmail.com")
        composeTestRule.onNodeWithTag("passwordInput").performTextInput("teste")
        composeTestRule.onNodeWithTag("loginButton").performClick()
        composeTestRule.onNodeWithTag("errorLogin").assertExists()
    }

    @Test
    fun ValidLogin() {
        /*Notas:
            - O dispositivo tem de estar conectado a internet
            - O teste pode reprovar se a conecção estiver muito lenta
            - A password da conta teste@gmail.com tem de ser Teste123!
        */
        val context: Context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val loadingMessage = context.resources.getString(R.string.please_wait)

        composeTestRule.setContent {
            LoginScreen(
                onComposing = {},
                onNavigateRegister = {},
                onNavigateHouses = {}
            )
        }

        composeTestRule.onNodeWithTag("errorLogin").assertDoesNotExist()
        composeTestRule.onNodeWithTag("emailInput").performTextInput("teste@gmail.com")
        composeTestRule.onNodeWithTag("passwordInput").performTextInput("Teste123!")
        composeTestRule.onNodeWithTag("loginButton").performClick()
        composeTestRule.waitUntil(10000) {
            composeTestRule
                .onAllNodesWithText(loadingMessage)
                .fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.onNodeWithTag("errorLogin").assertDoesNotExist()
    }

    @Test
    fun InValidLogin() {
        val context: Context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val loadingMessage = context.resources.getString(R.string.please_wait)

        composeTestRule.setContent {
            LoginScreen(
                onComposing = {},
                onNavigateRegister = {},
                onNavigateHouses = {}
            )
        }

        composeTestRule.onNodeWithTag("errorLogin").assertDoesNotExist()
        composeTestRule.onNodeWithTag("emailInput").performTextInput("teste@gmail.com")
        composeTestRule.onNodeWithTag("passwordInput").performTextInput("Teste1234!")
        composeTestRule.onNodeWithTag("loginButton").performClick()
        composeTestRule.waitUntil(10000) {
            composeTestRule
                .onAllNodesWithText(loadingMessage)
                .fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.onNodeWithTag("errorLogin").assertExists()
    }
}
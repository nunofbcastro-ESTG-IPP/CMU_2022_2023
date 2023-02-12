package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.AppBarState
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.adddivision.AddDivisionScreen
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.login.LoginScreen
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.signup.SignUpScreen
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.settings.SettingsScreen
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.addhouse.AddHouseScreen
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.division_details.DivisionDetailsScreen
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.division_details.NavigationsSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.divisions.DivisionsScreens
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.divisions.NavigationsDivision
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.editdivision.EditDivisionScreen
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.edithouse.EditHouseScreen
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.houses.HousesScreen
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.houses.NavigationsHouses
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.maps.Maps
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.splash.NavigationsSpash
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.splash.SplashScreen

@Composable
fun NavHost(
    navController: NavHostController,
    onComposing: (AppBarState) -> Unit,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onComposing = onComposing,
                navigationsSpash = NavigationsSpash(
                    onNavigateToLogin = {
                        navController.popBackStack()
                        navController.navigate(Screen.Login.route)
                    },
                    onNavigateToHome = {
                        navController.popBackStack()
                        navController.navigate(Screen.Houses.route)
                    }
                )
            )
        }
        composable(route = Screen.Login.route) {
            LoginScreen(
                onComposing = onComposing,
                onNavigateRegister = {
                    navController.popBackStack()
                    navController.navigate(Screen.SignUp.route)
                },
                onNavigateHouses = {
                    navController.popBackStack()
                    navController.navigate(Screen.Houses.route)
                },
            )
        }
        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                onComposing = onComposing,
                onNavigateToLogin = {
                    navController.popBackStack()
                    navController.navigate(Screen.Login.route)
                },
                onNavigateHouses = {
                    navController.popBackStack()
                    navController.navigate(Screen.Houses.route)
                },
            )
        }
        composable(route = Screen.Houses.route) {
            HousesScreen(
                onComposing = onComposing,
                navigations = NavigationsHouses(
                    onNagivateToHouse = { idHouse ->
                        navController.navigate(Screen.Divisions.route + "/${idHouse}")
                    },
                    onNavigateToAddHouses = {
                        navController.navigate(Screen.AddHouse.route)
                    },
                )
            )
        }
        composable(route = Screen.AddHouse.route) {
            AddHouseScreen(
                onComposing = onComposing,
                onNavigateToBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.EditHouse.route + "/{houseId}") {
            EditHouseScreen(
                onComposing = onComposing,
                onNavigateToBack = {
                    navController.popBackStack()
                },
                idHouse = it.arguments?.getString("houseId")!!
            )
        }
        composable(route = Screen.Divisions.route + "/{houseId}") {
            DivisionsScreens(
                onComposing = onComposing,
                idHouse = it.arguments?.getString("houseId")!!,
                navigations = NavigationsDivision(
                    onNagivateToDivision = { idHouse, idDivision ->
                        navController.navigate(Screen.DivisionDetails.route + "/${idHouse}" + "/${idDivision}")
                    },
                    onNagivateToAddDivision = { idHouse ->
                        navController.navigate(Screen.AddDivision.route + "/${idHouse}")
                    },
                    onNagivateToEditHouse = { idHouse ->
                        navController.navigate(Screen.EditHouse.route + "/${idHouse}")
                    },
                    onNavigateToRemoveHouseBack = {
                        navController.popBackStack()
                    },
                    onNavigateToMap = { idHouse ->
                        navController.navigate(Screen.Maps.route + "/${idHouse}")
                    }
                )
            )
        }
        composable(route = Screen.DivisionDetails.route + "/{houseId}/{divisionId}") {
            DivisionDetailsScreen(
                onComposing = onComposing,
                idHouse = it.arguments?.getString("houseId")!!,
                idDivision = it.arguments?.getString("divisionId")!!,
                navigations = NavigationsSensor(
                    onNavigateToEditDivision = { idHouse, idDivision ->
                        navController.navigate(Screen.EditDivision.route + "/${idHouse}" + "/${idDivision}")
                    },
                    onNavigateToRemoveDivisionBack = {
                        navController.popBackStack()
                    }
                )

            )
        }
        composable(route = Screen.AddDivision.route + "/{houseId}") {
            AddDivisionScreen(
                onComposing = onComposing,
                onNavigateToBack = {
                    navController.popBackStack()
                },
                idHouse = it.arguments?.getString("houseId")!!,
            )
        }
        composable(route = Screen.EditDivision.route + "/{houseId}/{divisionId}") {
            EditDivisionScreen(
                onComposing = onComposing,
                onNavigateToBack = {
                    navController.popBackStack()
                },
                idDivision = it.arguments?.getString("divisionId")!!,
                idHouse = it.arguments?.getString("houseId")!!,
            )
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onComposing = onComposing,
            )
        }
        composable(route = Screen.Maps.route + "/{houseId}") {
            Maps(
                onComposing = onComposing,
                idHouse = it.arguments?.getString("houseId")!!,
            )
        }
    }
}

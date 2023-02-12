package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.navigation

sealed class Screen(val route: String) {
    object Splash: Screen("splash")
    object Login: Screen("login")
    object SignUp: Screen("signUp")
    object Houses: Screen("houses")
    object Settings: Screen("settings")
    object AddHouse: Screen("addhouse")
    object EditHouse: Screen("edithouse")
    object AddDivision: Screen("addDivision")
    object EditDivision: Screen("editdivision")
    object Divisions: Screen("divisions")
    object DivisionDetails: Screen("divisiondetails")
    object Maps: Screen("Maps")
}

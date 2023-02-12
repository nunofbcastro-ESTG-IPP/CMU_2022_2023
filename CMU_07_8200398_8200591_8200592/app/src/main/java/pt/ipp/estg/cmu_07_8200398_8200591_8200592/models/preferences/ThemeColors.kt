package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.preferences

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R

enum class ThemeColors(
    val value: Boolean?,
    val string: Int
) {
    SystemMode(null, R.string.system),
    LightMode(false, R.string.light),
    DarkMode(true, R.string.dark),
}
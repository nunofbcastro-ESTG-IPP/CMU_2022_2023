package pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.User

fun nameValidation(name: String): Boolean {
    return name.matches(Regex("^([a-zA-Z\\u00C0-\\u017F]+\\s?)+\$"))
}

fun houseAndDivisionNameVaidation(name: String): Boolean {
    return name.matches(Regex("^([a-zA-Z0-9\\u00C0-\\u017F]+\\s?)+\$"))
}

fun emailValidation(email: String): Boolean {
    return email.lowercase()
        .matches(Regex("^(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])\$"))
}

fun ipValidation(ip: String): Boolean {
    return ip.matches(Regex("^((1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\.?){3}(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])(:([1-9][0-9]*))?\$"))
}

fun checkUserExists(email: String, userList: List<User>?): Boolean {
    userList?.let { users ->
        for (i in users.indices) {
            if (users[i].email == email) {
                return true
            }
        }
    }

    return false
}

fun passwordValidation(
    password: String,
    minCharacters: Int = 8,
    numberLowercases: Int = 1,
    numberUppercases: Int = 1,
    numberNumbers: Int = 1,
    numberSpecialCharacters: Int = 1,
): Boolean {
    return password.matches(Regex("^(?=.*[a-z]{$numberLowercases,})(?=.*[A-Z]{$numberUppercases,})(?=.*\\d{$numberNumbers,})(?=.*[-._!\"`'#%&,:;<>=@{}~\\\$\\(\\)\\*\\+\\/\\\\\\?\\[\\]\\^\\|]{$numberSpecialCharacters,}).{$minCharacters,}\$"))
}

fun latitudeValidation(
    latitude: Double?
): Boolean {
    latitude?.let { lat ->
        if (lat < -90 || lat > 90) {
            return false
        }
        return true
    }
    return false
}

fun longitudeValidation(
    longitude: Double?
): Boolean {
    longitude?.let { lng ->
        if (lng < -180 || lng > 180) {
            return false
        }
        return true
    }
    return false
}
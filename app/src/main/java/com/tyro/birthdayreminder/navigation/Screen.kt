package com.tyro.birthdayreminder.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object Signup: Screen("signup")
    object AccountVerification: Screen("account_verification")
    object AddBirthDay: Screen("add_birthday")
    object BirthDayDetail: Screen("birthday_details/{contactId}"){
        fun passContactId(contactId: String?) = "birthday_details/$contactId"
    }
    object EditBirthDay: Screen("edit_birthday/{contactId}"){
        fun passContactId(contactId: String?) = "edit_birthday/$contactId"
    }
    object Home: Screen("home")
    object ProfileEdit: Screen("profile_edit")
    object ProfileSetting: Screen("profile_setting")
    object Splash: Screen("splash")
    object Stats: Screen("stats")
    object Notification: Screen("notification")
    object Calendar: Screen("calendar")
    object Profile: Screen("profile")
    object EmailVerification: Screen("email_verification")
    object ContactList: Screen("contact_list/{listType}")
}
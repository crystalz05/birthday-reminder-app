package com.tyro.birthdayreminder.custom_class

import com.tyro.birthdayreminder.R

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int? = null,
    val lottieRes: Int? = null

)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Never Miss a Birthday 🎂",
        description = "Keep track of all your friends’ and family’s birthdays in one place.",
        imageRes = R.drawable.page0
    ),
    OnboardingPage(
        title = "Add a birthday contact",
        description = "Click on the add icon",
        imageRes = R.drawable.page1
    ),
    OnboardingPage(
        title = "Add a birthday contact",
        description = "Fill the form completely",
        imageRes = R.drawable.page2
    ),
    OnboardingPage(
        title = "Add a birthday contact",
        description = "Fill in the contact details for easy connection",
        imageRes = R.drawable.page3
    ),
    OnboardingPage(
        title = "Add a birthday contact",
        description = "Check the required reminder periods and submit",
        imageRes = R.drawable.page4
    ),
    OnboardingPage(
        title = "Checking contact details",
        description = "Click on the contact card",
        imageRes = R.drawable.page5
    ),
    OnboardingPage(
        title = "Modify or delete birthday contact",
        description = "You can edit, remove photo or delete a contact here",
        imageRes = R.drawable.page6
    )
    ,
    OnboardingPage(
        title = "Add a journal",
        description = "Add a journal that can be used to generate customized birthday gift suggestions",
        imageRes = R.drawable.page7
    )
    ,
    OnboardingPage(
        title = "Generate gift suggestions",
        description = "Click Get gift suggestion to get 5 gift suggestions",
        imageRes = R.drawable.page8
    )
    ,
    OnboardingPage(
        title = "Onboarding Completed",
        description = "Proceed to home screen",
//        imageRes = R.drawable.page9
        lottieRes = R.raw.celebration
    )

)
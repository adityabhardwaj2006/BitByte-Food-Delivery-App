package com.example.zomatoclone.presentation.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation{
    @Serializable
    object LoginSignUpScreen : SubNavigation()

    @Serializable
    object MainHomeScreen : SubNavigation()
}

sealed class Routes{

    @Serializable
    object LoginScreen : Routes()

    @Serializable
    object SignUpScreen : Routes()

    @Serializable
    object DeliveryScreen : Routes()

    @Serializable
    object QuickScreen : Routes()

    @Serializable
    object FinalCheckoutScreen : Routes()

    @Serializable
    object ParticularCardScreen : Routes()

    @Serializable
    object SearchBarScreen : Routes()

    @Serializable
    object ProfileScreen : Routes()

    @Serializable
    object CartScreen : Routes()

    @Serializable
    object PastOrders : Routes()

    @Serializable
    object PaymentMethodSelectScreen : Routes()

    @Serializable
    object YourFeedback : Routes()

    @Serializable
    object AddressBook : Routes()

    @Serializable
    object AboutScreen:Routes()
}

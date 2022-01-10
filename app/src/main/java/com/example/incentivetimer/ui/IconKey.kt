package com.example.incentivetimer.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class IconKey(val rewardIcon: ImageVector) {
    STAR(Icons.Default.Star),
    CAKE(Icons.Default.Cake),
    BATH_TUB(Icons.Default.Bathtub),
    TV(Icons.Default.Tv),
    FAVORITE(Icons.Default.Favorite),
    PETS(Icons.Default.Pets),
    PHONE(Icons.Default.Phone),
    GIFT_CARD(Icons.Default.CardGiftcard),
    GAME_PAD(Icons.Default.Gamepad),
    MONEY(Icons.Default.Money),
    COMPUTER(Icons.Default.Computer),
    GROUP(Icons.Default.Group),
    HAPPY(Icons.Default.Mood),
    BEVERAGE(Icons.Default.EmojiFoodBeverage),
    MOTORBIKE(Icons.Default.SportsMotorsports),
    FOOTBALL(Icons.Default.SportsFootball),
    HEADPHONES(Icons.Default.Headphones),
    BICYCLE(Icons.Default.DirectionsBike),
    SHOPPING_CART(Icons.Default.ShoppingCart),
    PIZZA(Icons.Default.LocalPizza),
}
val defaultIcon = Icons.Default.Star

/*
val rewardIcons = mapOf<String, ImageVector>(
    Pair(IconKeys.STAR, Icons.Default.Star),
    Pair(IconKeys.CAKE, Icons.Default.Cake),
    Pair(IconKeys.BATH_TUB, Icons.Default.Bathtub),
    Pair(IconKeys.TV, Icons.Default.Tv),
    Pair(IconKeys.FAVORITE, Icons.Default.Favorite),
    Pair(IconKeys.PETS, Icons.Default.Pets),
    Pair(IconKeys.PHONE, Icons.Default.Phone),
    Pair(IconKeys.GIFT_CARD, Icons.Default.CardGiftcard),
    Pair(IconKeys.GAME_PAD, Icons.Default.Gamepad),
    Pair(IconKeys.MONEY, Icons.Default.Money),
    Pair(IconKeys.COMPUTER, Icons.Default.Computer),
    Pair(IconKeys.GROUP, Icons.Default.Group),
    Pair(IconKeys.MOOD, Icons.Default.Mood),
    Pair(IconKeys.TV, Icons.Default.EmojiFoodBeverage),
    Pair(IconKeys.TV, Icons.Default.SportsMotorsports),
    Pair(IconKeys.TV, Icons.Default.SportsFootball),
    Pair(IconKeys.TV, Icons.Default.Headphones),
    Pair(IconKeys.TV, Icons.Default.ShoppingCart),
    Pair(IconKeys.TV, Icons.Default.DirectionsBike),
    Pair(IconKeys.TV, Icons.Default.LocalPizza),

    )*/
/*
enum class IconKeys(val dbKey: String, val icon: ImageVector) {
    CAKE(dbKey = "CAKE", icon = Icons.Default.Cake)
}
*/


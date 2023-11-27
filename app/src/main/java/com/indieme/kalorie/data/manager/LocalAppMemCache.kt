package com.indieme.kalorie.data.manager

//Central Singleton Cache Repo to access frequently accessed items throughout whole app
object LocalAppMemCache {
    var token: String? = null
    var userBMR: String? = null
    var userTDEE: String? = null
    var dailyCalorie: String? = null
    var bmi: String? = null
}
package com.devid_academy.coachtrackercompose.data.network

object ApiRoutes {

//    const val BASE_URL = "http://10.0.2.2:8000/api/"
        const val BASE_URL = "https://coachtracker.thierryleung.fr/api/"

//    const val BASE_URL = "https://679fd82024322f8329c4c0db.mockapi.io/api/"

    const val LOGIN_USER = "login_check"
    const val REGISTER_USER = "register"

    const val GET_ALL_EVENTS = "events"
    const val GET_EVENT = "events/{id}"
    const val ADD_EVENT = "events/new"
    const val GET_EVENT_TYPES = "event_types"

    const val GET_TEAM = "teams/{id}"

    const val GET_VISITOR_TEAM_LIST = "visitor_teams"
    const val GET_STATIUM_LIST = "stadia"
    const val GET_SEASON_LIST = "seasons"

    const val GET_USER_PROFILE = "user/profile"
}
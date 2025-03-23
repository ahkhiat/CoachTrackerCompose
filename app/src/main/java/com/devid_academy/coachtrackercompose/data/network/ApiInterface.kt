package com.devid_academy.coachtrackercompose.data.network


import com.devid_academy.coachtrackercompose.data.dto.CreateConvocationDTO
import com.devid_academy.coachtrackercompose.data.dto.CreateEventDTO
import com.devid_academy.coachtrackercompose.data.dto.CreatePresenceDTO
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import com.devid_academy.coachtrackercompose.data.dto.EventTypeDTO
import com.devid_academy.coachtrackercompose.data.dto.PlayerProfileDTO
import com.devid_academy.coachtrackercompose.data.dto.SeasonDTO
import com.devid_academy.coachtrackercompose.data.dto.StadiumDTO
import com.devid_academy.coachtrackercompose.data.dto.TeamDTO
import com.devid_academy.coachtrackercompose.data.dto.UpdateConvocationDTO
import com.devid_academy.coachtrackercompose.data.dto.VisitorTeamDTO
import com.devid_academy.coachtrackercompose.data.dto.auth.LoginDTO
import com.devid_academy.coachtrackercompose.data.dto.auth.RegisterDTO
import com.devid_academy.coachtrackercompose.data.dto.auth.StatusAuthDTO
import com.devid_academy.coachtrackercompose.data.dto.response.ResponseCreateDTO
import com.devid_academy.coachtrackercompose.data.dto.UserProfileDTO
import com.devid_academy.coachtrackercompose.data.dto.response.ResponseConvocationDTO
import com.devid_academy.coachtrackercompose.data.dto.response.ResponsePresenceDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

//    Auth
    @POST(ApiRoutes.LOGIN_USER)
    suspend fun loginUser(@Body user: LoginDTO): Response<StatusAuthDTO>

    @POST(ApiRoutes.REGISTER_USER)
    suspend fun registerUser(@Body user: RegisterDTO): Response<StatusAuthDTO>

//    User
    @GET(ApiRoutes.GET_USER_PROFILE)
    suspend fun getUserProfile(): UserProfileDTO

    @GET(ApiRoutes.GET_PLAYER_PROFILE)
    suspend fun getPlayerProfile(@Query("user_id") userId: Int) : Response<PlayerProfileDTO>

//    Events
    @GET(ApiRoutes.GET_ALL_EVENTS)
    suspend fun getAllEvents(
        @Query("team.name") teamName: String,
        @Query("order[date]") order: String? = "DESC"
    ): Response<List<EventDTO>>

    @GET(ApiRoutes.GET_EVENT)
    suspend fun getEvent(@Path("id") eventId: Int): Response <EventDTO>

    @POST(ApiRoutes.ADD_EVENT)
    suspend fun insertEvent(@Body event: CreateEventDTO): Response<ResponseCreateDTO>

    @GET(ApiRoutes.GET_EVENT_TYPES)
    suspend fun getEventTypes(): List<EventTypeDTO>

    @POST(ApiRoutes.ADD_CONVOCATION)
    suspend fun insertConvocation(@Body convocations: CreateConvocationDTO): Response<ResponseConvocationDTO>

    @PUT(ApiRoutes.UPDATE_CONVOCATION)
    suspend fun updateConvocation(@Body convocations: UpdateConvocationDTO): Response<ResponseConvocationDTO>

    @POST(ApiRoutes.ADD_PRESENCE)
    suspend fun insertPresence(@Body convocations: CreatePresenceDTO): Response<ResponsePresenceDTO>

//    Teams
    @GET(ApiRoutes.GET_TEAM)
    suspend fun getTeam(@Path("id") teamId: Int): TeamDTO


//    Spinner menus
    @GET(ApiRoutes.GET_VISITOR_TEAM_LIST)
    suspend fun getVisitorTeamList(): List<VisitorTeamDTO>

    @GET(ApiRoutes.GET_STADIUM_LIST)
    suspend fun getStadiumList(): List<StadiumDTO>

    @GET(ApiRoutes.GET_SEASON_LIST)
    suspend fun getSeasonList(): List<SeasonDTO>

}
package com.typownik.app.data.network

import com.typownik.app.data.model.LeagueRound
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("predictions/next-round")
    suspend fun getNextRoundPredictions(): List<LeagueRound>

    @GET("predictions/next-round")
    suspend fun getNextRoundPredictions(
        @Query("league") league: String
    ): List<LeagueRound>
}

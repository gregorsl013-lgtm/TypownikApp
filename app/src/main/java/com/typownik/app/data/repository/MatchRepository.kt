package com.typownik.app.data.repository

import com.typownik.app.data.model.LeagueRound
import com.typownik.app.data.network.RetrofitClient

class MatchRepository {

    private val api = RetrofitClient.apiService

    suspend fun getNextRoundPredictions(): Result<List<LeagueRound>> {
        return try {
            Result.success(api.getNextRoundPredictions())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

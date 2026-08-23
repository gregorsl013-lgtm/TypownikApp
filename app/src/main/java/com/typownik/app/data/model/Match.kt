package com.typownik.app.data.model

data class Match(
    val id: String,
    val league: String,
    val homeTeam: String,
    val awayTeam: String,
    val kickoffTime: String,
    val probHomeWin: Int,
    val probDraw: Int,
    val probAwayWin: Int,
    val suggestedTip: String
)

data class LeagueRound(
    val league: String,
    val roundNumber: Int,
    val matches: List<Match>
)

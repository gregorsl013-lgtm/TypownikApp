package com.typownik.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.typownik.app.data.model.LeagueRound
import com.typownik.app.data.model.Match
import com.typownik.app.ui.viewmodel.MatchViewModel
import com.typownik.app.ui.viewmodel.UiState

@Composable
fun MatchListScreen(viewModel: MatchViewModel = MatchViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Typownik - najbliższa kolejka") })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val s = state) {
                is UiState.Loading -> LoadingView()
                is UiState.Error -> ErrorView(s.message, onRetry = viewModel::loadPredictions)
                is UiState.Success -> RoundsList(s.rounds)
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Nie udało się pobrać typów")
        Spacer(Modifier.height(8.dp))
        Text(message, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Spróbuj ponownie") }
    }
}

@Composable
private fun RoundsList(rounds: List<LeagueRound>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        rounds.forEach { round ->
            item {
                Text(
                    "${round.league} — kolejka ${round.roundNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            items(round.matches) { match ->
                MatchCard(match)
            }
        }
    }
}

@Composable
private fun MatchCard(match: Match) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "${match.homeTeam} vs ${match.awayTeam}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            ProbabilityRow(match)
            Spacer(Modifier.height(8.dp))
            Text(
                "Sugerowany typ: ${match.suggestedTip}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ProbabilityRow(match: Match) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        ProbabilityItem("1 (${match.homeTeam})", match.probHomeWin)
        ProbabilityItem("X (remis)", match.probDraw)
        ProbabilityItem("2 (${match.awayTeam})", match.probAwayWin)
    }
}

@Composable
private fun ProbabilityItem(label: String, percent: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$percent%", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

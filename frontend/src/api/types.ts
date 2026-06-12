export interface Tournament {
  id: number
  bcpEventId: string | null
  name: string
  date: string
  location: string
  format: string
  playerCount: number
}

export interface FactionWinRate {
  faction: string
  wins: number
  losses: number
  draws: number
  totalGames: number
  winRate: number
}

export interface TrendPoint {
  date: string
  winRate: number
  gamesPlayed: number
}

export interface FactionTrend {
  faction: string
  dataPoints: TrendPoint[]
}

export interface Matchup {
  faction1: string
  faction2: string
  faction1Wins: number
  faction2Wins: number
  draws: number
  totalGames: number
  faction1WinRate: number
}

export interface PlayerResult {
  tournamentId: number
  tournamentName: string
  tournamentDate: string
  tournamentFormat: string
  faction: string
  placement: number
  wins: number
  losses: number
  draws: number
}

export interface Player {
  id: number
  bcpPlayerId: string | null
  name: string
  region: string
  totalWins: number
  totalLosses: number
  totalDraws: number
  overallWinRate: number
  results: PlayerResult[]
}

export interface MetaSnapshot {
  generatedAt: string
  totalTournaments: number
  totalPlayers: number
  totalMatchups: number
  topFactionsByWinRate: FactionWinRate[]
  mostPlayedFaction: string
}

import type {
  Tournament,
  TournamentResultEntry,
  FactionWinRate,
  FactionTrend,
  Matchup,
  Player,
  MetaSnapshot,
} from './types'

const BASE = '/api'

async function http<T>(path: string): Promise<T> {
  const res = await fetch(`${BASE}${path}`, {
    headers: { Accept: 'application/json' },
  })
  if (!res.ok) {
    throw new Error(`Request failed (${res.status}): ${path}`)
  }
  return res.json() as Promise<T>
}

export interface TournamentFilters {
  after?: string
  minPlayers?: number
}

export const api = {
  getMetaSnapshot: () => http<MetaSnapshot>('/meta/snapshot'),

  getTournaments: (filters?: TournamentFilters) => {
    const params = new URLSearchParams()
    if (filters?.after) params.set('after', filters.after)
    if (filters?.minPlayers != null) params.set('minPlayers', String(filters.minPlayers))
    const qs = params.toString()
    return http<Tournament[]>(`/tournaments${qs ? `?${qs}` : ''}`)
  },

  getTournament: (id: number) => http<Tournament>(`/tournaments/${id}`),

  getTournamentResults: (id: number) => http<TournamentResultEntry[]>(`/tournaments/${id}/results`),

  getFactionWinRates: () => http<FactionWinRate[]>('/factions/winrates'),

  getFactionTrend: (faction: string) =>
    http<FactionTrend>(`/factions/${encodeURIComponent(faction)}/trend`),

  getMatchup: (faction1: string, faction2: string) =>
    http<Matchup>(
      `/matchups/${encodeURIComponent(faction1)}/vs/${encodeURIComponent(faction2)}`,
    ),

  getPlayer: (id: number) => http<Player>(`/players/${id}`),
}

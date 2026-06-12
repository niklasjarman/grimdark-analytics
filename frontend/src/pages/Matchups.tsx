import { useEffect, useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { api } from '../api/client'
import { PageHeader } from '../components/PageHeader'
import { Loading, ErrorState, Empty } from '../components/States'
import { factionColor } from '../lib/factions'
import { pct } from '../lib/format'

function FactionSelect({
  value,
  onChange,
  options,
  label,
}: {
  value: string
  onChange: (v: string) => void
  options: string[]
  label: string
}) {
  return (
    <label className="flex-1 min-w-[200px]">
      <span className="block text-xs uppercase tracking-widest text-gray-500 mb-1.5">
        {label}
      </span>
      <select
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full bg-grim-card border border-grim-border rounded-lg px-3 py-2.5 text-gray-100 focus:border-blood-bright focus:outline-none focus:shadow-glow transition-all"
      >
        {options.map((o) => (
          <option key={o} value={o} className="bg-grim-card">
            {o}
          </option>
        ))}
      </select>
    </label>
  )
}

export function Matchups() {
  const winrates = useQuery({
    queryKey: ['winrates'],
    queryFn: api.getFactionWinRates,
  })

  const factions = (winrates.data ?? []).map((f) => f.faction).sort()
  const [f1, setF1] = useState('')
  const [f2, setF2] = useState('')

  useEffect(() => {
    if (factions.length >= 2 && !f1 && !f2) {
      setF1(factions[0])
      setF2(factions[1])
    }
  }, [factions, f1, f2])

  const matchup = useQuery({
    queryKey: ['matchup', f1, f2],
    queryFn: () => api.getMatchup(f1, f2),
    enabled: !!f1 && !!f2 && f1 !== f2,
  })

  if (winrates.isLoading) return <Loading />
  if (winrates.isError) return <ErrorState error={winrates.error} />

  return (
    <div>
      <PageHeader
        title="Matchup Matrix"
        subtitle="Head-to-head combat records between factions"
      />

      <div className="card p-6 mb-8">
        <div className="flex flex-wrap items-end gap-4">
          <FactionSelect label="Faction One" value={f1} onChange={setF1} options={factions} />
          <div className="pb-2.5 font-gothic text-blood-bright text-lg">VS</div>
          <FactionSelect label="Faction Two" value={f2} onChange={setF2} options={factions} />
        </div>
      </div>

      {f1 === f2 ? (
        <Empty label="Choose two different factions to compare." />
      ) : matchup.isLoading ? (
        <Loading label="Replaying the engagement…" />
      ) : matchup.isError ? (
        <ErrorState error={matchup.error} />
      ) : matchup.data ? (
        <MatchupResult data={matchup.data} />
      ) : null}
    </div>
  )
}

function MatchupResult({
  data,
}: {
  data: import('../api/types').Matchup
}) {
  const c1 = factionColor(data.faction1)
  const c2 = factionColor(data.faction2)
  const f1Rate = data.faction1WinRate
  const f2Rate =
    data.totalGames > 0 ? data.faction2Wins / data.totalGames : 0
  const drawRate =
    data.totalGames > 0 ? data.draws / data.totalGames : 0

  if (data.totalGames === 0) {
    return <Empty label="These factions have not yet met on the battlefield." />
  }

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-3 gap-4">
        <SideCard faction={data.faction1} wins={data.faction1Wins} color={c1} rate={f1Rate} />
        <div className="card p-6 flex flex-col items-center justify-center text-center">
          <p className="text-xs uppercase tracking-widest text-gray-500">Draws</p>
          <p className="text-4xl font-gothic font-bold text-gray-300 mt-2">
            {data.draws}
          </p>
          <p className="text-xs text-gray-600 mt-1">{pct(drawRate)}</p>
          <p className="mt-4 text-xs text-gray-500">
            {data.totalGames} total games
          </p>
        </div>
        <SideCard faction={data.faction2} wins={data.faction2Wins} color={c2} rate={f2Rate} />
      </div>

      {/* Win share bar */}
      <div className="card p-6">
        <p className="text-xs uppercase tracking-widest text-gray-500 mb-3">
          Win Share
        </p>
        <div className="flex h-10 w-full rounded-lg overflow-hidden border border-grim-border">
          <div
            className="flex items-center justify-start px-3 text-sm font-bold text-white/90 transition-all duration-500"
            style={{ width: `${f1Rate * 100}%`, backgroundColor: c1 }}
          >
            {f1Rate > 0.12 && pct(f1Rate)}
          </div>
          {drawRate > 0 && (
            <div
              className="flex items-center justify-center text-xs text-gray-200 bg-grim-border transition-all duration-500"
              style={{ width: `${drawRate * 100}%` }}
            >
              {drawRate > 0.08 && 'D'}
            </div>
          )}
          <div
            className="flex items-center justify-end px-3 text-sm font-bold text-white/90 transition-all duration-500"
            style={{ width: `${f2Rate * 100}%`, backgroundColor: c2 }}
          >
            {f2Rate > 0.12 && pct(f2Rate)}
          </div>
        </div>
        <div className="mt-3 flex justify-between text-sm">
          <span style={{ color: c1 }} className="font-semibold">
            {data.faction1}
          </span>
          <span style={{ color: c2 }} className="font-semibold">
            {data.faction2}
          </span>
        </div>
      </div>
    </div>
  )
}

function SideCard({
  faction,
  wins,
  color,
  rate,
}: {
  faction: string
  wins: number
  color: string
  rate: number
}) {
  return (
    <div
      className="card p-6 flex flex-col items-center justify-center text-center relative overflow-hidden"
      style={{ borderColor: `${color}50` }}
    >
      <div
        className="absolute inset-x-0 top-0 h-1"
        style={{ background: color, boxShadow: `0 0 14px ${color}` }}
      />
      <p className="text-sm font-semibold mb-3" style={{ color }}>
        {faction}
      </p>
      <p className="text-5xl font-gothic font-bold text-gray-100">{wins}</p>
      <p className="text-xs uppercase tracking-widest text-gray-500 mt-1">Wins</p>
      <p className="mt-3 text-sm font-semibold" style={{ color }}>
        {pct(rate)} win rate
      </p>
    </div>
  )
}

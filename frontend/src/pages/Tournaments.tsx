import { useQuery } from '@tanstack/react-query'
import { Link, useParams } from 'react-router-dom'
import { api } from '../api/client'
import { PageHeader } from '../components/PageHeader'
import { StatCard } from '../components/StatCard'
import { Loading, ErrorState, Empty } from '../components/States'
import { FactionBadge, FormatBadge } from '../components/FactionBadge'
import { WinRateBar } from '../components/WinRateBar'
import { factionColor, factionImage } from '../lib/factions'
import { formatDate, pct } from '../lib/format'
import type { Tournament, TournamentResultEntry } from '../api/types'

export function Tournaments() {
  const { id } = useParams()
  if (id) return <TournamentDetail id={Number(id)} />
  return <TournamentList />
}

function TournamentList() {
  const { data, isLoading, isError, error } = useQuery({
    queryKey: ['tournaments'],
    queryFn: () => api.getTournaments(),
  })

  if (isLoading) return <Loading />
  if (isError) return <ErrorState error={error} />
  if (!data || data.length === 0) return <Empty label="No tournaments recorded." />

  const sorted = [...data].sort((a, b) => b.date.localeCompare(a.date))
  const totalPlayers = data.reduce((s, t) => s + t.playerCount, 0)

  return (
    <div>
      <PageHeader
        title="Tournaments"
        subtitle={`${data.length} events · ${totalPlayers} combatants fielded`}
      />
      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-5">
        {sorted.map((t) => (
          <TournamentCard key={t.id} t={t} />
        ))}
      </div>
    </div>
  )
}

function TournamentCard({ t }: { t: Tournament }) {
  return (
    <Link
      to={`/tournaments/${t.id}`}
      className="card card-hover p-5 flex flex-col group"
    >
      <div className="flex items-start justify-between gap-3">
        <h3 className="font-gothic text-lg text-gray-100 group-hover:text-blood-bright transition-colors">
          {t.name}
        </h3>
        <FormatBadge format={t.format} />
      </div>
      <div className="mt-3 space-y-1.5 text-sm text-gray-400">
        <p className="flex items-center gap-2">
          <span className="text-gray-600">Date</span>
          {formatDate(t.date)}
        </p>
        <p className="flex items-center gap-2">
          <span className="text-gray-600">Venue</span>
          {t.location}
        </p>
      </div>
      <div className="mt-4 pt-4 border-t border-grim-border flex items-center justify-between">
        <span className="text-xs uppercase tracking-widest text-gray-600">
          Players
        </span>
        <span className="font-gothic font-bold text-2xl text-gray-100">
          {t.playerCount}
        </span>
      </div>
    </Link>
  )
}

function TournamentDetail({ id }: { id: number }) {
  const tournament = useQuery({
    queryKey: ['tournament', id],
    queryFn: () => api.getTournament(id),
  })
  const results = useQuery({
    queryKey: ['tournament-results', id],
    queryFn: () => api.getTournamentResults(id),
  })

  if (tournament.isLoading) return <Loading />
  if (tournament.isError) return <ErrorState error={tournament.error} />
  if (!tournament.data) return <Empty />

  const data = tournament.data

  return (
    <div>
      <Link
        to="/tournaments"
        className="text-sm text-gray-500 hover:text-blood-bright transition-colors"
      >
        ← Back to Tournaments
      </Link>
      <PageHeader
        title={data.name}
        subtitle={`${data.location} · ${formatDate(data.date)}`}
        right={<FormatBadge format={data.format} />}
      />
      <section className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard label="Format" value={data.format} accent />
        <StatCard label="Players" value={data.playerCount} />
        <StatCard label="Date" value={formatDate(data.date)} />
        <StatCard label="Location" value={data.location} />
      </section>

      {results.isLoading ? (
        <div className="mt-8"><Loading label="Marshalling forces…" /></div>
      ) : results.isError ? (
        <div className="mt-8"><ErrorState error={results.error} /></div>
      ) : results.data && results.data.length > 0 ? (
        <>
          <FactionShowcase results={results.data} />
          <ResultsTable results={results.data} />
        </>
      ) : (
        <div className="card p-6 mt-6 text-sm text-gray-400">
          <h2 className="font-gothic text-lg text-gray-100 mb-2">Event Briefing</h2>
          <p>
            <span className="text-blood-bright font-semibold">{data.name}</span> was
            a {data.format} format event held in {data.location} on{' '}
            {formatDate(data.date)}, drawing {data.playerCount} commanders to the
            battlefield.
          </p>
        </div>
      )}
    </div>
  )
}

function FactionShowcase({ results }: { results: TournamentResultEntry[] }) {
  // Count players per faction and find the winner per faction
  const factionMap = new Map<string, { count: number; bestPlacement: number }>()
  for (const r of results) {
    const existing = factionMap.get(r.faction)
    if (!existing) {
      factionMap.set(r.faction, { count: 1, bestPlacement: r.placement })
    } else {
      factionMap.set(r.faction, {
        count: existing.count + 1,
        bestPlacement: Math.min(existing.bestPlacement, r.placement),
      })
    }
  }
  const top = [...factionMap.entries()]
    .sort((a, b) => b[1].count - a[1].count || a[1].bestPlacement - b[1].bestPlacement)
    .slice(0, 4)

  return (
    <section className="mt-8">
      <h2 className="font-gothic text-xl text-gray-100 mb-4">Armies of the Field</h2>
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {top.map(([faction, { count, bestPlacement }]) => (
          <FactionCard
            key={faction}
            faction={faction}
            playerCount={count}
            bestPlacement={bestPlacement}
          />
        ))}
      </div>
    </section>
  )
}

function FactionCard({
  faction,
  playerCount,
  bestPlacement,
}: {
  faction: string
  playerCount: number
  bestPlacement: number
}) {
  const color = factionColor(faction)
  const imgUrl = factionImage(faction)

  return (
    <Link
      to={`/factions/${encodeURIComponent(faction)}`}
      className="relative overflow-hidden rounded-xl border border-grim-border group"
      style={{ minHeight: 180 }}
    >
      {/* Background: image or gradient */}
      {imgUrl ? (
        <img
          src={imgUrl}
          alt={faction}
          className="absolute inset-0 w-full h-full object-cover object-center opacity-40 group-hover:opacity-55 transition-opacity duration-500 scale-105 group-hover:scale-100 transition-transform"
          onError={(e) => {
            const el = e.currentTarget
            el.style.display = 'none'
          }}
        />
      ) : null}
      {/* Gradient overlay */}
      <div
        className="absolute inset-0"
        style={{
          background: `linear-gradient(160deg, ${color}22 0%, #0a0a0f 80%)`,
        }}
      />
      {/* Top accent line */}
      <div
        className="absolute inset-x-0 top-0 h-0.5"
        style={{ background: `linear-gradient(90deg, transparent, ${color}, transparent)` }}
      />
      {/* Content */}
      <div className="relative p-4 flex flex-col justify-between h-full" style={{ minHeight: 180 }}>
        {bestPlacement === 1 && (
          <span className="self-start text-xs font-bold uppercase tracking-widest px-2 py-0.5 rounded"
            style={{ background: `${color}33`, color }}>
            Champion
          </span>
        )}
        <div className="mt-auto">
          <p className="font-gothic text-base font-bold leading-tight" style={{ color }}>
            {faction}
          </p>
          <p className="text-xs text-gray-500 mt-1">
            {playerCount} {playerCount === 1 ? 'commander' : 'commanders'}
          </p>
        </div>
      </div>
    </Link>
  )
}

function ResultsTable({ results }: { results: TournamentResultEntry[] }) {
  return (
    <section className="card overflow-hidden mt-8">
      <div className="px-5 py-4 border-b border-grim-border flex items-center justify-between">
        <h2 className="font-gothic text-xl text-gray-100">Final Standings</h2>
        <span className="text-xs text-gray-600 uppercase tracking-widest">
          {results.length} commanders
        </span>
      </div>
      <table className="w-full text-sm">
        <thead>
          <tr className="text-left text-xs uppercase tracking-wider text-gray-500 border-b border-grim-border">
            <th className="px-5 py-3 font-medium w-16 text-center">#</th>
            <th className="px-5 py-3 font-medium">Commander</th>
            <th className="px-5 py-3 font-medium">Army</th>
            <th className="px-5 py-3 font-medium text-center">Record</th>
            <th className="px-5 py-3 font-medium hidden md:table-cell">Win Rate</th>
          </tr>
        </thead>
        <tbody>
          {results.map((r) => {
            const total = r.wins + r.losses + r.draws
            const rate = total > 0 ? r.wins / total : 0
            return (
              <tr
                key={r.playerId}
                className="border-b border-grim-border/60 last:border-0 hover:bg-grim-hover transition-colors"
              >
                <td className="px-5 py-3 text-center">
                  <PlacementBadge place={r.placement} />
                </td>
                <td className="px-5 py-3">
                  <Link
                    to={`/players`}
                    className="text-gray-100 hover:text-blood-bright transition-colors font-medium"
                  >
                    {r.playerName}
                  </Link>
                </td>
                <td className="px-5 py-3">
                  <FactionBadge faction={r.faction} />
                </td>
                <td className="px-5 py-3 text-center tabular-nums text-gray-300">
                  <span className="text-emerald-400">{r.wins}</span>
                  {' / '}
                  <span className="text-red-400">{r.losses}</span>
                  {' / '}
                  <span className="text-gray-400">{r.draws}</span>
                </td>
                <td className="px-5 py-3 hidden md:table-cell min-w-[140px]">
                  <div className="flex items-center gap-2">
                    <WinRateBar rate={rate} height={6} />
                    <span className="text-xs tabular-nums text-gray-500 w-10 text-right">
                      {pct(rate)}
                    </span>
                  </div>
                </td>
              </tr>
            )
          })}
        </tbody>
      </table>
    </section>
  )
}

function PlacementBadge({ place }: { place: number }) {
  const medal =
    place === 1
      ? 'text-yellow-400 border-yellow-400/40 bg-yellow-400/10'
      : place === 2
      ? 'text-gray-300 border-gray-400/40 bg-gray-400/10'
      : place === 3
      ? 'text-amber-600 border-amber-600/40 bg-amber-600/10'
      : 'text-gray-500 border-grim-border bg-grim-bg'
  const label =
    place === 1 ? '1st' : place === 2 ? '2nd' : place === 3 ? '3rd' : `${place}th`
  return (
    <span
      className={`inline-flex items-center justify-center min-w-[2.5rem] px-2 py-1 rounded-md text-sm font-bold border ${medal}`}
    >
      {label}
    </span>
  )
}

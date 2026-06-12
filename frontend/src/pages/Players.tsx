import { useState, type FormEvent } from 'react'
import { useQuery } from '@tanstack/react-query'
import { Link } from 'react-router-dom'
import { api } from '../api/client'
import { PageHeader } from '../components/PageHeader'
import { StatCard } from '../components/StatCard'
import { Loading, ErrorState } from '../components/States'
import { FactionBadge, FormatBadge } from '../components/FactionBadge'
import { WinRateBar } from '../components/WinRateBar'
import { formatDate, pct } from '../lib/format'

export function Players() {
  const [input, setInput] = useState('')
  const [playerId, setPlayerId] = useState<number | null>(null)

  const { data, isLoading, isError, error, isFetching } = useQuery({
    queryKey: ['player', playerId],
    queryFn: () => api.getPlayer(playerId!),
    enabled: playerId != null,
  })

  function onSubmit(e: FormEvent) {
    e.preventDefault()
    const n = parseInt(input, 10)
    if (!isNaN(n) && n > 0) setPlayerId(n)
  }

  return (
    <div>
      <PageHeader
        title="Player Lookup"
        subtitle="Retrieve a commander's combat dossier by ID"
      />

      <form onSubmit={onSubmit} className="card p-5 mb-8 flex flex-wrap gap-3 items-end">
        <label className="flex-1 min-w-[220px]">
          <span className="block text-xs uppercase tracking-widest text-gray-500 mb-1.5">
            Player ID
          </span>
          <input
            type="number"
            min={1}
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="e.g. 1"
            className="w-full bg-grim-bg border border-grim-border rounded-lg px-3 py-2.5 text-gray-100 placeholder-gray-600 focus:border-blood-bright focus:outline-none focus:shadow-glow transition-all"
          />
        </label>
        <button
          type="submit"
          className="px-6 py-2.5 rounded-lg font-gothic font-semibold tracking-wide text-white bg-blood hover:bg-blood-bright transition-all shadow-glow hover:shadow-glow-strong"
        >
          Search
        </button>
        <div className="flex gap-2 text-xs text-gray-600 items-center">
          <span>Try:</span>
          {[1, 2, 3].map((id) => (
            <button
              key={id}
              type="button"
              onClick={() => {
                setInput(String(id))
                setPlayerId(id)
              }}
              className="px-2 py-1 rounded border border-grim-border hover:border-blood-bright hover:text-blood-bright transition-colors"
            >
              #{id}
            </button>
          ))}
        </div>
      </form>

      {playerId == null ? (
        <div className="card p-10 text-center text-gray-500">
          <p className="font-gothic">Enter a player ID to view their record.</p>
        </div>
      ) : isLoading || isFetching ? (
        <Loading label="Retrieving dossier…" />
      ) : isError ? (
        <ErrorState error={error} />
      ) : data ? (
        <div className="space-y-6">
          <div className="card p-6">
            <div className="flex items-center justify-between flex-wrap gap-3">
              <div>
                <h2 className="text-2xl font-gothic font-bold text-gray-100">
                  {data.name}
                </h2>
                <p className="text-sm text-gray-500 mt-1">
                  Region: <span className="text-gray-300">{data.region}</span> ·
                  Player #{data.id}
                </p>
              </div>
              <div className="text-right">
                <p className="text-xs uppercase tracking-widest text-gray-500">
                  Win Rate
                </p>
                <p className="text-3xl font-gothic font-bold text-blood-bright">
                  {pct(data.overallWinRate)}
                </p>
              </div>
            </div>
            <div className="mt-4">
              <WinRateBar rate={data.overallWinRate} height={12} />
            </div>
          </div>

          <section className="grid grid-cols-3 gap-4">
            <StatCard label="Total Wins" value={data.totalWins} />
            <StatCard label="Total Losses" value={data.totalLosses} />
            <StatCard label="Total Draws" value={data.totalDraws} />
          </section>

          <section className="card overflow-hidden">
            <div className="px-5 py-4 border-b border-grim-border">
              <h3 className="font-gothic text-lg text-gray-100">
                Tournament Appearances
              </h3>
            </div>
            <table className="w-full text-sm">
              <thead>
                <tr className="text-left text-xs uppercase tracking-wider text-gray-500 border-b border-grim-border">
                  <th className="px-5 py-3 font-medium">Tournament</th>
                  <th className="px-5 py-3 font-medium">Faction</th>
                  <th className="px-5 py-3 font-medium text-center">Placement</th>
                  <th className="px-5 py-3 font-medium text-center">Record</th>
                </tr>
              </thead>
              <tbody>
                {data.results.map((r) => (
                  <tr
                    key={r.tournamentId}
                    className="border-b border-grim-border/60 last:border-0 hover:bg-grim-hover transition-colors"
                  >
                    <td className="px-5 py-3">
                      <Link
                        to={`/tournaments/${r.tournamentId}`}
                        className="text-gray-100 hover:text-blood-bright transition-colors font-medium"
                      >
                        {r.tournamentName}
                      </Link>
                      <div className="text-xs text-gray-600 mt-0.5 flex items-center gap-2">
                        {formatDate(r.tournamentDate)}
                        <FormatBadge format={r.tournamentFormat} />
                      </div>
                    </td>
                    <td className="px-5 py-3">
                      <FactionBadge faction={r.faction} />
                    </td>
                    <td className="px-5 py-3 text-center">
                      <PlacementBadge place={r.placement} />
                    </td>
                    <td className="px-5 py-3 text-center tabular-nums text-gray-300">
                      <span className="text-emerald-400">{r.wins}</span>
                      {' / '}
                      <span className="text-red-400">{r.losses}</span>
                      {' / '}
                      <span className="text-gray-400">{r.draws}</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </section>
        </div>
      ) : null}
    </div>
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
      : 'text-gray-400 border-grim-border bg-grim-bg'
  return (
    <span
      className={`inline-flex items-center justify-center min-w-[2.5rem] px-2 py-1 rounded-md text-sm font-bold border ${medal}`}
    >
      {place === 1 ? '1st' : place === 2 ? '2nd' : place === 3 ? '3rd' : `${place}th`}
    </span>
  )
}

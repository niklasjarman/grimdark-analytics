import { useMemo, useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { api } from '../api/client'
import { PageHeader } from '../components/PageHeader'
import { Loading, ErrorState, Empty } from '../components/States'
import { WinRateBar } from '../components/WinRateBar'
import { factionColor } from '../lib/factions'
import { winRateTextClass } from '../lib/format'
import type { FactionWinRate } from '../api/types'

type SortKey = 'faction' | 'totalGames' | 'wins' | 'losses' | 'draws' | 'winRate'

export function FactionMeta() {
  const navigate = useNavigate()
  const [sortKey, setSortKey] = useState<SortKey>('winRate')
  const [asc, setAsc] = useState(false)

  const { data, isLoading, isError, error } = useQuery({
    queryKey: ['winrates'],
    queryFn: api.getFactionWinRates,
  })

  const rows = useMemo(() => {
    if (!data) return []
    const sorted = [...data].sort((a, b) => {
      const av = a[sortKey]
      const bv = b[sortKey]
      let cmp: number
      if (typeof av === 'string' && typeof bv === 'string') cmp = av.localeCompare(bv)
      else cmp = (av as number) - (bv as number)
      return asc ? cmp : -cmp
    })
    return sorted
  }, [data, sortKey, asc])

  function toggleSort(key: SortKey) {
    if (key === sortKey) setAsc((p) => !p)
    else {
      setSortKey(key)
      setAsc(key === 'faction')
    }
  }

  if (isLoading) return <Loading />
  if (isError) return <ErrorState error={error} />
  if (!rows.length) return <Empty />

  const totalGames = rows.reduce((s, r) => s + r.totalGames, 0)

  return (
    <div>
      <PageHeader
        title="Faction Meta"
        subtitle={`${rows.length} factions · ${totalGames} games recorded across the circuit`}
      />

      <div className="card overflow-hidden">
        <table className="w-full text-sm">
          <thead>
            <tr className="text-left text-xs uppercase tracking-wider text-gray-500 border-b border-grim-border">
              <Th label="Faction" k="faction" {...{ sortKey, asc, toggleSort }} />
              <Th label="Games" k="totalGames" align="right" {...{ sortKey, asc, toggleSort }} />
              <Th label="Wins" k="wins" align="right" {...{ sortKey, asc, toggleSort }} />
              <Th label="Losses" k="losses" align="right" {...{ sortKey, asc, toggleSort }} />
              <Th label="Draws" k="draws" align="right" {...{ sortKey, asc, toggleSort }} />
              <th className="px-4 py-3 font-medium w-[28%]">
                <button
                  onClick={() => toggleSort('winRate')}
                  className="uppercase tracking-wider hover:text-gray-200"
                >
                  Win Rate {sortKey === 'winRate' ? (asc ? '▲' : '▼') : ''}
                </button>
              </th>
            </tr>
          </thead>
          <tbody>
            {rows.map((r: FactionWinRate) => (
              <tr
                key={r.faction}
                onClick={() => navigate(`/factions/${encodeURIComponent(r.faction)}`)}
                className="border-b border-grim-border/60 last:border-0 hover:bg-grim-hover cursor-pointer transition-colors group"
              >
                <td className="px-4 py-3">
                  <div className="flex items-center gap-2.5">
                    <span
                      className="h-3 w-3 rounded-full shrink-0"
                      style={{
                        backgroundColor: factionColor(r.faction),
                        boxShadow: `0 0 8px ${factionColor(r.faction)}`,
                      }}
                    />
                    <span className="font-semibold text-gray-100 group-hover:text-blood-bright transition-colors">
                      {r.faction}
                    </span>
                  </div>
                </td>
                <td className="px-4 py-3 text-right tabular-nums text-gray-400">
                  {r.totalGames}
                </td>
                <td className="px-4 py-3 text-right tabular-nums text-emerald-400/90">
                  {r.wins}
                </td>
                <td className="px-4 py-3 text-right tabular-nums text-red-400/90">
                  {r.losses}
                </td>
                <td className="px-4 py-3 text-right tabular-nums text-gray-400">
                  {r.draws}
                </td>
                <td className="px-4 py-3">
                  <div className="flex items-center gap-3">
                    <WinRateBar rate={r.winRate} showLabel={false} />
                    <span
                      className={`w-14 text-right font-bold tabular-nums ${winRateTextClass(
                        r.winRate,
                      )}`}
                    >
                      {(r.winRate * 100).toFixed(1)}%
                    </span>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="mt-4 flex items-center gap-5 text-xs text-gray-500">
        <Legend color="#34d399" label="Strong (≥55%)" />
        <Legend color="#fbbf24" label="Balanced (45–55%)" />
        <Legend color="#f87171" label="Struggling (<45%)" />
      </div>
    </div>
  )
}

function Th({
  label,
  k,
  align = 'left',
  sortKey,
  asc,
  toggleSort,
}: {
  label: string
  k: SortKey
  align?: 'left' | 'right'
  sortKey: SortKey
  asc: boolean
  toggleSort: (k: SortKey) => void
}) {
  return (
    <th className={`px-4 py-3 font-medium ${align === 'right' ? 'text-right' : ''}`}>
      <button
        onClick={() => toggleSort(k)}
        className="uppercase tracking-wider hover:text-gray-200"
      >
        {label} {sortKey === k ? (asc ? '▲' : '▼') : ''}
      </button>
    </th>
  )
}

function Legend({ color, label }: { color: string; label: string }) {
  return (
    <span className="flex items-center gap-1.5">
      <span className="h-2.5 w-2.5 rounded-full" style={{ backgroundColor: color }} />
      {label}
    </span>
  )
}

import { useQuery } from '@tanstack/react-query'
import { Link, useNavigate } from 'react-router-dom'
import {
  Bar,
  BarChart,
  Cell,
  LabelList,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts'
import { api } from '../api/client'
import { StatCard } from '../components/StatCard'
import { Loading, ErrorState } from '../components/States'
import { FormatBadge } from '../components/FactionBadge'
import { factionColor } from '../lib/factions'
import { pct, winRateBarColor, formatDate } from '../lib/format'

export function Dashboard() {
  const navigate = useNavigate()
  const meta = useQuery({
    queryKey: ['meta'],
    queryFn: api.getMetaSnapshot,
  })
  const winrates = useQuery({
    queryKey: ['winrates'],
    queryFn: api.getFactionWinRates,
  })
  const tournaments = useQuery({
    queryKey: ['tournaments'],
    queryFn: () => api.getTournaments(),
  })

  if (meta.isLoading) return <Loading />
  if (meta.isError) return <ErrorState error={meta.error} />
  const m = meta.data!

  const topFactions = (winrates.data ?? [])
    .slice()
    .sort((a, b) => b.winRate - a.winRate)
    .slice(0, 5)
    .map((f) => ({ ...f, label: pct(f.winRate), value: +(f.winRate * 100).toFixed(1) }))

  const recent = (tournaments.data ?? [])
    .slice()
    .sort((a, b) => b.date.localeCompare(a.date))
    .slice(0, 4)

  return (
    <div>
      {/* Hero */}
      <section className="mb-10">
        <p className="text-xs uppercase tracking-[0.4em] text-blood-bright">
          Meta Snapshot · {formatDate(m.generatedAt)}
        </p>
        <h1 className="mt-2 text-4xl md:text-5xl font-gothic font-bold text-gray-100">
          The State of the <span className="text-blood-bright">Galaxy</span>
        </h1>
        <p className="mt-3 max-w-2xl text-gray-400">
          Live competitive intelligence across the Warhammer 40,000 tournament
          circuit. Faction performance, head-to-head matchups, and player
          dossiers — compiled from the field of battle.
        </p>
      </section>

      {/* Stat cards */}
      <section className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-10">
        <StatCard label="Tournaments" value={m.totalTournaments} accent />
        <StatCard label="Players Tracked" value={m.totalPlayers} />
        <StatCard label="Matchups Recorded" value={m.totalMatchups} />
        <StatCard
          label="Most Played"
          value={
            <span style={{ color: factionColor(m.mostPlayedFaction) }}>
              {m.mostPlayedFaction}
            </span>
          }
          sub="Highest representation"
        />
      </section>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Top factions chart */}
        <section className="lg:col-span-2 card p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="font-gothic text-xl text-gray-100">
              Top Factions by Win Rate
            </h2>
            <Link
              to="/factions"
              className="text-sm text-blood-bright hover:underline"
            >
              View all →
            </Link>
          </div>
          {winrates.isLoading ? (
            <Loading label="Tallying victories…" />
          ) : winrates.isError ? (
            <ErrorState error={winrates.error} />
          ) : (
            <ResponsiveContainer width="100%" height={300}>
              <BarChart
                data={topFactions}
                layout="vertical"
                margin={{ left: 20, right: 50, top: 10, bottom: 10 }}
              >
                <XAxis
                  type="number"
                  domain={[0, 100]}
                  tick={{ fill: '#6b7280', fontSize: 12 }}
                  axisLine={{ stroke: '#1e1e2e' }}
                  tickLine={false}
                  unit="%"
                />
                <YAxis
                  type="category"
                  dataKey="faction"
                  width={140}
                  tick={{ fill: '#d1d5db', fontSize: 13 }}
                  axisLine={false}
                  tickLine={false}
                />
                <Tooltip
                  cursor={{ fill: 'rgba(196,30,58,0.08)' }}
                  contentStyle={{
                    background: '#13131a',
                    border: '1px solid #1e1e2e',
                    borderRadius: 8,
                  }}
                  formatter={(v: number) => [`${v}%`, 'Win Rate']}
                />
                <Bar
                  dataKey="value"
                  radius={[0, 6, 6, 0]}
                  onClick={(d: any) => navigate(`/factions/${encodeURIComponent(d.faction)}`)}
                  cursor="pointer"
                >
                  {topFactions.map((f) => (
                    <Cell key={f.faction} fill={winRateBarColor(f.winRate)} />
                  ))}
                  <LabelList
                    dataKey="label"
                    position="right"
                    fill="#9ca3af"
                    fontSize={12}
                  />
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          )}
        </section>

        {/* Recent tournaments */}
        <section className="card p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="font-gothic text-xl text-gray-100">
              Recent Tournaments
            </h2>
            <Link
              to="/tournaments"
              className="text-sm text-blood-bright hover:underline"
            >
              All →
            </Link>
          </div>
          {tournaments.isLoading ? (
            <Loading label="Mustering forces…" />
          ) : tournaments.isError ? (
            <ErrorState error={tournaments.error} />
          ) : (
            <ul className="space-y-3">
              {recent.map((t) => (
                <li key={t.id}>
                  <Link
                    to={`/tournaments/${t.id}`}
                    className="block rounded-lg border border-grim-border bg-grim-bg/40 p-3 card-hover"
                  >
                    <div className="flex items-center justify-between gap-2">
                      <span className="font-semibold text-gray-100 text-sm">
                        {t.name}
                      </span>
                      <FormatBadge format={t.format} />
                    </div>
                    <div className="mt-1.5 flex items-center justify-between text-xs text-gray-500">
                      <span>{formatDate(t.date)}</span>
                      <span>{t.playerCount} players</span>
                    </div>
                  </Link>
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>
    </div>
  )
}

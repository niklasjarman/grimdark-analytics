import { useQuery } from '@tanstack/react-query'
import { Link, useParams } from 'react-router-dom'
import {
  CartesianGrid,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts'
import { api } from '../api/client'
import { PageHeader } from '../components/PageHeader'
import { StatCard } from '../components/StatCard'
import { Loading, ErrorState, Empty } from '../components/States'
import { WinRateBar } from '../components/WinRateBar'
import { factionColor } from '../lib/factions'
import { formatDate, pct } from '../lib/format'

export function FactionDetail() {
  const { faction = '' } = useParams()
  const decoded = decodeURIComponent(faction)
  const color = factionColor(decoded)

  const winrates = useQuery({
    queryKey: ['winrates'],
    queryFn: api.getFactionWinRates,
  })
  const trend = useQuery({
    queryKey: ['trend', decoded],
    queryFn: () => api.getFactionTrend(decoded),
  })

  if (winrates.isLoading) return <Loading />
  if (winrates.isError) return <ErrorState error={winrates.error} />

  const stats = winrates.data?.find((f) => f.faction === decoded)

  const chartData =
    trend.data?.dataPoints.map((p) => ({
      date: formatDate(p.date),
      winRate: +(p.winRate * 100).toFixed(1),
      games: p.gamesPlayed,
    })) ?? []

  return (
    <div>
      <Link
        to="/factions"
        className="text-sm text-gray-500 hover:text-blood-bright transition-colors"
      >
        ← Back to Faction Meta
      </Link>

      <PageHeader
        title={
          <span className="flex items-center gap-3">
            <span
              className="h-7 w-7 rounded-md"
              style={{ backgroundColor: color, boxShadow: `0 0 16px ${color}` }}
            />
            <span style={{ color }}>{decoded}</span>
          </span>
        }
        subtitle="Faction performance dossier"
      />

      {!stats ? (
        <Empty label="No win-rate data for this faction." />
      ) : (
        <>
          <section className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
            <StatCard label="Wins" value={stats.wins} />
            <StatCard label="Losses" value={stats.losses} />
            <StatCard label="Draws" value={stats.draws} />
            <StatCard label="Overall Win Rate" value={pct(stats.winRate)} accent />
          </section>

          <section className="card p-6 mb-8">
            <p className="text-xs uppercase tracking-widest text-gray-500 mb-2">
              Overall Win Rate · {stats.totalGames} games
            </p>
            <WinRateBar rate={stats.winRate} height={14} />
          </section>
        </>
      )}

      <section className="card p-6">
        <h2 className="font-gothic text-xl text-gray-100 mb-4">
          Win Rate Trend Over Time
        </h2>
        {trend.isLoading ? (
          <Loading label="Charting the campaign…" />
        ) : trend.isError ? (
          <ErrorState error={trend.error} />
        ) : chartData.length === 0 ? (
          <Empty label="No trend data available." />
        ) : (
          <ResponsiveContainer width="100%" height={340}>
            <LineChart data={chartData} margin={{ left: 0, right: 20, top: 10 }}>
              <defs>
                <linearGradient id="trendGlow" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stopColor={color} stopOpacity={0.4} />
                  <stop offset="100%" stopColor={color} stopOpacity={0} />
                </linearGradient>
              </defs>
              <CartesianGrid stroke="#1e1e2e" strokeDasharray="3 3" />
              <XAxis
                dataKey="date"
                tick={{ fill: '#6b7280', fontSize: 12 }}
                axisLine={{ stroke: '#1e1e2e' }}
                tickLine={false}
              />
              <YAxis
                domain={[0, 100]}
                tick={{ fill: '#6b7280', fontSize: 12 }}
                axisLine={{ stroke: '#1e1e2e' }}
                tickLine={false}
                unit="%"
              />
              <Tooltip
                contentStyle={{
                  background: '#13131a',
                  border: '1px solid #1e1e2e',
                  borderRadius: 8,
                }}
                labelStyle={{ color: '#d1d5db' }}
                formatter={(v: number, name) =>
                  name === 'winRate' ? [`${v}%`, 'Win Rate'] : [v, 'Games']
                }
              />
              <Line
                type="monotone"
                dataKey="winRate"
                stroke={color}
                strokeWidth={3}
                dot={{ r: 4, fill: color, strokeWidth: 0 }}
                activeDot={{ r: 6, fill: color }}
              />
            </LineChart>
          </ResponsiveContainer>
        )}
      </section>
    </div>
  )
}

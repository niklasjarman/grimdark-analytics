import { useQuery } from '@tanstack/react-query'
import { Link, useParams } from 'react-router-dom'
import { api } from '../api/client'
import { PageHeader } from '../components/PageHeader'
import { StatCard } from '../components/StatCard'
import { Loading, ErrorState, Empty } from '../components/States'
import { FormatBadge } from '../components/FactionBadge'
import { formatDate } from '../lib/format'
import type { Tournament } from '../api/types'

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
  const { data, isLoading, isError, error } = useQuery({
    queryKey: ['tournament', id],
    queryFn: () => api.getTournament(id),
  })

  if (isLoading) return <Loading />
  if (isError) return <ErrorState error={error} />
  if (!data) return <Empty />

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

      <div className="card p-6 mt-6 text-sm text-gray-400">
        <h2 className="font-gothic text-lg text-gray-100 mb-2">Event Briefing</h2>
        <p>
          <span className="text-blood-bright font-semibold">{data.name}</span> was
          a {data.format} format event held in {data.location} on{' '}
          {formatDate(data.date)}, drawing {data.playerCount} commanders to the
          battlefield.
        </p>
      </div>
    </div>
  )
}

import type { ReactNode } from 'react'

export function StatCard({
  label,
  value,
  sub,
  accent = false,
}: {
  label: string
  value: ReactNode
  sub?: string
  accent?: boolean
}) {
  return (
    <div className="card card-hover p-5 relative overflow-hidden">
      <div
        className="absolute inset-x-0 top-0 h-px"
        style={{
          background: accent
            ? 'linear-gradient(90deg, transparent, #c41e3a, transparent)'
            : 'linear-gradient(90deg, transparent, #2a2a3a, transparent)',
        }}
      />
      <p className="text-xs uppercase tracking-widest text-gray-500">{label}</p>
      <p
        className={`mt-2 text-3xl font-gothic font-bold tabular-nums ${
          accent ? 'text-blood-bright' : 'text-gray-100'
        }`}
      >
        {value}
      </p>
      {sub && <p className="mt-1 text-xs text-gray-500">{sub}</p>}
    </div>
  )
}

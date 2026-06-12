import { factionColor } from '../lib/factions'

export function FactionBadge({ faction }: { faction: string }) {
  const color = factionColor(faction)
  return (
    <span
      className="inline-flex items-center gap-2 px-2.5 py-1 rounded-md text-xs font-semibold"
      style={{
        color,
        backgroundColor: `${color}1a`,
        border: `1px solid ${color}40`,
      }}
    >
      <span
        className="h-2 w-2 rounded-full"
        style={{ backgroundColor: color, boxShadow: `0 0 6px ${color}` }}
      />
      {faction}
    </span>
  )
}

export function FormatBadge({ format }: { format: string }) {
  return (
    <span className="inline-flex items-center px-2.5 py-1 rounded-md text-xs font-bold tracking-wider uppercase text-blood-bright bg-blood/15 border border-blood/40">
      {format}
    </span>
  )
}

import { pct, winRateBarColor } from '../lib/format'

export function WinRateBar({
  rate,
  showLabel = true,
  height = 8,
}: {
  rate: number
  showLabel?: boolean
  height?: number
}) {
  const width = Math.max(0, Math.min(100, rate * 100))
  const color = winRateBarColor(rate)
  return (
    <div className="flex items-center gap-3 w-full">
      <div
        className="flex-1 rounded-full bg-grim-bg/80 overflow-hidden border border-grim-border"
        style={{ height }}
      >
        <div
          className="h-full rounded-full transition-all duration-500"
          style={{
            width: `${width}%`,
            backgroundColor: color,
            boxShadow: `0 0 10px ${color}80`,
          }}
        />
      </div>
      {showLabel && (
        <span
          className="text-sm font-semibold tabular-nums w-14 text-right"
          style={{ color }}
        >
          {pct(rate)}
        </span>
      )}
    </div>
  )
}

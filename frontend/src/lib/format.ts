export function pct(rate: number): string {
  return `${(rate * 100).toFixed(1)}%`
}

// Returns a tailwind text colour class for a win rate (0-1).
export function winRateTextClass(rate: number): string {
  if (rate >= 0.55) return 'text-emerald-400'
  if (rate >= 0.45) return 'text-amber-400'
  return 'text-red-400'
}

// Returns a hex colour for a win rate bar (0-1).
export function winRateBarColor(rate: number): string {
  if (rate >= 0.55) return '#34d399' // emerald
  if (rate >= 0.45) return '#fbbf24' // amber
  return '#f87171' // red
}

export function formatDate(iso: string): string {
  const d = new Date(iso)
  if (isNaN(d.getTime())) return iso
  return d.toLocaleDateString('en-GB', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  })
}

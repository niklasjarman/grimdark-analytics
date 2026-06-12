export function Loading({ label = 'Summoning data…' }: { label?: string }) {
  return (
    <div className="flex flex-col items-center justify-center py-20 text-gray-400">
      <div className="h-10 w-10 rounded-full border-2 border-grim-border border-t-blood-bright animate-spin" />
      <p className="mt-4 text-sm font-gothic tracking-wide">{label}</p>
    </div>
  )
}

export function ErrorState({ error }: { error: unknown }) {
  const message = error instanceof Error ? error.message : 'Unknown error'
  return (
    <div className="card p-6 border-blood/60">
      <h3 className="font-gothic text-blood-bright text-lg">The vox is silent</h3>
      <p className="mt-2 text-sm text-gray-400">
        Could not retrieve data from the cogitators.
      </p>
      <p className="mt-1 text-xs text-gray-600 font-mono">{message}</p>
    </div>
  )
}

export function Empty({ label = 'No records found.' }: { label?: string }) {
  return (
    <div className="card p-10 text-center text-gray-500">
      <p className="font-gothic">{label}</p>
    </div>
  )
}

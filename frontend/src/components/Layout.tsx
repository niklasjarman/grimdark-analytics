import { NavLink, Outlet } from 'react-router-dom'

const NAV = [
  { to: '/', label: 'Dashboard', end: true },
  { to: '/factions', label: 'Faction Meta' },
  { to: '/matchups', label: 'Matchups' },
  { to: '/tournaments', label: 'Tournaments' },
  { to: '/players', label: 'Players' },
]

function Skull() {
  return (
    <svg viewBox="0 0 24 24" className="h-7 w-7 fill-blood-bright" aria-hidden>
      <path d="M12 2C7.6 2 4 5.6 4 10c0 2.4 1 4.5 2.7 6V19c0 .6.4 1 1 1h1.3v-2h2v2h2v-2h2v2h1.3c.6 0 1-.4 1-1v-3c1.7-1.5 2.7-3.6 2.7-6 0-4.4-3.6-8-8-8zM9 13a2 2 0 110-4 2 2 0 010 4zm6 0a2 2 0 110-4 2 2 0 010 4z" />
    </svg>
  )
}

export function Layout() {
  return (
    <div className="min-h-screen flex flex-col">
      <header className="sticky top-0 z-30 backdrop-blur-md bg-grim-bg/80 border-b border-grim-border">
        <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
          <NavLink to="/" className="flex items-center gap-3 group">
            <Skull />
            <div className="leading-none">
              <span className="font-gothic font-bold text-xl tracking-wide text-gray-100 group-hover:text-blood-bright transition-colors">
                GRIM<span className="text-blood-bright">STATS</span>
              </span>
              <p className="text-[10px] uppercase tracking-[0.3em] text-gray-600 mt-0.5">
                40k Meta Analytics
              </p>
            </div>
          </NavLink>

          <nav className="flex items-center gap-1">
            {NAV.map((item) => (
              <NavLink
                key={item.to}
                to={item.to}
                end={item.end}
                className={({ isActive }) =>
                  `px-4 py-2 rounded-lg text-sm font-medium transition-all ${
                    isActive
                      ? 'text-blood-bright bg-blood/10 shadow-glow'
                      : 'text-gray-400 hover:text-gray-100 hover:bg-grim-hover'
                  }`
                }
              >
                {item.label}
              </NavLink>
            ))}
          </nav>
        </div>
      </header>

      <main className="flex-1 max-w-7xl w-full mx-auto px-6 py-8">
        <Outlet />
      </main>

      <footer className="border-t border-grim-border">
        <div className="max-w-7xl mx-auto px-6 py-6 text-center text-xs text-gray-600">
          GrimStats — In the grim darkness of the far future, there is only
          data. <span className="text-grim-border">·</span> Built for the
          Warhammer 40,000 tournament meta.
        </div>
      </footer>
    </div>
  )
}

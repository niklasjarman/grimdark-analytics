import { Link } from 'react-router-dom'

export function NotFound() {
  return (
    <div className="flex flex-col items-center justify-center py-24 text-center">
      <p className="font-gothic text-7xl font-bold text-blood-bright">404</p>
      <p className="mt-4 font-gothic text-xl text-gray-300">
        This sector is lost to the Warp.
      </p>
      <Link
        to="/"
        className="mt-6 px-6 py-2.5 rounded-lg font-gothic font-semibold text-white bg-blood hover:bg-blood-bright transition-all shadow-glow"
      >
        Return to the Dashboard
      </Link>
    </div>
  )
}

import { Routes, Route } from 'react-router-dom'
import { Layout } from './components/Layout'
import { Dashboard } from './pages/Dashboard'
import { FactionMeta } from './pages/FactionMeta'
import { FactionDetail } from './pages/FactionDetail'
import { Matchups } from './pages/Matchups'
import { Tournaments } from './pages/Tournaments'
import { Players } from './pages/Players'
import { NotFound } from './pages/NotFound'

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<Dashboard />} />
        <Route path="/factions" element={<FactionMeta />} />
        <Route path="/factions/:faction" element={<FactionDetail />} />
        <Route path="/matchups" element={<Matchups />} />
        <Route path="/tournaments" element={<Tournaments />} />
        <Route path="/tournaments/:id" element={<Tournaments />} />
        <Route path="/players" element={<Players />} />
        <Route path="*" element={<NotFound />} />
      </Route>
    </Routes>
  )
}

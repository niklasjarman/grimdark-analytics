// Thematic faction colour mapping. Falls back to a deterministic palette
// colour for any faction the API returns that isn't listed here.

const FACTION_COLORS: Record<string, string> = {
  'Space Marines': '#4a7fb5', // steel blue
  'Adeptus Astartes': '#4a7fb5',
  Necrons: '#39ff7a', // glowing green
  Tyranids: '#9b59b6', // purple
  Aeldari: '#e8c547', // yellow-gold
  'Chaos Space Marines': '#c0392b', // red
  'Death Guard': '#7a8b3a', // sickly green
  Orks: '#5a9e3a', // ork green
  "T'au Empire": '#1ab5a8', // teal
  Tau: '#1ab5a8',
  'Astra Militarum': '#6b7a4a', // military drab
  'World Eaters': '#a01818', // brutal red
  'Adeptus Mechanicus': '#b05a2a', // copper/rust
  'Grey Knights': '#8fa3b3',
  'Adepta Sororitas': '#9c2b3a',
  "Genestealer Cults": '#7b4fa0',
  'Imperial Knights': '#3d6e9e',
  'Chaos Knights': '#8b2020',
  "Thousand Sons": '#2b8fb0',
  "Drukhari": '#5b2a6e',
  "Leagues of Votann": '#c98a2a',
  "Custodes": '#c9a227',
  "Adeptus Custodes": '#c9a227',
  "Dark Angels": '#1f5c3a',
  "Blood Angels": '#a01828',
  "Space Wolves": '#7a8a9a',
  "Black Templars": '#cfcfcf',
  Tyrants: '#9b59b6',
}

const FALLBACK_PALETTE = [
  '#c41e3a',
  '#4a7fb5',
  '#39ff7a',
  '#9b59b6',
  '#e8c547',
  '#1ab5a8',
  '#b05a2a',
  '#5a9e3a',
]

// Official faction artwork from Wikimedia Commons (CC/fair-use GW promotional art)
const FACTION_IMAGES: Record<string, string> = {
  'Aeldari': 'https://upload.wikimedia.org/wikipedia/en/1/1e/WH40K_Eldar_Guardian.jpg',
  'Necrons': 'https://upload.wikimedia.org/wikipedia/en/6/69/WH40K_Necron_Warrior.jpg',
  'Tyranids': 'https://upload.wikimedia.org/wikipedia/en/a/af/WH40K_Tyranid_Warrior.jpg',
  'Chaos Space Marines': 'https://upload.wikimedia.org/wikipedia/en/1/17/WH40K_Chaos_Space_Marine.jpg',
  "T'au Empire": 'https://upload.wikimedia.org/wikipedia/en/f/f5/WH40K_Tau_Cadre_Fireblade.jpg',
  'Tau': 'https://upload.wikimedia.org/wikipedia/en/f/f5/WH40K_Tau_Cadre_Fireblade.jpg',
  'Orks': 'https://upload.wikimedia.org/wikipedia/en/d/de/WH40K_Ork_Boy.png',
  'Astra Militarum': 'https://upload.wikimedia.org/wikipedia/en/8/8c/WH40K_Cadian_Shock_Troop.jpg',
  'Leagues of Votann': 'https://upload.wikimedia.org/wikipedia/en/c/ce/WH40K_League_of_Votann_warrior.jpg',
  'Space Marines': 'https://upload.wikimedia.org/wikipedia/commons/8/86/40k_Space_Marine.png',
  'Adeptus Astartes': 'https://upload.wikimedia.org/wikipedia/commons/8/86/40k_Space_Marine.png',
  'Death Guard': 'https://upload.wikimedia.org/wikipedia/commons/8/81/Wh40k_painting_miniature.jpg',
}

export function factionImage(faction: string): string | null {
  return FACTION_IMAGES[faction] ?? null
}

export function factionColor(faction: string): string {
  if (FACTION_COLORS[faction]) return FACTION_COLORS[faction]
  let hash = 0
  for (let i = 0; i < faction.length; i++) {
    hash = (hash * 31 + faction.charCodeAt(i)) >>> 0
  }
  return FALLBACK_PALETTE[hash % FALLBACK_PALETTE.length]
}

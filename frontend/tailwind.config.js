/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  theme: {
    extend: {
      colors: {
        grim: {
          bg: '#0a0a0f',
          card: '#13131a',
          border: '#1e1e2e',
          hover: '#1a1a24',
        },
        blood: {
          DEFAULT: '#8b0000',
          bright: '#c41e3a',
        },
      },
      fontFamily: {
        gothic: ['Cinzel', 'Georgia', 'serif'],
        body: ['Inter', 'system-ui', 'sans-serif'],
      },
      boxShadow: {
        glow: '0 0 20px rgba(196, 30, 58, 0.15)',
        'glow-strong': '0 0 30px rgba(196, 30, 58, 0.35)',
      },
    },
  },
  plugins: [],
}

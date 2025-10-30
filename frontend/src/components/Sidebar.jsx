import { Link, useLocation } from 'react-router-dom'
import { useTheme } from '../design/theme.jsx'

export default function Sidebar() {
  const { theme, toggle } = useTheme()
  const { pathname } = useLocation()
  const Item = ({ to, label, icon }) => (
    <Link to={to} className="flex items-center gap-3 px-3 py-2 rounded hover:bg-tertiary">
      {icon}
      <span className="hidden lg:inline">{label}</span>
    </Link>
  )
  return (
    <aside
      className="h-screen fixed top-0 left-0 bg-secondary border-r border-base transition-all duration-300 w-[60px] lg:w-[220px]"
      style={{ background: 'var(--bg-secondary)', borderRight: '1px solid var(--border)' }}
    >
      <div className="px-3 py-4 flex items-center gap-2">
        <div className="w-8 h-8 rounded bg-[var(--primary)] flex items-center justify-center text-white font-bold">S</div>
        <div className="hidden lg:block">
          <div className="font-semibold">Shodh</div>
          <div className="text-xs text-secondary">Coding</div>
        </div>
      </div>
      <nav className="px-3 space-y-1">
        <Item to="/" label="Home" icon={<svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 001 1h3m-6-11v10a1 1 0 001 1h3" />
        </svg>} />
        <Item to="/events" label="Events" icon={<svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 1 0 002 2z" />
        </svg>} />
      </nav>
      <div className="mt-auto px-3 py-4 absolute bottom-0 left-0 right-0">
        <button onClick={toggle} className="w-full px-3 py-2 border border-base rounded flex items-center justify-center gap-2">
          <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 3v1m0 16v1m9-9h1M3 12H2m15.325-7.757l.707-.707M6.343 17.657l-.707.707M16.95 7.05l.707-.707M6.343 6.343l-.707-.707M12 12a3 3 0 110-6 3 3 0 010 6z" />
          </svg>
          <span className="hidden lg:inline">{theme === 'light' ? 'Dark' : 'Light'} Mode</span>
        </button>
      </div>
    </aside>
  )
}

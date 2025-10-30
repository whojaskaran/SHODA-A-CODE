import { Link, useLocation } from 'react-router-dom'
import { useTheme } from '../design/theme.jsx'

export default function Navbar() {
  const { theme, toggle } = useTheme()
  const { pathname } = useLocation()
  const NavItem = ({ to, label }) => (
    <Link to={to} className={`px-3 py-2 rounded-md text-sm font-medium ${pathname === to ? 'bg-tertiary' : 'hover:bg-tertiary'}`}>
      {label}
    </Link>
  )
  return (
    <header
      className="w-full sticky top-0 left-0 bg-secondary border-b border-base z-10"
      style={{ background: 'var(--bg-surface)', borderBottom: '1px solid var(--border)' }}
    >
      <div className="container-page flex items-center justify-between h-16">
        <div className="flex items-center gap-8">
            <Link to="/" className="flex items-center gap-2">
                <div className="w-9 h-9 rounded-lg bg-[var(--primary)] flex items-center justify-center text-white font-bold text-lg">S</div>
                <div className="font-semibold text-lg">Shodh</div>
            </Link>
            <nav className="hidden md:flex items-center gap-4">
                <NavItem to="/" label="Home" />
                <NavItem to="/events" label="Events" />
            </nav>
        </div>

        <div className="flex items-center gap-4">
            <button onClick={toggle} className="p-2 border border-transparent rounded-full hover:bg-tertiary flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 3v1m0 16v1m9-9h1M3 12H2m15.325-7.757l.707-.707M6.343 17.657l-.707.707M16.95 7.05l.707-.707M6.343 6.343l-.707-.707M12 12a3 3 0 110-6 3 3 0 010 6z" />
              </svg>
            </button>
        </div>
      </div>
    </header>
  )
}

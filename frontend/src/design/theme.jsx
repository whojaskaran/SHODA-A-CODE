import { createContext, useContext, useEffect, useMemo, useState } from 'react'

const AppThemes = {
  light: {
    primary: '#10b981', // emerald-500
    primary_light: '#34d399', // emerald-400
    primary_dark: '#059669', // emerald-600
    background_main: '#f9fafb', // gray-50
    background_secondary: '#f3f4f6', // gray-100
    background_tertiary: '#e5e7eb', // gray-200
    surface: '#ffffff',
    text_primary: '#111827', // gray-900
    text_secondary: '#4b5563', // gray-600
    border: '#d1d5db' // gray-300
  },
  dark: {
    primary: '#2dd4bf', // teal-400
    primary_light: '#5eead4', // teal-300
    primary_dark: '#0d9488', // teal-600
    background_main: '#0f172a', // slate-900
    background_secondary: '#1e293b', // slate-800
    background_tertiary: '#334155', // slate-700
    surface: '#1e293b', // slate-800
    text_primary: '#f1f5f9', // slate-100
    text_secondary: '#94a3b8', // slate-400
    border: '#334155' // slate-700
  }
}

const ThemeContext = createContext({ theme: 'light', colors: AppThemes.light, toggle: () => {} })

export function ThemeProvider({ children }) {
  const initial = localStorage.getItem('coding_test_theme') || 'light'
  const [theme, setTheme] = useState(initial)
  useEffect(() => { localStorage.setItem('coding_test_theme', theme); document.documentElement.setAttribute('data-theme', theme) }, [theme])
  const value = useMemo(() => ({ theme, colors: AppThemes[theme], toggle: () => setTheme(t => t === 'light' ? 'dark' : 'light') }), [theme])
  return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>
}

export function useTheme() { return useContext(ThemeContext) }



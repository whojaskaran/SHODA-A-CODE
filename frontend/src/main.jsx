import React from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import App from './pages/App.jsx'
import Join from './pages/Join.jsx'
import Contest from './pages/Contest.jsx'
import Events from './pages/Events.jsx'
import { ThemeProvider } from './design/theme.jsx'
import Layout from './components/Layout.jsx'
import ContestLeaderboard from './pages/ContestLeaderboard.jsx'

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      { index: true, element: <App /> },
      { path: 'join', element: <Join /> },
      { path: 'events', element: <Events /> },
      { path: 'contest/:contestId', element: <Contest /> },
      { path: 'contest/:contestId/leaderboard', element: <ContestLeaderboard /> },
    ],
  },
])

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <ThemeProvider>
      <RouterProvider router={router} />
    </ThemeProvider>
  </React.StrictMode>
)



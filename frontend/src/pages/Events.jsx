import { useEffect, useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import Spinner from '../components/Spinner.jsx'

const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080'

export default function Events() {
  const [contestList, setContestList] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const navigate = useNavigate()

  useEffect(() => {
    setIsLoading(true)
    fetch(`${API_BASE}/api/contests`)
      .then(r => r.ok ? r.json() : Promise.reject())
      .then(setContestList)
      .catch(() => {})
      .finally(() => setIsLoading(false))
  }, [])

  const onEventClick = (eventId) => {
    let username = sessionStorage.getItem('username')
    if (!username) {
      username = prompt('Please enter your username:')
      if (username) {
        sessionStorage.setItem('username', username)
      } else {
        return // User cancelled or entered empty username
      }
    }
    navigate(`/contest/${eventId}`)
  }

  return (
    <div className="container-page py-12">
      <div className="text-center mb-12">
        <h1 className="text-4xl md:text-5xl font-bold tracking-tight mb-2 bg-gradient-to-r from-[var(--primary)] to-[var(--primary-dark)] text-transparent bg-clip-text">
          Available Contests
        </h1>
        <p className="text-lg text-secondary">
          Choose an event to start competing.
        </p>
      </div>

      {isLoading ? (
        <div className="flex items-center justify-center gap-2 text-secondary"><Spinner size={32} /> <span>Loading events...</span></div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {contestList.map(event => (
            <div key={event.id} className="card p-6 flex flex-col justify-between transition-all hover:shadow-lg hover:-translate-y-1">
              <div onClick={() => onEventClick(event.id)} className="cursor-pointer flex-grow">
                <h2 className="text-xl font-semibold mb-2 text-primary">{event.name}</h2>
                <p className="text-secondary">{event.problems?.length || 0} problems</p>
              </div>
              <div className="mt-6 flex justify-between items-center">
                <Link to={`/contest/${event.id}/leaderboard`} className="text-sm font-medium text-primary hover:underline">
                  Leaderboard
                </Link>
                <button onClick={() => onEventClick(event.id)} className="btn-primary px-4 py-2 rounded-lg font-semibold">
                  Enter
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}


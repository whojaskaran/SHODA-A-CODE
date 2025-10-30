import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Spinner from '../components/Spinner.jsx'

export default function Join() {
  const [id, setId] = useState('')
  const [user, setUser] = useState('')
  const navigate = useNavigate()
  const [isJoining, setJoining] = useState(false)

  function handleSubmit(e) {
    e.preventDefault()
    if (!id || !user) return
    setJoining(true)
    sessionStorage.setItem('username', user)
    navigate(`/contest/${id}`)
  }

  return (
    <div className="min-h-full flex items-center justify-center p-6">
      <form onSubmit={handleSubmit} className="max-w-md w-full card p-8 space-y-4">
        <h2 className="text-xl font-semibold text-primary">Join Contest</h2>
        <div>
          <label className="block text-sm text-secondary mb-1">Contest ID</label>
          <input value={id} onChange={e=>setId(e.target.value)} className="w-full border border-base rounded px-3 py-2" placeholder="paste contest id" />
        </div>
        <div>
          <label className="block text-sm text-secondary mb-1">Username</label>
          <input value={user} onChange={e=>setUser(e.target.value)} className="w-full border border-base rounded px-3 py-2" placeholder="your name" />
        </div>
        <button className="w-full btn-primary px-4 py-2 rounded inline-flex items-center justify-center gap-2 disabled:opacity-60" type="submit" disabled={isJoining}>
          {isJoining && <Spinner size={18} className="text-white" />}<span>Enter</span>
        </button>
      </form>
    </div>
  )
}



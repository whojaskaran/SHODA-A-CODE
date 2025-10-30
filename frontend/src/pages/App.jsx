import { Link } from 'react-router-dom'

export default function App() {
  return (
    <div className="container-page">
        <div className="text-center py-24 md:py-32">
            <h1 className="text-4xl md:text-6xl font-bold tracking-tight mb-4 bg-gradient-to-r from-[var(--primary)] to-[var(--primary-dark)] text-transparent bg-clip-text">
                Welcome to Shodh-a-Code
            </h1>
            <p className="text-lg md:text-xl text-secondary mb-8 max-w-2xl mx-auto">
                The ultimate platform for real-time coding contests. Sharpen your skills, compete with others, and climb the leaderboard.
            </p>
            <div className="flex justify-center gap-4">
                <Link className="inline-flex items-center btn-primary px-6 py-3 rounded-lg text-lg font-semibold" to="/join">
                    Join a Contest
                </Link>
                <Link className="inline-flex items-center px-6 py-3 rounded-lg text-lg font-semibold border border-base hover:bg-tertiary" to="/events">
                    View Events
                </Link>
            </div>
        </div>
    </div>
  )
}




import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Leaderboard from '../components/Leaderboard.jsx';
import Spinner from '../components/Spinner.jsx';

const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080';

export default function ContestLeaderboard() {
  const { contestId } = useParams();
  const [scores, setScores] = useState([]);
  const [contestInfo, setContestInfo] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(true);
    Promise.all([
      fetch(`${API_BASE}/api/contests/${contestId}/leaderboard`).then(r => r.ok ? r.json() : []),
      fetch(`${API_BASE}/api/contests/${contestId}`).then(r => r.ok ? r.json() : null)
    ])
    .then(([leaderboardData, contestData]) => {
      setScores(leaderboardData);
      setContestInfo(contestData);
    })
    .catch(() => {})
    .finally(() => setIsLoading(false));
  }, [contestId]);

  return (
    <div className="container-page py-8">
      <h1 className="text-2xl font-semibold text-primary mb-1">Leaderboard</h1>
      {contestInfo && <h2 className="text-xl font-semibold text-secondary mb-6">{contestInfo.name}</h2>}
      {isLoading ? (
        <div className="flex items-center gap-2 text-secondary"><Spinner /> <span>Loading leaderboard...</span></div>
      ) : (
        <div className="card p-4 relative">
          <Leaderboard entries={scores} />
        </div>
      )}
    </div>
  );
}

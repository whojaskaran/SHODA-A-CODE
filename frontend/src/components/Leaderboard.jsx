export default function Leaderboard({ results }) {
  if (!results || results.length === 0) {
    return (
      <div className="text-sm text-secondary">No entries yet.</div>
    )
  }
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full text-sm">
        <thead>
          <tr className="text-left text-secondary">
            <th className="py-2 pr-4">Rank</th>
            <th className="py-2 pr-4">User</th>
            <th className="py-2 pr-4">Score</th>
          </tr>
        </thead>
        <tbody>
          {results.map((e, i) => (
            <tr key={e.userId || i} className="border-t border-base">
              <td className="py-2 pr-4">{i + 1}</td>
              <td className="py-2 pr-4">{e.username || e.userId}</td>
              <td className="py-2 pr-4">{e.score ?? '-'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}



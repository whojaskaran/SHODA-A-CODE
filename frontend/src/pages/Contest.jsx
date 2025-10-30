import { useEffect, useMemo, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Allotment } from 'allotment'
import 'allotment/dist/style.css'
import Leaderboard from '../components/Leaderboard.jsx'
import Spinner from '../components/Spinner.jsx'
import TestResults from '../components/TestResults.jsx'

const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080'

const testEventSolutions = {
  java: {
    "Hello World": `public class Main {
  public static void main(String[] args) {
    System.out.println("Hello, World!");
  }
}`,
    "Sum of Two Numbers": `import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    long x = s.nextLong();
    long y = s.nextLong();
    System.out.println(x + y);
  }
}`
  },
  cpp: {
    "Hello World": `#include <iostream>

int main() {
    std::cout << "Hello, World!" << std::endl;
    return 0;
}`,
    "Sum of Two Numbers": `#include <iostream>

int main() {
    long x, y;
    std::cin >> x >> y;
    std::cout << x + y << std::endl;
    return 0;
}`
  },
  python: {
    "Hello World": `print("Hello, World!")`,
    "Sum of Two Numbers": `x, y = map(int, input().split())
print(x + y)`
  },
  javascript: {
    "Hello World": `console.log("Hello, World!");`,
    "Sum of Two Numbers": `const readline = require('readline');
const rl = readline.createInterface({ input: process.stdin, output: process.stdout });
rl.on('line', (line) => {
    const [x, y] = line.split(' ').map(Number);
    console.log(x + y);
    rl.close();
});`
  }
};

const defaultCodes = {
  java: `public class Main {
  public static void main(String[] args) {
    // Your code here
  }
}`,
  cpp: `#include <iostream>

int main() {
    // Your code here
    return 0;
}`,
  python: `# Your code here`,
  javascript: `// Your code here`
};

export default function Contest() {
  const { contestId } = useParams()
  const [currentContest, setCurrentContest] = useState(null)
  const [isLoadingContest, setIsLoadingContest] = useState(true)
  const [activeProblem, setActiveProblem] = useState('')
  const [lang, setLang] = useState('java')
  const [codeSources, setCodeSources] = useState({ java: '', cpp: '', python: '', javascript: '' })
  const [currentSubmissionId, setCurrentSubmissionId] = useState('')
  const [submissionMessage, setSubmissionMessage] = useState('')
  const [submissionState, setSubmissionState] = useState('')
  const [details, setDetails] = useState(null)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [isRunning, setIsRunning] = useState(false)
  const [output, setOutput] = useState(null)
  const [testResults, setTestResults] = useState(null)
  const [scores, setScores] = useState([])
  const [isLoadingScores, setIsLoadingScores] = useState(true)
  const username = useMemo(() => sessionStorage.getItem('username') || 'demo-user', [])

  useEffect(() => {
    setIsLoadingContest(true)
    fetch(`${API_BASE}/api/contests/${contestId}`)
      .then(r => r.ok ? r.json() : Promise.reject())
      .then(data => {
        setCurrentContest(data)
        const first = data?.problems?.[0]?.id
        if (first) setActiveProblem(first)
      })
      .catch(() => {})
      .finally(() => setIsLoadingContest(false))
  }, [contestId])

  useEffect(() => {
    if (!currentContest || !activeProblem) return;

    const problem = currentContest.problems.find(p => p.id === activeProblem);
    if (!problem) return;

    const newCodes = { ...codeSources };
    let changed = false;
    for (const l of ['java', 'cpp', 'python', 'javascript']) {
      if (newCodes[l]) continue;
      if (currentContest.name === 'Test Event' && testEventSolutions[l] && testEventSolutions[l][problem.title]) {
        newCodes[l] = testEventSolutions[l][problem.title];
        changed = true;
      } else {
        newCodes[l] = defaultCodes[l];
        changed = true;
      }
    }
    if (changed) {
      setCodeSources(newCodes);
    }
  }, [currentContest, activeProblem]);

  useEffect(() => {
    const load = () => {
      setIsLoadingScores(true)
      fetch(`${API_BASE}/api/contests/${contestId}/leaderboard`)
        .then(r => r.ok ? r.json() : [])
        .then(setScores)
        .catch(() => {})
        .finally(() => setIsLoadingScores(false))
    }
    load()
    const iv = setInterval(load, 15000)
    return () => clearInterval(iv)
  }, [contestId])

  useEffect(() => {
    if (!currentSubmissionId) return
    const terminal = (st) => ['ACCEPTED','WRONG_ANSWER','COMPILE_ERROR','TIME_LIMIT_EXCEEDED','FAILED','RUNTIME_ERROR'].includes(st)
    const iv = setInterval(() => {
      fetch(`${API_BASE}/api/submissions/${currentSubmissionId}`)
        .then(r => r.ok ? r.json() : Promise.reject())
        .then(s => {
          let statusStr = s.status;
          if (s.status === 'ACCEPTED' || s.status === 'WRONG_ANSWER') {
            if (s.testCaseResults && s.testCaseResults.length > 0) {
              const passedCount = s.testCaseResults.filter(r => r.passed).length;
              const totalCount = s.testCaseResults.length;
              statusStr += ` - ${passedCount}/${totalCount} tests passed`;
            }
          }
          setSubmissionMessage(statusStr);
          setSubmissionState(s.status);
          setDetails(s);
          if (terminal(s.status)) {
            clearInterval(iv);
          }
        })
        .catch(() => {})
    }, 2500)
    return () => clearInterval(iv)
  }, [currentSubmissionId])

  function onRun() {
    if (!activeProblem) return
    const payload = {
      contestId,
      problemId: activeProblem,
      userId: username,
      language: lang,
      sourceCode: codeSources[lang]
    }
    setIsRunning(true)
    setOutput(null)
    setTestResults(null)
    setDetails(null)
    fetch(`${API_BASE}/api/run`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload)
    })
      .then(r => r.ok ? r.json() : Promise.reject())
      .then(data => {
        setOutput(data.output)
        setTestResults(data.testCaseResults)
      })
      .catch(() => {})
      .finally(() => setIsRunning(false))
  }

  function onSubmit() {
    if (!activeProblem) return
    const payload = {
      contestId,
      problemId: activeProblem,
      userId: username,
      language: lang,
      sourceCode: codeSources[lang]
    }
    setIsSubmitting(true)
    setSubmissionMessage('Submitting...')
    setDetails(null)
    setOutput(null)
    setTestResults(null)
    fetch(`${API_BASE}/api/submissions`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload)
    })
      .then(r => r.ok ? r.json() : Promise.reject())
      .then(s => { setCurrentSubmissionId(s.id); setSubmissionMessage(s.status); setSubmissionState(s.status); setDetails(s) })
      .catch(() => setSubmissionMessage('Submit failed'))
      .finally(() => setIsSubmitting(false))
  }

  return (
    <div className="w-full min-h-screen flex flex-col">
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 flex-1 p-6">
        <div className="flex flex-col gap-6 lg:col-span-2 h-[95vh] lg:h-full">
          <Allotment vertical>
            <Allotment.Pane>
              <div className="card p-4 h-full overflow-y-auto">
                <div className="flex items-center justify-between mb-3">
                  <h2 className="font-medium text-primary">Problem</h2>
                  <select className="border border-base rounded px-2 py-1" value={activeProblem} onChange={e=>setActiveProblem(e.target.value)}>
                    {currentContest?.problems?.map(p => (
                      <option key={p.id} value={p.id}>{p.title}</option>
                    ))}
                  </select>
                </div>
                {isLoadingContest ? (
                  <div className="flex items-center gap-2 text-secondary"><Spinner /> <span>Loading...</span></div>
                ) : (
                  <div className="prose max-w-none">
                    <p className="text-secondary whitespace-pre-wrap">{currentContest?.problems?.find(p => p.id === activeProblem)?.description || 'No description'}</p>
                  </div>
                )}
              </div>
            </Allotment.Pane>
            <Allotment.Pane>
              <div className="card p-0 h-full overflow-hidden flex flex-col">
                <div className="flex items-center justify-between px-4 py-3 border-b border-base">
                  <h2 className="font-medium text-primary">Code editor</h2>
                  <div className="flex items-center gap-4">
                    <select className="border border-base rounded px-2 py-1 bg-secondary" value={lang} onChange={e => setLang(e.target.value)}>
                      <option value="java">Java</option>
                      <option value="cpp">C++</option>
                      <option value="python">Python</option>
                      <option value="javascript">JavaScript</option>
                    </select>
                    <button onClick={onRun} disabled={isRunning || isSubmitting} className="btn-secondary px-4 py-2 rounded inline-flex items-center gap-2">
                      {isRunning && <Spinner size={18} />}<span>Run ▷</span>
                    </button>
                  </div>
                </div>
                <textarea className="w-full code-surface p-4 font-mono text-sm flex-1 border-t border-base" value={codeSources[lang]} onChange={e => setCodeSources({...codeSources, [lang]: e.target.value})} />
                <div className="flex items-center gap-3 px-4 py-3 border-t border-base">
                  <button onClick={onSubmit} disabled={isSubmitting || isRunning} className="btn-primary disabled:opacity-60 px-4 py-2 rounded inline-flex items-center gap-2">
                    {isSubmitting && <Spinner size={18} className="text-white" />}<span>Submit</span>
                  </button>
                  <div className="text-sm text-secondary flex items-center gap-2">
                    {currentSubmissionId && !isSubmitting && !['ACCEPTED','WRONG_ANSWER','COMPILE_ERROR','TIME_LIMIT_EXCEEDED','FAILED','RUNTIME_ERROR'].includes(submissionState) && (
                      <Spinner size={16} />
                    )}
                    <span>{submissionMessage}</span>
                  </div>
                </div>
              </div>
            </Allotment.Pane>
          </Allotment>
        </div>

        <div className="space-y-6 overflow-y-auto lg:col-span-1">
          <div className="card p-4 relative max-h-[50vh] overflow-y-auto">
            <h2 className="font-medium mb-2">Leaderboard</h2>
            {isLoadingScores && <div className="loading-bar"></div>}
            {isLoadingScores ? (
              <div className="flex items-center gap-2 text-secondary"><Spinner /> <span>Loading...</span></div>
            ) : (
              <Leaderboard entries={scores} />
            )}
          </div>
          {output && <TestResults output={output} testCaseResults={testResults} />}
          {details && <TestResults submission={details} />}
        </div>
      </div>
    </div>
  )
}

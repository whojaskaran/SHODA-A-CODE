export default function TestResults({ submission, output, testCaseResults }) {
  const outputToShow = output || submission?.output;
  const testCasesToShow = testCaseResults || submission?.testCaseResults;

  if (!submission && !output && !testCaseResults) {
    return (
      <div className="card p-4">
        <h2 className="font-medium mb-2">Test Results</h2>
        <div className="text-sm text-secondary">Run or submit your code to see test results.</div>
      </div>
    );
  }

  const resultsToShow = (submission && !output && !testCaseResults) ? testCasesToShow : (testCasesToShow ? testCasesToShow.slice(0, 2) : []);

  return (
    <div className="card p-4">
      <h2 className="font-medium mb-2">Test Results</h2>
      {outputToShow && (
        <div className="mb-4">
          <h3 className="font-semibold">Console Output</h3>
          <pre className="code-surface p-2 rounded text-sm whitespace-pre-wrap">{outputToShow}</pre>
        </div>
      )}
      {resultsToShow && resultsToShow.length > 0 && (
        <div className="space-y-4">
          {resultsToShow.map((result, index) => (
            <div key={index} className="border-b border-base pb-2">
              <div className="flex items-center justify-between">
                <h3 className="font-semibold">{result.name}</h3>
                <span className={`px-2 py-1 text-xs rounded ${result.passed ? 'bg-green-500 text-white' : 'bg-red-500 text-white'}`}>
                  {result.passed ? 'Passed' : 'Failed'}
                </span>
              </div>
              {!result.passed && (
                <div className="mt-2 text-sm">
                  <p><span className="font-semibold">Output:</span> <code className="code-surface p-1 rounded">{result.output}</code></p>
                  <p><span className="font-semibold">Expected:</span> <code className="code-surface p-1 rounded">{result.expectedOutput}</code></p>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
      {submission && !output && !testCaseResults && (
        <div className="text-sm text-secondary mt-4">
          Overall Status: {submission.status} {submission.resultMessage && `- ${submission.resultMessage}`}
        </div>
      )}
    </div>
  );
}

#!/usr/bin/env bash
set -euo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"

# Every batch gets full source/contract regression plus a focused emulator run.
# Every fifth batch additionally gets the longer emulator restart/persistence path.
batch="$(grep -E '^## Current batch under verification:' BUILD_PROGRESS.md | sed -E 's/.*: *([0-9]+).*/\1/' | head -n1 || true)"
if [[ -z "$batch" ]]; then
  batch=1
fi
if (( batch % 5 == 0 )); then
  mode=full
else
  mode=fast
fi

echo "QA_CURRENT_BATCH=$batch"
echo "QA_EMULATOR_MODE=$mode"
echo "QA_CURRENT_BATCH=$batch" >> "$GITHUB_ENV"
echo "QA_EMULATOR_MODE=$mode" >> "$GITHUB_ENV"

#!/usr/bin/env bash
set -euo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"

# Batch 13 is the current verification gate. Never silently verify an older batch.
batch=13
if grep -Eq '^## Current batch under verification:' BUILD_PROGRESS.md; then
  documented="$(grep -E '^## Current batch under verification:' BUILD_PROGRESS.md | sed -E 's/.*: *([0-9]+).*/\1/' | head -n1 || true)"
  if [[ -n "$documented" && "$documented" -gt "$batch" ]]; then
    batch="$documented"
  fi
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

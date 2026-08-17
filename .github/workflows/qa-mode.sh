#!/usr/bin/env bash
set -euo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"

# The cumulative gate is intentionally frozen at the current master-list milestone.
# Batch 12 is the release gate; do not silently downgrade QA to an earlier batch.
batch="$(grep -E '^## Current batch under verification:' BUILD_PROGRESS.md | sed -E 's/.*: *([0-9]+).*/\1/' | head -n1 || true)"
if [[ -z "$batch" || "$batch" -lt 12 ]]; then
  batch=12
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

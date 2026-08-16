#!/usr/bin/env bash
set -uo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"
PASS=0; FAIL=0
pass(){ echo "[BATCH9-PASS] $1"; PASS=$((PASS+1)); }
fail(){ echo "[BATCH9-FAIL] $1"; FAIL=$((FAIL+1)); }
FILE="app/src/main/java/com/amb8657/websitebuilder/Batch9FeatureActivity.java"
LAUNCH="app/src/main/java/com/amb8657/websitebuilder/Batch4PersistenceActivity.java"
for item in \
  'multi-selection:chooseBlocks' \
  'horizontal distribution:distributeHorizontal' \
  'vertical distribution:distributeVertical' \
  'match width:matchWidth' \
  'match height:matchHeight' \
  'align X:alignToFirstX' \
  'align Y:alignToFirstY' \
  'nudge left:nudgeLeft' \
  'nudge right:nudgeRight' \
  'nudge down:nudgeDown'; do
  label="${item%%:*}"; symbol="${item#*:}"
  grep -q "$symbol" "$FILE" && pass "$label" || fail "$label"
done
grep -q 'extends Batch9FeatureActivity' "$LAUNCH" && pass 'canonical launcher includes Batch 9' || fail 'canonical launcher includes Batch 9'
grep -q 'Batch 8' BUILD_PROGRESS.md && pass 'cumulative progress retained' || fail 'cumulative progress retained'
echo "BATCH9_PASS=$PASS"
echo "BATCH9_FAIL=$FAIL"
(( FAIL == 0 ))

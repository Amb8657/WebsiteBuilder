#!/usr/bin/env bash
set -uo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"
TARGET="app/src/main/java/com/amb8657/websitebuilder/Batch8FeatureActivity.java"
LAUNCHER="app/src/main/java/com/amb8657/websitebuilder/Batch4PersistenceActivity.java"
PASS=0
FAIL=0
check(){ if "$@"; then echo "[BATCH8-PASS] $2"; PASS=$((PASS+1)); else echo "[BATCH8-FAIL] $2"; FAIL=$((FAIL+1)); fi; }
[[ -f "$TARGET" ]] && check grep -q 'class Batch8FeatureActivity' "$TARGET" 'Batch 8 activity exists' || { echo '[BATCH8-FAIL] Batch 8 activity exists'; FAIL=$((FAIL+1)); }
check grep -q 'alignLeft' "$TARGET" 'Left alignment'
check grep -q 'alignCenter' "$TARGET" 'Horizontal center alignment'
check grep -q 'alignRight' "$TARGET" 'Right alignment'
check grep -q 'alignTop' "$TARGET" 'Top alignment'
check grep -q 'alignMiddle' "$TARGET" 'Vertical center alignment'
check grep -q 'alignBottom' "$TARGET" 'Bottom alignment'
check grep -q 'snapGrid' "$TARGET" '8dp grid snapping'
check grep -q 'bringFront' "$TARGET" 'Bring to front'
check grep -q 'sendBack' "$TARGET" 'Send to back'
check grep -q 'resetZoom' "$TARGET" 'Canvas zoom reset'
check grep -q 'extends Batch8FeatureActivity' "$LAUNCHER" 'Canonical launcher uses Batch 8'
echo "BATCH8_PASS=$PASS"
echo "BATCH8_FAIL=$FAIL"
((FAIL == 0))

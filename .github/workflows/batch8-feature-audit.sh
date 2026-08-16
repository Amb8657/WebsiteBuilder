#!/usr/bin/env bash
set -uo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"
TARGET="app/src/main/java/com/amb8657/websitebuilder/Batch8FeatureActivity.java"
LAUNCHER="app/src/main/java/com/amb8657/websitebuilder/Batch4PersistenceActivity.java"
PASS=0
FAIL=0
check(){
  local label="$1"; shift
  if "$@"; then echo "[BATCH8-PASS] $label"; PASS=$((PASS+1)); else echo "[BATCH8-FAIL] $label"; FAIL=$((FAIL+1)); fi
}
check 'Batch 8 activity exists' test -f "$TARGET"
check 'Left alignment' grep -q 'alignLeft' "$TARGET"
check 'Horizontal center alignment' grep -q 'alignCenter' "$TARGET"
check 'Right alignment' grep -q 'alignRight' "$TARGET"
check 'Top alignment' grep -q 'alignTop' "$TARGET"
check 'Vertical center alignment' grep -q 'alignMiddle' "$TARGET"
check 'Bottom alignment' grep -q 'alignBottom' "$TARGET"
check '8dp grid snapping' grep -q 'snapGrid' "$TARGET"
check 'Bring to front' grep -q 'bringFront' "$TARGET"
check 'Send to back' grep -q 'sendBack' "$TARGET"
check 'Canvas zoom reset' grep -q 'resetZoom' "$TARGET"
# Batch 9 inherits Batch 8, so the canonical launcher does not need to extend Batch 8 directly.
check 'Canonical launcher includes Batch 8 through inheritance' grep -Eq 'extends Batch(8|9)FeatureActivity' "$LAUNCHER"
echo "BATCH8_PASS=$PASS"
echo "BATCH8_FAIL=$FAIL"
if (( FAIL != 0 )); then exit 1; fi

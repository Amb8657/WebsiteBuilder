#!/usr/bin/env bash
set -euo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"
ROOT="app/src/main/java/com/amb8657/websitebuilder"
fail=0
check(){ local label="$1"; shift; if "$@"; then echo "[BATCH13-PASS] $label"; else echo "[BATCH13-FAIL] $label"; fail=1; fi; }
file="$ROOT/Batch13FeatureActivity.java"
launcher="$ROOT/Batch4PersistenceActivity.java"
check 'Batch 13 activity exists' test -f "$file"
check 'Batch 13 extends Batch 12' grep -q 'class Batch13FeatureActivity extends Batch12FeatureActivity' "$file"
check 'Canonical launcher reaches Batch 13' grep -q 'class Batch4PersistenceActivity extends Batch13FeatureActivity' "$launcher"
check 'Layers hierarchy' grep -q 'layersPanel' "$file"
check 'Multi-select' grep -q 'chooseBlocks' "$file"
check 'Grouping' grep -q 'groupSelected' "$file"
check 'Ungrouping' grep -q 'ungroupSelected' "$file"
check 'Duplicate element' grep -q 'duplicateSelected' "$file"
check 'Copy/paste' grep -q 'copyPaste' "$file"
check 'Lock enforcement' grep -q 'toggleLock' "$file"
check 'Hide/show' grep -q 'toggleHide' "$file"
check 'Canvas controls' grep -q 'canvasControls' "$file"
check 'Rulers / coordinates' grep -q 'rulers' "$file"
check 'Zoom controls' grep -q 'zoom(' "$file"
check 'Pan controls' grep -q 'pan(' "$file"
check 'Snap-to-grid enforcement' grep -q 'snapEnabled' "$file"
check 'Keyboard shortcuts' grep -q 'onKeyDown' "$file"
check 'Contextual rename' grep -q 'renameSelected' "$file"
if [[ "$fail" -ne 0 ]]; then exit 1; fi
echo 'BATCH13_AUDIT_GREEN: 18/18 checks passed'

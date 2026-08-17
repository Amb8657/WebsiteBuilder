#!/usr/bin/env bash
set -uo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"
TARGET="app/src/main/java/com/amb8657/websitebuilder/Batch12FeatureActivity.java"
LAUNCHER="app/src/main/java/com/amb8657/websitebuilder/Batch4PersistenceActivity.java"
PASS=0; FAIL=0
check(){ local label="$1"; shift; if "$@"; then echo "[BATCH12-PASS] $label"; PASS=$((PASS+1)); else echo "[BATCH12-FAIL] $label"; FAIL=$((FAIL+1)); fi; }
check 'Batch 12 activity exists' test -f "$TARGET"
check 'Desktop preview' grep -q 'desktopPreview' "$TARGET"
check 'Tablet preview' grep -q 'tabletPreview' "$TARGET"
check 'Mobile preview' grep -q 'mobilePreview' "$TARGET"
check 'Breakpoint presets' grep -q 'breakpointPreset' "$TARGET"
check 'Device-specific visibility' grep -q 'hideOnDevice' "$TARGET"
check 'Responsive positioning' grep -q 'responsivePosition' "$TARGET"
check 'Responsive sizing' grep -q 'responsiveSize' "$TARGET"
check 'Responsive typography' grep -q 'responsiveType' "$TARGET"
check 'Responsive sections' grep -q 'responsiveSection' "$TARGET"
check 'Responsive settings reset' grep -q 'resetResponsive' "$TARGET"
check 'Canonical launcher reaches Batch 13' grep -q 'extends Batch13FeatureActivity' "$LAUNCHER"
echo "BATCH12_PASS=$PASS"; echo "BATCH12_FAIL=$FAIL"; if (( FAIL != 0 )); then exit 1; fi

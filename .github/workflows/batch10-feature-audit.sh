#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
SRC="$ROOT/app/src/main/java/com/amb8657/websitebuilder"
B10="$SRC/Batch10FeatureActivity.java"
LAUNCH="$SRC/Batch4PersistenceActivity.java"
check(){ local pattern="$1"; local file="$2"; local label="$3"; grep -Fq "$pattern" "$file" || { echo "FAIL: $label"; exit 1; }; echo "PASS: $label"; }
[[ -f "$B10" ]] || { echo "FAIL: Batch10FeatureActivity.java missing"; exit 1; }
check "extends Batch9FeatureActivity" "$B10" "Batch 10 extends Batch 9"
check "opacity100" "$B10" "Opacity 100%"
check "opacity75" "$B10" "Opacity 75%"
check "opacity50" "$B10" "Opacity 50%"
check "radius0" "$B10" "Corner radius 0dp"
check "radius12" "$B10" "Corner radius 12dp"
check "radius24" "$B10" "Corner radius 24dp"
check "fontUp" "$B10" "Font size increase"
check "fontDown" "$B10" "Font size decrease"
check "toggleFill" "$B10" "Solid/outline fill toggle"
check "resetStyle" "$B10" "Visual style reset"
check "extends Batch10FeatureActivity" "$LAUNCH" "Canonical launcher includes Batch 10"
echo "Batch 10 audit: 11/11 checks passed"

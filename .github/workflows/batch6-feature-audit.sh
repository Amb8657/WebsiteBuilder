#!/usr/bin/env bash
set -euo pipefail
ROOT="${GITHUB_WORKSPACE:-$(pwd)}"
F="$ROOT/app/src/main/java/com/amb8657/websitebuilder/Batch6FeatureActivity.java"
pass=0; fail=0
check(){ local name="$1"; local pattern="$2"; if grep -Eq "$pattern" "$F"; then echo "PASS: $name"; pass=$((pass+1)); else echo "FAIL: $name"; fail=$((fail+1)); fi; }
check "Mobile preview" 'Mobile|360dp'
check "Tablet preview" 'Tablet|768dp'
check "Desktop preview" 'Desktop|1280dp'
check "All-device preview" 'All devices|showDeviceSummary'
check "Preview/testing panel" 'Preview & Testing|testingPanel'
check "Autosave" 'autosave|onStop'
check "About Project" 'About Project|projectInfo'
check "Active/inactive status" 'Active|Inactive|projectStatus'
check "Performance scan" 'performanceCheck|Images:|Animations:'
check "Fullscreen/native preview" 'Fullscreen preview|fullPreview|preview\(\)'
if (( fail > 0 )); then echo "BATCH6_AUDIT_PASS=$pass"; echo "BATCH6_AUDIT_FAIL=$fail"; echo "BATCH6_AUDIT_GREEN=false"; exit 1; fi
echo "BATCH6_AUDIT_PASS=$pass"; echo "BATCH6_AUDIT_FAIL=0"; echo "BATCH6_AUDIT_GREEN=true"

#!/usr/bin/env bash
set -euo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"
ROOT="app/src/main/java/com/amb8657/websitebuilder"
fail=0
check(){ local label="$1"; shift; if "$@"; then echo "[PASS] $label"; else echo "[FAIL] $label"; fail=1; fi; }
file="$ROOT/Batch11FeatureActivity.java"
b10="$ROOT/Batch10FeatureActivity.java"
b12="$ROOT/Batch12FeatureActivity.java"
launcher="$ROOT/Batch4PersistenceActivity.java"
check 'Batch 11 activity exists' test -f "$file"
check 'Batch 10 exists' test -f "$b10"
check 'Batch 12 exists' test -f "$b12"
check 'Batch 11 extends Batch 10' grep -q 'class Batch11FeatureActivity extends Batch10FeatureActivity' "$file"
check 'Batch 12 extends Batch 11' grep -q 'class Batch12FeatureActivity extends Batch11FeatureActivity' "$b12"
check 'Canonical launcher reaches Batch 12' grep -q 'class Batch4PersistenceActivity extends Batch12FeatureActivity' "$launcher"
check 'Add page' grep -q 'private void addPage' "$file"
check 'Rename page' grep -q 'private void renamePage' "$file"
check 'Duplicate page' grep -q 'private void duplicatePage' "$file"
check 'Delete page' grep -q 'private void deletePage' "$file"
check 'Move page left' grep -q 'private void movePageLeft' "$file"
check 'Move page right' grep -q 'private void movePageRight' "$file"
check 'Page background' grep -q 'private void pageBackground' "$file"
check 'Set Home page' grep -q 'private void setHomePage' "$file"
check 'Navigation preview' grep -q 'private void pagePreview' "$file"
check 'Mobile navigation preview' grep -q 'private void mobileNavigationPreview' "$file"
check 'Site structure menu' grep -q 'siteMenu' "$file"
if [[ "$fail" -ne 0 ]]; then exit 1; fi
echo 'BATCH11_AUDIT_GREEN: 17/17 checks passed'

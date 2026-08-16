#!/usr/bin/env bash
set -euo pipefail
ROOT="${GITHUB_WORKSPACE:-$(pwd)}"
F="$ROOT/app/src/main/java/com/amb8657/websitebuilder/Batch7FeatureActivity.java"
L="$ROOT/app/src/main/java/com/amb8657/websitebuilder/Batch4PersistenceActivity.java"
pass=0; fail=0
check(){ local name="$1"; local file="$2"; local pattern="$3"; if grep -Eq "$pattern" "$file"; then echo "PASS: $name"; pass=$((pass+1)); else echo "FAIL: $name"; fail=$((fail+1)); fi; }
check "Project rename" "$F" 'renameProject|Rename Project'
check "Project description" "$F" 'description|Project Description'
check "Created timestamp" "$F" 'created|Created:'
check "Last modified timestamp" "$F" 'modified|Last Modified'
check "Project size" "$F" 'documentSize|Project Size'
check "Progress/status" "$F" 'progressInfo|Project Progress'
check "Privacy flag" "$F" 'privacyToggle|private|Privacy'
check "Form/submission panel" "$F" 'submissionInfo|Form & Submission Data'
check "Local backup" "$F" 'backup\(|websitebuilder-project-backup'
check "Local restore" "$F" 'restoreBackup|Local backup restored'
check "Canonical launcher keeps persistence" "$L" 'extends Batch7FeatureActivity|builder_v3|persistBatch4Metadata'
if (( fail > 0 )); then echo "BATCH7_AUDIT_PASS=$pass"; echo "BATCH7_AUDIT_FAIL=$fail"; echo "BATCH7_AUDIT_GREEN=false"; exit 1; fi
echo "BATCH7_AUDIT_PASS=$pass"; echo "BATCH7_AUDIT_FAIL=0"; echo "BATCH7_AUDIT_GREEN=true"

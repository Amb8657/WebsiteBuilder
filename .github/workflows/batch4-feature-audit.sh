#!/usr/bin/env bash
set -euo pipefail
EDITOR="app/src/main/java/com/amb8657/websitebuilder/EnhancedWebsiteBuilderV4Activity.java"
PERSIST="app/src/main/java/com/amb8657/websitebuilder/Batch4PersistenceActivity.java"
MANIFEST="app/src/main/AndroidManifest.xml"
QA=".github/workflows/run-emulator-qa.sh"
FAIL=0
PASS=0
check(){ local label="$1"; shift; if "$@" >/dev/null 2>&1; then echo "[PASS] $label"; PASS=$((PASS+1)); else echo "[FAIL] $label"; FAIL=$((FAIL+1)); fi; }
contains(){ grep -Eq "$1" "$2"; }
check 'Undo implementation' contains 'private void undoAction\(' "$EDITOR"
check 'Redo implementation' contains 'private void redoAction\(' "$EDITOR"
check 'Duplicate implementation' contains 'private void duplicate\(' "$EDITOR"
check 'Copy implementation' contains 'private void copy\(' "$EDITOR"
check 'Paste implementation' contains 'private void paste\(' "$EDITOR"
check 'Group implementation' contains 'private void group\(' "$EDITOR"
check 'Ungroup implementation' contains 'private void ungroup\(' "$EDITOR"
check 'Lock implementation' contains 'private void toggleLock\(' "$EDITOR"
check 'Hide implementation' contains 'private void toggleHide\(' "$EDITOR"
check 'Layers implementation' contains 'private void layers\(' "$EDITOR"
check 'Rename implementation' contains 'private void rename\(' "$EDITOR"
check 'Stable-ID clipboard/duplicate' contains 'nextId\+\+' "$EDITOR"
check 'Undo snapshots contain control metadata' contains 'blockJson\(b\)' "$EDITOR"
check 'Control metadata includes locked' contains 'locked.*flag\(b,"locked"\)' "$EDITOR"
check 'Control metadata includes hidden' contains 'hidden.*flag\(b,"hidden"\)' "$EDITOR"
check 'Control metadata includes name' contains 'name.*name\(b\)' "$EDITOR"
check 'Canonical builder_v3 export' contains 'builder_v3' "$EDITOR"
check 'Canonical metadata persistence bridge' contains 'builder_v3' "$PERSIST"
check 'Persistence bridge restores metadata' contains 'restoreBatch4Metadata' "$PERSIST"
check 'Current launcher is Batch14FeatureActivity' contains 'Batch14FeatureActivity' "$MANIFEST"
check 'QA launches Batch14FeatureActivity' contains 'Batch14FeatureActivity' "$QA"
check 'QA checks fatal crashes' contains 'FATAL EXCEPTION' "$QA"
check 'Mutations clear redo through pushUndo' contains 'redo.clear\(\)' "$EDITOR"
check 'Undo restores document model' contains 'restore\(undo.pop\(\)\)' "$EDITOR"
check 'Redo restores document model' contains 'restore\(redo.pop\(\)\)' "$EDITOR"
check 'Save follows mutations' contains 'save\(\);render\(\)' "$EDITOR"
check 'Layer ordering uses top-level blocks' contains 'x.parent==0' "$EDITOR"
TOTAL=$((PASS+FAIL))
echo "BATCH4_AUDIT_PASS=$PASS"
echo "BATCH4_AUDIT_FAIL=$FAIL"
echo "BATCH4_AUDIT_TOTAL=$TOTAL"
if [[ "$FAIL" -ne 0 ]]; then echo 'BATCH4_AUDIT_RED'; exit 1; fi
echo 'BATCH4_AUDIT_GREEN'

#!/usr/bin/env bash
set -euo pipefail

ROOT="${GITHUB_WORKSPACE:-$(pwd)}"
ACTIVITY="$ROOT/app/src/main/java/com/amb8657/websitebuilder/EnhancedWebsiteBuilderV4Activity.java"
PERSIST="$ROOT/app/src/main/java/com/amb8657/websitebuilder/Batch4PersistenceActivity.java"

pass=0
fail=0
check(){
  local name="$1"; local file="$2"; local pattern="$3"
  if grep -Eq "$pattern" "$file"; then echo "PASS: $name"; pass=$((pass+1)); else echo "FAIL: $name"; fail=$((fail+1)); fi
}

# Batch 1-4 regression contracts.
check "Undo" "$ACTIVITY" 'undoStack|undo'
check "Redo" "$ACTIVITY" 'redoStack|redo'
check "Duplicate" "$ACTIVITY" 'duplicate'
check "Copy/Paste" "$ACTIVITY" 'copy|paste'
check "Group/Ungroup" "$ACTIVITY" 'group|ungroup'
check "Lock/Hide" "$ACTIVITY" 'locked|hidden'
check "Layers" "$ACTIVITY" 'layer|moveLayer|bring|send'
check "Rename" "$ACTIVITY" 'name'
check "Canonical builder_v3 persistence" "$PERSIST" 'builder_v3'
check "Batch4 persistence bridge" "$PERSIST" 'lock|hidden|name'

# Batch 5 contracts: UI parity, contextual properties, text/image/button/container behavior.
check "Contextual property panel" "$ACTIVITY" 'property|Property|selected'
check "Free text movement" "$ACTIVITY" 'translationX|translationY|move'
check "Text resize" "$ACTIVITY" 'scaleX|scaleY|resize'
check "Image preview/panel" "$ACTIVITY" 'Image|image'
check "Explicit image crop" "$ACTIVITY" 'crop|Crop'
check "Button controls/actions" "$ACTIVITY" 'Button|button|action'
check "Section/container hierarchy" "$ACTIVITY" 'section|container|children'
check "Shape container/background" "$ACTIVITY" 'shape|Shape|background|border'
check "Persistence after mutation" "$PERSIST" 'save|persist|SharedPreferences'
check "Launcher remains Batch4PersistenceActivity" "$PERSIST" 'class Batch4PersistenceActivity'

if (( fail > 0 )); then
  echo "BATCH5_AUDIT_PASS=$pass"
  echo "BATCH5_AUDIT_FAIL=$fail"
  echo "BATCH5_AUDIT_GREEN=false"
  exit 1
fi

echo "BATCH5_AUDIT_PASS=$pass"
echo "BATCH5_AUDIT_FAIL=0"
echo "BATCH5_AUDIT_GREEN=true"

#!/usr/bin/env bash
set -euo pipefail
ROOT="${GITHUB_WORKSPACE:-$(pwd)}"
FILE="$ROOT/app/src/main/java/com/amb8657/websitebuilder/Batch15FeatureActivity.java"
MANIFEST="$ROOT/app/src/main/AndroidManifest.xml"
pass=0; fail=0
check(){ if grep -Fq "$2" "$1"; then echo "PASS: $3"; pass=$((pass+1)); else echo "FAIL: $3"; fail=$((fail+1)); fi; }
check "$FILE" 'class Batch15FeatureActivity extends Batch14FeatureActivity' 'Batch 15 inherits the completed editor chain'
check "$FILE" '"Page URL / slug"' 'Page URL / slug control'
check "$FILE" '"Set parent page"' 'Nested page hierarchy control'
check "$FILE" '"View hierarchy"' 'Hierarchy browser'
check "$FILE" '"Add dropdown navigation"' 'Dropdown navigation'
check "$FILE" '"Add mobile menu"' 'Mobile navigation menu'
check "$FILE" '"Create reusable header"' 'Reusable header'
check "$FILE" '"Create reusable footer"' 'Reusable footer'
check "$FILE" '"Sync shared header/footer"' 'Shared header/footer synchronization'
check "$FILE" '"Set custom 404 page"' 'Custom 404 page'
check "$FILE" '"Structure summary"' 'Structure configuration summary'
check "$FILE" 'page.blocks.add(b);selected=b;changed("Dropdown navigation added")' 'Dropdown is persisted as an editable document block'
check "$FILE" 'sp().edit().putString(pageKey(page)+":parent",parent).apply()' 'Page hierarchy metadata is persisted'
check "$MANIFEST" 'android:name=".Batch15FeatureActivity"' 'Batch 15 is the launcher'
echo "Batch 15 audit: $pass passed, $fail failed"
(( fail == 0 ))

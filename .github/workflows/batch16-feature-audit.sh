#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
F="$ROOT/app/src/main/java/com/amb8657/websitebuilder/Batch16FeatureActivity.java"
M="$ROOT/app/src/main/AndroidManifest.xml"
test -f "$F"
grep -q 'class Batch16FeatureActivity extends Batch15FeatureActivity' "$F"
for token in 'siteTitle' 'description' 'keywords' 'favicon' 'language' 'canonical' 'robots' 'ogTitle' 'ogDescription' 'customCss'; do grep -q "$token" "$F"; done
grep -q 'Batch16FeatureActivity' "$M"
# Guard against documentation-only completion: the ten controls must have persisted keys.
for key in 'title' 'description' 'keywords' 'favicon' 'language' 'canonical' 'robots' 'og_title' 'og_description' 'custom_css'; do grep -q "\"$key\"" "$F"; done
echo 'Batch 16 source contract: PASS'

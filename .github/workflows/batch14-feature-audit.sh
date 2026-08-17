#!/usr/bin/env bash
set -euo pipefail
ROOT="${GITHUB_WORKSPACE:-$(pwd)}"
FILE="$ROOT/app/src/main/java/com/amb8657/websitebuilder/Batch14FeatureActivity.java"
MANIFEST="$ROOT/app/src/main/AndroidManifest.xml"
pass=0; fail=0
check(){ if grep -Fq "$2" "$1"; then echo "PASS: $3"; pass=$((pass+1)); else echo "FAIL: $3"; fail=$((fail+1)); fi; }
check "$FILE" 'class Batch14FeatureActivity extends Batch4PersistenceActivity' 'Batch 14 activity inherits the completed editor/persistence chain'
check "$FILE" 'Button elements=btn("Elements")' 'Advanced Elements toolbar entry exists'
check "$FILE" '"Icon"' 'Icon element'
check "$FILE" '"Line"' 'Line element'
check "$FILE" '"Video"' 'Video embed element'
check "$FILE" '"Audio"' 'Audio embed element'
check "$FILE" '"Map"' 'Map element'
check "$FILE" '"Social"' 'Social icon element'
check "$FILE" '"Card"' 'Card element'
check "$FILE" '"Testimonial"' 'Testimonial element'
check "$FILE" '"Pricing"' 'Pricing table element'
check "$FILE" '"FAQ"' 'FAQ/accordion element'
check "$FILE" '"Countdown"' 'Countdown element'
check "$FILE" '"Table"' 'Table element'
check "$FILE" '"Gallery"' 'Gallery element'
check "$FILE" '"Slider"' 'Slider/carousel element'
check "$FILE" 'Block b=new Block(nextId++,type,text)' 'Advanced elements are real editable blocks'
check "$FILE" '@Override TextView blockView(Block b,int index)' 'Advanced elements render in the real canvas'
check "$MANIFEST" 'android:name=".Batch14FeatureActivity"' 'Batch 14 is the Android launcher'
echo "Batch 14 audit: $pass passed, $fail failed"
(( fail == 0 ))

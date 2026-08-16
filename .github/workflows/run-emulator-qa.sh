#!/usr/bin/env bash
set -euo pipefail
cd "$GITHUB_WORKSPACE"

echo '--- BUILD ---'
gradle --no-daemon clean assembleDebug --stacktrace

echo '--- INSTALL ---'
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb logcat -c || true
adb shell am force-stop com.amb8657.websitebuilder || true
adb shell am start -n com.amb8657.websitebuilder/.WebsiteBuilderV4Activity

echo '--- WAIT FOR PROCESS ---'
process_ok=0
for i in $(seq 1 30); do
  if adb shell pidof com.amb8657.websitebuilder >/dev/null 2>&1; then process_ok=1; break; fi
  sleep 2
done
if [ "$process_ok" -ne 1 ]; then echo 'WebsiteBuilder process did not start'; adb logcat -d -v threadtime | tail -n 300 || true; exit 1; fi

echo '--- WAIT FOR ACTIVITY ---'
activity_ok=0
for i in $(seq 1 30); do
  adb shell dumpsys activity activities > activity-dump.txt
  if grep -q 'com\.amb8657\.websitebuilder/.WebsiteBuilderV4Activity' activity-dump.txt; then activity_ok=1; break; fi
  sleep 2
done
if [ "$activity_ok" -ne 1 ]; then echo 'WebsiteBuilder activity did not become foreground'; cat activity-dump.txt || true; exit 1; fi
sleep 3
adb exec-out screencap -p > v4-launch.png
adb logcat -d -v threadtime > v4-logcat.txt

echo '--- CRASH CHECK ---'
if grep -qE 'FATAL EXCEPTION|Process: com\.amb8657\.websitebuilder' v4-logcat.txt; then grep -E -C 15 'FATAL EXCEPTION|AndroidRuntime|Process: com\.amb8657\.websitebuilder' v4-logcat.txt || true; exit 1; fi
adb shell pidof com.amb8657.websitebuilder >/dev/null

echo '--- UI AUTOMATOR EVIDENCE ---'
# uiautomator dump writes the hierarchy to the device; exec-out is not used because
# dump itself does not stream the XML to stdout on all emulator images.
if ! adb shell uiautomator dump /sdcard/v4-window.xml >/dev/null 2>&1; then echo 'uiautomator dump failed'; exit 1; fi
adb exec-out cat /sdcard/v4-window.xml > v4-window.xml
if [ ! -s v4-window.xml ]; then echo 'UI hierarchy is empty'; exit 1; fi
if ! grep -q 'com.amb8657.websitebuilder' v4-window.xml && ! grep -q 'WebsiteBuilder' v4-window.xml; then echo 'WebsiteBuilder is not represented in UI hierarchy'; head -c 5000 v4-window.xml || true; exit 1; fi

echo '--- FINAL ACTIVITY CHECK ---'
adb shell dumpsys activity activities > activity-dump.txt
if ! grep -q 'com\.amb8657\.websitebuilder/.WebsiteBuilderV4Activity' activity-dump.txt; then echo 'WebsiteBuilder activity is no longer active'; exit 1; fi
adb shell pidof com.amb8657.websitebuilder >/dev/null
echo '--- EMULATOR LAUNCH QA PASSED ---'

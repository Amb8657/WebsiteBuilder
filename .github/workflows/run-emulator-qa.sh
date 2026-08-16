#!/usr/bin/env bash
set -uo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"

PACKAGE="com.amb8657.websitebuilder"
ACTIVITY="${PACKAGE}/.EnhancedWebsiteBuilderV4Activity"
RESULT=0
FAILURES=()
PASS_COUNT=0
fail() { echo "[FAIL] $1"; FAILURES+=("$1"); RESULT=1; }
pass() { echo "[PASS] $1"; PASS_COUNT=$((PASS_COUNT+1)); }
run_check() { local name="$1"; shift; if "$@"; then pass "$name"; else fail "$name"; fi; }

mkdir -p qa-artifacts
: > qa-artifacts/qa-summary.txt

collect_diagnostics() {
  adb logcat -d -v threadtime > qa-artifacts/logcat.txt 2>&1 || true
  adb shell dumpsys activity activities > qa-artifacts/activity-dump.txt 2>&1 || true
  adb shell dumpsys window windows > qa-artifacts/window-dump.txt 2>&1 || true
  adb shell pidof "$PACKAGE" > qa-artifacts/pid.txt 2>&1 || true
  adb shell uiautomator dump /sdcard/v4-window.xml >/dev/null 2>&1 || true
  adb exec-out cat /sdcard/v4-window.xml > qa-artifacts/v4-window.xml 2>/dev/null || true
  adb exec-out screencap -p > qa-artifacts/emulator-final.png 2>/dev/null || true
}

APK="app/build/outputs/apk/debug/app-debug.apk"
echo '--- BUILD ---'
if gradle --no-daemon clean assembleDebug --stacktrace > qa-artifacts/build.log 2>&1; then pass 'Android build'; else fail 'Android build'; fi

if [[ -f "$APK" ]]; then pass 'APK exists'; else fail 'APK exists'; fi

echo '--- DEVICE SETUP ---'
run_check 'ADB device available' adb get-state
adb wait-for-device >/dev/null 2>&1 || fail 'ADB wait-for-device'

boot=''
for i in $(seq 1 60); do
  boot="$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r' || true)"
  [[ "$boot" == "1" ]] && break
  sleep 2
done
if [[ "$boot" == "1" ]]; then pass 'Emulator boot completed'; else fail 'Emulator boot completed'; fi

if [[ -f "$APK" ]]; then
  if adb install -r "$APK" > qa-artifacts/install.log 2>&1; then pass 'APK installed'; else fail 'APK installed'; fi
else
  fail 'APK install skipped because APK is missing'
fi

adb logcat -c >/dev/null 2>&1 || true
adb shell am force-stop "$PACKAGE" >/dev/null 2>&1 || true
if adb shell am start -n "$ACTIVITY" > qa-artifacts/launch.log 2>&1; then pass 'Activity launch command'; else fail 'Activity launch command'; fi

process_ok=0
for i in $(seq 1 30); do
  if adb shell pidof "$PACKAGE" >/dev/null 2>&1; then process_ok=1; break; fi
  sleep 2
done
[[ "$process_ok" == "1" ]] && pass 'WebsiteBuilder process alive' || fail 'WebsiteBuilder process alive'

activity_ok=0
for i in $(seq 1 30); do
  adb shell dumpsys activity activities > qa-artifacts/activity-dump-live.txt 2>&1 || true
  if grep -q "$ACTIVITY" qa-artifacts/activity-dump-live.txt; then activity_ok=1; break; fi
  sleep 2
done
[[ "$activity_ok" == "1" ]] && pass 'Enhanced WebsiteBuilder activity foreground' || fail 'Enhanced WebsiteBuilder activity foreground'

sleep 3
collect_diagnostics

if [[ -s qa-artifacts/emulator-final.png ]]; then pass 'Emulator screenshot captured'; else fail 'Emulator screenshot captured'; fi
if [[ -s qa-artifacts/v4-window.xml ]]; then pass 'UI hierarchy captured'; else fail 'UI hierarchy captured'; fi

if grep -qE 'FATAL EXCEPTION|Process: com\.amb8657\.websitebuilder' qa-artifacts/logcat.txt 2>/dev/null; then
  fail 'No WebsiteBuilder fatal crash'
else
  pass 'No WebsiteBuilder fatal crash'
fi

if grep -q "$ACTIVITY" qa-artifacts/activity-dump.txt 2>/dev/null; then pass 'Enhanced activity remains active'; else fail 'Enhanced activity remains active'; fi

if adb shell pidof "$PACKAGE" >/dev/null 2>&1; then pass 'Process remains alive at final check'; else fail 'Process remains alive at final check'; fi

{
  echo "WebsiteBuilder emulator QA summary"
  echo "PACKAGE=$PACKAGE"
  echo "ACTIVITY=$ACTIVITY"
  echo "PASS_COUNT=$PASS_COUNT"
  echo "FAIL_COUNT=${#FAILURES[@]}"
  if ((${#FAILURES[@]})); then
    echo 'FAILURES:'
    printf '%s\n' "${FAILURES[@]}"
  else
    echo 'FAILURES: none'
  fi
} | tee qa-artifacts/qa-summary.txt

if ((${#FAILURES[@]})); then
  echo '--- FAILURE DIAGNOSTICS ---'
  grep -E -C 12 'FATAL EXCEPTION|AndroidRuntime|Process: com\.amb8657\.websitebuilder' qa-artifacts/logcat.txt || true
  echo '--- END DIAGNOSTICS ---'
  exit "$RESULT"
fi

echo '--- EMULATOR_QA_GREEN ---'
exit 0

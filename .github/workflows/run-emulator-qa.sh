#!/usr/bin/env bash
set -uo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"

PACKAGE="com.amb8657.websitebuilder"
ACTIVITY="${PACKAGE}/.Batch14FeatureActivity"
RESULT=0
FAILURES=()
PASS_COUNT=0
fail(){ echo "[FAIL] $1"; FAILURES+=("$1"); RESULT=1; }
pass(){ echo "[PASS] $1"; PASS_COUNT=$((PASS_COUNT+1)); }
wait_for_adb(){ local state=""; for i in $(seq 1 30); do state="$(adb get-state 2>/dev/null | tr -d '\r\n' || true)"; [[ "$state" == "device" ]] && return 0; adb start-server >/dev/null 2>&1 || true; adb reconnect offline >/dev/null 2>&1 || true; sleep 2; done; return 1; }
adb_retry(){ local attempts=1 output=""; while ((attempts<=5)); do if wait_for_adb; then output="$("$@" 2>&1)" && { printf '%s\n' "$output"; return 0; }; else output="adb unavailable"; fi; echo "$output"; adb kill-server >/dev/null 2>&1 || true; adb start-server >/dev/null 2>&1 || true; sleep 2; attempts=$((attempts+1)); done; return 1; }
mkdir -p qa-artifacts
: > qa-artifacts/qa-summary.txt
APK="app/build/outputs/apk/debug/app-debug.apk"

echo '--- BUILD ---'
gradle --no-daemon clean assembleDebug --stacktrace > qa-artifacts/build.log 2>&1 && pass 'Android build' || fail 'Android build'
[[ -f "$APK" ]] && pass 'APK exists' || fail 'APK exists'

echo '--- DEVICE ---'
wait_for_adb && pass 'ADB device available' || fail 'ADB device available'
adb_retry adb wait-for-device >/dev/null 2>&1 && pass 'ADB wait-for-device' || fail 'ADB wait-for-device'
boot=''; for i in $(seq 1 90); do if wait_for_adb; then boot="$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r' || true)"; [[ "$boot" == "1" ]] && break; fi; sleep 2; done
[[ "$boot" == "1" ]] && pass 'Emulator boot completed' || fail 'Emulator boot completed'
[[ -f "$APK" ]] && adb_retry adb install -r "$APK" > qa-artifacts/install.log 2>&1 && pass 'APK installed' || fail 'APK installed'

adb_retry adb logcat -c >/dev/null 2>&1 || true
adb_retry adb shell am force-stop "$PACKAGE" >/dev/null 2>&1 || true
adb_retry adb shell am start -n "$ACTIVITY" > qa-artifacts/launch.log 2>&1 && pass 'Batch14 activity launch command' || fail 'Batch14 activity launch command'

process_ok=0; for i in $(seq 1 30); do adb_retry adb shell pidof "$PACKAGE" >/dev/null 2>&1 && { process_ok=1; break; }; sleep 2; done
[[ "$process_ok" == "1" ]] && pass 'WebsiteBuilder process alive' || fail 'WebsiteBuilder process alive'
activity_ok=0; for i in $(seq 1 30); do wait_for_adb || true; adb shell dumpsys activity activities > qa-artifacts/activity-dump-live.txt 2>&1 || true; grep -q "$ACTIVITY" qa-artifacts/activity-dump-live.txt && { activity_ok=1; break; }; sleep 2; done
[[ "$activity_ok" == "1" ]] && pass 'Batch14 activity foreground' || fail 'Batch14 activity foreground'

wait_for_adb || true
adb logcat -d -v threadtime > qa-artifacts/logcat.txt 2>&1 || true
adb shell dumpsys activity activities > qa-artifacts/activity-dump.txt 2>&1 || true
adb shell dumpsys window windows > qa-artifacts/window-dump.txt 2>&1 || true
adb shell pidof "$PACKAGE" > qa-artifacts/pid.txt 2>&1 || true
adb shell uiautomator dump /sdcard/v4-window.xml >/dev/null 2>&1 || true
adb exec-out cat /sdcard/v4-window.xml > qa-artifacts/v4-window.xml 2>/dev/null || true
adb exec-out screencap -p > qa-artifacts/emulator-final.png 2>/dev/null || true
[[ -s qa-artifacts/emulator-final.png ]] && pass 'Emulator screenshot captured' || fail 'Emulator screenshot captured'
[[ -s qa-artifacts/v4-window.xml ]] && pass 'UI hierarchy captured' || fail 'UI hierarchy captured'
if grep -qE 'FATAL EXCEPTION|Process: com\.amb8657\.websitebuilder' qa-artifacts/logcat.txt 2>/dev/null; then fail 'No WebsiteBuilder fatal crash'; else pass 'No WebsiteBuilder fatal crash'; fi
if grep -q "$ACTIVITY" qa-artifacts/activity-dump.txt 2>/dev/null; then pass 'Batch14 activity remains active'; else fail 'Batch14 activity remains active'; fi
if wait_for_adb && adb shell pidof "$PACKAGE" >/dev/null 2>&1; then pass 'Process remains alive at final check'; else fail 'Process remains alive at final check'; fi

{ echo 'WebsiteBuilder emulator QA summary'; echo "PACKAGE=$PACKAGE"; echo "ACTIVITY=$ACTIVITY"; echo "PASS_COUNT=$PASS_COUNT"; echo "FAIL_COUNT=${#FAILURES[@]}"; if ((${#FAILURES[@]})); then echo 'FAILURES:'; printf '%s\n' "${FAILURES[@]}"; else echo 'FAILURES: none'; fi; } | tee qa-artifacts/qa-summary.txt
if ((${#FAILURES[@]})); then exit "$RESULT"; fi
echo '--- EMULATOR_QA_GREEN ---'
exit 0

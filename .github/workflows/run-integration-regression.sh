#!/usr/bin/env bash
set -uo pipefail
cd "${GITHUB_WORKSPACE:-$(pwd)}"
PACKAGE="com.amb8657.websitebuilder"
ACTIVITY="${PACKAGE}/.Batch14FeatureActivity"
RESULT=0
FAILURES=()
PASS_COUNT=0
fail(){ echo "[INTEGRATION-FAIL] $1"; FAILURES+=("$1"); RESULT=1; }
pass(){ echo "[INTEGRATION-PASS] $1"; PASS_COUNT=$((PASS_COUNT+1)); }
mkdir -p qa-artifacts/integration
wait_for_process(){
  local attempts="${1:-30}"
  local i
  for i in $(seq 1 "$attempts"); do
    if adb get-state >/dev/null 2>&1 && adb shell pidof "$PACKAGE" >/dev/null 2>&1; then return 0; fi
    sleep 2
  done
  return 1
}
wait_for_activity(){
  local attempts="${1:-30}"
  local i
  for i in $(seq 1 "$attempts"); do
    adb shell dumpsys activity activities > /tmp/integration-activity.txt 2>&1 || true
    if grep -q "$ACTIVITY" /tmp/integration-activity.txt; then return 0; fi
    sleep 2
  done
  return 1
}
for cycle in 1 2 3; do
  adb wait-for-device >/dev/null 2>&1 || true
  adb logcat -c >/dev/null 2>&1 || true
  adb shell am force-stop "$PACKAGE" >/dev/null 2>&1 || true
  if adb shell am start -n "$ACTIVITY" >/dev/null 2>&1; then pass "cycle $cycle launch"; else fail "cycle $cycle launch"; fi
  wait_for_process 30 && pass "cycle $cycle process alive" || fail "cycle $cycle process alive"
  adb shell dumpsys activity activities > "qa-artifacts/integration/cycle-$cycle-activity.txt" 2>&1 || true
  if grep -q "$ACTIVITY" "qa-artifacts/integration/cycle-$cycle-activity.txt" || wait_for_activity 15; then pass "cycle $cycle activity foreground"; else fail "cycle $cycle activity foreground"; fi
  if adb exec-out screencap -p > "qa-artifacts/integration/cycle-$cycle.png" 2>/dev/null && [[ -s "qa-artifacts/integration/cycle-$cycle.png" ]]; then pass "cycle $cycle screenshot"; else fail "cycle $cycle screenshot"; fi
  adb logcat -d -v threadtime > "qa-artifacts/integration/cycle-$cycle-logcat.txt" 2>&1 || true
  if grep -qE 'FATAL EXCEPTION|Process: com\.amb8657\.websitebuilder' "qa-artifacts/integration/cycle-$cycle-logcat.txt"; then fail "cycle $cycle no fatal crash"; else pass "cycle $cycle no fatal crash"; fi
done
adb shell am force-stop "$PACKAGE" >/dev/null 2>&1 || true
sleep 2
if adb shell am start -n "$ACTIVITY" >/dev/null 2>&1; then pass "post-cycle relaunch"; else fail "post-cycle relaunch"; fi
if wait_for_process 30; then pass "post-cycle process alive"; else fail "post-cycle process alive"; fi
if wait_for_activity 15; then pass "post-cycle activity foreground"; else fail "post-cycle activity foreground"; fi
adb logcat -d -v threadtime > qa-artifacts/integration/post-cycle-logcat.txt 2>&1 || true
if grep -qE 'FATAL EXCEPTION|Process: com\.amb8657\.websitebuilder' qa-artifacts/integration/post-cycle-logcat.txt; then fail "post-cycle no fatal crash"; else pass "post-cycle no fatal crash"; fi
printf 'INTEGRATION_REGRESSION_PASS=%s\n' "$PASS_COUNT"
printf 'INTEGRATION_REGRESSION_FAIL=%s\n' "${#FAILURES[@]}"
if ((${#FAILURES[@]})); then printf '%s\n' "${FAILURES[@]}"; exit "$RESULT"; fi

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
for cycle in 1 2 3; do
  adb wait-for-device >/dev/null 2>&1 || true
  adb logcat -c >/dev/null 2>&1 || true
  adb shell am force-stop "$PACKAGE" >/dev/null 2>&1 || true
  if adb shell am start -n "$ACTIVITY" >/dev/null 2>&1; then pass "cycle $cycle launch"; else fail "cycle $cycle launch"; fi
  alive=0
  for i in $(seq 1 20); do
    if adb shell pidof "$PACKAGE" >/dev/null 2>&1; then alive=1; break; fi
    sleep 1
  done
  [[ "$alive" == 1 ]] && pass "cycle $cycle process alive" || fail "cycle $cycle process alive"
  adb shell dumpsys activity activities > "qa-artifacts/integration/cycle-$cycle-activity.txt" 2>&1 || true
  if grep -q "$ACTIVITY" "qa-artifacts/integration/cycle-$cycle-activity.txt"; then pass "cycle $cycle activity foreground"; else fail "cycle $cycle activity foreground"; fi
  if adb exec-out screencap -p > "qa-artifacts/integration/cycle-$cycle.png" 2>/dev/null && [[ -s "qa-artifacts/integration/cycle-$cycle.png" ]]; then pass "cycle $cycle screenshot"; else fail "cycle $cycle screenshot"; fi
  adb logcat -d -v threadtime > "qa-artifacts/integration/cycle-$cycle-logcat.txt" 2>&1 || true
  if grep -qE 'FATAL EXCEPTION|Process: com\.amb8657\.websitebuilder' "qa-artifacts/integration/cycle-$cycle-logcat.txt"; then fail "cycle $cycle no fatal crash"; else pass "cycle $cycle no fatal crash"; fi
done
adb shell am force-stop "$PACKAGE" >/dev/null 2>&1 || true
sleep 1
if adb shell am start -n "$ACTIVITY" >/dev/null 2>&1; then pass "post-cycle relaunch"; else fail "post-cycle relaunch"; fi
sleep 2
if adb shell pidof "$PACKAGE" >/dev/null 2>&1; then pass "post-cycle process alive"; else fail "post-cycle process alive"; fi
printf 'INTEGRATION_REGRESSION_PASS=%s\n' "$PASS_COUNT"
printf 'INTEGRATION_REGRESSION_FAIL=%s\n' "${#FAILURES[@]}"
if ((${#FAILURES[@]})); then printf '%s\n' "${FAILURES[@]}"; exit "$RESULT"; fi

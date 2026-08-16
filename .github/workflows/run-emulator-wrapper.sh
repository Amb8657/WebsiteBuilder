#!/usr/bin/env bash
set -euo pipefail

ROOT="${GITHUB_WORKSPACE:-$(pwd)}"

bash "$ROOT/.github/workflows/run-emulator-qa.sh"

if [[ "${QA_EMULATOR_MODE:-fast}" == "full" ]]; then
  bash "$ROOT/.github/workflows/run-full-emulator-regression.sh"
fi

#!/usr/bin/env bash

set -Eeuo pipefail

readonly PROJECT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
readonly BACKEND_DIR="${PROJECT_DIR}/backend"
readonly SOURCE_JAR="${BACKEND_DIR}/target/game-server.jar"
readonly INSTALLED_JAR="/opt/wss-game/game-server.jar"
readonly SERVICE_NAME="wss-game"

usage() {
    cat <<'EOF'
Usage: ./refresh.sh

Builds and tests the game backend, installs its JAR, restarts wss-game, and
checks its local health endpoint. This does not modify or restart Netric.
EOF
}

if [[ "${1:-}" == "--help" || "${1:-}" == "-h" ]]; then
    usage
    exit 0
fi

if [[ "$#" -ne 0 ]]; then
    usage >&2
    exit 2
fi

if [[ "${EUID}" -eq 0 ]]; then
    printf 'Run this script as your normal deployment user, not as root.\n' >&2
    exit 1
fi

if ! systemctl cat "${SERVICE_NAME}.service" >/dev/null 2>&1; then
    printf '%s.service is not installed. Follow deploy/README.md first.\n' "${SERVICE_NAME}" >&2
    exit 1
fi

printf 'Building and testing the WSS game backend...\n'
"${BACKEND_DIR}/mvnw" -f "${BACKEND_DIR}/pom.xml" clean package

if [[ ! -f "${SOURCE_JAR}" ]]; then
    printf 'Expected build output is missing: %s\n' "${SOURCE_JAR}" >&2
    exit 1
fi

printf '\nBuild passed. Administrator access is required to refresh %s.\n' "${SERVICE_NAME}"
sudo install -o root -g root -m 0644 "${SOURCE_JAR}" "${INSTALLED_JAR}"
sudo systemctl restart "${SERVICE_NAME}.service"

curl --fail --silent --show-error \
    --retry 20 --retry-delay 1 --retry-connrefused \
    http://127.0.0.1:8080/health >/dev/null

readonly SOURCE_HASH="$(sha256sum "${SOURCE_JAR}" | awk '{print $1}')"
readonly INSTALLED_HASH="$(sha256sum "${INSTALLED_JAR}" | awk '{print $1}')"

if [[ "${SOURCE_HASH}" != "${INSTALLED_HASH}" ]]; then
    printf 'Installed JAR does not match the build output.\n' >&2
    exit 1
fi

printf '\nWSS game backend refreshed successfully.\n'
printf 'Service: %s (active)\n' "${SERVICE_NAME}"
printf 'JAR SHA-256: %s\n' "${INSTALLED_HASH}"
printf 'Health: http://127.0.0.1:8080/health\n'

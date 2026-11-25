#!/usr/bin/env bash
# Construit le livrable unique : interface Vue compilée + API Spring Boot dans un seul JAR
set -euo pipefail
cd "$(dirname "$0")"
(cd frontend && npm ci && npm run build)
./mvnw -B package
ls -lh target/*.jar

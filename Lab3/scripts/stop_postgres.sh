#!/usr/bin/env bash
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$DIR/.." && pwd)"
DATA_DIR="$PROJECT_ROOT/pg_data"

if [ -d "$DATA_DIR" ]; then
    pg_ctl -D "$DATA_DIR" stop
fi

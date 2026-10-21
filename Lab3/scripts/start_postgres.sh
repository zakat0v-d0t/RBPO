#!/usr/bin/env bash
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$DIR/.." && pwd)"
DATA_DIR="$PROJECT_ROOT/pg_data"
LOG_FILE="$PROJECT_ROOT/pg_data/postgres.log"
PORT=5432

if pg_isready -h localhost -p $PORT > /dev/null 2>&1; then
    echo "PostgreSQL уже запущен на порту $PORT."
else
    if [ ! -d "$DATA_DIR" ]; then
        initdb -D "$DATA_DIR" -U postgres --auth=trust
    fi
    pg_ctl -D "$DATA_DIR" -o "-p $PORT -k /tmp" -l "$LOG_FILE" start
    sleep 2
    if ! pg_isready -h localhost -p $PORT; then
        echo "Ошибка: Не удалось запустить PostgreSQL. Логи: $LOG_FILE"
        exit 1
    fi
fi

psql -h localhost -p $PORT -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'library_db'" | grep -q 1 || \
    psql -h localhost -p $PORT -U postgres -c "CREATE DATABASE library_db;"

psql -h localhost -p $PORT -U postgres -d library_db -f "$PROJECT_ROOT/src/main/resources/schema.sql"
psql -h localhost -p $PORT -U postgres -d library_db -f "$PROJECT_ROOT/src/main/resources/data.sql"
psql -h localhost -p $PORT -U postgres -d library_db -f "$DIR/setup_postgres.sql"

echo "PostgreSQL и база library_db готовы к работе."

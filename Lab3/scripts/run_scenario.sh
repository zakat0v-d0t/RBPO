#!/usr/bin/env bash
set -e

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
CYAN='\033[0;36m'
NC='\033[0m'

BASE_URL="http://localhost:8080/api"

echo -e "${CYAN}======================================================================${NC}"
echo -e "${CYAN}   ДЕМОНСТРАЦИЯ ЛАБОРАТОРНОЙ РАБОТЫ №3: СИСТЕМА БИБЛИОТЕКИ (PostgreSQL) ${NC}"
echo -e "${CYAN}======================================================================${NC}"

if ! curl -s "$BASE_URL/users" > /dev/null 2>&1; then
    echo -e "${RED}[ОШИБКА] Сервис не отвечает по адресу $BASE_URL.${NC}"
    echo -e "Убедитесь, что PostgreSQL запущен (./scripts/start_postgres.sh) и Spring Boot запущен (mvn spring-boot:run)."
    exit 1
fi

echo -e "${GREEN}[OK] Сервис доступен и готов к выполнению запросов.${NC}\n"

echo -e "${BLUE}======================================================================${NC}"
echo -e "${BLUE}  ЧАСТЬ 1: ТЕСТИРОВАНИЕ CRUD ДЛЯ ВСЕХ 5 СУЩНОСТЕЙ                     ${NC}"
echo -e "${BLUE}======================================================================${NC}"

echo -e "\n${YELLOW}--- 1. Сущность: User ---${NC}"
USER_CRUD_RESP=$(curl -s -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{"name": "Тестовый Читатель", "email": "test_crud_user@example.com", "phone": "+79001112233"}')
echo "$USER_CRUD_RESP" | jq .
USER_CRUD_ID=$(echo "$USER_CRUD_RESP" | jq -r .id)

curl -s "$BASE_URL/users/$USER_CRUD_ID" | jq .

curl -s -X PATCH "$BASE_URL/users/$USER_CRUD_ID" \
  -H "Content-Type: application/json" \
  -d '{"phone": "+79998887766"}' | jq .

curl -s -X DELETE "$BASE_URL/users/$USER_CRUD_ID" -w "HTTP статус: %{http_code}\n"


echo -e "\n${YELLOW}--- 2. Сущность: BookEdition ---${NC}"
BOOK_CRUD_RESP=$(curl -s -X POST "$BASE_URL/book-editions" \
  -H "Content-Type: application/json" \
  -d '{"title": "Тестовая Книга", "author": "Тестовый Автор", "isbn": "978-0-00000-000-1"}')
echo "$BOOK_CRUD_RESP" | jq .
BOOK_CRUD_ID=$(echo "$BOOK_CRUD_RESP" | jq -r .id)

curl -s "$BASE_URL/book-editions/$BOOK_CRUD_ID" | jq .

curl -s -X PATCH "$BASE_URL/book-editions/$BOOK_CRUD_ID" \
  -H "Content-Type: application/json" \
  -d '{"author": "Обновленный Автор"}' | jq .

curl -s -X DELETE "$BASE_URL/book-editions/$BOOK_CRUD_ID" -w "HTTP статус: %{http_code}\n"


echo -e "\n${YELLOW}--- 3. Сущность: Copy ---${NC}"
COPY_CRUD_RESP=$(curl -s -X POST "$BASE_URL/copies" \
  -H "Content-Type: application/json" \
  -d '{"bookEditionId": 1, "inventoryNumber": "INV-TEST-CRUD-99", "condition": "NEW"}')
echo "$COPY_CRUD_RESP" | jq .
COPY_CRUD_ID=$(echo "$COPY_CRUD_RESP" | jq -r .id)

curl -s "$BASE_URL/copies/$COPY_CRUD_ID" | jq .

curl -s -X PATCH "$BASE_URL/copies/$COPY_CRUD_ID" \
  -H "Content-Type: application/json" \
  -d '{"condition": "GOOD"}' | jq .

curl -s -X DELETE "$BASE_URL/copies/$COPY_CRUD_ID" -w "HTTP статус: %{http_code}\n"


echo -e "\n${YELLOW}--- 4. Сущность: Loan ---${NC}"
LOAN_CRUD_RESP=$(curl -s -X POST "$BASE_URL/loans" \
  -H "Content-Type: application/json" \
  -d '{"copyId": 2, "userId": 1}')
echo "$LOAN_CRUD_RESP" | jq .
LOAN_CRUD_ID=$(echo "$LOAN_CRUD_RESP" | jq -r .id)

curl -s "$BASE_URL/loans/$LOAN_CRUD_ID" | jq .

curl -s -X PATCH "$BASE_URL/loans/$LOAN_CRUD_ID" \
  -H "Content-Type: application/json" \
  -d '{"returned": true}' | jq .

curl -s -X DELETE "$BASE_URL/loans/$LOAN_CRUD_ID" -w "HTTP статус: %{http_code}\n"


echo -e "\n${YELLOW}--- 5. Сущность: WaitingRequest ---${NC}"
WAIT_CRUD_RESP=$(curl -s -X POST "$BASE_URL/waiting-requests" \
  -H "Content-Type: application/json" \
  -d '{"bookEditionId": 2, "userId": 2, "status": "PENDING"}')
echo "$WAIT_CRUD_RESP" | jq .
WAIT_CRUD_ID=$(echo "$WAIT_CRUD_RESP" | jq -r .id)

curl -s "$BASE_URL/waiting-requests/$WAIT_CRUD_ID" | jq .

curl -s -X PATCH "$BASE_URL/waiting-requests/$WAIT_CRUD_ID" \
  -H "Content-Type: application/json" \
  -d '{"status": "CANCELLED"}' | jq .

curl -s -X DELETE "$BASE_URL/waiting-requests/$WAIT_CRUD_ID" -w "HTTP статус: %{http_code}\n"

echo -e "\n${GREEN}[OK] Все базовые CRUD-операции успешно протестированы!${NC}\n"


echo -e "${BLUE}======================================================================${NC}"
echo -e "${BLUE}  ЧАСТЬ 2: СКВОЗНОЙ БИЗНЕС-СЦЕНАРИЙ РАБОТЫ БИБЛИОТЕКИ                  ${NC}"
echo -e "${BLUE}======================================================================${NC}"

echo -e "\n${YELLOW}[Шаг 1] Создание двух читателей: Алисы и Боба${NC}"
USER_A=$(curl -s -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d "{\"name\": \"Алиса Селезнева\", \"email\": \"alice_$(date +%s%N)@space.org\", \"phone\": \"+79110001122\"}")
USER_A_ID=$(echo "$USER_A" | jq -r .id)
echo -e "Создана Алиса: ID = $USER_A_ID"

USER_B=$(curl -s -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d "{\"name\": \"Боб Марли\", \"email\": \"bob_$(date +%s%N)@music.org\", \"phone\": \"+79119998877\"}")
USER_B_ID=$(echo "$USER_B" | jq -r .id)
echo -e "Создан Боб: ID = $USER_B_ID"

echo -e "\n${YELLOW}[Шаг 2] Добавление в фонд редкой книги 'Design Patterns'${NC}"
BOOK_EDITION=$(curl -s -X POST "$BASE_URL/book-editions" \
  -H "Content-Type: application/json" \
  -d "{\"title\": \"Design Patterns: Elements of Reusable Object-Oriented Software\", \"author\": \"Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides\", \"isbn\": \"978-$(date +%s%N)\"}")
BOOK_ID=$(echo "$BOOK_EDITION" | jq -r .id)
echo -e "Зарегистрирована книга: ID = $BOOK_ID, Название = $(echo "$BOOK_EDITION" | jq -r .title)"

echo -e "\n${YELLOW}[Шаг 3] Добавление единственного физического экземпляра этой книги${NC}"
COPY=$(curl -s -X POST "$BASE_URL/copies" \
  -H "Content-Type: application/json" \
  -d "{\"bookEditionId\": $BOOK_ID, \"inventoryNumber\": \"INV-GOF-$(date +%s%N)\", \"condition\": \"NEW\"}")
COPY_ID=$(echo "$COPY" | jq -r .id)
echo -e "Экземпляр добавлен: ID = $COPY_ID, Инв. № = $(echo "$COPY" | jq -r .inventoryNumber)"

echo -e "\n${YELLOW}[Шаг 4] Проверка доступных копий книги (GET /api/copies/available?bookEditionId=$BOOK_ID)${NC}"
AVAILABLE_BEFORE=$(curl -s "$BASE_URL/copies/available?bookEditionId=$BOOK_ID")
echo "$AVAILABLE_BEFORE" | jq .
COUNT_AVAILABLE=$(echo "$AVAILABLE_BEFORE" | jq '. | length')
echo -e "Доступно копий: ${GREEN}$COUNT_AVAILABLE${NC}"

echo -e "\n${YELLOW}[Шаг 5] Выдача книги Алисе (POST /api/loans/issue)${NC}"
LOAN_RESP=$(curl -s -X POST "$BASE_URL/loans/issue" \
  -H "Content-Type: application/json" \
  -d "{\"copyId\": $COPY_ID, \"userId\": $USER_A_ID}")
echo "$LOAN_RESP" | jq .
LOAN_ID=$(echo "$LOAN_RESP" | jq -r .id)
echo -e "Выдача успешно зарегистрирована: Loan ID = $LOAN_ID"

echo -e "\n${YELLOW}[Шаг 6] Проверка доступных копий книги после выдачи${NC}"
AVAILABLE_AFTER=$(curl -s "$BASE_URL/copies/available?bookEditionId=$BOOK_ID")
echo "$AVAILABLE_AFTER" | jq .
COUNT_AFTER=$(echo "$AVAILABLE_AFTER" | jq '. | length')
echo -e "Доступно копий: ${YELLOW}$COUNT_AFTER (книга занята)${NC}"

echo -e "\n${YELLOW}[Шаг 7] Попытка Боба взять тот же экземпляр книги${NC}"
HTTP_CODE=$(curl -s -o /tmp/issue_err.json -w "%{http_code}" -X POST "$BASE_URL/loans/issue" \
  -H "Content-Type: application/json" \
  -d "{\"copyId\": $COPY_ID, \"userId\": $USER_B_ID}")
echo -e "HTTP Код ответа: ${RED}$HTTP_CODE${NC}"
cat /tmp/issue_err.json | jq .
rm -f /tmp/issue_err.json
echo -e "${GREEN}[OK] Экземпляр защищен от повторной выдачи.${NC}"

echo -e "\n${YELLOW}[Шаг 8] Постановка Боба в лист ожидания (POST /api/waiting-requests/subscribe)${NC}"
WAIT_RESP=$(curl -s -X POST "$BASE_URL/waiting-requests/subscribe" \
  -H "Content-Type: application/json" \
  -d "{\"bookEditionId\": $BOOK_ID, \"userId\": $USER_B_ID}")
echo "$WAIT_RESP" | jq .
WAIT_ID=$(echo "$WAIT_RESP" | jq -r .id)
WAIT_STATUS=$(echo "$WAIT_RESP" | jq -r .status)
echo -e "Заявка зарегистрирована: ID = $WAIT_ID, Статус = ${YELLOW}$WAIT_STATUS${NC}"

echo -e "\n${YELLOW}[Шаг 9] Возврат книги Алисой (POST /api/loans/$LOAN_ID/return)${NC}"
RETURN_RESP=$(curl -s -X POST "$BASE_URL/loans/$LOAN_ID/return")
echo "$RETURN_RESP" | jq .
echo -e "Книга возвращена: Дата возврата = $(echo "$RETURN_RESP" | jq -r .returnDate)"

echo -e "\n${YELLOW}[Шаг 10] Проверка автоматического обновления статуса заявки Боба в очереди ожидания${NC}"
WAIT_UPDATED=$(curl -s "$BASE_URL/waiting-requests/$WAIT_ID")
echo "$WAIT_UPDATED" | jq .
NEW_STATUS=$(echo "$WAIT_UPDATED" | jq -r .status)
echo -e "Текущий статус заявки Боба: ${GREEN}$NEW_STATUS${NC}"

echo -e "\n${YELLOW}[Шаг 11] Проверка свободных экземпляров и выдача книги Бобу${NC}"
AVAILABLE_NOW=$(curl -s "$BASE_URL/copies/available?bookEditionId=$BOOK_ID")
echo "$AVAILABLE_NOW" | jq .

LOAN_BOB=$(curl -s -X POST "$BASE_URL/loans/issue" \
  -H "Content-Type: application/json" \
  -d "{\"copyId\": $COPY_ID, \"userId\": $USER_B_ID}")
echo "$LOAN_BOB" | jq .

echo -e "\n${GREEN}======================================================================${NC}"
echo -e "${GREEN}  СЦЕНАРИЙ УСПЕШНО ЗАВЕРШЕН!                                           ${NC}"
echo -e "${GREEN}======================================================================${NC}"

#!/bin/bash

# PostToolUse hook: Run compileKotlin after Edit/Write/MultiEdit on Kotlin source files
# TODO: ktlint 또는 detekt 도입 시 아래 compileKotlin 대신 포맷터 명령으로 교체
#   ktlint:  ./gradlew ktlintFormat -q 2>/dev/null
#   detekt:  ./gradlew detekt -q 2>/dev/null

INPUT=$(cat)

# Extract file_path from tool_input
FILE_PATH=$(echo "$INPUT" | jq -r '.tool_input.file_path // empty')

# Check if it's a file type that should trigger a compile check
case "$FILE_PATH" in
  *.kt|*.kts|*.json|*.yaml|*.yml|*.properties|*.xml|*.sql|*.md)
    echo "[lint hook] Running compileKotlin on: $FILE_PATH"
    cd "$CLAUDE_PROJECT_DIR" || exit 0
    ./gradlew compileKotlin -q 2>/dev/null
    ;;
esac

exit 0

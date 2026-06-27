#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

find_jdk() {
    if command -v javac >/dev/null 2>&1; then
        local javac_path
        javac_path="$(command -v javac)"
        dirname "$(dirname "$javac_path")"
        return 0
    fi

    local candidates=(
        "/c/Program Files/Eclipse Adoptium/jdk-21.0.11.10-hotspot"
        "/c/Program Files/Eclipse Adoptium/jdk-21"*
        "/c/Program Files/Microsoft/jdk-21"*
        "/c/Program Files/Java/jdk-21"*
        "$LOCALAPPDATA/Programs/Eclipse Adoptium/jdk-21"*
    )

    for candidate in "${candidates[@]}"; do
        if [[ -x "$candidate/bin/javac.exe" ]]; then
            echo "$candidate"
            return 0
        fi
    done

    return 1
}

if ! JAVA_HOME="$(find_jdk)"; then
    echo "Erro: JDK nao encontrado."
    echo "Instale com: winget install -e --id EclipseAdoptium.Temurin.21.JDK"
    exit 1
fi

JAVAC="$JAVA_HOME/bin/javac"
JAVA="$JAVA_HOME/bin/java"

if [[ "$OSTYPE" == msys* || "$OSTYPE" == cygwin* ]]; then
    JAVAC="${JAVAC}.exe"
    JAVA="${JAVA}.exe"
fi

SOURCES=$(find src -name "*.java")

echo "Usando JDK: $JAVA_HOME"
echo
echo "Compilando..."
rm -rf out
mkdir -p out
"$JAVAC" -d out $SOURCES

echo
echo "Executando Main..."
echo
"$JAVA" -cp out app.Main

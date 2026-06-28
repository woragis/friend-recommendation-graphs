#!/usr/bin/env bash
# Compila e executa o LinkedIn Analyzer.
# Exige JDK 16+ (text blocks e Stream.toList no codigo do professor).

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

JAVA_MIN_MAJOR=16

java_major_from_javac() {
    local javac_bin="$1"
    local version_line
    version_line="$("$javac_bin" -version 2>&1)"

    if [[ "$version_line" =~ javac\ 1\.([0-9]+) ]]; then
        echo "${BASH_REMATCH[1]}"
        return 0
    fi

    if [[ "$version_line" =~ javac\ ([0-9]+) ]]; then
        echo "${BASH_REMATCH[1]}"
        return 0
    fi

    return 1
}

jdk_suporta_projeto() {
    local javac_bin="$1"
    local major

    if ! major="$(java_major_from_javac "$javac_bin")"; then
        return 1
    fi

    [[ "$major" -ge "$JAVA_MIN_MAJOR" ]]
}

find_jdk() {
    local javac_path java_home major

    if command -v javac >/dev/null 2>&1; then
        javac_path="$(command -v javac)"
        if jdk_suporta_projeto "$javac_path"; then
            dirname "$(dirname "$javac_path")"
            return 0
        fi
        major="$(java_major_from_javac "$javac_path" || echo "?")"
        echo "Aviso: javac do PATH e Java $major (minimo $JAVA_MIN_MAJOR). Procurando outro JDK..." >&2
    fi

    local candidates=(
        "/c/Program Files/Eclipse Adoptium/jdk-21.0.11.10-hotspot"
        "/c/Program Files/Eclipse Adoptium/jdk-21"*
        "/c/Program Files/Microsoft/jdk-21"*
        "/c/Program Files/Java/jdk-21"*
        "/c/Program Files/Java/jdk-17"*
        "/c/Program Files/Eclipse Adoptium/jdk-17"*
        "$LOCALAPPDATA/Programs/Eclipse Adoptium/jdk-21"*
        "$LOCALAPPDATA/Programs/Eclipse Adoptium/jdk-17"*
    )

    for candidate in "${candidates[@]}"; do
        if [[ -x "$candidate/bin/javac.exe" ]] && jdk_suporta_projeto "$candidate/bin/javac.exe"; then
            echo "$candidate"
            return 0
        fi
        if [[ -x "$candidate/bin/javac" ]] && jdk_suporta_projeto "$candidate/bin/javac"; then
            echo "$candidate"
            return 0
        fi
    done

    return 1
}

if ! JAVA_HOME="$(find_jdk)"; then
    echo "Erro: JDK $JAVA_MIN_MAJOR+ nao encontrado."
    echo "O projeto nao compila com Java 8. Instale JDK 21:"
    echo "  winget install -e --id EclipseAdoptium.Temurin.21.JDK"
    exit 1
fi

JAVAC="$JAVA_HOME/bin/javac"
JAVA="$JAVA_HOME/bin/java"

if [[ "$OSTYPE" == msys* || "$OSTYPE" == cygwin* ]]; then
    JAVAC="${JAVAC}.exe"
    JAVA="${JAVA}.exe"
fi

if [[ ! -x "$JAVAC" ]]; then
    echo "Erro: javac nao encontrado em $JAVA_HOME"
    exit 1
fi

echo "Usando JDK: $JAVA_HOME ($("$JAVAC" -version 2>&1))"
echo
echo "Compilando..."
rm -rf out
mkdir -p out

mapfile -d '' -t SOURCES < <(find src -name '*.java' -print0)
if [[ "${#SOURCES[@]}" -eq 0 ]]; then
    echo "Erro: nenhum arquivo .java em src/"
    exit 1
fi

"$JAVAC" -encoding UTF-8 -d out "${SOURCES[@]}"

echo
echo "Executando Main..."
echo
"$JAVA" -cp out app.Main

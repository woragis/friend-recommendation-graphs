#!/usr/bin/env bash
# Script para compilar e executar o LinkedIn Analyzer com um único comando.
# Encontra automaticamente o JDK 21 (evita conflito com Java 8 do PATH).

set -euo pipefail

# Diretório onde o script está (raiz do projeto)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Tenta localizar o JDK instalado no sistema
find_jdk() {
    # Se javac já está no PATH, usa ele
    if command -v javac >/dev/null 2>&1; then
        local javac_path
        javac_path="$(command -v javac)"
        dirname "$(dirname "$javac_path")"
        return 0
    fi

    # Caminhos comuns do JDK 21 no Windows
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

# Aborta se nenhum JDK for encontrado
if ! JAVA_HOME="$(find_jdk)"; then
    echo "Erro: JDK nao encontrado."
    echo "Instale com: winget install -e --id EclipseAdoptium.Temurin.21.JDK"
    exit 1
fi

JAVAC="$JAVA_HOME/bin/javac"
JAVA="$JAVA_HOME/bin/java"

# No Git Bash (Windows), os executáveis têm extensão .exe
if [[ "$OSTYPE" == msys* || "$OSTYPE" == cygwin* ]]; then
    JAVAC="${JAVAC}.exe"
    JAVA="${JAVA}.exe"
fi

# Lista todos os arquivos .java dentro de src/
SOURCES=$(find src -name "*.java")

echo "Usando JDK: $JAVA_HOME"
echo
echo "Compilando..."
rm -rf out
mkdir -p out
# Compila todos os fontes para a pasta out/ (respeitando os pacotes)
"$JAVAC" -d out $SOURCES

echo
echo "Executando Main..."
echo
# Executa a classe Main do pacote app
"$JAVA" -cp out app.Main

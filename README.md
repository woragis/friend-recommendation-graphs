# LinkedIn Analyzer

Projeto prático da disciplina de Grafos: um motor de análises e recomendações para uma rede de conexões profissionais.

Os arquivos ficam organizados em pacotes dentro de `src/`, sem Maven/Gradle. Basta compilar e executar com o JDK.

## Estrutura do projeto

```
friend-recommendation-graphs/
├── src/
│   ├── modelo/          ← código do professor (grafo)
│   │   ├── Vertice.java
│   │   ├── Aresta.java
│   │   ├── Grafo.java
│   │   └── ResultadoRota.java
│   ├── analyzer/        ← lógica da atividade
│   │   ├── LinkedInAnalyzer.java
│   │   └── SugestaoConexao.java
│   └── app/             ← demonstração
│       └── Main.java
├── run.sh               ← compila e executa
├── README.md
└── .gitignore
```

| Arquivo | Pacote | Origem | Descrição |
|---|---|---|---|
| `Vertice.java` | `modelo` | Professor | Representa um perfil da rede |
| `Aresta.java` | `modelo` | Professor | Representa uma conexão entre dois perfis |
| `Grafo.java` | `modelo` | Professor + Dijkstra | Estrutura do grafo e algoritmos das aulas |
| `ResultadoRota.java` | `modelo` | Projeto | Caminho encontrado + custo acumulado |
| `SugestaoConexao.java` | `analyzer` | Projeto | Nome sugerido + amigos em comum |
| `LinkedInAnalyzer.java` | `analyzer` | Projeto | Cérebro das análises da atividade |
| `Main.java` | `app` | Projeto | Demo com saída formatada para o vídeo |

### O que veio do professor

`Vertice`, `Aresta` e quase todo o `Grafo` foram copiados do repositório `grafos_2026.1`. A única diferença é que o Lombok foi substituído por getters manuais, já que aqui não há dependências externas.

No `Grafo`, o bloco marcado com comentários é o único código novo:

- `getVertices()` — necessário para varrer toda a rede
- `menorCaminhoPonderado()` — implementação do algoritmo de **Dijkstra**

Todo o restante (`dfsIterativo`, `dfsRecursivo`, `greedySearch`, matrizes, etc.) permanece igual ao material das aulas.

### O que é código do projeto

- **`LinkedInAnalyzer`**: implementa as 5 missões da atividade
- **`Main`**: monta a rede no `main`, executa todas as missões e imprime saída formatada para o vídeo
- **`SugestaoConexao`**: estrutura de retorno da missão 2
- **`ResultadoRota`**: estrutura de retorno da missão 4 (fica em `modelo` pois o `Grafo` também a usa)

## As 5 missões

### 1. Construtor

Recebe a instância do `Grafo` e a guarda para as demais análises.

### 2. Sugestão de conexões (`sugerirConexoes`)

Para um usuário, encontra pessoas de 2º grau (amigos de amigos) que ainda não são contato direto. A lista vem ordenada por quantidade de amigos em comum, do maior para o menor.

### 3. Grau de separação (`grauDeSeparacao`)

Usa **BFS** (busca em largura) para encontrar o menor número de passos entre duas pessoas. Retorna `-1` se não houver conexão.

### 4. Rota de maior afinidade (`rotaDeMaiorAfinidade`)

Delega ao **Dijkstra** do `Grafo` para encontrar o caminho de menor custo (maior afinidade). Retorna o caminho e a soma dos pesos.

### 5. Grupos isolados (`mapearGruposIsolados`)

Usa o `dfsRecursivo` do professor para identificar componentes conexos — sub-redes que não se comunicam entre si.

## Rede de testes

```
Rede principal: Ana, Bruno, Carlos, Daniela, Eduardo, Fernanda
Grupo isolado 1: Gabriel ↔ Hugo
Grupo isolado 2: Igor ↔ Juliana
```

### Conexões e pesos

| Conexão | Peso |
|---|---|
| Ana ↔ Bruno | 1 |
| Ana ↔ Carlos | 2 |
| Ana ↔ Daniela | 8 |
| Bruno ↔ Eduardo | 1 |
| Carlos ↔ Eduardo | 1 |
| Daniela ↔ Fernanda | 5 |
| Eduardo ↔ Fernanda | 1 |
| Gabriel ↔ Hugo | 1 |
| Igor ↔ Juliana | 1 |

### Resultados esperados

**Sugestões para Ana:**
- Eduardo (2 amigos em comum: Bruno e Carlos)
- Fernanda (1 amigo em comum: Daniela)

**Grau de separação Ana → Fernanda:** `2` passos

**Rota de maior afinidade Ana → Fernanda:**
- Caminho mais curto em passos: `Ana → Daniela → Fernanda` (custo 13)
- Caminho de Dijkstra: `Ana → Bruno → Eduardo → Fernanda` (custo **3**)

**Componentes conexos:**
1. Ana, Bruno, Carlos, Daniela, Eduardo, Fernanda
2. Gabriel, Hugo
3. Igor, Juliana

## Como executar

Requer **Java 16+** (o código do professor usa text blocks e `.toList()`).

Se `javac` ou `java` não funcionarem, use o script (ele encontra o JDK 21 automaticamente):

```bash
cd friend-recommendation-graphs
chmod +x run.sh
./run.sh
```

Ou manualmente com o JDK 21:

```bash
javac -d out $(find src -name "*.java")
java -cp out app.Main
```

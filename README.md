# LinkedIn Analyzer

Projeto prático da disciplina de Grafos: motor de recomendações para uma rede de conexões profissionais.

A rede é modelada como um **grafo não-direcionado e ponderado**:
- **Vértices** = perfis dos usuários
- **Arestas** = conexões de amizade ou trabalho (bidirecionais)
- **Pesos** = afinidade entre as pessoas (1 = muita proximidade, 5+ = pouca)

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
│   │   ├── SugestaoConexao.java
│   │   └── ResultadoCaminho.java
│   └── app/
│       ├── Main.java
│       └── RedeFactory.java
├── run.sh
├── README.md
└── .gitignore
```

| Arquivo | Descrição |
|---|---|
| `Vertice.java` | Representa um perfil da rede |
| `Aresta.java` | Conexão entre dois perfis com peso de afinidade |
| `Grafo.java` | Estrutura do grafo, BFS/DFS e Dijkstra |
| `LinkedInAnalyzer.java` | Amigos, sugestões, BFS e rota ponderada |
| `SugestaoConexao.java` | Candidato sugerido + amigos em comum |
| `ResultadoCaminho.java` | Caminho e passos retornados pelo BFS |
| `ResultadoRota.java` | Caminho e custo retornados pelo Dijkstra |
| `Main.java` | Menu interativo no console |
| `RedeFactory.java` | Cadastra perfis e conexões com peso |

## Modo interativo

Ao rodar `./run.sh`:

1. **Entrar como usuário** — escolhe um dos perfis
2. **Ver amigos** — conexões diretas (1º grau)
3. **Ver sugestões** — amigos de amigos, ordenados por amigos em comum
4. **Grau de separação** — BFS: menor número de passos até outro perfil
5. **Rota de maior afinidade** — Dijkstra: caminho de menor custo (maior proximidade)

## Funcionalidades

### Sugestão de conexões (`sugerirConexoes`)

A partir das conexões do usuário, encontra pessoas a 2 passos que ainda não são contato direto. Ordena por amigos em comum. **Não usa peso.**

### Grau de separação (`grauDeSeparacao` / `caminhoEmPassos`)

**BFS** — menor número de passos entre duas pessoas. Ignora pesos.

### Rota de maior afinidade (`rotaDeMaiorAfinidade`)

**Dijkstra** — caminho de menor soma de pesos (maior afinidade acumulada).

**Exemplo didático (Ana → Fernanda):**
- BFS: 2 passos — `Ana → Daniela → Fernanda`
- Dijkstra: custo 3 — `Ana → Bruno → Eduardo → Fernanda` (em vez de custo 13 pelo caminho mais curto em passos)

## Rede de testes

**10 perfis** em uma única rede conectada:

Ana, Bruno, Carlos, Daniela, Eduardo, Fernanda, Gabriel, Hugo, Igor, Juliana.

```java
rede.addAresta("Ana", "Bruno", 1);
rede.addAresta("Ana", "Daniela", 8);  // pouca afinidade
// ...
```

## Como executar

Requer **Java 16+** (JDK, não só JRE).

**Git Bash / Linux / macOS:**

```bash
cd friend-recommendation-graphs
chmod +x run.sh
./run.sh
```

**Windows (CMD):**

```bat
run.bat
```

Ou manualmente:

```bash
javac -d out $(find src -name "*.java")
java -cp out app.Main
```

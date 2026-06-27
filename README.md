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
│   │   ├── SugestaoConexao.java
│   │   └── ResultadoCaminho.java
│   └── app/             ← demonstração
│       ├── Main.java
│       └── RedeFactory.java
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
| `Main.java` | `app` | Projeto | Menu interativo no console |
| `RedeFactory.java` | `app` | Projeto | Monta a rede com 25 perfis |
| `ResultadoCaminho.java` | `analyzer` | Projeto | Caminho e passos do BFS |

### O que veio do professor

`Vertice`, `Aresta` e quase todo o `Grafo` foram copiados do repositório `grafos_2026.1`. A única diferença é que o Lombok foi substituído por getters manuais, já que aqui não há dependências externas.

No `Grafo`, o bloco marcado com comentários é o único código novo:

- `getVertices()` — necessário para varrer toda a rede
- `menorCaminhoPonderado()` — implementação do algoritmo de **Dijkstra**

Todo o restante (`dfsIterativo`, `dfsRecursivo`, `greedySearch`, matrizes, etc.) permanece igual ao material das aulas.

### O que é código do projeto

- **`LinkedInAnalyzer`**: implementa as 5 missões da atividade
- **`Main`**: menu interativo — login, amigos, sugestões, grau de separação
- **`RedeFactory`**: cadastra 25 perfis e suas conexões
- **`SugestaoConexao`**: sugestão com amigos em comum e caminho exemplo
- **`ResultadoCaminho`**: caminho completo retornado pelo BFS
- **`ResultadoRota`**: estrutura de retorno da missão 4 (fica em `modelo` pois o `Grafo` também a usa)

## Modo interativo

Ao rodar `./run.sh`, você pode:

1. **Entrar como usuário** — escolhe um dos 25 perfis
2. **Ver amigos** — conexões diretas (1º grau)
3. **Ver sugestões** — amigos de 2º grau, com amigos em comum e caminho `você -> amigo -> sugerido`
4. **Grau de separação** — BFS mostra passos e caminho completo até qualquer perfil
5. **Rota ponderada** — Dijkstra (missão 4 do enunciado, opcional no menu)
6. **Grupos isolados** — componentes conexos da rede inteira

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

**25 perfis** organizados em:

- **Rede principal** (~19 perfis conectados): Ana, Bruno, Carlos, Daniela, Eduardo, Fernanda, Lucas, Marina, Pedro, Rafaela, Thiago, Vanessa, William, Beatriz, Caio, Diego, Elisa, Isabella...
- **Grupo isolado 1:** Gabriel ↔ Hugo
- **Grupo isolado 2:** Igor ↔ Juliana
- **Grupo isolado 3:** Fabio ↔ Gabriela ↔ Henrique

As conexões são cadastradas linha a linha em `RedeFactory.java`.

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

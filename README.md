# LinkedIn Analyzer

Projeto prático da disciplina de Grafos: um motor de recomendações baseado em **amizades** entre usuários.

Cada usuário é um vértice e cada amizade é uma aresta — cadastrada apenas com os nomes dos dois perfis, sem peso.

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
| `Aresta.java` | Representa uma amizade entre dois perfis |
| `Grafo.java` | Estrutura do grafo e algoritmos das aulas |
| `LinkedInAnalyzer.java` | Amigos, sugestões e grau de separação |
| `SugestaoConexao.java` | Candidato sugerido + amigos em comum |
| `ResultadoCaminho.java` | Caminho e passos retornados pelo BFS |
| `Main.java` | Menu interativo no console |
| `RedeFactory.java` | Cadastra perfis e amizades |

## Modo interativo

Ao rodar `./run.sh`:

1. **Entrar como usuário** — escolhe um dos perfis
2. **Ver amigos** — amizades diretas (1º grau)
3. **Ver sugestões** — amigos de amigos, ordenados por amigos em comum
4. **Grau de separação** — BFS mostra passos e caminho de amizades até outro perfil

## Funcionalidades

### Construtor

Recebe o `Grafo` de amizades e o guarda para as análises.

### Sugestão de conexões (`sugerirConexoes`)

A partir das amizades do usuário, encontra pessoas a 2 passos que ainda não são amigo direto. Ordena por quantidade de amigos em comum.

### Grau de separação (`grauDeSeparacao` / `caminhoEmPassos`)

Usa **BFS** para encontrar o menor número de passos de amizade entre duas pessoas. Retorna `-1` se não houver caminho.

## Rede de testes

**10 perfis** conectados por amizades em uma única rede:

Ana, Bruno, Carlos, Daniela, Eduardo, Fernanda, Gabriel, Hugo, Igor, Juliana.

As amizades são cadastradas em `RedeFactory.java`:

```java
rede.addAresta("Ana", "Bruno");
rede.addAresta("Ana", "Carlos");
// ...
```

## Como executar

Requer **Java 16+**.

```bash
cd friend-recommendation-graphs
chmod +x run.sh
./run.sh
```

Ou manualmente:

```bash
javac -d out $(find src -name "*.java")
java -cp out app.Main
```

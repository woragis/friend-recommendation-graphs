// Pacote modelo: estrutura principal do grafo (código do professor + Dijkstra)
package modelo;

import java.util.*;

/**
 * Classe principal que representa o GRAFO da rede social.
 * Gerencia vértices, arestas e todos os algoritmos das aulas.
 * A maior parte veio do professor; o bloco do Dijkstra foi adicionado para o projeto.
 */
public class Grafo {

    // Lista de todas as arestas (conexões) do grafo
    private final List<Aresta> arestas;
    // Lista de todos os vértices (perfis) do grafo
    private final List<Vertice> vertices;
    // Indica se o grafo é direcionado (arestas têm direção)
    private boolean eDirigido;
    // Ordem = quantidade de vértices
    private int ordem;
    // Tamanho = quantidade de arestas
    private int tamanho;
    // Indica se as arestas possuem peso (afinidade)
    private final boolean ePonderado;

    /** Construtor padrão: grafo não-direcionado e não-ponderado. */
    public Grafo() {
        this(false, false);
    }

    /**
     * Construtor parametrizado.
     * @param eDirigido true se as arestas têm direção
     * @param ePonderado true se as arestas têm peso (afinidade)
     */
    public Grafo(boolean eDirigido, boolean ePonderado) {
        this.eDirigido = eDirigido;
        this.ePonderado = ePonderado;
        arestas = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    /**
     * Adiciona um ou mais vértices ao grafo.
     * Aceita quantos nomes forem passados (varargs).
     */
    public void adicionaVertices(String... nomes) {
        for (String nome : nomes) {
            vertices.add(new Vertice(nome)); // cria o vértice e adiciona na lista
            ordem++; // incrementa a contagem de vértices
        }
    }

    /** Adiciona aresta sem nome e sem peso entre v1 e v2. */
    public void addAresta(String v1, String v2) {
        arestas.add(criaAresta("", v1, v2, null));
    }

    /** Adiciona aresta com peso entre v1 e v2 (usado no projeto). */
    public void addAresta(String v1, String v2, int peso) {
        arestas.add(criaAresta("", v1, v2, peso));
    }

    /** Adiciona aresta com nome, sem peso. */
    public void addAresta(String nome, String v1, String v2) {
        arestas.add(criaAresta(nome, v1, v2, null));
    }

    /** Adiciona aresta com nome e peso. */
    public void addAresta(String nome, String v1, String v2, int peso) {
        arestas.add(criaAresta(nome, v1, v2, peso));
    }

    /**
     * Método interno que cria a aresta e atualiza graus e adjacências.
     * Centraliza a lógica de adicionar conexões.
     */
    private Aresta criaAresta(String nomeAresta, String nomeVertice1, String nomeVertice2, Integer peso) {
        // Busca os vértices pelo nome; lança exceção se não existirem
        Vertice v1 = encontraVertice(nomeVertice1).orElseThrow(
                () -> new IllegalArgumentException("Vertice " + nomeVertice1 + " não encontrado."));
        Vertice v2 = encontraVertice(nomeVertice2).orElseThrow(
                () -> new IllegalArgumentException("Vertice " + nomeVertice2 + " não encontrado."));
        // Se o grafo foi criado como não-direcionado, verifica se precisa virar direcionado
        if (!eDirigido) {
            infereSeGrafoEDirecionado(v1, v2);
        }
        aumentaGrauDosVertices(v1, v2); // atualiza graus
        resolveAdjacencias(v1, v2);       // atualiza listas de vizinhos
        tamanho++;                        // incrementa contagem de arestas
        return new Aresta(nomeAresta, v1, v2, peso);
    }

    /**
     * Atualiza as listas de adjacências dos dois vértices.
     * Em grafos não-direcionados, a conexão vale nos dois sentidos.
     */
    private void resolveAdjacencias(Vertice v1, Vertice v2) {
        v1.adicionaAdjacencia(v2); // v1 envia para v2
        v2.adicionaAdjacente(v1);  // v2 recebe de v1
        if (!eDirigido) {
            // Em grafo não-direcionado, a conexão é bidirecional
            v1.adicionaAdjacente(v2);
            v2.adicionaAdjacencia(v1);
        }
    }

    /** Incrementa o grau correto conforme o grafo seja direcionado ou não. */
    private void aumentaGrauDosVertices(Vertice v1, Vertice v2) {
        if (eDirigido) {
            v1.aumentaOutDegree(); // v1 é origem
            v2.aumentaInDegree();  // v2 é destino
        } else {
            v1.aumentaGrau(); // ambos ganham +1 grau
            v2.aumentaGrau();
        }
    }

    /**
     * Detecta situações que forçam o grafo a se tornar direcionado:
     * self-loop ou aresta duplicada na direção oposta.
     */
    private void infereSeGrafoEDirecionado(Vertice v1, Vertice v2) {
        if (eSelfLoop(v1, v2)) {
            reprocessamentoParaDigrafo();
        } else {
            for (Aresta aresta : arestas) {
                if (eViaMaoDupla(v1, v2, aresta) || eArestaDuplicada(v2, v1, aresta)) {
                    reprocessamentoParaDigrafo();
                    break;
                }
            }
        }
    }

    /** Verifica se já existe aresta de v1 para v2. */
    private static boolean eArestaDuplicada(Vertice v1, Vertice v2, Aresta aresta) {
        return aresta.getVerticeOrigem().equals(v1) && aresta.getVerticeDestino().equals(v2);
    }

    /** Verifica se já existe aresta de v2 para v1 (direção oposta). */
    private static boolean eViaMaoDupla(Vertice v1, Vertice v2, Aresta aresta) {
        return aresta.getVerticeOrigem().equals(v2) && aresta.getVerticeDestino().equals(v1);
    }

    /** Verifica se v1 e v2 são o mesmo vértice (loop). */
    private static boolean eSelfLoop(Vertice v1, Vertice v2) {
        return v1.getNome().equals(v2.getNome());
    }

    /**
     * Busca um vértice pelo nome (case-insensitive).
     * Retorna Optional vazio se não encontrar.
     */
    public Optional<Vertice> encontraVertice(String nome) {
        for (Vertice vertice : vertices) {
            if (vertice.getNome().equalsIgnoreCase(nome)) {
                return Optional.of(vertice);
            }
        }
        return Optional.empty();
    }

    // ==================== CÓDIGO ADICIONADO PARA O PROJETO LINKEDIN ANALYZER ====================

    /**
     * Retorna a lista de vértices do grafo.
     * Necessário para varrer toda a rede (ex: componentes conexos).
     */
    public List<Vertice> getVertices() {
        return vertices;
    }

    /**
     * Algoritmo de DIJKSTRA - menor caminho ponderado.
     * Encontra a rota de MENOR CUSTO (maior afinidade) entre origem e destino.
     * Usado na Missão 4 do LinkedIn Analyzer.
     */
    public ResultadoRota menorCaminhoPonderado(String origem, String destino) {
        // Só funciona em grafos ponderados
        if (!ePonderado) {
            throw new IllegalStateException("O grafo precisa ser ponderado para calcular o menor caminho ponderado.");
        }

        // Localiza os vértices de origem e destino
        Vertice verticeOrigem = encontraVertice(origem).orElseThrow(
                () -> new IllegalArgumentException("Vertice " + origem + " não encontrado."));
        Vertice verticeDestino = encontraVertice(destino).orElseThrow(
                () -> new IllegalArgumentException("Vertice " + destino + " não encontrado."));

        // distancias: custo acumulado do menor caminho até cada vértice
        Map<Vertice, Integer> distancias = new HashMap<>();
        // anteriores: de qual vértice viemos para reconstruir o caminho
        Map<Vertice, Vertice> anteriores = new HashMap<>();
        // Fila de prioridade: sempre processa o vértice de menor custo primeiro
        PriorityQueue<Vertice> fila = new PriorityQueue<>(
                Comparator.comparingInt(v -> distancias.getOrDefault(v, Integer.MAX_VALUE)));

        // Inicializa todos com custo infinito (ainda não visitados)
        for (Vertice vertice : vertices) {
            distancias.put(vertice, Integer.MAX_VALUE);
        }

        // A origem tem custo 0
        distancias.put(verticeOrigem, 0);
        fila.add(verticeOrigem);

        // Loop principal do Dijkstra
        while (!fila.isEmpty()) {
            // Remove o vértice de menor custo da fila
            Vertice atual = fila.poll();
            int custoAtual = distancias.get(atual);

            // Entrada obsoleta na fila (já processamos com custo menor)
            if (custoAtual == Integer.MAX_VALUE) {
                continue;
            }

            // Se chegamos no destino, podemos parar
            if (atual.equals(verticeDestino)) {
                break;
            }

            // Relaxa cada vizinho do vértice atual
            for (Vertice vizinho : atual.getAdjacencias()) {
                // Busca a aresta entre atual e vizinho
                List<Aresta> arestasVizinhas = obtemArestasParaVizinho(atual, vizinho);
                if (arestasVizinhas.isEmpty()) {
                    continue;
                }

                // Pega a aresta de menor peso (caso haja mais de uma)
                Aresta melhorAresta = arestasVizinhas.stream()
                        .min(Comparator.comparing(Aresta::getPeso))
                        .orElseThrow();
                int peso = melhorAresta.getPeso() != null ? melhorAresta.getPeso() : 0;
                int novoCusto = custoAtual + peso; // custo para chegar ao vizinho

                // Se encontramos um caminho mais barato até o vizinho, atualiza
                if (novoCusto < distancias.get(vizinho)) {
                    distancias.put(vizinho, novoCusto);
                    anteriores.put(vizinho, atual); // guarda de onde viemos
                    fila.add(vizinho); // reenfileira com novo custo
                }
            }
        }

        // Se o destino ficou com custo infinito, não há caminho
        if (distancias.get(verticeDestino) == Integer.MAX_VALUE) {
            return ResultadoRota.inalcancavel();
        }

        // Reconstrói o caminho de trás para frente usando o mapa anteriores
        List<String> caminho = new ArrayList<>();
        Vertice atual = verticeDestino;
        while (atual != null) {
            caminho.add(0, atual.getNome()); // insere no início da lista
            atual = anteriores.get(atual);   // volta um passo
        }

        return new ResultadoRota(caminho, distancias.get(verticeDestino));
    }

    // ==================== FIM DO CÓDIGO ADICIONADO PARA O PROJETO ====================

    /**
     * Converte o grafo para direcionado e recalcula graus e adjacências.
     * Chamado quando self-loop ou aresta duplicada é detectada.
     */
    private void reprocessamentoParaDigrafo() {
        eDirigido = true;
        System.out.println("Reprocessamento para digrafo necessário. O grafo agora é direcionado.");
        limpezaGrausEAdjacencias();
        recalculaGrausEAdjacencias();
    }

    /** Recalcula graus e adjacências de todas as arestas existentes. */
    private void recalculaGrausEAdjacencias() {
        arestas.forEach(aresta -> {
            Vertice origem = aresta.getVerticeOrigem();
            Vertice destino = aresta.getVerticeDestino();
            aumentaGrauDosVertices(origem, destino);
            resolveAdjacencias(origem, destino);
        });
    }

    /** Zera graus e limpa adjacências de todos os vértices. */
    private void limpezaGrausEAdjacencias() {
        vertices.forEach(vertice -> {
            vertice.resetaGraus();
            vertice.resetaAdjacenciasEAdjacentes();
        });
    }

    /** Retorna string com os graus de todos os vértices. */
    public String exibeGrausDosVertices() {
        StringBuilder graus = new StringBuilder();
        for (Vertice vertice : vertices) {
            graus.append(vertice.exibeGraus());
        }
        return graus.toString();
    }

    /** Retorna string com as adjacências (saída) de cada vértice. */
    public String exibeAdjacencias() {
        StringBuilder adjacencias = new StringBuilder();
        for (Vertice vertice : vertices) {
            adjacencias.append("\n").append(vertice.getNome()).append(": ").append(vertice.getAdjacencias());
        }
        return adjacencias.toString();
    }

    /** Retorna string com os adjacentes (entrada) de cada vértice. */
    public String exibeAdjacentes() {
        StringBuilder adjacencias = new StringBuilder();
        for (Vertice vertice : vertices) {
            adjacencias.append("\n").append(vertice.getNome()).append(": ").append(vertice.getAdjacentes());
        }
        return adjacencias.toString();
    }

    /** Imprime a matriz de adjacência no console (1 = conectado, 0 = não). */
    public void exibeMatrizAdjacencia() {
        List<Vertice> verticesOrdenados = vertices.stream().sorted(Comparator.comparing(Vertice::getNome)).toList();

        StringBuilder matriz = new StringBuilder("\nMatriz de Adjacência\n");
        matriz.append("\t");
        verticesOrdenados.forEach(v -> matriz.append(v.getNome()).append("\t"));
        matriz.append("\n");

        for (Vertice vertice : verticesOrdenados) {
            matriz.append(vertice.getNome()).append("\t");
            List<Vertice> adjacencias = vertice.getAdjacencias();
            for (Vertice outroVertice : verticesOrdenados) {
                matriz.append(adjacencias.contains(outroVertice) ? "1" : "0").append("\t");
            }
            matriz.append("\n");
        }

        System.out.println(matriz);
    }

    /** Imprime a matriz de incidência no console. */
    public void exibeMatrizIncidencia() {
        List<Vertice> verticesOrdenados = vertices.stream().sorted(Comparator.comparing(Vertice::getNome)).toList();
        StringBuilder matriz = new StringBuilder("\nMatriz de Incidência\n\t");
        arestas.forEach(a -> matriz.append(a.getNome()).append("\t"));
        matriz.append("\n");
        for (Vertice vertice : verticesOrdenados) {
            matriz.append(vertice.getNome()).append("\t");
            for (Aresta aresta : arestas) {
                Vertice origem = aresta.getVerticeOrigem();
                Vertice destino = aresta.getVerticeDestino();
                String valor;
                if (origem.equals(vertice) && destino.equals(vertice)) {
                    valor = " 2"; // self-loop
                } else if (origem.equals(vertice)) {
                    valor = eDirigido ? "-1" : "1";
                } else if (destino.equals(vertice)) {
                    valor = " 1";
                } else {
                    valor = " 0"; // vértice não incide nesta aresta
                }
                matriz.append(valor).append("\t");
            }
            matriz.append("\n");
        }
        System.out.println(matriz);
    }

    /**
     * DFS ITERATIVO - Busca em Profundidade usando pilha.
     * Explora o grafo indo o mais fundo possível antes de voltar (backtrack).
     */
    public List<String> dfsIterativo(String origem, String destino) {
        Vertice verticeOrigem = encontraVertice(origem).orElseThrow(
                () -> new IllegalArgumentException("Vertice " + origem + " não encontrado."));
        // destino null = explora todo o grafo sem parar em um alvo
        Vertice verticeDestino = destino == null ? null
                : encontraVertice(destino).orElseThrow(
                        () -> new IllegalArgumentException("Vertice " + destino + " não encontrado."));

        Stack<Vertice> pilha = new Stack<>();       // pilha LIFO para o DFS
        List<Vertice> visitados = new ArrayList<>(); // vértices já visitados
        StringBuilder percurso = new StringBuilder("Percurso = ");

        visitados.add(verticeOrigem);
        pilha.push(verticeOrigem);
        percurso.append(verticeOrigem.getNome()).append(", ");

        while (!pilha.isEmpty()) {
            Vertice atual = pilha.peek(); // olha o topo sem remover

            if (atual.equals(verticeDestino)) {
                break; // encontrou o destino
            }

            List<Vertice> adjacencias = atual.getAdjacencias();
            List<Vertice> adjacenciasOrdenadas = adjacencias.stream().sorted(Comparator.comparing(Vertice::getNome))
                    .toList();

            // Primeiro vizinho ainda não visitado (em ordem alfabética)
            Optional<Vertice> proximo = adjacenciasOrdenadas.stream().filter(a -> !visitados.contains(a)).findFirst();

            if (proximo.isPresent()) {
                Vertice adjacencia = proximo.get();
                visitados.add(adjacencia);
                percurso.append(adjacencia.getNome()).append(", ");
                pilha.push(adjacencia); // avança para o vizinho
            } else {
                pilha.pop(); // backtrack: remove vértice esgotado
            }
        }

        System.out.println(percurso);
        return visitados.stream().map(Vertice::getNome).toList();
    }

    /**
     * DFS RECURSIVO - Busca em Profundidade por recursão.
     * Usado na Missão 5 para encontrar componentes conexos.
     */
    public List<String> dfsRecursivo(String origem, String destino, List<Vertice> visitados) {
        // Se visitados for null, cria lista nova (primeira chamada)
        final List<Vertice> visitadosAtual = visitados != null ? visitados : new ArrayList<>();

        Vertice v = encontraVertice(origem).orElseThrow(
                () -> new IllegalArgumentException("Vertice " + origem + " não encontrado."));
        visitadosAtual.add(v); // marca como visitado

        if (origem.equals(destino)) {
            return visitadosAtual.stream().map(Vertice::getNome).toList();
        }

        // Para cada vizinho não visitado, chama recursivamente
        for (Vertice adj : v.getAdjacencias()) {
            if (visitadosAtual.contains(adj)) {
                continue; // já visitado, pula
            }

            dfsRecursivo(adj.getNome(), destino, visitadosAtual);

            // Se encontrou o destino em algum ramo, retorna
            if (destino != null && visitadosAtual.stream().anyMatch(x -> x.getNome().equals(destino))) {
                return visitadosAtual.stream().map(Vertice::getNome).toList();
            }
        }

        return visitadosAtual.stream().map(Vertice::getNome).toList();
    }

    /**
     * Calcula o comprimento (custo) de um caminho informado como sequência de nomes.
     * Em grafos não-ponderados, retorna a quantidade de arestas.
     */
    public int encontraComprimentoCaminho(String... caminho) {
        if (!ePonderado) {
            return caminho.length - 1;
        }
        int comprimento = 0;
        List<Aresta> arestasPercorridas = new ArrayList<>();

        for (int i = 0; i < caminho.length - 1; i++) {
            int indiceAtual = i;
            Vertice origem = encontraVertice(caminho[indiceAtual]).orElseThrow(
                    () -> new IllegalArgumentException("Vertice " + caminho[indiceAtual] + " não encontrado."));
            Vertice destino = encontraVertice(caminho[indiceAtual + 1]).orElseThrow(
                    () -> new IllegalArgumentException("Vertice " + caminho[indiceAtual + 1] + " não encontrado."));
            Optional<Aresta> aresta = arestas.stream()
                    .filter(a -> a.getVerticeOrigem().equals(origem) && a.getVerticeDestino().equals(destino))
                    .findFirst();
            if (aresta.isPresent()) {
                if (arestasPercorridas.contains(aresta.get())) {
                    throw new IllegalArgumentException("Aresta repetida!");
                }
                arestasPercorridas.add(aresta.get());
                comprimento += aresta.get().getPeso();
            }
        }
        return comprimento;
    }

    /** Verifica se o grafo é conexo (todos os vértices alcançáveis). */
    public boolean eConexo() {
        for (Vertice v : vertices)
            if (v.getInDegree() == 0 || v.getOutDegree() == 0) {
                return false;
            }

        for (Vertice v : vertices) {
            List<String> caminho = dfsIterativo(v.getNome(), null);
            if (caminho.size() < vertices.size()) {
                return false;
            }
        }
        return true;
    }

    /** Versão simplificada da verificação de conexidade usando streams. */
    public boolean eConexoSimplificado() {
        if (vertices.stream().anyMatch(v -> v.getInDegree() == 0 || v.getOutDegree() == 0)) {
            return false;
        }
        return vertices.stream().noneMatch(v -> dfsIterativo(v.getNome(), null).size() < vertices.size());
    }

    /**
     * GREEDY SEARCH - Busca gulosa pelo menor peso em cada passo.
     * Não garante caminho ótimo global (diferente do Dijkstra).
     */
    public List<String> greedySearch(String nomeVerticeOrigem, String nomeVerticeDestino) {
        List<Vertice> verticesVisitados = new ArrayList<>();
        int comprimentoCaminho = 0;

        Vertice verticeOrigem = encontraVertice(nomeVerticeOrigem).orElseThrow();
        Vertice verticeDestino = encontraVertice(nomeVerticeDestino).orElseThrow();

        verticesVisitados.add(verticeOrigem);
        Vertice atual = verticeOrigem;

        while (!atual.equals(verticeDestino)) {
            Vertice verticeAlvo = atual;

            List<Vertice> adjacencias = verticeAlvo.getAdjacencias();
            if (adjacencias == null || adjacencias.isEmpty()) {
                System.out.println("Caminho não encontrado. Busca falhou em: " + atual.getNome());
                return null;
            }

            // Coleta arestas para vizinhos ainda não visitados
            List<Aresta> arestasVizinhas = new ArrayList<>();
            for (Vertice vizinho : adjacencias) {
                if (!verticesVisitados.contains(vizinho)) {
                    arestasVizinhas.addAll(obtemArestasParaVizinho(verticeAlvo, vizinho));
                }
            }

            if (arestasVizinhas.isEmpty()) {
                System.out.println("Caminho não encontrado. Busca falhou em: " + atual.getNome());
                return null;
            }

            // Escolhe gulosamente a aresta de menor peso
            Aresta melhorAresta = arestasVizinhas.stream()
                    .min(Comparator.comparing(Aresta::getPeso))
                    .orElseThrow();

            comprimentoCaminho += melhorAresta.getPeso() != null ? melhorAresta.getPeso() : 0;
            atual = obtemVerticeOposto(melhorAresta, verticeAlvo);
            verticesVisitados.add(atual);

            System.out.println("Percorrendo aresta " + melhorAresta.getNome() +
                    " (peso " + melhorAresta.getPeso() +
                    ") para o vértice " + atual.getNome());
        }

        List<String> nomesVisitados = verticesVisitados.stream().map(Vertice::getNome).toList();

        System.out.println("Destino " + verticeDestino.getNome() + " encontrado! Busca concluída com sucesso.");
        System.out.println("Caminho: " + String.join(" -> ", nomesVisitados));
        System.out.println("Comprimento do caminho: " + comprimentoCaminho);

        return nomesVisitados;
    }

    /**
     * Retorna as arestas que conectam dois vértices vizinhos.
     * Considera direção em grafos não-direcionados.
     */
    private List<Aresta> obtemArestasParaVizinho(Vertice atual, Vertice vizinho) {
        return arestas.stream()
                .filter(a -> (a.getVerticeOrigem().equals(atual) && a.getVerticeDestino().equals(vizinho)) ||
                        (!eDirigido && a.getVerticeDestino().equals(atual) && a.getVerticeOrigem().equals(vizinho)))
                .toList();
    }

    /** Retorna o outro extremo da aresta (o que não é o vértice informado). */
    private Vertice obtemVerticeOposto(Aresta aresta, Vertice vertice) {
        return aresta.getVerticeOrigem().equals(vertice) ? aresta.getVerticeDestino() : aresta.getVerticeOrigem();
    }

    /** Representação textual completa do grafo. */
    @Override
    public String toString() {
        return """
                direcionado = %s,
                ordem = %d,
                tamanho = %d,
                vertices = %s,
                arestas = %s,
                graus = %s,
                adjacencias = %s,
                adjacentes = %s
                }""".formatted(eDirigido ? "sim" : "não", ordem, tamanho, vertices, arestas, exibeGrausDosVertices(),
                exibeAdjacencias(), exibeAdjacentes());
    }
}

// Pacote analyzer: implementa as 5 missões da atividade
package analyzer;

// Importa as classes do grafo que serão analisadas
import modelo.Grafo;
import modelo.ResultadoRota;
import modelo.Vertice;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Cérebro das análises do LinkedIn Analyzer.
 * Implementa as 5 missões do projeto prático:
 * 1. Construtor (recebe o grafo)
 * 2. Sugestão de conexões (amigos de 2º grau)
 * 3. Grau de separação (BFS)
 * 4. Rota de maior afinidade (Dijkstra via Grafo)
 * 5. Grupos isolados (componentes conexos via DFS)
 */
public class LinkedInAnalyzer {

    // Referência ao grafo da rede social (guardada no construtor - Missão 1)
    private final Grafo grafo;

    /**
     * MISSÃO 1 - Construtor da Análise.
     * Recebe a instância do Grafo e a guarda para as demais missões.
     */
    public LinkedInAnalyzer(Grafo grafo) {
        // Validação: o grafo não pode ser nulo
        if (grafo == null) {
            throw new IllegalArgumentException("O grafo não pode ser nulo.");
        }
        // Guarda a referência do grafo para uso nos outros métodos
        this.grafo = grafo;
    }

    /**
     * MISSÃO 2 - Sugestão de Conexões (Amigos de 2º Grau).
     * Encontra pessoas que são amigas de amigas, mas ainda não são contato direto.
     * Ordena por quantidade de amigos em comum (decrescente).
     */
    public List<SugestaoConexao> sugerirConexoes(String nome) {
        // Busca o vértice do usuário no grafo; lança exceção se não existir
        Vertice usuario = grafo.encontraVertice(nome).orElseThrow(
                () -> new IllegalArgumentException("Usuário " + nome + " não encontrado na rede."));

        // Contatos diretos (1º grau): vizinhos imediatos do usuário
        Set<Vertice> contatosDiretos = new HashSet<>(usuario.getAdjacencias());
        // Mapa: nome do candidato -> quantidade de amigos em comum
        Map<String, Integer> amigosEmComum = new HashMap<>();

        // Para cada amigo direto do usuário...
        for (Vertice amigo : contatosDiretos) {
            // ...percorre os amigos desse amigo (candidatos de 2º grau)
            for (Vertice candidato : amigo.getAdjacencias()) {
                // Regra 1: não sugerir o próprio usuário
                // Regra 2: não sugerir quem já é contato direto (1º grau)
                if (candidato.equals(usuario) || contatosDiretos.contains(candidato)) {
                    continue; // pula para o próximo candidato
                }
                // Incrementa a contagem de amigos em comum para este candidato
                amigosEmComum.merge(candidato.getNome(), 1, Integer::sum);
            }
        }

        // Converte o mapa em lista de SugestaoConexao
        List<SugestaoConexao> sugestoes = new ArrayList<>();
        for (Map.Entry<String, Integer> entrada : amigosEmComum.entrySet()) {
            sugestoes.add(new SugestaoConexao(entrada.getKey(), entrada.getValue()));
        }

        // Ordena: primeiro por amigos em comum (maior primeiro), depois por nome
        sugestoes.sort(Comparator
                .comparingInt(SugestaoConexao::getAmigosEmComum).reversed()
                .thenComparing(SugestaoConexao::getNome));

        return sugestoes;
    }

    /**
     * MISSÃO 3 - Grau de Separação.
     * Descobre quantos "passos" de conexão existem entre duas pessoas.
     * Usa BFS (Busca em Largura) para encontrar o menor caminho em número de arestas.
     * Retorna -1 se não houver conexão.
     */
    public int grauDeSeparacao(String origem, String destino) {
        // Localiza os vértices de origem e destino no grafo
        Vertice verticeOrigem = grafo.encontraVertice(origem).orElseThrow(
                () -> new IllegalArgumentException("Vertice " + origem + " não encontrado."));
        Vertice verticeDestino = grafo.encontraVertice(destino).orElseThrow(
                () -> new IllegalArgumentException("Vertice " + destino + " não encontrado."));

        // Caso especial: mesma pessoa = 0 passos
        if (verticeOrigem.equals(verticeDestino)) {
            return 0;
        }

        // Mapa que guarda a distância (em passos) de cada vértice até a origem
        Map<Vertice, Integer> distancias = new HashMap<>();
        // Fila para o BFS (processa vértices em ordem de descoberta)
        Queue<Vertice> fila = new LinkedList<>();

        // Inicializa todos os vértices como não visitados (-1)
        for (Vertice vertice : grafo.getVertices()) {
            distancias.put(vertice, -1);
        }

        // A origem está a 0 passos de si mesma
        distancias.put(verticeOrigem, 0);
        // Coloca a origem na fila para iniciar a busca
        fila.add(verticeOrigem);

        // BFS: enquanto houver vértices na fila...
        while (!fila.isEmpty()) {
            // Remove o primeiro da fila (FIFO)
            Vertice atual = fila.poll();

            // Se chegamos no destino, retorna a distância encontrada
            if (atual.equals(verticeDestino)) {
                return distancias.get(atual);
            }

            // Explora cada vizinho do vértice atual
            for (Vertice vizinho : atual.getAdjacencias()) {
                // Se o vizinho ainda não foi visitado (-1)...
                if (distancias.get(vizinho) == -1) {
                    // ...marca como visitado com distância = atual + 1 passo
                    distancias.put(vizinho, distancias.get(atual) + 1);
                    // Adiciona na fila para explorar seus vizinhos depois
                    fila.add(vizinho);
                }
            }
        }

        // Se a fila esvaziou sem encontrar o destino, não há caminho
        return -1;
    }

    /**
     * MISSÃO 4 - Rota de Maior Afinidade.
     * Encontra o caminho de MENOR CUSTO (maior afinidade) entre duas pessoas.
     * Delega ao algoritmo de Dijkstra implementado na classe Grafo.
     */
    public ResultadoRota rotaDeMaiorAfinidade(String origem, String destino) {
        return grafo.menorCaminhoPonderado(origem, destino);
    }

    /**
     * MISSÃO 5 - Mapear Grupos Isolados (Componentes Conexos).
     * Encontra sub-redes que não se comunicam entre si.
     * Usa o DFS recursivo do professor para explorar cada componente.
     */
    public List<List<String>> mapearGruposIsolados() {
        // Conjunto global de nomes já visitados em qualquer componente
        Set<String> visitadosGlobal = new HashSet<>();
        // Lista final de componentes (cada um é uma lista de nomes)
        List<List<String>> componentes = new ArrayList<>();

        // Percorre todos os vértices do grafo
        for (Vertice vertice : grafo.getVertices()) {
            // Se este vértice já foi incluído em algum componente, pula
            if (visitadosGlobal.contains(vertice.getNome())) {
                continue;
            }

            // DFS a partir deste vértice descobre todo o componente conexo
            List<String> grupo = grafo.dfsRecursivo(vertice.getNome(), null, new ArrayList<>());
            // Marca todos os nomes do grupo como visitados
            visitadosGlobal.addAll(grupo);

            // Ordena os nomes do grupo alfabeticamente
            List<String> grupoOrdenado = new ArrayList<>(grupo);
            grupoOrdenado.sort(String::compareToIgnoreCase);
            // Adiciona o grupo à lista de componentes
            componentes.add(grupoOrdenado);
        }

        // Ordena os componentes pelo primeiro nome de cada grupo
        componentes.sort(Comparator.comparing(grupo -> grupo.get(0)));
        return componentes;
    }
}

// Pacote analyzer: implementa as 5 missões da atividade
package analyzer;

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
        if (grafo == null) {
            throw new IllegalArgumentException("O grafo não pode ser nulo.");
        }
        this.grafo = grafo;
    }

    /** Lista os amigos diretos (1º grau) do usuário, em ordem alfabética. */
    public List<String> listarAmigos(String nome) {
        Vertice usuario = buscarUsuario(nome);
        List<String> amigos = new ArrayList<>();
        for (Vertice amigo : usuario.getAdjacencias()) {
            amigos.add(amigo.getNome());
        }
        amigos.sort(String::compareToIgnoreCase);
        return amigos;
    }

    /** Lista todos os perfis cadastrados na rede, em ordem alfabética. */
    public List<String> listarPerfis() {
        List<String> perfis = new ArrayList<>();
        for (Vertice vertice : grafo.getVertices()) {
            perfis.add(vertice.getNome());
        }
        perfis.sort(String::compareToIgnoreCase);
        return perfis;
    }

    /**
     * MISSÃO 2 - Sugestão de Conexões (Amigos de 2º Grau).
     * Encontra pessoas que são amigas de amigas, mas ainda não são contato direto.
     * Ordena por quantidade de amigos em comum (decrescente).
     */
    public List<SugestaoConexao> sugerirConexoes(String nome) {
        Vertice usuario = buscarUsuario(nome);
        Set<Vertice> contatosDiretos = new HashSet<>(usuario.getAdjacencias());
        Map<String, List<String>> amigosMutuosPorCandidato = new HashMap<>();

        for (Vertice amigo : contatosDiretos) {
            for (Vertice candidato : amigo.getAdjacencias()) {
                // Não sugerir o próprio usuário nem quem já é amigo direto
                if (candidato.equals(usuario) || contatosDiretos.contains(candidato)) {
                    continue;
                }
                String nomeCandidato = candidato.getNome();
                amigosMutuosPorCandidato
                        .computeIfAbsent(nomeCandidato, chave -> new ArrayList<>())
                        .add(amigo.getNome());
            }
        }

        List<SugestaoConexao> sugestoes = new ArrayList<>();
        for (Map.Entry<String, List<String>> entrada : amigosMutuosPorCandidato.entrySet()) {
            String nomeCandidato = entrada.getKey();
            List<String> amigosMutuos = entrada.getValue();
            amigosMutuos.sort(String::compareToIgnoreCase);

            // Caminho exemplo: usuário -> primeiro amigo em comum -> candidato
            String amigoPonte = amigosMutuos.get(0);
            String caminhoExemplo = nome + " -> " + amigoPonte + " -> " + nomeCandidato;

            sugestoes.add(new SugestaoConexao(
                    nomeCandidato,
                    amigosMutuos.size(),
                    amigosMutuos,
                    caminhoExemplo));
        }

        sugestoes.sort(Comparator
                .comparingInt(SugestaoConexao::getAmigosEmComum).reversed()
                .thenComparing(SugestaoConexao::getNome));

        return sugestoes;
    }

    /**
     * MISSÃO 3 - Grau de Separação (apenas o número de passos).
     * Delega ao BFS de caminhoEmPassos.
     */
    public int grauDeSeparacao(String origem, String destino) {
        return caminhoEmPassos(origem, destino).getPassos();
    }

    /**
     * BFS que retorna o caminho completo e o número de passos entre duas pessoas.
     * Usado no menu interativo para mostrar como o algoritmo chegou no destino.
     */
    public ResultadoCaminho caminhoEmPassos(String origem, String destino) {
        Vertice verticeOrigem = buscarUsuario(origem);
        Vertice verticeDestino = buscarUsuario(destino);

        if (verticeOrigem.equals(verticeDestino)) {
            return new ResultadoCaminho(List.of(origem), 0);
        }

        // distancias: passos do origem até cada vértice (-1 = ainda não visitado)
        Map<Vertice, Integer> distancias = new HashMap<>();
        // anteriores: de qual vértice viemos (para reconstruir o caminho)
        Map<Vertice, Vertice> anteriores = new HashMap<>();
        Queue<Vertice> fila = new LinkedList<>();

        // Inicializa todos como não visitados
        for (Vertice vertice : grafo.getVertices()) {
            distancias.put(vertice, -1);
        }

        distancias.put(verticeOrigem, 0);
        fila.add(verticeOrigem);

        while (!fila.isEmpty()) {
            Vertice atual = fila.poll();

            if (atual.equals(verticeDestino)) {
                break;
            }

            for (Vertice vizinho : atual.getAdjacencias()) {
                if (distancias.get(vizinho) == -1) {
                    distancias.put(vizinho, distancias.get(atual) + 1);
                    anteriores.put(vizinho, atual);
                    fila.add(vizinho);
                }
            }
        }

        if (distancias.get(verticeDestino) == -1) {
            return ResultadoCaminho.inalcancavel();
        }

        // Reconstrói o caminho de trás para frente
        List<String> caminho = new ArrayList<>();
        Vertice atual = verticeDestino;
        while (atual != null) {
            caminho.add(0, atual.getNome());
            atual = anteriores.get(atual);
        }

        return new ResultadoCaminho(caminho, distancias.get(verticeDestino));
    }

    /**
     * MISSÃO 4 - Rota de Maior Afinidade.
     * Delega ao Dijkstra implementado na classe Grafo.
     */
    public ResultadoRota rotaDeMaiorAfinidade(String origem, String destino) {
        return grafo.menorCaminhoPonderado(origem, destino);
    }

    /**
     * MISSÃO 5 - Mapear Grupos Isolados (Componentes Conexos).
     * Usa o DFS recursivo do professor para explorar cada sub-rede.
     */
    public List<List<String>> mapearGruposIsolados() {
        Set<String> visitadosGlobal = new HashSet<>();
        List<List<String>> componentes = new ArrayList<>();

        for (Vertice vertice : grafo.getVertices()) {
            if (visitadosGlobal.contains(vertice.getNome())) {
                continue;
            }

            List<String> grupo = grafo.dfsRecursivo(vertice.getNome(), null, new ArrayList<>());
            visitadosGlobal.addAll(grupo);

            List<String> grupoOrdenado = new ArrayList<>(grupo);
            grupoOrdenado.sort(String::compareToIgnoreCase);
            componentes.add(grupoOrdenado);
        }

        componentes.sort(Comparator.comparing(grupo -> grupo.get(0)));
        return componentes;
    }

    /** Localiza um vértice pelo nome ou lança exceção se o perfil não existir. */
    private Vertice buscarUsuario(String nome) {
        return grafo.encontraVertice(nome).orElseThrow(
                () -> new IllegalArgumentException("Usuário " + nome + " não encontrado na rede."));
    }
}

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

public class LinkedInAnalyzer {

    private final Grafo grafo;

    public LinkedInAnalyzer(Grafo grafo) {
        if (grafo == null) {
            throw new IllegalArgumentException("O grafo não pode ser nulo.");
        }
        this.grafo = grafo;
    }

    public List<String> listarAmigos(String nome) {
        Vertice usuario = buscarUsuario(nome);
        List<String> amigos = new ArrayList<>();
        for (Vertice amigo : usuario.getAdjacencias()) {
            amigos.add(amigo.getNome());
        }
        amigos.sort(String::compareToIgnoreCase);
        return amigos;
    }

    public List<String> listarPerfis() {
        List<String> perfis = new ArrayList<>();
        for (Vertice vertice : grafo.getVertices()) {
            perfis.add(vertice.getNome());
        }
        perfis.sort(String::compareToIgnoreCase);
        return perfis;
    }

    public List<SugestaoConexao> sugerirConexoes(String nome) {
        Vertice usuario = buscarUsuario(nome);
        Set<Vertice> contatosDiretos = new HashSet<>(usuario.getAdjacencias());
        Map<String, List<String>> amigosMutuosPorCandidato = new HashMap<>();

        for (Vertice amigo : contatosDiretos) {
            for (Vertice candidato : amigo.getAdjacencias()) {
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

    public int grauDeSeparacao(String origem, String destino) {
        return caminhoEmPassos(origem, destino).getPassos();
    }

    public ResultadoCaminho caminhoEmPassos(String origem, String destino) {
        Vertice verticeOrigem = buscarUsuario(origem);
        Vertice verticeDestino = buscarUsuario(destino);

        if (verticeOrigem.equals(verticeDestino)) {
            return new ResultadoCaminho(List.of(origem), 0);
        }

        Map<Vertice, Integer> distancias = new HashMap<>();
        Map<Vertice, Vertice> anteriores = new HashMap<>();
        Queue<Vertice> fila = new LinkedList<>();

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

        List<String> caminho = new ArrayList<>();
        Vertice atual = verticeDestino;
        while (atual != null) {
            caminho.add(0, atual.getNome());
            atual = anteriores.get(atual);
        }

        return new ResultadoCaminho(caminho, distancias.get(verticeDestino));
    }

    public ResultadoRota rotaDeMaiorAfinidade(String origem, String destino) {
        return grafo.menorCaminhoPonderado(origem, destino);
    }

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

    private Vertice buscarUsuario(String nome) {
        return grafo.encontraVertice(nome).orElseThrow(
                () -> new IllegalArgumentException("Usuário " + nome + " não encontrado na rede."));
    }
}

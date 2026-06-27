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

    public List<SugestaoConexao> sugerirConexoes(String nome) {
        Vertice usuario = grafo.encontraVertice(nome).orElseThrow(
                () -> new IllegalArgumentException("Usuário " + nome + " não encontrado na rede."));

        Set<Vertice> contatosDiretos = new HashSet<>(usuario.getAdjacencias());
        Map<String, Integer> amigosEmComum = new HashMap<>();

        for (Vertice amigo : contatosDiretos) {
            for (Vertice candidato : amigo.getAdjacencias()) {
                if (candidato.equals(usuario) || contatosDiretos.contains(candidato)) {
                    continue;
                }
                amigosEmComum.merge(candidato.getNome(), 1, Integer::sum);
            }
        }

        List<SugestaoConexao> sugestoes = new ArrayList<>();
        for (Map.Entry<String, Integer> entrada : amigosEmComum.entrySet()) {
            sugestoes.add(new SugestaoConexao(entrada.getKey(), entrada.getValue()));
        }

        sugestoes.sort(Comparator
                .comparingInt(SugestaoConexao::getAmigosEmComum).reversed()
                .thenComparing(SugestaoConexao::getNome));

        return sugestoes;
    }

    public int grauDeSeparacao(String origem, String destino) {
        Vertice verticeOrigem = grafo.encontraVertice(origem).orElseThrow(
                () -> new IllegalArgumentException("Vertice " + origem + " não encontrado."));
        Vertice verticeDestino = grafo.encontraVertice(destino).orElseThrow(
                () -> new IllegalArgumentException("Vertice " + destino + " não encontrado."));

        if (verticeOrigem.equals(verticeDestino)) {
            return 0;
        }

        Map<Vertice, Integer> distancias = new HashMap<>();
        Queue<Vertice> fila = new LinkedList<>();

        for (Vertice vertice : grafo.getVertices()) {
            distancias.put(vertice, -1);
        }

        distancias.put(verticeOrigem, 0);
        fila.add(verticeOrigem);

        while (!fila.isEmpty()) {
            Vertice atual = fila.poll();

            if (atual.equals(verticeDestino)) {
                return distancias.get(atual);
            }

            for (Vertice vizinho : atual.getAdjacencias()) {
                if (distancias.get(vizinho) == -1) {
                    distancias.put(vizinho, distancias.get(atual) + 1);
                    fila.add(vizinho);
                }
            }
        }

        return -1;
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
}

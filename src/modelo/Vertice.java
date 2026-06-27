// Pacote modelo: contém as classes base do grafo (código do professor)
package modelo;

// Importa ArrayList para criar listas dinâmicas de vizinhos
import java.util.ArrayList;
// Importa List para tipar as listas de adjacências
import java.util.List;

/**
 * Representa um VÉRTICE do grafo.
 * No contexto do LinkedIn Analyzer, cada vértice é um perfil de usuário.
 * Exemplo: "Ana", "Bruno", "Carlos"...
 */
public class Vertice {

    // Nome do perfil (identificador do vértice)
    private String nome;
    // Grau total do vértice (quantidade de conexões)
    private int grau;
    // Grau de entrada: quantas arestas CHEGAM neste vértice (usado em grafos direcionados)
    private int inDegree; // deve ser 0 para não-dirigido
    // Grau de saída: quantas arestas SAEM deste vértice (usado em grafos direcionados)
    private int outDegree;// deve ser 0 para não-dirigido
    // Lista de vértices para os quais este vértice ENVIA conexão (vizinhos de saída)
    private List<Vertice> adjacencias; //out
    // Lista de vértices que ENVIAM conexão para este vértice (vizinhos de entrada)
    private List<Vertice> adjacentes; //in

    /**
     * Construtor: cria um vértice com o nome informado.
     * Inicializa as listas de adjacências vazias.
     */
    public Vertice(String nome) {
        // Guarda o nome do perfil
        this.nome = nome;
        // Cria lista vazia de vizinhos de saída
        adjacencias = new ArrayList<>();
        // Cria lista vazia de vizinhos de entrada
        adjacentes = new ArrayList<>();
    }

    // Retorna o nome do vértice (ex: "Ana")
    public String getNome() {
        return nome;
    }

    // Retorna o grau total do vértice
    public int getGrau() {
        return grau;
    }

    // Retorna o grau de entrada (in-degree)
    public int getInDegree() {
        return inDegree;
    }

    // Retorna o grau de saída (out-degree)
    public int getOutDegree() {
        return outDegree;
    }

    // Retorna a lista de vértices adjacentes (vizinhos de saída)
    // Usado pelos algoritmos BFS, DFS e Dijkstra para navegar o grafo
    public List<Vertice> getAdjacencias() {
        return adjacencias;
    }

    // Retorna a lista de vértices que apontam para este
    public List<Vertice> getAdjacentes() {
        return adjacentes;
    }

    /**
     * Zera todos os graus do vértice.
     * Usado quando o grafo precisa ser reprocessado como direcionado.
     */
    public void resetaGraus() {
        grau = inDegree = outDegree = 0;
    }

    /**
     * Limpa as listas de adjacências e adjacentes.
     * Usado antes de recalcular o grafo como direcionado.
     */
    public void resetaAdjacenciasEAdjacentes() {
        adjacencias.clear();
        adjacentes.clear();
    }

    /**
     * Incrementa o grau em grafos NÃO-direcionados.
     * Cada aresta adicionada aumenta o grau em 1.
     */
    public void aumentaGrau() { // não-dirigido
        grau++;
    }

    /**
     * Incrementa grau total e grau de entrada.
     * Usado em grafos direcionados quando uma aresta chega neste vértice.
     */
    public void aumentaInDegree() {
        grau++;
        inDegree++;
    }

    /**
     * Incrementa grau total e grau de saída.
     * Usado em grafos direcionados quando uma aresta sai deste vértice.
     */
    public void aumentaOutDegree() {
        grau++;
        outDegree++;
    }

    /**
     * Adiciona um vértice à lista de adjacências (saída).
     * Significa: "este vértice tem conexão com o vértice informado".
     */
    public void adicionaAdjacencia(Vertice vertice) {
        adjacencias.add(vertice);
    }

    /**
     * Adiciona um vértice à lista de adjacentes (entrada).
     * Significa: "o vértice informado tem conexão com este".
     */
    public void adicionaAdjacente(Vertice vertice) {
        adjacentes.add(vertice);
    }

    /**
     * Formata os graus do vértice para exibição no console.
     * Exemplo de saída: "\nAna: grau 3 (0 in | 0 out)"
     */
    public String exibeGraus() {
        return "\n%s: grau %d (%d in | %d out)".formatted(nome, grau, inDegree, outDegree);
    }

    /**
     * Ao imprimir o vértice, mostra apenas o nome.
     * Facilita a leitura em listas e logs.
     */
    @Override
    public String toString() {
        return nome;
    }
}

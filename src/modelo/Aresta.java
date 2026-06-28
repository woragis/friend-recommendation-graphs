// Pacote modelo: classes base do grafo (código do professor)
package modelo;

/**
 * Representa uma ARESTA do grafo — uma conexão entre dois perfis.
 * O peso representa a afinidade: 1 = muita proximidade, 5+ = pouca interação.
 */
public class Aresta {

    // Nome opcional da aresta (identificador)
    private String nome;
    // Vértice de onde a aresta parte
    private Vertice verticeOrigem;
    // Vértice para onde a aresta chega
    private Vertice verticeDestino;
    // Peso da aresta: 1 = muita afinidade, 5+ = pouca afinidade
    private Integer peso;

    /**
     * Construtor completo: cria aresta com nome, origem, destino e peso.
     */
    public Aresta(String nome, Vertice verticeOrigem, Vertice verticeDestino, Integer peso) {
        this.nome = nome;
        this.verticeOrigem = verticeOrigem;
        this.verticeDestino = verticeDestino;
        this.peso = peso;
    }

    /**
     * Construtor sem peso: delega para o construtor principal com peso null.
     */
    public Aresta(String nome, Vertice verticeOrigem, Vertice verticeDestino) {
        this(nome, verticeOrigem, verticeDestino, null);
    }

    /**
     * Construtor sem nome: delega com nome null.
     */
    public Aresta(Vertice verticeOrigem, Vertice verticeDestino, Integer peso) {
        this(null, verticeOrigem, verticeDestino, peso);
    }

    /**
     * Construtor mínimo: apenas origem e destino, sem nome e sem peso.
     */
    public Aresta(Vertice verticeOrigem, Vertice verticeDestino) {
        this(null, verticeOrigem, verticeDestino, null);
    }

    /**
     * Retorna o nome da aresta.
     * Se for null, retorna string vazia para evitar NullPointerException.
     */
    public String getNome() {
        return nome != null ? nome : "";
    }

    // Retorna o vértice de origem da aresta
    public Vertice getVerticeOrigem() {
        return verticeOrigem;
    }

    // Retorna o vértice de destino da aresta
    public Vertice getVerticeDestino() {
        return verticeDestino;
    }

    // Retorna o peso (afinidade) da conexão
    public Integer getPeso() {
        return peso;
    }

    /**
     * Formata a aresta para exibição.
     * Exemplo: "{Ana,Bruno}"
     */
    @Override
    public String toString() {
        return "\n" + getNome() + "{" + verticeOrigem.getNome() + "," + verticeDestino.getNome() + "}";
    }
}

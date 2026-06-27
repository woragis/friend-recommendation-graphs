// Pacote modelo: estruturas de dados usadas pelo grafo
package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe de retorno da Missão 4 (rota de maior afinidade).
 * Guarda o caminho encontrado e o custo total (soma dos pesos).
 */
public class ResultadoRota {

    // Sequência ordenada de nomes que formam o caminho (ex: Ana -> Bruno -> Eduardo)
    private final List<String> caminho;
    // Soma dos pesos das arestas percorridas (-1 se inalcançável)
    private final int custo;

    /**
     * Construtor: recebe o caminho e o custo.
     * Cria uma cópia da lista para evitar alterações externas.
     */
    public ResultadoRota(List<String> caminho, int custo) {
        this.caminho = new ArrayList<>(caminho);
        this.custo = custo;
    }

    /**
     * Factory method: cria um resultado para quando NÃO há caminho entre os perfis.
     * Retorna caminho vazio e custo -1.
     */
    public static ResultadoRota inalcancavel() {
        return new ResultadoRota(Collections.emptyList(), -1);
    }

    /**
     * Retorna o caminho de forma imutável (não pode ser alterado de fora).
     */
    public List<String> getCaminho() {
        return Collections.unmodifiableList(caminho);
    }

    // Retorna o custo total da rota
    public int getCusto() {
        return custo;
    }

    /**
     * Verifica se existe caminho entre origem e destino.
     * Custo >= 0 significa alcançável; -1 significa inalcançável.
     */
    public boolean eAlcancavel() {
        return custo >= 0;
    }

    /**
     * Formata o resultado para exibição no console.
     */
    @Override
    public String toString() {
        // Se não há caminho, informa que é inalcançável
        if (!eAlcancavel()) {
            return "Inalcançável (custo = -1, caminho vazio)";
        }
        // Junta os nomes com " -> " e exibe o custo
        return String.join(" -> ", caminho) + " | custo = " + custo;
    }
}

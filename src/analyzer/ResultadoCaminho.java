// Pacote analyzer: retorno do BFS com caminho completo
package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Resultado do BFS: caminho de amizades entre dois perfis.
 * Guarda a sequência de nomes e o número de passos.
 */
public class ResultadoCaminho {

    // Sequência ordenada de perfis no caminho (ex: Ana -> Bruno -> Eduardo)
    private final List<String> caminho;
    // Número de passos (-1 se inalcançável)
    private final int passos;

    /**
     * Construtor: recebe o caminho e a quantidade de passos.
     * Cria cópia da lista para evitar alterações externas.
     */
    public ResultadoCaminho(List<String> caminho, int passos) {
        this.caminho = new ArrayList<>(caminho);
        this.passos = passos;
    }

    /** Factory para quando não existe caminho entre os perfis. */
    public static ResultadoCaminho inalcancavel() {
        return new ResultadoCaminho(Collections.emptyList(), -1);
    }

    // Retorna o caminho de forma imutável
    public List<String> getCaminho() {
        return Collections.unmodifiableList(caminho);
    }

    // Retorna o número de passos (-1 se inalcançável)
    public int getPassos() {
        return passos;
    }

    /** Verifica se existe caminho (passos >= 0). */
    public boolean eAlcancavel() {
        return passos >= 0;
    }

    /** Formata o caminho com setas para exibição no console. */
    public String caminhoFormatado() {
        return String.join(" -> ", caminho);
    }

    @Override
    public String toString() {
        if (!eAlcancavel()) {
            return "Sem conexao";
        }
        return caminhoFormatado() + " (" + passos + " passo(s))";
    }
}

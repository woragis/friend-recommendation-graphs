// Pacote analyzer: estruturas de retorno das análises
package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe de retorno da Missão 2 (sugestão de conexões).
 * Guarda quem foi sugerido, quantos amigos em comum existem,
 * quais são esses amigos e um caminho exemplo de 2 passos.
 */
public class SugestaoConexao {

    // Nome da pessoa sugerida (ex: "Eduardo")
    private final String nome;
    // Quantidade de amigos em comum com o usuário analisado
    private final int amigosEmComum;
    // Nomes dos amigos que conectam o usuário ao candidato
    private final List<String> nomesAmigosEmComum;
    // Caminho de 2 passos: usuario -> amigo -> candidato
    private final String caminhoExemplo;

    /**
     * Cria uma sugestão com todos os dados necessários para exibição no menu.
     */
    public SugestaoConexao(String nome, int amigosEmComum, List<String> nomesAmigosEmComum, String caminhoExemplo) {
        this.nome = nome;
        this.amigosEmComum = amigosEmComum;
        this.nomesAmigosEmComum = new ArrayList<>(nomesAmigosEmComum);
        this.caminhoExemplo = caminhoExemplo;
    }

    // Retorna o nome do candidato sugerido
    public String getNome() {
        return nome;
    }

    // Retorna quantos amigos em comum existem com o usuário
    public int getAmigosEmComum() {
        return amigosEmComum;
    }

    // Retorna a lista imutável dos nomes dos amigos em comum
    public List<String> getNomesAmigosEmComum() {
        return Collections.unmodifiableList(nomesAmigosEmComum);
    }

    // Retorna o caminho exemplo usado na sugestão (2 passos)
    public String getCaminhoExemplo() {
        return caminhoExemplo;
    }

    @Override
    public String toString() {
        return nome + " (" + amigosEmComum + " em comum: " + String.join(", ", nomesAmigosEmComum) + ")";
    }
}

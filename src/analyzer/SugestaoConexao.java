// Pacote analyzer: lógica de análise da rede (código do projeto)
package analyzer;

/**
 * Classe de retorno da Missão 2 (sugestão de conexões).
 * Representa uma pessoa sugerida e quantos amigos em comum ela tem com o usuário.
 */
public class SugestaoConexao {

    // Nome da pessoa sugerida (ex: "Eduardo")
    private final String nome;
    // Quantidade de amigos em comum com o usuário analisado
    private final int amigosEmComum;

    /**
     * Construtor: recebe o nome do candidato e a contagem de amigos em comum.
     */
    public SugestaoConexao(String nome, int amigosEmComum) {
        this.nome = nome;
        this.amigosEmComum = amigosEmComum;
    }

    // Retorna o nome da pessoa sugerida
    public String getNome() {
        return nome;
    }

    // Retorna quantos amigos em comum existem
    public int getAmigosEmComum() {
        return amigosEmComum;
    }

    /**
     * Formata a sugestão para exibição.
     * Exemplo: "Eduardo (2 amigo(s) em comum)"
     */
    @Override
    public String toString() {
        return nome + " (" + amigosEmComum + " amigo(s) em comum)";
    }
}

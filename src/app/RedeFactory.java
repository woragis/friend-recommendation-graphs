// Pacote app: montagem da rede social de testes
package app;

import modelo.Grafo;

/**
 * Factory responsável por criar e popular o grafo de conexões.
 * Grafo não-direcionado e ponderado: peso 1 = muita afinidade, 5+ = pouca afinidade.
 * Todos os perfis pertencem à mesma rede conectada.
 */
public class RedeFactory {

    /**
     * Cria o grafo não-direcionado e ponderado com perfis e conexões.
     * @return grafo pronto para ser passado ao LinkedInAnalyzer
     */
    public static Grafo criarRede() {
        Grafo rede = new Grafo(false, true);

        rede.adicionaVertices("Ana");
        rede.adicionaVertices("Bruno");
        rede.adicionaVertices("Carlos");
        rede.adicionaVertices("Daniela");
        rede.adicionaVertices("Eduardo");
        rede.adicionaVertices("Fernanda");
        rede.adicionaVertices("Gabriel");
        rede.adicionaVertices("Hugo");
        rede.adicionaVertices("Igor");
        rede.adicionaVertices("Juliana");

        // Conexões com peso de afinidade (1 = muita proximidade, 5+ = pouca)
        rede.addAresta("Ana", "Bruno", 1);
        rede.addAresta("Ana", "Carlos", 2);
        rede.addAresta("Ana", "Daniela", 8);
        rede.addAresta("Bruno", "Eduardo", 1);
        rede.addAresta("Carlos", "Eduardo", 1);
        rede.addAresta("Daniela", "Fernanda", 5);
        rede.addAresta("Eduardo", "Fernanda", 1);
        rede.addAresta("Fernanda", "Gabriel", 1);
        rede.addAresta("Gabriel", "Hugo", 1);
        rede.addAresta("Hugo", "Igor", 1);
        rede.addAresta("Igor", "Juliana", 1);
        rede.addAresta("Juliana", "Carlos", 2);

        return rede;
    }
}

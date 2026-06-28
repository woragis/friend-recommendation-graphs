// Pacote app: montagem da rede social de testes
package app;

import modelo.Grafo;

/**
 * Factory responsável por criar e popular o grafo de amizades.
 * Cada aresta representa uma amizade entre dois usuários — apenas nomes, sem peso.
 * Todos os perfis pertencem à mesma rede social conectada.
 */
public class RedeFactory {

    /**
     * Cria o grafo não-direcionado e não-ponderado com perfis e amizades.
     * @return grafo pronto para ser passado ao LinkedInAnalyzer
     */
    public static Grafo criarRede() {
        Grafo rede = new Grafo(false, false);

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

        // Amizades cadastradas apenas com os nomes dos usuários
        rede.addAresta("Ana", "Bruno");
        rede.addAresta("Ana", "Carlos");
        rede.addAresta("Ana", "Daniela");
        rede.addAresta("Bruno", "Eduardo");
        rede.addAresta("Carlos", "Eduardo");
        rede.addAresta("Daniela", "Fernanda");
        rede.addAresta("Eduardo", "Fernanda");
        rede.addAresta("Fernanda", "Gabriel");
        rede.addAresta("Gabriel", "Hugo");
        rede.addAresta("Hugo", "Igor");
        rede.addAresta("Igor", "Juliana");
        rede.addAresta("Juliana", "Carlos");

        return rede;
    }
}

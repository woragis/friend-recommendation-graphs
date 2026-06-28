// Pacote app: montagem da rede social de testes
package app;

import modelo.Grafo;

/**
 * Factory responsável por criar e popular o grafo da rede social.
 * Separa a montagem dos dados do Main para manter o menu interativo enxuto.
 *
 * A rede possui 25 perfis, ~30 conexões e 4 componentes conexos:
 * - Rede principal (Ana até Isabella): maior sub-rede, com caminhos longos
 * - Gabriel + Hugo: grupo isolado de 2 perfis
 * - Igor + Juliana: grupo isolado de 2 perfis
 * - Fabio + Gabriela + Henrique: grupo isolado de 3 perfis
 *
 * Peso 1 = muita afinidade | Peso 5+ = pouca afinidade
 */
public class RedeFactory {

    /**
     * Cria o grafo não-direcionado e ponderado com todos os perfis e conexões.
     * @return grafo pronto para ser passado ao LinkedInAnalyzer
     */
    public static Grafo criarRede() {
        // false = não-direcionado | true = arestas com peso (afinidade)
        Grafo rede = new Grafo(false, true);

        // --- Cadastro dos 25 perfis (vértices) ---
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
        rede.adicionaVertices("Lucas");
        rede.adicionaVertices("Marina");
        rede.adicionaVertices("Pedro");
        rede.adicionaVertices("Rafaela");
        rede.adicionaVertices("Thiago");
        rede.adicionaVertices("Vanessa");
        rede.adicionaVertices("William");
        rede.adicionaVertices("Beatriz");
        rede.adicionaVertices("Caio");
        rede.adicionaVertices("Diego");
        rede.adicionaVertices("Elisa");
        rede.adicionaVertices("Fabio");
        rede.adicionaVertices("Gabriela");
        rede.adicionaVertices("Henrique");
        rede.adicionaVertices("Isabella");

        // --- Rede principal: núcleo original + extensões ---
        rede.addAresta("Ana", "Bruno", 1);
        rede.addAresta("Ana", "Carlos", 2);
        rede.addAresta("Ana", "Daniela", 8);       // pouca afinidade (peso alto)
        rede.addAresta("Bruno", "Eduardo", 1);
        rede.addAresta("Carlos", "Eduardo", 1);
        rede.addAresta("Daniela", "Fernanda", 5);
        rede.addAresta("Eduardo", "Fernanda", 1);

        // Ramificações que ampliam sugestões e caminhos do BFS/Dijkstra
        rede.addAresta("Bruno", "Lucas", 1);
        rede.addAresta("Carlos", "Marina", 2);
        rede.addAresta("Eduardo", "Pedro", 1);
        rede.addAresta("Fernanda", "Rafaela", 3);
        rede.addAresta("Lucas", "Marina", 1);
        rede.addAresta("Marina", "Pedro", 1);
        rede.addAresta("Pedro", "Thiago", 2);
        rede.addAresta("Rafaela", "Thiago", 1);
        rede.addAresta("Daniela", "Vanessa", 4);
        rede.addAresta("Vanessa", "William", 1);
        rede.addAresta("William", "Beatriz", 1);
        rede.addAresta("Beatriz", "Caio", 1);
        rede.addAresta("Vanessa", "Caio", 2);
        rede.addAresta("Thiago", "Diego", 1);
        rede.addAresta("Diego", "Elisa", 1);
        rede.addAresta("Elisa", "Lucas", 1);       // fecha ciclo na sub-rede principal
        rede.addAresta("Beatriz", "Isabella", 1);
        rede.addAresta("Isabella", "Marina", 1);

        // --- Grupos isolados (componentes conexos separados) ---
        rede.addAresta("Gabriel", "Hugo", 1);      // componente 2
        rede.addAresta("Igor", "Juliana", 1);      // componente 3
        rede.addAresta("Fabio", "Gabriela", 1);    // componente 4
        rede.addAresta("Gabriela", "Henrique", 1);

        return rede;
    }
}

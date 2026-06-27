package app;

import modelo.Grafo;

public class RedeFactory {

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

        rede.addAresta("Ana", "Bruno", 1);
        rede.addAresta("Ana", "Carlos", 2);
        rede.addAresta("Ana", "Daniela", 8);
        rede.addAresta("Bruno", "Eduardo", 1);
        rede.addAresta("Carlos", "Eduardo", 1);
        rede.addAresta("Daniela", "Fernanda", 5);
        rede.addAresta("Eduardo", "Fernanda", 1);
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
        rede.addAresta("Elisa", "Lucas", 1);
        rede.addAresta("Beatriz", "Isabella", 1);
        rede.addAresta("Isabella", "Marina", 1);
        rede.addAresta("Gabriel", "Hugo", 1);
        rede.addAresta("Igor", "Juliana", 1);
        rede.addAresta("Fabio", "Gabriela", 1);
        rede.addAresta("Gabriela", "Henrique", 1);

        return rede;
    }
}

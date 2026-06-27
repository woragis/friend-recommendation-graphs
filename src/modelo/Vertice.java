package modelo;

import java.util.ArrayList;
import java.util.List;

public class Vertice {

    private String nome;
    private int grau;
    private int inDegree; // deve ser 0 para não-dirigido
    private int outDegree;// deve ser 0 para não-dirigido
    private List<Vertice> adjacencias; //out
    private List<Vertice> adjacentes; //in

    public Vertice(String nome) {
        this.nome = nome;
        adjacencias = new ArrayList<>();
        adjacentes = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public int getGrau() {
        return grau;
    }

    public int getInDegree() {
        return inDegree;
    }

    public int getOutDegree() {
        return outDegree;
    }

    public List<Vertice> getAdjacencias() {
        return adjacencias;
    }

    public List<Vertice> getAdjacentes() {
        return adjacentes;
    }

    public void resetaGraus() {
        grau = inDegree = outDegree = 0;
    }

    public void resetaAdjacenciasEAdjacentes() {
        adjacencias.clear();
        adjacentes.clear();
    }

    public void aumentaGrau() { // não-dirigido
        grau++;
    }

    public void aumentaInDegree() {
        grau++;
        inDegree++;
    }

    public void aumentaOutDegree() {
        grau++;
        outDegree++;
    }

    public void adicionaAdjacencia(Vertice vertice) {
        adjacencias.add(vertice);
    }

    public void adicionaAdjacente(Vertice vertice) {
        adjacentes.add(vertice);
    }

    public String exibeGraus() {
        return "\n%s: grau %d (%d in | %d out)".formatted(nome, grau, inDegree, outDegree);
    }

    @Override
    public String toString() {
        return nome;
    }
}

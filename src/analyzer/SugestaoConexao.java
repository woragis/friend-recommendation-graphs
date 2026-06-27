package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SugestaoConexao {

    private final String nome;
    private final int amigosEmComum;
    private final List<String> nomesAmigosEmComum;
    private final String caminhoExemplo;

    public SugestaoConexao(String nome, int amigosEmComum, List<String> nomesAmigosEmComum, String caminhoExemplo) {
        this.nome = nome;
        this.amigosEmComum = amigosEmComum;
        this.nomesAmigosEmComum = new ArrayList<>(nomesAmigosEmComum);
        this.caminhoExemplo = caminhoExemplo;
    }

    public String getNome() {
        return nome;
    }

    public int getAmigosEmComum() {
        return amigosEmComum;
    }

    public List<String> getNomesAmigosEmComum() {
        return Collections.unmodifiableList(nomesAmigosEmComum);
    }

    public String getCaminhoExemplo() {
        return caminhoExemplo;
    }

    @Override
    public String toString() {
        return nome + " (" + amigosEmComum + " em comum: " + String.join(", ", nomesAmigosEmComum) + ")";
    }
}

package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultadoCaminho {

    private final List<String> caminho;
    private final int passos;

    public ResultadoCaminho(List<String> caminho, int passos) {
        this.caminho = new ArrayList<>(caminho);
        this.passos = passos;
    }

    public static ResultadoCaminho inalcancavel() {
        return new ResultadoCaminho(Collections.emptyList(), -1);
    }

    public List<String> getCaminho() {
        return Collections.unmodifiableList(caminho);
    }

    public int getPassos() {
        return passos;
    }

    public boolean eAlcancavel() {
        return passos >= 0;
    }

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

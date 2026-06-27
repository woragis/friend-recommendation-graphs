import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultadoRota {

    private final List<String> caminho;
    private final int custo;

    public ResultadoRota(List<String> caminho, int custo) {
        this.caminho = new ArrayList<>(caminho);
        this.custo = custo;
    }

    public static ResultadoRota inalcantravel() {
        return new ResultadoRota(Collections.emptyList(), -1);
    }

    public List<String> getCaminho() {
        return Collections.unmodifiableList(caminho);
    }

    public int getCusto() {
        return custo;
    }

    public boolean eAlcancavel() {
        return custo >= 0;
    }

    @Override
    public String toString() {
        if (!eAlcancavel()) {
            return "Inalcançável (custo = -1, caminho vazio)";
        }
        return String.join(" -> ", caminho) + " | custo = " + custo;
    }
}

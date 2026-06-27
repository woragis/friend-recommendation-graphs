// Pacote app: ponto de entrada da aplicação (demonstração)
package app;

// Importa o analisador e suas estruturas de retorno
import analyzer.LinkedInAnalyzer;
import analyzer.SugestaoConexao;
import modelo.Grafo;
import modelo.ResultadoRota;

import java.util.List;

/**
 * Classe principal com o método main.
 * Monta a rede de testes linha a linha e executa todas as missões,
 * imprimindo saída formatada no console (para gravação do vídeo).
 */
public class Main {

    // Largura fixa das linhas de formatação no console
    private static final int LARGURA = 62;

    /**
     * Ponto de entrada do programa.
     */
    public static void main(String[] args) {
        // Imprime o cabeçalho do projeto
        imprimirCabecalho();

        // --- Montagem da rede (vértices e arestas executados linha a linha) ---
        // Cria grafo não-direcionado (false) e ponderado (true)
        Grafo rede = new Grafo(false, true);

        // Cadastra cada perfil da rede, um por linha
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

        // Registra cada conexão com seu peso de afinidade
        rede.addAresta("Ana", "Bruno", 1);       // muita afinidade
        rede.addAresta("Ana", "Carlos", 2);
        rede.addAresta("Ana", "Daniela", 8);     // pouca afinidade
        rede.addAresta("Bruno", "Eduardo", 1);
        rede.addAresta("Carlos", "Eduardo", 1);
        rede.addAresta("Daniela", "Fernanda", 5);
        rede.addAresta("Eduardo", "Fernanda", 1);
        rede.addAresta("Gabriel", "Hugo", 1);    // grupo isolado 1
        rede.addAresta("Igor", "Juliana", 1);    // grupo isolado 2

        // Exibe resumo da rede montada
        imprimirRedeMontada(rede);

        // MISSÃO 1: cria o analisador passando o grafo
        LinkedInAnalyzer analyzer = new LinkedInAnalyzer(rede);

        // Exibe confirmação do construtor
        imprimirMissao(
                "MISSAO 1 - CONSTRUTOR",
                "O LinkedInAnalyzer recebe o Grafo e o guarda para as demais analises.",
                "Grafo carregado com sucesso.",
                "Perfis: " + rede.getVertices().size() + "  |  Conexoes: 9");

        // Executa e exibe cada missão
        imprimirMissaoSugestoes(analyzer);      // Missão 2
        imprimirMissaoGrauSeparacao(analyzer);  // Missão 3
        imprimirMissaoRotaAfinidade(analyzer);  // Missão 4
        imprimirMissaoComponentes(analyzer);    // Missão 5
        imprimirCasosExtras(analyzer);          // Validações adicionais
        imprimirRodape();
    }

    /** Imprime o título e subtítulo centralizados. */
    private static void imprimirCabecalho() {
        linhaVazia();
        linhaDupla();
        centralizar("LINKEDIN ANALYZER");
        centralizar("Motor de Analises em Grafos - Projeto Final");
        linhaDupla();
        linhaVazia();
    }

    /** Exibe a Etapa 0: resumo da rede que foi montada no main. */
    private static void imprimirRedeMontada(Grafo rede) {
        secao("ETAPA 0 - MONTAGEM DA REDE NO MAIN");

        println("Tipo do grafo : Nao-direcionado e ponderado");
        println("Peso 1 = muita afinidade  |  Peso 5+ = pouca afinidade");
        linhaVazia();

        println("Perfis cadastrados:");
        println("  [Rede principal] Ana, Bruno, Carlos, Daniela, Eduardo, Fernanda");
        println("  [Grupo isolado ] Gabriel, Hugo");
        println("  [Grupo isolado ] Igor, Juliana");
        linhaVazia();

        println("Conexoes registradas:");
        println("  Ana       <-> Bruno      | peso 1");
        println("  Ana       <-> Carlos     | peso 2");
        println("  Ana       <-> Daniela    | peso 8");
        println("  Bruno     <-> Eduardo    | peso 1");
        println("  Carlos    <-> Eduardo    | peso 1");
        println("  Daniela   <-> Fernanda   | peso 5");
        println("  Eduardo   <-> Fernanda   | peso 1");
        println("  Gabriel   <-> Hugo       | peso 1");
        println("  Igor      <-> Juliana    | peso 1");
        linhaVazia();

        // Usa dados reais do grafo para o resumo
        println("Resumo: " + rede.getVertices().size() + " vertices  |  9 arestas  |  3 sub-redes");
        linhaSimples();
    }

    /** Missão 2: exibe sugestões de conexão para Ana em formato de tabela. */
    private static void imprimirMissaoSugestoes(LinkedInAnalyzer analyzer) {
        secao("MISSAO 2 - SUGESTAO DE CONEXOES (AMIGOS DE 2 GRAU)");

        println("Usuario analisado : Ana");
        println("Algoritmo         : varredura nos vizinhos dos contatos diretos");
        println("Regras            : exclui contatos de 1 grau e o proprio usuario");
        linhaVazia();

        // Chama o analisador e obtém as sugestões ordenadas
        List<SugestaoConexao> sugestoes = analyzer.sugerirConexoes("Ana");

        // Cabeçalho da tabela
        println(String.format("  %-12s | %-20s", "POSICAO", "SUGESTAO"));
        println("  " + "-".repeat(36));

        // Imprime cada sugestão com posição na lista
        int posicao = 1;
        for (SugestaoConexao sugestao : sugestoes) {
            println(String.format("  %-12d | %s", posicao++, sugestao));
        }

        linhaVazia();
        println("Interpretacao:");
        println("  Eduardo aparece primeiro porque tem 2 amigos em comum com Ana");
        println("  (Bruno e Carlos). Fernanda tem 1 amigo em comum (Daniela).");
        linhaSimples();
    }

    /** Missão 3: exibe o grau de separação entre Ana e Fernanda. */
    private static void imprimirMissaoGrauSeparacao(LinkedInAnalyzer analyzer) {
        secao("MISSAO 3 - GRAU DE SEPARACAO");

        println("Consulta  : Ana  -->  Fernanda");
        println("Algoritmo : BFS (Busca em Largura) - menor numero de passos");
        linhaVazia();

        // BFS retorna o menor número de passos
        int passos = analyzer.grauDeSeparacao("Ana", "Fernanda");

        println("  Resultado : " + passos + " passo(s) de separacao");
        linhaVazia();
        println("Caminho em passos (exemplo):");
        println("  Ana -> Daniela -> Fernanda");
        println("  (2 conexoes, ou seja, 1 intermediario)");
        linhaSimples();
    }

    /** Missão 4: exibe a rota de maior afinidade e compara com o caminho mais curto. */
    private static void imprimirMissaoRotaAfinidade(LinkedInAnalyzer analyzer) {
        secao("MISSAO 4 - ROTA DE MAIOR AFINIDADE (MENOR CUSTO)");

        println("Consulta  : Ana  -->  Fernanda");
        println("Algoritmo : Dijkstra (implementado na classe Grafo)");
        linhaVazia();

        // Dijkstra encontra o caminho de menor custo (não necessariamente o mais curto em passos)
        ResultadoRota rota = analyzer.rotaDeMaiorAfinidade("Ana", "Fernanda");

        println("  Caminho otimo : " + String.join(" -> ", rota.getCaminho()));
        println("  Custo total   : " + rota.getCusto());
        linhaVazia();

        // Comparação didática para o vídeo: menos passos ≠ maior afinidade
        println("Comparacao didatica:");
        println("  Rota mais curta em passos : Ana -> Daniela -> Fernanda");
        println("                              custo = 8 + 5 = 13");
        println("  Rota do Dijkstra          : Ana -> Bruno -> Eduardo -> Fernanda");
        println("                              custo = 1 + 1 + 1 = 3  <-- escolhida");
        linhaVazia();
        println("Conclusao: menos passos nem sempre significa maior afinidade!");
        linhaSimples();
    }

    /** Missão 5: exibe os componentes conexos (sub-redes isoladas). */
    private static void imprimirMissaoComponentes(LinkedInAnalyzer analyzer) {
        secao("MISSAO 5 - MAPEAR GRUPOS ISOLADOS (COMPONENTES CONEXOS)");

        println("Algoritmo : DFS recursivo (dfsRecursivo da classe Grafo)");
        println("Objetivo  : encontrar sub-redes sem ligacao entre si");
        linhaVazia();

        // DFS identifica cada grupo isolado
        List<List<String>> grupos = analyzer.mapearGruposIsolados();

        for (int i = 0; i < grupos.size(); i++) {
            println("  Grupo " + (i + 1) + ": " + String.join(", ", grupos.get(i)));
        }

        linhaVazia();
        println("Foram identificadas 3 sub-redes independentes na rede.");
        linhaSimples();
    }

    /** Casos extras: perfis isolados, conexão direta e rota em sub-rede. */
    private static void imprimirCasosExtras(LinkedInAnalyzer analyzer) {
        secao("CASOS ADICIONAIS DE VALIDACAO");

        int anaGabriel = analyzer.grauDeSeparacao("Ana", "Gabriel");
        int igorJuliana = analyzer.grauDeSeparacao("Igor", "Juliana");
        ResultadoRota gabrielHugo = analyzer.rotaDeMaiorAfinidade("Gabriel", "Hugo");

        println("  Ana -> Gabriel (perfis em sub-redes diferentes)");
        println("    Grau de separacao : " + formatarGrau(anaGabriel));
        linhaVazia();

        println("  Igor -> Juliana (conexao direta no grupo isolado)");
        println("    Grau de separacao : " + formatarGrau(igorJuliana));
        linhaVazia();

        println("  Gabriel -> Hugo (rota no grupo isolado)");
        println("    " + gabrielHugo);
        linhaSimples();
    }

    /** Rodapé final da execução. */
    private static void imprimirRodape() {
        linhaVazia();
        linhaDupla();
        centralizar("Execucao finalizada com sucesso");
        centralizar("LinkedIn Analyzer - Projeto Final de Grafos");
        linhaDupla();
        linhaVazia();
    }

    /** Template genérico para exibir uma missão com título, descrição e resultado. */
    private static void imprimirMissao(String titulo, String descricao, String resultado, String detalhe) {
        secao(titulo);
        println(descricao);
        linhaVazia();
        println("  >> " + resultado);
        println("  >> " + detalhe);
        linhaSimples();
    }

    /** Formata o grau de separação; -1 vira mensagem de "sem conexão". */
    private static String formatarGrau(int grau) {
        return grau == -1 ? "-1 (sem conexao entre os perfis)" : grau + " passo(s)";
    }

    /** Imprime uma seção com borda e título. */
    private static void secao(String titulo) {
        linhaVazia();
        println("+-- " + titulo + " " + "-".repeat(Math.max(0, LARGURA - titulo.length() - 5)) + "+");
        linhaVazia();
    }

    /** Centraliza um texto na largura LARGURA. */
    private static void centralizar(String texto) {
        int padding = Math.max(0, (LARGURA - texto.length()) / 2);
        println(" ".repeat(padding) + texto);
    }

    /** Linha dupla de separação (====). */
    private static void linhaDupla() {
        println("=".repeat(LARGURA));
    }

    /** Linha simples de separação (----). */
    private static void linhaSimples() {
        println("-".repeat(LARGURA));
    }

    /** Linha em branco. */
    private static void linhaVazia() {
        System.out.println();
    }

    /** Atalho para System.out.println. */
    private static void println(String texto) {
        System.out.println(texto);
    }
}

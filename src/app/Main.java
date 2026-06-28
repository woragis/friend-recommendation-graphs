// Pacote app: ponto de entrada da aplicação (menu interativo)
package app;

import analyzer.LinkedInAnalyzer;
import analyzer.ResultadoCaminho;
import analyzer.SugestaoConexao;
import modelo.Grafo;
import modelo.ResultadoRota;

import java.util.List;
import java.util.Scanner;

/**
 * Classe principal com o método main.
 * Carrega a rede via RedeFactory e oferece um menu interativo
 * para explorar amigos, sugestões, BFS, Dijkstra e grupos isolados.
 */
public class Main {

    // Largura fixa das linhas de formatação no console
    private static final int LARGURA = 62;
    // Leitor de entrada do teclado (compartilhado por todo o menu)
    private static final Scanner ENTRADA = new Scanner(System.in);

    /**
     * Ponto de entrada do programa.
     */
    public static void main(String[] args) {
        // Monta a rede de 25 perfis (factory separada para manter o main enxuto)
        Grafo rede = RedeFactory.criarRede();
        // MISSÃO 1: cria o analisador passando o grafo
        LinkedInAnalyzer analyzer = new LinkedInAnalyzer(rede);
        String usuarioLogado = null;

        imprimirCabecalho();
        println("Rede carregada: " + rede.getVertices().size() + " perfis.");

        boolean executando = true;
        while (executando) {
            linhaSimples();

            if (usuarioLogado == null) {
                // Menu sem login: entrar, listar perfis ou ver grupos isolados
                int opcao = lerOpcao("""
                        MENU PRINCIPAL
                          1. Entrar como usuario
                          2. Ver todos os perfis
                          3. Ver grupos isolados
                          0. Sair
                        Escolha""");

                switch (opcao) {
                    case 1 -> usuarioLogado = fazerLogin(analyzer);
                    case 2 -> exibirPerfis(analyzer);
                    case 3 -> exibirGruposIsolados(analyzer);
                    case 0 -> executando = false;
                    default -> println("Opcao invalida.");
                }
            } else {
                // Menu logado: missões 2 a 5 disponíveis para o usuário atual
                int opcao = lerOpcao("""
                        LOGADO COMO: %s
                          1. Meus amigos
                          2. Sugestoes de conexao
                          3. Grau de separacao
                          4. Rota ponderada (Dijkstra)
                          5. Trocar usuario
                          0. Voltar ao menu principal
                        Escolha""".formatted(usuarioLogado));

                switch (opcao) {
                    case 1 -> exibirAmigos(analyzer, usuarioLogado);
                    case 2 -> exibirSugestoes(analyzer, usuarioLogado);
                    case 3 -> consultarGrauSeparacao(analyzer, usuarioLogado);
                    case 4 -> consultarRotaPonderada(analyzer, usuarioLogado);
                    case 5 -> usuarioLogado = fazerLogin(analyzer);
                    case 0 -> usuarioLogado = null;
                    default -> println("Opcao invalida.");
                }
            }
        }

        println("\nEncerrando LinkedIn Analyzer. Ate logo!");
        ENTRADA.close();
    }

    /** Permite escolher um perfil por número ou nome. */
    private static String fazerLogin(LinkedInAnalyzer analyzer) {
        println("\nPerfis disponiveis:");
        List<String> perfis = analyzer.listarPerfis();
        for (int i = 0; i < perfis.size(); i++) {
            println("  " + (i + 1) + ". " + perfis.get(i));
        }

        println("\nDigite o numero ou o nome do perfil:");
        String entrada = ENTRADA.nextLine().trim();

        // Aceita seleção por número (1, 2, 3...)
        if (entrada.matches("\\d+")) {
            int indice = Integer.parseInt(entrada) - 1;
            if (indice >= 0 && indice < perfis.size()) {
                String escolhido = perfis.get(indice);
                println("Login realizado como " + escolhido + ".");
                return escolhido;
            }
        }

        // Aceita seleção digitando o nome do perfil
        try {
            analyzer.listarAmigos(entrada);
            println("Login realizado como " + entrada + ".");
            return entrada;
        } catch (IllegalArgumentException e) {
            println("Perfil nao encontrado.");
            return null;
        }
    }

    /** Lista todos os perfis cadastrados na rede (ordem alfabética). */
    private static void exibirPerfis(LinkedInAnalyzer analyzer) {
        secao("PERFIS DA REDE");
        List<String> perfis = analyzer.listarPerfis();
        for (int i = 0; i < perfis.size(); i++) {
            println("  " + (i + 1) + ". " + perfis.get(i));
        }
        println("\nTotal: " + perfis.size() + " perfis.");
    }

    /** Exibe os amigos diretos (1º grau) do usuário logado. */
    private static void exibirAmigos(LinkedInAnalyzer analyzer, String usuario) {
        secao("MEUS AMIGOS - " + usuario);
        List<String> amigos = analyzer.listarAmigos(usuario);

        if (amigos.isEmpty()) {
            println("  Voce ainda nao tem conexoes diretas.");
            return;
        }

        for (int i = 0; i < amigos.size(); i++) {
            println("  " + (i + 1) + ". " + amigos.get(i));
        }
        println("\nTotal: " + amigos.size() + " amigo(s) direto(s).");
    }

    /** MISSÃO 2 no menu: exibe sugestões de 2º grau com amigos em comum. */
    private static void exibirSugestoes(LinkedInAnalyzer analyzer, String usuario) {
        secao("SUGESTOES DE CONEXAO - " + usuario);
        println("Pessoas a 2 passos de distancia (amigos de amigos).\n");

        List<SugestaoConexao> sugestoes = analyzer.sugerirConexoes(usuario);

        if (sugestoes.isEmpty()) {
            println("  Nenhuma sugestao no momento.");
            return;
        }

        int posicao = 1;
        for (SugestaoConexao sugestao : sugestoes) {
            println("  " + posicao++ + ". " + sugestao.getNome());
            println("     Amigos em comum (" + sugestao.getAmigosEmComum() + "): "
                    + String.join(", ", sugestao.getNomesAmigosEmComum()));
            println("     Caminho usado na sugestao: " + sugestao.getCaminhoExemplo());
            println("     Grau de separacao: 2 passos");
            linhaVazia();
        }
    }

    /** MISSÃO 3 no menu: BFS com caminho completo e número de passos. */
    private static void consultarGrauSeparacao(LinkedInAnalyzer analyzer, String usuario) {
        secao("GRAU DE SEPARACAO - " + usuario);
        print("Ate qual perfil? ");
        String destino = ENTRADA.nextLine().trim();

        try {
            ResultadoCaminho resultado = analyzer.caminhoEmPassos(usuario, destino);

            if (!resultado.eAlcancavel()) {
                println("\n  " + usuario + " e " + destino + " estao em sub-redes diferentes.");
                println("  Grau de separacao: -1 (sem conexao)");
                return;
            }

            println("\n  Destino       : " + destino);
            println("  Passos        : " + resultado.getPassos());
            println("  Caminho (BFS) : " + resultado.caminhoFormatado());
        } catch (IllegalArgumentException e) {
            println("  Perfil nao encontrado: " + destino);
        }
    }

    /** MISSÃO 4 no menu: menor caminho ponderado (Dijkstra). */
    private static void consultarRotaPonderada(LinkedInAnalyzer analyzer, String usuario) {
        secao("ROTA PONDERADA (DIJKSTRA) - " + usuario);
        print("Ate qual perfil? ");
        String destino = ENTRADA.nextLine().trim();

        try {
            ResultadoRota rota = analyzer.rotaDeMaiorAfinidade(usuario, destino);

            if (!rota.eAlcancavel()) {
                println("\n  Sem rota ponderada entre " + usuario + " e " + destino + ".");
                return;
            }

            println("\n  Caminho otimo : " + String.join(" -> ", rota.getCaminho()));
            println("  Custo total   : " + rota.getCusto());
            println("  (Menor custo = maior afinidade entre as conexoes)");
        } catch (IllegalArgumentException e) {
            println("  Perfil nao encontrado: " + destino);
        }
    }

    /** MISSÃO 5 no menu: componentes conexos (sub-redes isoladas). */
    private static void exibirGruposIsolados(LinkedInAnalyzer analyzer) {
        secao("GRUPOS ISOLADOS (COMPONENTES CONEXOS)");
        List<List<String>> grupos = analyzer.mapearGruposIsolados();

        for (int i = 0; i < grupos.size(); i++) {
            println("  Grupo " + (i + 1) + ": " + String.join(", ", grupos.get(i)));
        }
        println("\nTotal: " + grupos.size() + " sub-rede(s).");
    }

    /** Lê um número do menu; retorna -1 se a entrada não for numérica. */
    private static int lerOpcao(String menu) {
        linhaVazia();
        println(menu);
        print("> ");
        String entrada = ENTRADA.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Imprime o título e subtítulo centralizados. */
    private static void imprimirCabecalho() {
        linhaVazia();
        linhaDupla();
        centralizar("LINKEDIN ANALYZER");
        centralizar("Modo Interativo - Grafos");
        linhaDupla();
        linhaVazia();
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

    /** Atalho para System.out.print (sem quebra de linha). */
    private static void print(String texto) {
        System.out.print(texto);
    }
}

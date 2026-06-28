// Pacote app: ponto de entrada da aplicação (menu interativo)
package app;

import analyzer.LinkedInAnalyzer;
import analyzer.ResultadoCaminho;
import analyzer.SugestaoConexao;

import java.util.List;
import java.util.Scanner;

/**
 * Classe principal com o método main.
 * Carrega a rede de amizades e oferece um menu para explorar
 * amigos, sugestões e distância entre perfis.
 */
public class Main {

    private static final int LARGURA = 62;
    private static final Scanner ENTRADA = new Scanner(System.in);

    public static void main(String[] args) {
        LinkedInAnalyzer analyzer = new LinkedInAnalyzer(RedeFactory.criarRede());
        String usuarioLogado = null;

        imprimirCabecalho();
        println("Rede carregada: " + analyzer.listarPerfis().size() + " perfis.");

        boolean executando = true;
        while (executando) {
            linhaSimples();

            if (usuarioLogado == null) {
                int opcao = lerOpcao("""
                        MENU PRINCIPAL
                          1. Entrar como usuario
                          2. Ver todos os perfis
                          0. Sair
                        Escolha""");

                switch (opcao) {
                    case 1 -> usuarioLogado = fazerLogin(analyzer);
                    case 2 -> exibirPerfis(analyzer);
                    case 0 -> executando = false;
                    default -> println("Opcao invalida.");
                }
            } else {
                int opcao = lerOpcao("""
                        LOGADO COMO: %s
                          1. Meus amigos
                          2. Sugestoes de conexao
                          3. Grau de separacao
                          4. Trocar usuario
                          0. Voltar ao menu principal
                        Escolha""".formatted(usuarioLogado));

                switch (opcao) {
                    case 1 -> exibirAmigos(analyzer, usuarioLogado);
                    case 2 -> exibirSugestoes(analyzer, usuarioLogado);
                    case 3 -> consultarGrauSeparacao(analyzer, usuarioLogado);
                    case 4 -> usuarioLogado = fazerLogin(analyzer);
                    case 0 -> usuarioLogado = null;
                    default -> println("Opcao invalida.");
                }
            }
        }

        println("\nEncerrando LinkedIn Analyzer. Ate logo!");
        ENTRADA.close();
    }

    private static String fazerLogin(LinkedInAnalyzer analyzer) {
        println("\nPerfis disponiveis:");
        List<String> perfis = analyzer.listarPerfis();
        for (int i = 0; i < perfis.size(); i++) {
            println("  " + (i + 1) + ". " + perfis.get(i));
        }

        println("\nDigite o numero ou o nome do perfil:");
        String entrada = ENTRADA.nextLine().trim();

        if (entrada.matches("\\d+")) {
            int indice = Integer.parseInt(entrada) - 1;
            if (indice >= 0 && indice < perfis.size()) {
                String escolhido = perfis.get(indice);
                println("Login realizado como " + escolhido + ".");
                return escolhido;
            }
        }

        try {
            analyzer.listarAmigos(entrada);
            println("Login realizado como " + entrada + ".");
            return entrada;
        } catch (IllegalArgumentException e) {
            println("Perfil nao encontrado.");
            return null;
        }
    }

    private static void exibirPerfis(LinkedInAnalyzer analyzer) {
        secao("PERFIS DA REDE");
        List<String> perfis = analyzer.listarPerfis();
        for (int i = 0; i < perfis.size(); i++) {
            println("  " + (i + 1) + ". " + perfis.get(i));
        }
        println("\nTotal: " + perfis.size() + " perfis.");
    }

    private static void exibirAmigos(LinkedInAnalyzer analyzer, String usuario) {
        secao("MEUS AMIGOS - " + usuario);
        List<String> amigos = analyzer.listarAmigos(usuario);

        if (amigos.isEmpty()) {
            println("  Voce ainda nao tem amizades cadastradas.");
            return;
        }

        for (int i = 0; i < amigos.size(); i++) {
            println("  " + (i + 1) + ". " + amigos.get(i));
        }
        println("\nTotal: " + amigos.size() + " amigo(s).");
    }

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
            println("     Caminho: " + sugestao.getCaminhoExemplo());
            linhaVazia();
        }
    }

    private static void consultarGrauSeparacao(LinkedInAnalyzer analyzer, String usuario) {
        secao("GRAU DE SEPARACAO - " + usuario);
        print("Ate qual perfil? ");
        String destino = ENTRADA.nextLine().trim();

        try {
            ResultadoCaminho resultado = analyzer.caminhoEmPassos(usuario, destino);

            if (!resultado.eAlcancavel()) {
                println("\n  Nao ha caminho de amizades entre " + usuario + " e " + destino + ".");
                println("  Grau de separacao: -1");
                return;
            }

            println("\n  Destino       : " + destino);
            println("  Passos        : " + resultado.getPassos());
            println("  Caminho (BFS) : " + resultado.caminhoFormatado());
        } catch (IllegalArgumentException e) {
            println("  Perfil nao encontrado: " + destino);
        }
    }

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

    private static void imprimirCabecalho() {
        linhaVazia();
        linhaDupla();
        centralizar("LINKEDIN ANALYZER");
        centralizar("Rede de Amizades");
        linhaDupla();
        linhaVazia();
    }

    private static void secao(String titulo) {
        linhaVazia();
        println("+-- " + titulo + " " + "-".repeat(Math.max(0, LARGURA - titulo.length() - 5)) + "+");
        linhaVazia();
    }

    private static void centralizar(String texto) {
        int padding = Math.max(0, (LARGURA - texto.length()) / 2);
        println(" ".repeat(padding) + texto);
    }

    private static void linhaDupla() {
        println("=".repeat(LARGURA));
    }

    private static void linhaSimples() {
        println("-".repeat(LARGURA));
    }

    private static void linhaVazia() {
        System.out.println();
    }

    private static void println(String texto) {
        System.out.println(texto);
    }

    private static void print(String texto) {
        System.out.print(texto);
    }
}

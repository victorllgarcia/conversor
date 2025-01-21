import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Conversor {

    private static final String API_KEY = "COLOQUE-SUA-CHAVE-API-AQUI";

    public static double obterTaxaCambio(String moedaBase, String moedaAlvo) {
        String endpoint = String.format("https://v6.exchangerate-api.com/v6/%s/latest/%s", API_KEY, moedaBase);

        try {
            URL url = new URL(endpoint);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = leitor.readLine()) != null) {
                resposta.append(linha);
            }
            leitor.close();

            JsonObject jsonResposta = JsonParser.parseString(resposta.toString()).getAsJsonObject();
            JsonObject taxasConversao = jsonResposta.getAsJsonObject("conversion_rates");

            if (taxasConversao.has(moedaAlvo)) {
                return taxasConversao.get(moedaAlvo).getAsDouble();
            } else {
                System.out.println("Moeda " + moedaAlvo + " não encontrada.");
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Erro ao acessar a API: " + e.getMessage());
            return -1;
        }
    }

    public static double converterMoeda(double valor, String moedaBase, String moedaAlvo) {
        double taxa = obterTaxaCambio(moedaBase, moedaAlvo);
        if (taxa != -1) {
            return valor * taxa;
        } else {
            return -1;
        }
    }

    public static void exibirMenu() {
        System.out.println("=== Conversor de Moedas ===");
        System.out.println("Escolha uma opção:");
        System.out.println("1. Converter USD para BRL");
        System.out.println("2. Converter BRL para EUR");
        System.out.println("3. Converter JPY para USD");
        System.out.println("4. Sair");
        System.out.print("Opção: ");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            exibirMenu();
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o valor em USD: ");
                    double valorUSD = scanner.nextDouble();
                    double resultadoUSD = converterMoeda(valorUSD, "USD", "BRL");
                    if (resultadoUSD != -1) {
                        System.out.printf("Resultado: %.2f USD = %.2f BRL%n", valorUSD, resultadoUSD);
                    } else {
                        System.out.println("Conversão não realizada.");
                    }
                    break;

                case 2:
                    System.out.print("Digite o valor em BRL: ");
                    double valorBRL = scanner.nextDouble();
                    double resultadoBRL = converterMoeda(valorBRL, "BRL", "EUR");
                    if (resultadoBRL != -1) {
                        System.out.printf("Resultado: %.2f BRL = %.2f EUR%n", valorBRL, resultadoBRL);
                    } else {
                        System.out.println("Conversão não realizada.");
                    }
                    break;

                case 3:
                    System.out.print("Digite o valor em JPY: ");
                    double valorJPY = scanner.nextDouble();
                    double resultadoJPY = converterMoeda(valorJPY, "JPY", "USD");
                    if (resultadoJPY != -1) {
                        System.out.printf("Resultado: %.2f JPY = %.2f USD%n", valorJPY, resultadoJPY);
                    } else {
                        System.out.println("Conversão não realizada.");
                    }
                    break;

                case 4:
                    System.out.println("Encerrando o programa. Obrigado por usar o Conversor de Moedas!");
                    continuar = false;
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }
}

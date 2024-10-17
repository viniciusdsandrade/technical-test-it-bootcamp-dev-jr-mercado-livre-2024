package q2;

import java.util.*;

import static java.lang.System.in;

public class Result {
    /**
     * Função para calcular o número de produtos duplicados.
     *
     * @param name   Lista com os nomes dos produtos.
     * @param price  Lista com os preços dos produtos.
     * @param weight Lista com os pesos dos produtos.
     * @return O número de produtos duplicados.
     */
    public static int numDuplicates(List<String> name, List<Integer> price, List<Integer> weight) {
        Map<String, Integer> productCount = new HashMap<>();

        for (int i = 0; i < name.size(); i++) {
            // Criação de uma chave única para cada produto baseado nos três atributos
            String key = name.get(i) + "|" + price.get(i) + "|" + weight.get(i);
            productCount.put(key, productCount.getOrDefault(key, 0) + 1);
        }

        int duplicates = 0;
        for (int count : productCount.values()) {
            if (count > 1) duplicates += (count - 1); // Apenas as ocorrências extras são consideradas duplicatas
        }

        return duplicates;
    }

    public static void main(String[] ignoredArgs) {
        Scanner scanner = new Scanner(in);

        // Leitura do número de produtos
        int n = scanner.nextInt();

        // Leitura dos nomes dos produtos
        List<String> names = new ArrayList<>();
        for (int i = 0; i < n; i++) names.add(scanner.next());


        // Leitura dos preços dos produtos
        List<Integer> prices = new ArrayList<>();
        for (int i = 0; i < n; i++) prices.add(scanner.nextInt());


        // Leitura dos pesos dos produtos
        List<Integer> weights = new ArrayList<>();
        for (int i = 0; i < n; i++) weights.add(scanner.nextInt());


        // Chamada da função numDuplicates
        int duplicates = numDuplicates(names, prices, weights);

        // Impressão do resultado
        System.out.println(duplicates);

        scanner.close();
    }
}

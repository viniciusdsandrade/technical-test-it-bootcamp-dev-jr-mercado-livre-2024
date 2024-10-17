package q3;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.*;

import static java.lang.String.format;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Solution {

    /**
     * Função principal que orquestra a busca, filtragem, ordenação e formatação dos dados dos países.
     *
     * @param region  A região para filtrar os países.
     * @param keyword A palavra-chave para buscar no nome do país.
     * @return Uma lista de strings no formato "NomeDoPaís,População", ordenadas conforme especificado.
     */
    public static List<String> findCountry(String region, String keyword) {
        List<Country> countries = fetchCountries(region, keyword);
        List<Country> filteredCountries = filterCountries(countries, region, keyword);
        sortCountries(filteredCountries);
        return formatCountries(filteredCountries);
    }

    /**
     * Constrói a URL da API com os parâmetros de consulta fornecidos.
     *
     * @param region     A região para filtrar os países.
     * @param keyword    A palavra-chave para buscar no nome do país.
     * @param pageNumber O número da página para paginação.
     * @return A URL completa para a requisição da API.
     */
    private static String buildApiUrl(String region, String keyword, int pageNumber) {
        String encodedRegion = encode(region, UTF_8);
        String encodedKeyword = encode(keyword, UTF_8);
        return format(
                "https://jsonmock.hackerrank.com/api/countries/search?region=%s&name=%s&page=%d",
                encodedRegion, encodedKeyword, pageNumber
        );
    }

    /**
     * Realiza as requisições HTTP para buscar os dados dos países, lidando com a paginação.
     *
     * @param region  A região para filtrar os países.
     * @param keyword A palavra-chave para buscar no nome do país.
     * @return Uma lista completa de países retornados pela API.
     */
    private static List<Country> fetchCountries(String region, String keyword) {
        List<Country> countries = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        try {
            // Inicializa a página atual e total de páginas
            int currentPage = 1;
            int totalPages = 1;

            while (currentPage <= totalPages) {
                // Constrói a URL da API para a página atual
                String apiUrl = buildApiUrl(region, keyword, currentPage);
                URI uri = URI.create(apiUrl);

                // Cria a requisição HTTP GET
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .GET()
                        .build();

                // Envia a requisição e obtém a resposta
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Verifica o status da resposta
                if (response.statusCode() != 200) {
                    throw new RuntimeException("Failed to fetch data from API: HTTP " + response.statusCode());
                }

                // Faz o parsing da resposta JSON
                ApiResponse apiResponse = gson.fromJson(response.body(), ApiResponse.class);

                // Atualiza o total de páginas após a primeira requisição
                if (currentPage == 1) {
                    totalPages = apiResponse.getTotalPages();
                }

                // Adiciona os países da página atual à lista
                countries.addAll(apiResponse.getData());

                // Avança para a próxima página
                currentPage++;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return countries;
    }

    /**
     * Filtra a lista de países com base na região e na palavra-chave no nome.
     *
     * @param countries A lista completa de países.
     * @param region    A região para filtrar os países.
     * @param keyword   A palavra-chave para buscar no nome do país.
     * @return Uma lista filtrada de países.
     */
    private static List<Country> filterCountries(List<Country> countries, String region, String keyword) {
        String keywordLower = keyword.toLowerCase();
        return countries.stream()
                .filter(country -> country.getRegion().equalsIgnoreCase(region)
                                   && country.getName().toLowerCase().contains(keywordLower))
                .collect(Collectors.toList());
    }

    /**
     * Ordena a lista de países primeiro pela população em ordem crescente e, em caso de empate,
     * pelo nome do país em ordem alfabética.
     *
     * @param countries A lista de países a ser ordenada.
     */
    private static void sortCountries(List<Country> countries) {
        countries.sort(Comparator
                .comparingInt(Country::getPopulation)
                .thenComparing(Country::getName));
    }

    /**
     * Formata a lista de países para o formato "NomeDoPaís,População".
     *
     * @param countries A lista de países a ser formatada.
     * @return Uma lista de strings formatadas.
     */
    private static List<String> formatCountries(List<Country> countries) {
        return countries.stream()
                .map(country -> format("%s,%d", country.getName(), country.getPopulation()))
                .collect(Collectors.toList());
    }

    /**
     * Classe que representa a resposta da API.
     */
    static class ApiResponse {
        private int page;
        private int per_page;
        private int total;
        private int total_pages;
        private List<Country> data;

        public int getPage() {
            return page;
        }

        public int getPerPage() {
            return per_page;
        }

        public int getTotal() {
            return total;
        }

        public int getTotalPages() {
            return total_pages;
        }

        public List<Country> getData() {
            return data;
        }
    }

    /**
     * Classe que representa um país.
     */
    static class Country {
        private String name;
        private String region;
        private int population;

        public String getName() {
            return name;
        }

        public String getRegion() {
            return region;
        }

        public int getPopulation() {
            return population;
        }
    }

    public static void main(String[] ignoredArgs) {
        // Exemplo de uso:
        List<String> result = findCountry("Europe", "de");
        result.forEach(System.out::println);

        // Exemplo de uso:
        List<String> result2 = findCountry("Asia", "ab");
        result2.forEach(System.out::println);
    }
}

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Scanner;

public class Main {


    private static final String[] COMMON_CODES = {
            "USD", "INR", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "SGD", "NZD", "ZAR"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CurrencyApiClient apiClient = new CurrencyApiClient();

        System.out.println("=== Currency Converter ===");
        System.out.println("Tip: Use 3-letter codes like USD, INR, EUR, GBP, JPY.");
        System.out.print("Common codes: ");
        for (int i = 0; i < COMMON_CODES.length; i++) {
            System.out.print(COMMON_CODES[i] + (i == COMMON_CODES.length - 1 ? "\n" : ", "));
        }

        while (true) {
            String base = readCurrencyCode(scanner, "Enter base currency code (e.g., USD): ");
            String target = readCurrencyCode(scanner, "Enter target currency code (e.g., INR): ");
            BigDecimal amount = readPositiveAmount(scanner, "Enter amount to convert: ");

            try {
                ConversionResult result = apiClient.convert(base, target, amount);

                String targetSymbol = CurrencySymbols.symbolFor(target);
                String baseSymbol = CurrencySymbols.symbolFor(base);

                System.out.println("\n--- Result ---");
                System.out.println("Rate: 1 " + base + " = " + result.rate.stripTrailingZeros().toPlainString() + " " + target);
                System.out.println("Converted: " + baseSymbol + amount + "  →  " + targetSymbol
                        + result.converted.setScale(2, RoundingMode.HALF_UP));

            } catch (IOException | InterruptedException e) {
                System.out.println("\nCould not fetch live exchange rates right now.");
                System.out.println("Reason: " + e.getMessage());
                System.out.println("Check your internet connection or try again later.");
            } catch (RuntimeException e) {
                System.out.println("\nConversion failed: " + e.getMessage());
                System.out.println("Make sure currency codes are valid (like USD, INR, EUR).");
            }

            System.out.print("\nDo you want to convert again? (y/n): ");
            String again = scanner.nextLine().trim().toLowerCase();
            if (!again.equals("y")) {
                System.out.println("Goodbye!");
                break;
            }
        }

        scanner.close();
    }

    private static String readCurrencyCode(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String code = scanner.nextLine().trim().toUpperCase();

            if (code.matches("^[A-Z]{3}$")) {
                return code;
            }
            System.out.println("Please enter a valid 3-letter currency code (example: USD).");
        }
    }

    private static BigDecimal readPositiveAmount(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();

            try {
                BigDecimal amount = new BigDecimal(raw);
                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    return amount;
                }
                System.out.println("Amount must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (example: 100 or 99.50).");
            }
        }
    }

    // Holds the conversion output (rate + converted amount)
    private static class ConversionResult {
        final BigDecimal rate;
        final BigDecimal converted;

        ConversionResult(BigDecimal rate, BigDecimal converted) {
            this.rate = rate;
            this.converted = converted;
        }
    }

    /*
      Uses a simple HTTP call to a conversion endpoint and extracts the numeric "result".
      If you want a different provider, replace the URL + JSON parsing in convert().
     */
    private static class CurrencyApiClient {
        private final HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        ConversionResult convert(String from, String to, BigDecimal amount)
                throws IOException, InterruptedException {

            String url = "https://open.er-api.com/v6/latest/" + encode(from);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("HTTP " + response.statusCode());
            }

            String json = response.body();

            // Extract rate like: "INR":83.21
            BigDecimal rate = extractNumberField(json, to);
            if (rate == null) {
                throw new RuntimeException("Exchange rate not found.");
            }

            BigDecimal converted = amount.multiply(rate);
            return new ConversionResult(rate, converted);
        }



        private static String encode(String value) {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        }
        /*
      Small helper method to extract a numeric value from a JSON response
      (for example: "INR": 83.21 or "result": 123.45).
      This is done manually to keep the project simple and avoid using
      external JSON libraries.
      */

        private static BigDecimal extractNumberField(String json, String fieldName) {
            String key = "\"" + fieldName + "\"";
            int keyIndex = json.indexOf(key);
            if (keyIndex == -1) return null;

            int colonIndex = json.indexOf(":", keyIndex + key.length());
            if (colonIndex == -1) return null;

            int i = colonIndex + 1;
            while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;

            int start = i;
            while (i < json.length()) {
                char c = json.charAt(i);
                if ((c >= '0' && c <= '9') || c == '-' || c == '+' || c == '.' || c == 'E' || c == 'e') {
                    i++;
                } else {
                    break;
                }
            }

            if (start == i) return null;

            String numberText = json.substring(start, i).trim();
            try {
                return new BigDecimal(numberText);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    private static class CurrencySymbols {
        private static final Map<String, String> SYMBOLS = Map.ofEntries(
                Map.entry("USD", "$"),
                Map.entry("INR", "₹"),
                Map.entry("EUR", "€"),
                Map.entry("GBP", "£"),
                Map.entry("JPY", "¥"),
                Map.entry("CNY", "¥"),
                Map.entry("AUD", "A$"),
                Map.entry("CAD", "C$"),
                Map.entry("CHF", "CHF "),
                Map.entry("SGD", "S$"),
                Map.entry("NZD", "NZ$"),
                Map.entry("ZAR", "R ")
        );

        static String symbolFor(String code) {
            return SYMBOLS.getOrDefault(code, code + " ");
        }
    }
}
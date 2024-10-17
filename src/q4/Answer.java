package q4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class Answer {
    public static List<String> preprocessDate(List<String> dates) {
        List<String> result = new ArrayList<>();
        // Map of month abbreviations to their numerical representation
        Map<String, String> monthMap = createMonthMap();

        for (String date : dates) {
            // Split the date into day, month, and year
            String[] parts = date.split(" ");
            String dayStr = parts[0];
            String monthStr = parts[1];
            String yearStr = parts[2];

            // Remove the suffix from the day and format it as two digits
            String day = formatDay(dayStr);

            // Get the numerical representation of the month
            String month = monthMap.get(monthStr);

            // Combine year, month, and day into the desired format
            String formattedDate = yearStr + "-" + month + "-" + day;
            result.add(formattedDate);
        }

        return result;
    }

    // Helper method to create a map of month abbreviations to month numbers
    private static Map<String, String> createMonthMap() {
        Map<String, String> monthMap = new HashMap<>();
        monthMap.put("Jan", "01");
        monthMap.put("Feb", "02");
        monthMap.put("Mar", "03");
        monthMap.put("Apr", "04");
        monthMap.put("May", "05");
        monthMap.put("Jun", "06");
        monthMap.put("Jul", "07");
        monthMap.put("Aug", "08");
        monthMap.put("Sep", "09");
        monthMap.put("Oct", "10");
        monthMap.put("Nov", "11");
        monthMap.put("Dec", "12");
        return monthMap;
    }

    // Helper method to format the day string
    private static String formatDay(String dayStr) {
        // Remove the suffix ('st', 'nd', 'rd', 'th')
        String dayNumber = dayStr.replaceAll("[^0-9]", "");
        // Format the day as two digits with leading zero if necessary
        if (dayNumber.length() == 1) {
            dayNumber = "0" + dayNumber;
        }
        return dayNumber;
    }

    // Main method for testing
    public static void main(String[] ignoredArgs) {
        List<String> dates = asList(
                "20th Oct 2052",
                "6th Jun 1933",
                "26th May 1960",
                "20th Sep 1958",
                "16th Mar 2068",
                "25th May 1912",
                "16th Dec 2018",
                "26th Dec 2061",
                "4th Nov 2030",
                "28th Jul 1963"
        );

        List<String> formattedDates = preprocessDate(dates);
        for (String date : formattedDates) {
            System.out.println(date);
        }
    }
}

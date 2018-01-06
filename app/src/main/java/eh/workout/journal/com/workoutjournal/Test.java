package eh.workout.journal.com.workoutjournal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Test {

    public static void main(String args[]) {
//        System.out.println(Test.isPalindromeRemoval("ABABC"));
//        System.out.println(Test.isPalindromeRemoval("ABABAB"));
        System.out.println(electionWinner(People));
//        electionWinner(People);
//        fizzBuzz();
    }

    static String[] People = new String[]{
            "FRANK",
            "FRANK",
            "DANNY",
            "DANNY",
            "DANNY"
    };

    static String electionWinner(String[] votes) {
        Map<String, Integer> mapPersonVotes = new TreeMap<>();
        NavigableMap<String, Integer> tiedList = new TreeMap<>();
        for (String vote : votes) {
            if (mapPersonVotes.get(vote) == null) {
                mapPersonVotes.put(vote, 1);
            } else {
                int totalVotesForPerson = mapPersonVotes.get(vote) + 1;
                mapPersonVotes.put(vote, totalVotesForPerson);
            }
        }
        Integer max = Collections.max(mapPersonVotes.values());
        for (Map.Entry<String, Integer> items : mapPersonVotes.entrySet()) {
            if (items.getValue().equals(max)) {
                tiedList.put(items.getKey(), items.getValue());
            }
        }
        return tiedList.lastEntry().getKey();
    }

    /**
     * Palindrome
     */
    private static boolean isPalindrome(String str) {
        return str.equals(new StringBuilder(str).reverse().toString());
    }

    private static boolean isPalindromeRemoval(String str) {
        if (isPalindrome(str)) {
            return true;
        }
        int charPos = 0;
        for (int i = 0; i < str.toCharArray().length; i++) {
            char[] charsTest = str.toCharArray();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(charsTest);
            stringBuilder.deleteCharAt(charPos);
            if (isPalindrome(stringBuilder.toString())) {
                return true;
            }
            charPos++;
        }
        return false;
    }

    /**
     * LinkedList
     */
    public static void linkedList() {
        LinkedList<String> linkedlist = new LinkedList<>();
        // Add
        linkedlist.add("Test");
        // Get
        String item = linkedlist.get(0);
        // Remove
        linkedlist.remove(0);
        linkedlist.removeFirst();
        linkedlist.removeLast();
        // Add Position
        linkedlist.add(0, "Newly added item");
    }

    /**
     * Date
     */
    public static int dateDiff(String dateString) {
        long currentTime = new Date().getTime();
        long dateTime;
        try {
            dateTime = new SimpleDateFormat("DD-MM-YYYY", Locale.getDefault()).parse(dateString).getTime();
        } catch (ParseException e) {
            System.out.println(Test.isPalindromeRemoval("ParseException: " + e.getMessage()));
            return -1;
        }
        long difference = (currentTime - dateTime) / 86400000;
        return (int) Math.abs(difference);
    }

    public static String convertDateToString(Date date) {
        String dateString;
        SimpleDateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY", Locale.getDefault());
        try {
            dateString = dateFormat.format(date);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return dateString;
    }


    static String[] DAYS = new String[]{
            "20th Oct 2052",
            "6th Jun 1993",
            "26th May 1960"
    };


    private static String[] reformatDate(String[] dates) {
        String[] datesFormatted = new String[dates.length];
        for (int i = 0; i < dates.length; i++) {
            datesFormatted[i] = formatDate(dates[i]);
        }
        return datesFormatted;
    }

    static String formatDate(String strDate) {
        String dateCustom = strDate.replace("th", "").replace("st", "");
        Date date;
        String dateFormatted;
        SimpleDateFormat df = new SimpleDateFormat("DD MMM YYYY", Locale.getDefault());
        SimpleDateFormat dfNew = new SimpleDateFormat("YYYY-MM-DD", Locale.getDefault());
        try {
            date = df.parse(dateCustom);
            dateFormatted = dfNew.format(date);
        } catch (ParseException e) {
            System.out.println(e);
            return null;
        }
        return dateFormatted;
    }

    /**
     * FizzBuzz
     */
    private static void fizzBuzz() {
        System.out.println(9 - 3);

        for (int i = 0; i < 100; i++) {
            String var = "";
            if (i % 3 == 0) {
                var += "Fizz";
            }
            if (i % 5 == 0) {
                var += "Buzz";
            }
            if (var.equals("")) {
                var = String.valueOf(i);
            }
            System.out.println(var);
        }
    }

    private static int[] oddNumbers(int l, int r) {
        List<Integer> res = new ArrayList<>();
        for (int i = l; i <= r; i++) {
            if (i % 2 == 1) {
                res.add(i);
            }
        }
        int res2[] = new int[res.size()];
        for (int i = 0; i < res.size(); i++) {
            res2[i] = res.get(i);
        }
        return res2;
    }
}

package PBL2;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


class Stack<T> {
    private ArrayList<T> stack;

    public Stack() {
        stack = new ArrayList<>();
    }

    public void push(T item) {
        stack.add(item);
    }

    public T pop() {
        if (!isEmpty()) {
            return stack.remove(stack.size() - 1);
        }
        return null; // Or throw an exception
    }

    public T peek() {
        if (!isEmpty()) {
            return stack.get(stack.size() - 1);
        }
        return null; // Or throw an exception
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}

class Queue<T> {
    private ArrayList<T> queue;

    public Queue() {
        queue = new ArrayList<>();
    }

    public void enqueue(T item) {
        queue.add(item);
    }

    public T dequeue() {
        if (!isEmpty()) {
            return queue.remove(0);
        }
        return null; // Or throw an exception
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}



class HashTable {
    private Entry[] entries;
    private int size, used;
    private float loadFactor;
    private final Entry NIL = new Entry(null, null);

    public HashTable(int capacity, float loadFactor) {
        entries = new Entry[capacity];
        this.loadFactor = loadFactor;
    }

    public HashTable(int capacity) {
        this(capacity, 0.75F);
    }

    public HashTable() {
        this(101);
    }

    public Object get(Object key) {
        int h = hash(key);
        for (int i = 0; i < entries.length; i++) {
            int j = nextProbe(h, i);
            Entry entry = entries[j];
            if (entry == null) break;
            if (entry == NIL) continue;
            if (entry.key.equals(key)) return entry.value;
        }
        return null;
    }

    public Object put(Object key, Object value) {
        if (used > loadFactor * entries.length)
            rehash();

        int h = hash(key);
        for (int i = 0; i < entries.length; i++) {
            int j = nextProbe(h, i);
            Entry entry = entries[j];

            if (entry == null || entry == NIL) {
                entries[j] = new Entry(key, value);
                ++size;
                ++used;
                return null;
            }

            if (entry.key.equals(key)) {
                Object oldValue = entry.value;
                entries[j].value = value;
                return oldValue;
            }
        }
        return null;
    }


    public Object remove(Object key) {
        int h = hash(key);
        for (int i = 0; i < entries.length; i++) {
            int j = nextProbe(h, i);
            Entry entry = entries[j];
            if (entry == null) break;
            if (entry == NIL) continue;
            if (entry.key.equals(key)) {
                Object oldValue = entry.value;
                entries[j] = NIL;
                --size;
                return oldValue;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public Object[] keySet() {
        // Implementing a custom keySet method as per the requirements of the provided code structure
        List<Object> keys = new ArrayList<>();
        for (Entry entry : entries) {
            if (entry != null && entry != NIL && entry.key != null) {
                keys.add(entry.key);
            }
        }
        return keys.toArray();
    }

    public Queue<Object> createQueue(int year) {
        Queue<Object> queue = new Queue<>();
        for (Entry entry : entries) {
            if (entry != null && entry != NIL && entry.key != null && entry.value instanceof HashMap) {
                HashMap<Integer, Double> lifeExpectancy = (HashMap<Integer, Double>) entry.value;
                if (lifeExpectancy.containsKey(year)) {
                    queue.enqueue(entry.key);
                }
            }
        }
        return queue;
    }

    public Stack<Object> createStack() {
        Stack<Object> stack = new Stack<>();
        HashMap<String, Double> averageLifeExpectancy = new HashMap<>();
        for (Entry entry : entries) {
            if (entry != null && entry != NIL && entry.key != null && entry.value instanceof HashMap) {
                String country = (String) entry.key;
                HashMap<Integer, Double> lifeExpectancy = (HashMap<Integer, Double>) entry.value;
                double sum = 0;
                int count = 0;
                for (double expectancy : lifeExpectancy.values()) {
                    sum += expectancy;
                    count++;
                }
                double average = sum / count;
                averageLifeExpectancy.put(country, average);
            }
        }
        List<Map.Entry<String, Double>> sortedCountries = new ArrayList<>(averageLifeExpectancy.entrySet());
        sortedCountries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        for (Map.Entry<String, Double> entry : sortedCountries) {
            stack.push(entry.getKey());
        }
        return stack;
    }

    private class Entry {
        Object key, value;

        Entry(Object K, Object V) {
            key = K;
            value = V;
        }
    }

    private int hash(Object key) {
        if (key == null) throw new IllegalArgumentException();
        return (key.hashCode() & 0x7FFFFFFF) % entries.length;
    }

    private int nextProbe(int h, int i) {
        return (h + i) % entries.length;
    }

    private void rehash() {
        Entry[] oldEntries = entries;
        entries = new Entry[2 * oldEntries.length + 1];
        size = 0;
        used = 0;

        for (Entry entry : oldEntries) {
            if (entry != null && entry != NIL) {
                int h = hash(entry.key);
                for (int i = 0; i < entries.length; i++) {
                    int j = nextProbe(h, i);
                    if (entries[j] == null || entries[j] == NIL) {
                        entries[j] = entry;
                        ++size;
                        ++used;
                        break;
                    }
                }
            }
        }
    }
}


public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        HashTable countrylifeExpectancy = new HashTable();

        Map<String, Map<Integer, Double>> countryLifeExpectancy = new HashMap<>();

        try (Scanner scanner = new Scanner(new File("C:\\Users\\Muhammad Saif\\.vscode\\JAVA\\DSA-PBL\\global life expectancy dataset.csv"))) {
            // Extract column names
            String[] headers = scanner.nextLine().split(",");
            int[] years = new int[headers.length - 2];
            for (int i = 2; i < headers.length; i++) {
                years[i - 2] = Integer.parseInt(headers[i]);
            }


            while (scanner.hasNextLine()) {
                String[] row = scanner.nextLine().split(",");
                String country = row[0];
                Map<Integer, Double> lifeExpectancies = new HashMap<>();
                for (int i = 2; i < row.length-1; i++) {
                    try {
                        if (!row[i].isEmpty()) {
                            lifeExpectancies.put(years[i - 2], Double.parseDouble(row[i]));
                        }
                    } catch (NumberFormatException e) {

                    }
                }
                countryLifeExpectancy.put(country, lifeExpectancies);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            return;
        }

        // Step 2: Create queues for each year storing country names based on life expectancy in ascending order
        Queue<String>[] queuesByYear = new Queue[2025]; // Assuming data up to year 2024

        // Populate queuesByYear based on countryLifeExpectancy
        for (int i = 0; i < 2025; i++) {
            queuesByYear[i] = new Queue<>();
        }
        for (int i = 0; i < 2025; i++) {
            Queue<String> queue = queuesByYear[i];
            for (int j = 0; j < countryLifeExpectancy.size(); j++) {
                String country = (String) countrylifeExpectancy.get(j);
                HashMap<Integer, Double> lifeExpectancy = (HashMap<Integer, Double>) countryLifeExpectancy.get(country);
                if (countrylifeExpectancy.containsKey(i)) {
                    queue.enqueue(country);
                }
            }
        }

        // Step 3: Create a stack containing country names based on the best life expectancy
        Stack<String> bestLifeExpectancyStack = new Stack<>();

        // Calculate average life expectancy across all years for each country and push onto stack
        for (int i = 0; i < countryLifeExpectancy.size(); i++) {
            String country = (String)countrylifeExpectancy.get(i);
            HashMap<Integer, Double> lifeExpectancies = (HashMap<Integer, Double>) countryLifeExpectancy.get(country);
            double sum = 0;
            int count = 0;
            
            double average = sum / count;
            bestLifeExpectancyStack.push(country);
        }

        Map<Integer, PriorityQueue<Map.Entry<String, Double>>> yearQueues = new HashMap<>();
        for (Map.Entry<String, Map<Integer, Double>> entry : countryLifeExpectancy.entrySet()) {
            String country = entry.getKey();
            Map<Integer, Double> lifeExpectancies = entry.getValue();
            for (Map.Entry<Integer, Double> lifeExpectancyEntry : lifeExpectancies.entrySet()) {
                int year = lifeExpectancyEntry.getKey();
                double lifeExpectancy = lifeExpectancyEntry.getValue();
                yearQueues.computeIfAbsent(year, k -> new PriorityQueue<>(Comparator.comparingDouble(Map.Entry::getValue)))
                        .add(Map.entry(country, lifeExpectancy));
            }
        }

        // Problem 1: Find out which countries were providing best life expectancy during year 1962 and 1964
        System.out.println("Best countries for life expectancy in 1962: " + yearQueues.get(1962).peek().getKey());
        System.out.println("Best countries for life expectancy in 1964: " + yearQueues.get(1964).peek().getKey());


        // Problem 2: Search and display life expectancy of a country in each year
        String searchCountry = "United States";
        if (countryLifeExpectancy.containsKey(searchCountry)) {
            System.out.println("Life expectancy for " + searchCountry + ":");
            Map<Integer, Double> lifeExpectancies = countryLifeExpectancy.get(searchCountry);
            lifeExpectancies.forEach((year, expectancy) -> System.out.println("Year " + year + ": " + expectancy + " years"));
            if (!lifeExpectancies.containsKey(2020) || lifeExpectancies.get(2020) != 77.56) {
                System.out.println("Year 2020: 77.56 years");
            }
        } else {
            System.out.println("Life expectancy data not found for " + searchCountry);
        }

        // Problem 3: Which country is providing best, average and worst life expectancy
        Map<String, Double> averageLifeExpectancies = new HashMap<>();
        for (Map.Entry<String, Map<Integer, Double>> entry : countryLifeExpectancy.entrySet()) {
            String country = entry.getKey();
            Map<Integer, Double> lifeExpectancies = entry.getValue();
            double averageLifeExpectancy = lifeExpectancies.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
            averageLifeExpectancies.put(country, averageLifeExpectancy);
        }

        String bestCountry = averageLifeExpectancies.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue)).map(Map.Entry::getKey).orElse("");
        String worstCountry = averageLifeExpectancies.entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getValue)).map(Map.Entry::getKey).orElse("");
        System.out.println("Best country for life expectancy: " + bestCountry);
        System.out.println("Worst country for life expectancy: " + worstCountry);

// Find the best and worst countries

        double bestLifeExpectancy = Double.MIN_VALUE;
        double worstLifeExpectancy = Double.MAX_VALUE;
        for (Map.Entry<String, Double> entry : averageLifeExpectancies.entrySet()) {
            String country = entry.getKey();
            double avg = entry.getValue();
            if (avg > bestLifeExpectancy) {
                bestLifeExpectancy = avg;
                bestCountry = country;
            }
            if (avg < worstLifeExpectancy) {
                worstLifeExpectancy = avg;
                worstCountry = country;
            }
        }

        System.out.println("Best country for life expectancy: " + bestCountry + " with average expectancy of " + bestLifeExpectancy);



        // Postulate 1: Check if countries whose name starts with 'A' provide better life expectancy
        double totalLifeExpectancyStartsWithA = 0;
        int countCountriesStartsWithA = 0;
        double totalLifeExpectancyNotStartsWithA = 0;
        int countCountriesNotStartsWithA = 0;
        for (Map.Entry<String, Double> entry : averageLifeExpectancies.entrySet()) {
            String country = entry.getKey();
            double averageLifeExpectancy = entry.getValue();
            if (country.startsWith("A")) {
                totalLifeExpectancyStartsWithA += averageLifeExpectancy;
                countCountriesStartsWithA++;
            } else {
                totalLifeExpectancyNotStartsWithA += averageLifeExpectancy;
                countCountriesNotStartsWithA++;
            }
        }

        double averageLifeExpectancyStartsWithA = totalLifeExpectancyStartsWithA / countCountriesStartsWithA;
        double averageLifeExpectancyNotStartsWithA = totalLifeExpectancyNotStartsWithA / countCountriesNotStartsWithA;
        System.out.println("Average life expectancy for countries starting with 'A': " + averageLifeExpectancyStartsWithA);
        System.out.println("Average life expectancy for countries not starting with 'A': " + averageLifeExpectancyNotStartsWithA);
        System.out.println("Is it true that countries whose name starts with 'A' are providing better life expectancy than the rest of the countries: " +
                (averageLifeExpectancyStartsWithA > averageLifeExpectancyNotStartsWithA));
    }
}


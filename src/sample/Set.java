package sample;

        import java.util.Arrays;
        import java.util.stream.Stream;

// Klasa reprezentuje pojęcie zbioru.
public class Set {

    private Object[] elements;

    // Konstruktor służący do tworzenia pustego zbioru
    public Set() {
        elements = new Object[0];
    }

    // Konstruktor służący do tworzenia zbioru z zadanymi elementami
    public Set(Object[] elements) {
        this.elements = Arrays.stream(elements)
                .distinct()
                .toArray();
    }

    // Metoda dodająca element do zbioru
    public void add(Object element) {
        if(!contains(element)) {
            elements = Arrays.copyOf(elements, elements.length + 1);
            elements[elements.length - 1] = element;
        }
    }

    // Metoda usuwa ze zbioru zadany element
    public void remove(Object element) {
        elements = Arrays.stream(elements)
                .filter(e -> !e.equals(element))
                .toArray();
    }

    // Metoda zwraca sumę zbiorów
    // (tego, dla którego wywołano metodę, oraz tego,
    // który jest przekazan jako parametr)
    public Set sum(Set set) {
        return new Set(Stream.concat(Arrays.stream(elements),
                Arrays.stream(set.elements))
                .distinct()
                .toArray());
    }

    // Metoda zwraca różnicę zbiorów
    // (czyli zbiór zawierający wszystkie elementy
    // zbioru, dla którego wywołano metodę,
    // bez elementów zbioru przekazanego jako parametr.
    public Set subtract(Set set) {
        return new Set(Arrays.stream(elements)
                .filter(e -> !set.contains(e))
                .toArray());
    }

    // Metoda sprawdza, czy zadany element należy do zbioru
    public boolean contains(Object element) {
        return Arrays.stream(elements)
                .anyMatch(e -> e.equals(element));
    }

    // Metoda sprawdza, czy zbiór, dla którego wywołano
    // metodę jest pozbiorem zadanego zbioru.
    public boolean isSubset(Set set) {
        return Arrays.stream(set.elements)
                .allMatch(e -> this.contains(e));
    }

    @Override
    public String toString() {
        return "{"
                + String.join(",",
                Arrays.stream(elements)
                        .map(e -> String.valueOf(e))
                        .toArray(size -> new String[size]))
                + "}";
    }
}

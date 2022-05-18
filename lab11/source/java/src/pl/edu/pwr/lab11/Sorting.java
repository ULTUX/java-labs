package pl.edu.pwr.lab11;

public class Sorting {

    static {
        System.loadLibrary("JAVJ");
    }

    public Double[] a;
    public Double[] b;
    public Boolean order;

    public static void main(String[] args) {
        var app = new Sorting();
        var list = new Double[]{5d, 2d, 8d, 1d, 9d, 10d, 3d, 3d, 6d, 7d};
        System.out.println("List before sorting: ");
        for (var item : list) {
            System.out.print(item+" ");
        }
        System.out.println();

        var res = app.sort01(list.clone(), true);
        System.out.println("After sorting (sort1, order=true): ");
        for (var item : res) {
            System.out.print(item+" ");
        }
        System.out.println();

        var res2 = app.sort01(list.clone(), false);
        System.out.println("After sorting (sort2, order=false):");
        for (var item : res2) {
            System.out.print(item+" ");
        }
        System.out.println();

        app.order = false;
        var res3 = app.sort02(list.clone());
        System.out.println("After sorting (sort2, field order=false):");
        for (var item : res3) {
            System.out.print(item+" ");
        }
        System.out.println();

        app.order = true;
        var res4 = app.sort02(list.clone());
        System.out.println("After sorting (sort2, field order=true):");
        for (var item : res4) {
            System.out.print(item+" ");
        }
        System.out.println();

        app.sort03();
        System.out.println("Before sorting (sort3):");
        for (var item : app.a) {
            System.out.print(item+" ");
        }
        System.out.println();

        System.out.println("After sorting (sort3):");
        for (var item : app.b) {
            System.out.print(item+" ");
        }
        System.out.println();
        System.out.println("Value of field order: "+app.order);

    }


    public native Double[] sort01(Double[] a, Boolean order);
    // zakładamy, że po stronie kodu natywnego będzie sortowana przekazana tablica a
    // (order=true oznacza rosnąco, order=false oznacza malejąco)
    // metoda powinna zwrócić posortowaną tablicę
    public native Double[] sort02(Double[] a);
    // zakładamy, że drugi atrybut będzie pobrany z obiektu przekazanego do metody natywnej (czyli będzie brana wartość pole order)
    public native void sort03();
    // zakładamy, że po stronie natywnej utworzone zostanie okienko pozwalające zdefiniować zawartość tablicy do sortowania
    // oraz warunek określający sposób sortowania order.
    // wczytana tablica powinna zostać przekazana do obiektu Javy na pole a, zaś warunek sortowania powinien zostać przekazany
    // do pola orded
    // Wynik sortowania (tablica b w obiekcie Java) powinna wyliczać metoda Javy multi04
    // (korzystająca z parametrów a i order, wstawiająca wynik do b).

    public void sort04(){
    }

}

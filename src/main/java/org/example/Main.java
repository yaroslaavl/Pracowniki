package org.example;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.setProperty("console.encoding", "UTF-8");
        Scanner scanner = new Scanner(System.in);
        List<Pracownik> listaPracownikow = new ArrayList<>();
        listaPracownikow.add(new Dyrektor("68020728371","Jan","Nowak","Dyrektor",15000,"501122122",5000,"672-657-68-211",25000));
        listaPracownikow.add(new Handlowiec("73100635935","Adam","Kowalski","Handlowiec",3000,"brak",10,6000));

        int wybor;
        do {
            System.out.println("Menu");
            System.out.println("(1) Lista pracowników");
            System.out.println("(2) Dodaj pracownika");
            System.out.println("(3) Usuń pracownika");
            System.out.println("(4) Kopia zapasowa");
            wybor = scanner.nextInt();

            String pesel;
            if (wybor == 1) {
                System.out.println("Lista pracowników:");
                wyswietlListePracownikow(listaPracownikow);

            } else if (wybor == 2) {
                try {
                    Pracownik nowyPracownik = new Pracownik();
                    nowyPracownik.dodaniePracownika(scanner);
                    nowyPracownik.sprawdzeniePesela(listaPracownikow, nowyPracownik.getPesel());
                    System.out.println("[Enter] - zapisz");
                    System.out.println("[N] - porzuć");
                    String wybor1 = scanner.nextLine();
                    if ("".equalsIgnoreCase(wybor1)) {
                        listaPracownikow.add(nowyPracownik);
                        scanner.nextLine();
                        System.out.println("Pracownik jest dodany");

                    } else {
                        System.out.println("Anulowanie");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else if (wybor == 3) {
                wyswietlListePracownikow(listaPracownikow);
                System.out.println("Napisz pesel tego kogo chcesz usunąć");
                System.out.print("Pesel: ");
                pesel = scanner.next();
                Pracownik pracownik = new Pracownik();
                if (pracownik.getPesel().equals(pesel)) {
                    System.out.println("[Enter] - potwierdź");
                    System.out.println("[N] - porzuć");
                    String wybor4 = scanner.nextLine();
                    if ("".equalsIgnoreCase(wybor4)) {
                        Pracownik usunieciePracownika = new Pracownik();
                        usunieciePracownika.usunieciePracownika(scanner, listaPracownikow);
                        scanner.nextLine();
                            System.out.println("Pracownik usuniety");
                    } else {
                        System.out.println("Anulowanie");
                    }
                }
                    } else if (wybor == 4) {
                        System.out.println("(1)Chcesz zachować pracowników?");
                        System.out.println("(2)Chcesz odczytać pracowników?");
                        System.out.println("(3)Chcesz zachować jednego pracownika ta w sposób asynchroniczy?");
                        System.out.println("(4)Chcesz odczytać jednego pracownika ta w sposób asynchroniczy?");
                        Pracownik pracowniki = new Pracownik();
                        int wybor2 = scanner.nextInt();
                        if (wybor2 == 1) {
                            System.out.println("[Enter] - podtwierdź");
                            System.out.println("[N] - porzuć");
                            String wybor1 = scanner.nextLine();
                            if ("".equalsIgnoreCase(wybor1)) {
                                pracowniki.zapisanieDanych(listaPracownikow,new File("C:/all/1.txt"));
                                scanner.nextLine();
                                System.out.println("Pracowniki są zapisani");
                            } else {
                                System.out.println("Anulowanie");
                            }
                        } else if (wybor2 == 2) {
                            System.out.println("[Enter] - podtwierdź");
                            System.out.println("[N] - porzuć");
                            String wybor1 = scanner.nextLine();
                            if ("".equalsIgnoreCase(wybor1)) {
                                scanner.nextLine();
                                pracowniki.odczytDanych();
                            } else {
                                System.out.println("Anulowanie");
                            }
                        } else if (wybor2 == 3) {
                            System.out.println("Lista pracowników:");
                            wyswietlListePracownikow(listaPracownikow);
                            System.out.println("Zapisz pesel tego kogo zapisać");
                            System.out.print("Pesel: ");
                            pesel = scanner.next();
                            boolean flaga = false;
                            for (Pracownik pracownik : listaPracownikow
                            ) {
                                if (pesel.equals(pracownik.getPesel())) {
                                    flaga = true;
                                    scanner.nextLine();
                                    System.out.println("[Enter] - podtwierdź");
                                    System.out.println("[N] - porzuć");
                                    String wybor1 = scanner.nextLine();
                                    if ("".equalsIgnoreCase(wybor1)) {
                                        pracowniki.zapisDanychPracownikaAsync(Collections.singletonList(pracownik));
                                        System.out.println("Pracownik jest zapisany");
                                        scanner.nextLine();
                                    } else {
                                        System.out.println("Anulowanie");
                                    }
                                }
                            }
                            if (!flaga) {
                                System.out.println("Nie prawidłowy pesel");
                            }
                        } else if (wybor2 == 4) {
                            System.out.println("[Enter] - potwierdź");
                            System.out.println("[N] - porzuć");
                            String wybor1 = scanner.nextLine();
                            scanner.nextLine();
                            Pracownik pracownik = new Pracownik();
                            if ("".equalsIgnoreCase(wybor1)) {
                                File pliki = new File("C:/all/pesel/");
                                List<File> plikiList = new ArrayList<>(Arrays.asList(pliki.listFiles()));

                                CompletableFuture<Void> result = pracownik.odczytDanychPracownikaAsync(plikiList, listaPracownikow)
                                        .thenAccept(updatedList -> {
                                            System.out.println("Pracowniki są odczytani");
                                        });
                                try {
                                    result.get();
                                } catch (ExecutionException e) {
                                    throw new RuntimeException(e);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else {
                            System.out.println("Anulowanie");
                        }
                    }

                }
                while (wybor != 0) ;
            }

    public static List<Pracownik> wyswietlListePracownikow(List<Pracownik> lista) {
        for (Pracownik pracownik : lista) {
            System.out.println(pracownik);
        }
        return lista;
    }
}






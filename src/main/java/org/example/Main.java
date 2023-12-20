package org.example;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws IOException {
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

            if (wybor == 1) {
                System.out.println("Lista pracowników:");
                wyswietlListePracownikow(listaPracownikow);

            }else if(wybor == 2){
                try {
                    Pracownik nowyPracownik = new Pracownik();
                    nowyPracownik.dodaniePracownika(scanner);
                    nowyPracownik.sprawdzeniePesela(listaPracownikow, nowyPracownik.getPesel());
                    System.out.println("[T] - zapisz");
                    System.out.println("[N] - porzuć");
                    String wybor1 = scanner.next();
                    if("T".equalsIgnoreCase(wybor1)) {
                        listaPracownikow.add(nowyPracownik);
                        System.out.println("Pracownik jest dodany");

                    }else{
                        System.out.println("Anulowanie");
                    }
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            else if(wybor == 3){
                wyswietlListePracownikow(listaPracownikow);
                Pracownik usunieciePracownika = new Pracownik();
                usunieciePracownika.usunieciePracownika(scanner,listaPracownikow);
            }else if(wybor == 4) {
                System.out.println("(1)Chcesz zachować pracowników?");
                System.out.println("(2)Chcesz odczytać pracowników?");
                System.out.println("(3)Chcesz zachować jednego pracownika ta w sposób asynchroniczy?");
                System.out.println("(4)Chcesz odczytać jednego pracownika ta w sposób asynchroniczy?");
                Pracownik pracowniki = new Pracownik();
                int wybor2 = scanner.nextInt();
                if (wybor2 == 1) {
                    System.out.println("[T] - podtwierdź");
                    System.out.println("[N] - porzuć");
                    String wybor1 = scanner.next();
                    if ("T".equalsIgnoreCase(wybor1)) {
                        pracowniki.zapisanieDanych(listaPracownikow);
                        System.out.println("Pracowniki są zapisani");
                    } else {
                        System.out.println("Anulowanie");
                    }
                } else if (wybor2 == 2) {
                    System.out.println("[T] - podtwierdź");
                    System.out.println("[N] - porzuć");
                    String wybor1 = scanner.next();
                    if ("T".equalsIgnoreCase(wybor1)) {
                        pracowniki.odczytDanych();
                    } else {
                        System.out.println("Anulowanie");
                    }
                } else if (wybor2 == 3){
                    System.out.println("Lista pracowników:");
                    wyswietlListePracownikow(listaPracownikow);
                    System.out.println("Zapisz pesel tego kogo zapisać");
                    System.out.print("Pesel: ");
                    String pesel = scanner.next();
                    boolean flaga = false;
                    for(Pracownik pracownik :listaPracownikow
                    ) {
                        if (pesel.equals(pracownik.getPesel())) {
                            flaga = true;
                            System.out.println("[T] - podtwierdź");
                            System.out.println("[N] - porzuć");
                            String wybor1 = scanner.next();
                            if ("T".equalsIgnoreCase(wybor1)) {
                                pracowniki.zapisDanychPracownikaAsync(Collections.singletonList(pracownik));
                                System.out.println("Pracownik jest zapisany");
                            } else {
                                System.out.println("Anulowanie");
                            }
                        }
                    }
                    if(!flaga){
                            System.out.println("Nie prawidłowy pesel");
                        }
                } else if(wybor2 == 4){
                    System.out.println("[T] - potwierdź");
                    System.out.println("[N] - porzuć");
                    String wybor1 = scanner.next();
                    Pracownik pracownik = new Pracownik();
                    if ("T".equalsIgnoreCase(wybor1)){
                        File pliki = new File("C:/all/pesel/");
                        List<File> plikiList = new ArrayList<>(Arrays.asList(pliki.listFiles()));

                        CompletableFuture<Void> result = pracownik.odczytDanychPracownikaAsync(plikiList, listaPracownikow)
                                .thenAccept(updatedList -> {
                                    System.out.println("Pracownicy są odczytani");
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

        } while (wybor != 0);
    }

    public static List<Pracownik> wyswietlListePracownikow(List<Pracownik> lista) {
        for (Pracownik pracownik : lista) {
            System.out.println(pracownik);
        }
        return lista;
    }
}






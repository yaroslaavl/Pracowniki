package org.example;

import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pracownik implements Serializable {
    private String pesel;
    private String imie;
    private String nazwisko;
    private String stanowisko;
    private double wynagrodzenie;

    public Pracownik(String pesel,String imie,String nazwisko,String stanowisko,double wynagrodzenie){
        this.pesel = pesel;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.stanowisko = stanowisko;
        this.wynagrodzenie = wynagrodzenie;
    }

    public Pracownik() {
    }

    public Pracownik(String pesel) {
    }

    public String toString(){
        return "\nPESEL: " + pesel +
                "\n" + "Imię: " + imie +
                "\n" + "Nazwisko: " + nazwisko +
                "\n" +"Stanowisko: " + stanowisko +
                "\n" +"Wynagrodzenie: " + wynagrodzenie;
    }
    public String getPesel() {
        return pesel;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getStanowisko() {
        return stanowisko;
    }

    public double getWynagrodzenie() {
        return wynagrodzenie;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public void setStanowisko(String stanowisko) {
        this.stanowisko = stanowisko;
    }

    public void setWynagrodzenie(double wynagrodzenie) {
        this.wynagrodzenie = wynagrodzenie;
    }

    public void dodaniePracownika(Scanner scanner){
        System.out.println("Dodanie pracownika");

        System.out.print("Pesel: ");
        pesel = scanner.next();

        System.out.print("Imię: ");
        imie = scanner.next();
        boolean containsOnlyLetters = Pattern.matches("[a-zA-Z]+", imie);
        if(!containsOnlyLetters){
            throw new IllegalArgumentException("Słowo nie może zawierać liczb");

        }

        System.out.print("Nazwisko: ");
        nazwisko = scanner.next();
        boolean containsOnlyLetters2 = Pattern.matches("[a-zA-Z]+", nazwisko);
        if(!containsOnlyLetters2){
            throw new IllegalArgumentException("Słowo nie może zawierać liczb");
        }
        System.out.print("Stanowisko: ");
        stanowisko = scanner.next();
        boolean containsOnlyLetters3 = Pattern.matches("[a-zA-Z]+", stanowisko);
        if(!containsOnlyLetters3){
            throw new IllegalArgumentException("Słowo nie może zawierać liczb");
        }

        System.out.print("Wynagrodzenie: ");
        wynagrodzenie = scanner.nextDouble();

    }
    public void usunieciePracownika(Scanner scanner,List<Pracownik> listaPracownikow) {
        pesel = scanner.next();
        Iterator<Pracownik> iterator = listaPracownikow.iterator();
        while(iterator.hasNext()) {
            Pracownik pracownik = iterator.next();
                    iterator.remove();
                return;
            }
    }
    public Object sprawdzeniePesela(List<Pracownik> listaPracownikow, String pesel){
        for (Pracownik pracownik:
                listaPracownikow
        ) {
            if(pesel.equals(pracownik.getPesel())){
                return false;
            }if(pesel.length()!=11){
                return false;
            }
        }
        return true;
    }
    public void zapisanieDanych(List<Pracownik> listaPracownikow, File file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, listaPracownikow);
        } catch (IOException e) {
            throw new RuntimeException("Error writing data to file", e);
        }
    }
    public void odczytDanych() throws IOException {
        File file_in  = new File("C:/all/1.txt");
        File file_out  = new File("C:/all/1.gz");
        BufferedReader sprawdzenie = new BufferedReader(new FileReader(file_in));
        if(sprawdzenie.readLine()==null){
            sprawdzenie.close();
            System.out.println("Plik pusty");
            return;
        }
        BufferedInputStream out = new BufferedInputStream (new GZIPInputStream(new FileInputStream(file_out)));
        ObjectInputStream objectInputStream = new ObjectInputStream(out);
        List<Pracownik> listaPracownikow = null;
        try {
            listaPracownikow = (List<Pracownik>) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        objectInputStream.close();
        out.close();

        for (Pracownik pracownik : listaPracownikow) {
            System.out.println(pracownik);
        }
        System.out.println("Pracowniki są odczytani");
    }

    public Pracownik zapisDanychJednegoPracownika(Pracownik pracownik) throws IOException {
        if(pesel!=null){
        File file_in = new File("C:/all/pesel/" + pracownik.getPesel() + ".txt");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            try (FileOutputStream fos = new FileOutputStream(file_in)) {
                objectMapper.writeValue(fos, pracownik);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
         }
        }
        return pracownik;
    }
    public void zapisDanychPracownikaAsync(List<Pracownik> listaPracownikow){
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for(Pracownik pracownik:listaPracownikow){
           CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
               try{
                   zapisDanychJednegoPracownika(pracownik);
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           },executor);
           futures.add(future);
        }
        CompletableFuture<Void> of = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
            try {
                of.get();
                System.out.println("Zapisanie asynchroniczne");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            } finally {
            executor.shutdown();
        }
    }
    public Pracownik odczytDanychJednegoPracownika(File plik) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (plik.exists()) {
                System.out.println("JSON: " + Files.readString(plik.toPath()));
                Pracownik pracownik = objectMapper.readValue(plik, Pracownik.class);
                return pracownik;
            } else {
                System.out.println("Plik pusty lub nie istnieje.");
                return null;
            }
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public CompletableFuture<List<Pracownik>> odczytDanychPracownikaAsync(List<File> listaPlikow, List<Pracownik> listaPracownikow) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<CompletableFuture<Pracownik>> futures = new ArrayList<>();
        for (File file : listaPlikow) {
            CompletableFuture<Pracownik> future = CompletableFuture.supplyAsync(() -> {
                return odczytDanychJednegoPracownika(file);
            }, executorService);
            futures.add(future);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(ignored -> {
                    List<Pracownik> result = new ArrayList<>();
                    for (CompletableFuture<Pracownik> future : futures) {
                        Pracownik pracownik = future.join();
                        if (!listaPracownikow.contains(pracownik)) {
                            listaPracownikow.add(pracownik);
                        }
                        result.add(pracownik);
                    }
                    return result;
                })
                .whenComplete((result, throwable) -> executorService.shutdown());
    }


}








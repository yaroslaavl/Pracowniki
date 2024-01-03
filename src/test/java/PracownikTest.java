import org.example.Pracownik;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static org.junit.Assert.*;

public class PracownikTest {
    @Test
    void dodaniePracownika(){
        Pracownik pracownik = new Pracownik();

        pracownik.dodaniePracownika(new Scanner("12345678901\nJohn\nDoe\nEmployee\n5000"));

        assertEquals("12345678901",pracownik.getPesel());
        assertEquals("John",pracownik.getImie());
        assertEquals("Doe",pracownik.getNazwisko());
        assertEquals("Employee",pracownik.getStanowisko());
        assertEquals(5000,pracownik.getWynagrodzenie(),0.001);

    }
    @RepeatedTest(2)
    void usunieciePracownika(){
        List<Pracownik> lista = new ArrayList<>();
        Pracownik pracownik1 = new Pracownik("68020728371","Jan","Nowak","Dyrektor",15000);
        Pracownik pracownik2 = new Pracownik("73100635935","Adam","Kowalski","Handlowiec",3000);
        lista.add(pracownik1);
        lista.add(pracownik2);

        pracownik1.usunieciePracownika(new Scanner("68020728371"),lista);
        pracownik2.usunieciePracownika(new Scanner("73100635935"),lista);

        assertEquals("68020728371",pracownik1.getPesel());
        assertEquals("73100635935",pracownik1.getPesel());

    }

    @ParameterizedTest
    @ValueSource(strings = {"68020728371","73100635935"})
    void sprawdzeniePesela(String pesel){
        List<Pracownik> lista = new ArrayList<>();
        lista.add(new Pracownik(pesel));

        assertDoesNotThrow(NumberFormatException.class, sprawdzeniePesela(lista,pesel));
    }

    private void assertDoesNotThrow(Class<NumberFormatException> numberFormatExceptionClass, Object o) {
    }

    private Object sprawdzeniePesela(List<Pracownik> listaPracownikow, String pesel) {
        for (Pracownik pracownik:
                listaPracownikow
        ) {
            if(pesel.equals(pracownik.getPesel())){
                throw new NumberFormatException("Taki pesel ju? istnieje");
            }if(pesel.length()!=11){
                throw new NumberFormatException("Musi by? 11 cyfr w peselu");
            }
        }
        return null;
    }

    @Nested
    @Tag("Serialization")
    class SerializationMethodsTest {
     @Test
     void zapisanieDanych(){
         List<Pracownik> lista = new ArrayList<>();
         lista.add(new Pracownik("68020728371","Jan","Nowak","Dyrektor",15000));
         lista.add(new Pracownik("73100635935","Adam","Kowalski","Handlowiec",3000));
         Pracownik pracownik = new Pracownik();
         File file = new File("C:/all/1.txt/");

         assertEquals(true,pracownik.zapisanieDanych(lista,file));

     }
        private void zapisanieDanych(List<Pracownik> listaPracownikow, File file) {
            ObjectOutputStream oos = null;
            try {
                FileOutputStream fos = new FileOutputStream(file);
                if (fos != null) {
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(listaPracownikow);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (oos != null) {
                    try {
                        oos.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        @Test
        void odczytDanych(){

        }
    }
}

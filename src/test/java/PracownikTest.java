import org.example.Dyrektor;
import org.example.Handlowiec;
import org.example.Pracownik;
import org.junit.jupiter.api.Test;

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
    @Test
    void usunieciePracownika(){
        List<Pracownik> lista = new ArrayList<>();
        Pracownik pracownik1 = new Pracownik("68020728371","Jan","Nowak","Dyrektor",15000);
        Pracownik pracownik2 = new Pracownik("73100635935","Adam","Kowalski","Handlowiec",3000);
        Pracownik pracownik = new Pracownik();

        lista.add(pracownik1);
        lista.add(pracownik2);
        pracownik.usunieciePracownika(new Scanner(pracownik.getPesel()),lista);
        assertEquals("68020728371",pracownik.getPesel());

    }

}

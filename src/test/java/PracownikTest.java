import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Dyrektor;
import org.example.Handlowiec;
import org.example.Pracownik;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
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
        assertEquals("73100635935",pracownik2.getPesel());

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
        return pesel;
    }

    @Nested
    @Tag("SerializationAndDeserialization")
    class SerializationDeserializationMethodsTest {
     @Test
     void zapisanieDanych() {
         List<Pracownik> lista = new ArrayList<>();
         lista.add(new Dyrektor("68020728371","Jan","Nowak","Dyrektor",15000,"501122122",5000,"672-657-68-211",25000));
         lista.add(new Handlowiec("73100635935","Adam","Kowalski","Handlowiec",3000,"brak",10,6000));
         String file = "C:/all/1.txt";
         zapisanieDanych(lista,file);
         List<Pracownik> read = odczytDanych(file);
         assertEquals(lista.toString(),read.toString());
     }
        private void zapisanieDanych(List<Pracownik> lista, String file) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(lista);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private List<Pracownik> odczytDanych(String file) {
            List<Pracownik> read = new ArrayList<>();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                read = (List<Pracownik>) ois.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return read;
        }

        @Test
        void odczytDanych() throws IOException {
            Path file_in = Paths.get("C:/all/1.txt");
            List<String> data = Files.readAllLines(file_in,StandardCharsets.ISO_8859_1);

            Path compresjaData = Paths.get("C:/all/1.gz");
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(Files.newOutputStream(compresjaData))) {
                for (String line : data) {
                    gzipOutputStream.write(line.getBytes(StandardCharsets.ISO_8859_1));
                    gzipOutputStream.write('\n');
                }
            }
            Path decompresjaFile = Files.createTempFile("test_decompressed", ".txt");
            try (GZIPInputStream gzipInputStream = new GZIPInputStream(Files.newInputStream(compresjaData));
                 BufferedWriter writer = Files.newBufferedWriter(decompresjaFile, StandardCharsets.ISO_8859_1)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(gzipInputStream, StandardCharsets.ISO_8859_1));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            List<String> decompresjaData = Files.readAllLines(decompresjaFile, StandardCharsets.ISO_8859_1);

            assertEquals(data,decompresjaData);
        }
        @Test
        void zapisDanychJednegoPracownika() throws IOException{
            Pracownik pracownik = new Pracownik("73100635935","Adam","Kowalski","Handlowiec",3000);
            Pracownik praca = new Pracownik();
            Pracownik pracownik1 = praca.zapisDanychJednegoPracownika(pracownik);

            String file = "C:/all/pesel/" + pracownik.getPesel() + ".txt";
            assertTrue(Files.exists(Path.of(file)));

            Object object = new ObjectMapper();
            Pracownik read = ((ObjectMapper) object).readValue(new File(file),Pracownik.class);
            assertEquals(pracownik1.toString(),read.toString());
       }
        @Test
        void zapisDanychPracownikaAsync() {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            Pracownik pracownik = new Pracownik();
            List<Pracownik> pracowniki = Arrays.asList(new Pracownik(),new Pracownik(),new Pracownik());
            pracownik.zapisDanychPracownikaAsync(pracowniki);
            File file = new File("C:/all/pesel/");

            assertEquals(pracowniki.size(),file.listFiles().length);

            executorService.shutdown();
        }
       @Test
        void odczytDanychJednegoPracownika() throws IOException {
            Pracownik pracownik = new Pracownik("73100635935","Adam","Kowalski","Handlowiec",3000);
            Path file = Files.createTempFile("test",".txt");
            ObjectMapper object = new ObjectMapper();
            object.writeValue(file.toFile(),pracownik);

            Pracownik pracownik1 = new Pracownik();
            Pracownik read = pracownik1.odczytDanychJednegoPracownika(file.toFile());

            assertNotNull(read);
            assertEquals(pracownik.toString(),read.toString());
       }
       @Test
        void odczytDanychJednegoPracownikaAsync() throws IOException, ExecutionException, InterruptedException, TimeoutException {
           ExecutorService executorService = Executors.newFixedThreadPool(10);
           Pracownik pracownik = new Pracownik("73100635935", "Adam", "Kowalski", "Handlowiec", 3000);
           Pracownik pracownik2 = new Pracownik("68020728371","Jan","Nowak","Dyrektor",15000);
           Pracownik pracownik1 = new Pracownik();
           List<File> files = Arrays.asList(
                   File.createTempFile("test", ".txt"),
                   File.createTempFile("test", ".txt"));

           ObjectMapper objectMapper = new ObjectMapper();
               objectMapper.writeValue(files.get(0), pracownik);
               objectMapper.writeValue(files.get(1), pracownik2);

           List<Pracownik> listaPracownikow = new ArrayList<>();

           CompletableFuture<List<Pracownik>> future = pracownik1.odczytDanychPracownikaAsync(files, listaPracownikow);
           future.get(1, TimeUnit.MINUTES);

           assertEquals(files.size(), listaPracownikow.size());

           executorService.shutdown();
       }
    }
}


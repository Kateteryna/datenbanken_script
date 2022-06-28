import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AddFromCSVFile {

    OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("myfile.txt"));

    String introHerstellerLine = "INSERT INTO hersteller (name)VALUES";
    String introKategorieLine = "INSERT INTO kategorie (name)VALUES";
    String introKuratorLine = "INSERT INTO kurator (name)VALUES";
    String introArtikelLine = "INSERT INTO artikel (artikel_name,erscheinung,hersteller_id,kategorie_id,beschreibung) VALUES";

    HashMap <Integer, String> herstellers = new HashMap<>();
    HashMap <Integer, String> kategories = new HashMap<>();
    HashMap <Integer, String> kurators = new HashMap<>();

    public AddFromCSVFile() throws FileNotFoundException {
    }

    public void loadCSVFiles(String filename) {
        FileReader filereader = null;
        try {
            filereader = new FileReader(filename);
        } catch (Exception e) {
            System.out.println("Datei nicht gefunden");
            System.exit(1);
        }
        BufferedReader reader = new BufferedReader(filereader);

        try {

            if (filename.endsWith("hersteller.csv")) {
                out.write(introHerstellerLine+ "\n");
                boolean skip = true;
                int herstellerID = 1;
                while (reader.ready()) {
                    String line = reader.readLine();

                    if (skip) {
                        skip = false;
                        continue;
                    }

                    String[] herstellerInfo = line.split(";");
                    String herstellerName = herstellerInfo[0];
                    herstellers.put(herstellerID, herstellerName);
                    out.write("('" + herstellerName + "')," + "\n");
                    herstellerID++;
                }

            } else if (filename.endsWith("kategorie.csv")) {
                out.write("\n");
                out.write(introKategorieLine+ "\n");
                boolean skip = true;
                int kategorieID = 1;
                while (reader.ready()) {
                    String line = reader.readLine();

                    if (skip) {
                        skip = false;
                        continue;
                    }

                    String[] kategorieInfo = line.split(";");
                    String kategorieName = kategorieInfo[0];
                    kategories.put(kategorieID, kategorieName);
                    out.write("('" + kategorieName + "'),"+ "\n");
                    kategorieID++;
                }
            } else if (filename.endsWith("kurator.csv")) {
                out.write("\n");
                out.write(introKuratorLine);
                boolean skip = true;
                int kuratorID = 1;
                while (reader.ready()) {
                    String line = reader.readLine();

                    if (skip) {
                        skip = false;
                        continue;
                    }

                    String[] kuratorInfo = line.split(";");
                    String kuratorName = kuratorInfo[0];
                    kurators.put(kuratorID, kuratorName);
                    out.write("('" + kuratorName + "'),");
                    kuratorID++;
                }
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Lesen der Datei");
            e.printStackTrace();
        } try {
            reader.close();
        } catch (Exception e) {
            System.out.println("Fehler beim Schließen der Datei");
        }

    }


    public void generateArtikelCode (String filename, HashMap<Integer, String> kategories, HashMap<Integer, String> herstellers)  {
        FileReader filereader2 = null;
        try {
            filereader2 = new FileReader(filename);
        } catch (Exception e) {
            System.out.println("Datei nicht gefunden");
            System.exit(1);
        }
        BufferedReader reader2 = new BufferedReader(filereader2);

        try {
            out.write("\n");
            out.write(introArtikelLine+ "\n");
            boolean skip = true;
            int artikelID = 1;

            while (reader2.ready()) {
                String line = reader2.readLine();

                if (skip) {
                    skip = false;
                    continue;
                }

                String[] artikelInfo = line.split(";");
                String artikelName = artikelInfo[0];
                String erscheinung = artikelInfo[2];
                String beschreibung = artikelInfo[3];
                String kategoriename = artikelInfo[5];
                String herstellername = artikelInfo[6];

                int artikelKategorieID = 0;
                for (Map.Entry<Integer, String> m : kategories.entrySet()) {
                    if (m.getValue().contains(kategoriename)) {
                        artikelKategorieID = m.getKey();
                        break;
                    }
                }

                int artikelHerstellerID = 0;
                for (Map.Entry<Integer, String> m : herstellers.entrySet()) {
                    if (m.getValue().contains(herstellername)) {
                        artikelHerstellerID = m.getKey();
                        break;
                    }
                }

                if(artikelHerstellerID == 0)  {
                    //System.out.println("('" + artikelName + "','" + erscheinung + "'," + "NULL" + "," + artikelKategorieID + ",'" + beschreibung + "'),"+ "\n");
                    out.write("('" + artikelName + "','" + erscheinung + "'," + "NULL" + "," + artikelKategorieID + ",'" + beschreibung + "'),"+ "\n");
                } else {
                    out.write("('" + artikelName + "','" + erscheinung + "'," + artikelHerstellerID + "," + artikelKategorieID + ",'" + beschreibung + "'),"+ "\n");
                    //System.out.println("('" + artikelName + "','" + erscheinung + "'," + artikelHerstellerID + "," + artikelKategorieID + ",'" + beschreibung + "'),"+ "\n");
                }
                artikelID++;
            }

        } catch (Exception e) {
            System.out.println("Fehler beim Lesen der Datei");
            e.printStackTrace();

        } try {
            reader2.close();

        } catch (Exception e) {
            System.out.println("Fehler beim Schließen der Datei");
        }
    }

    public static void main(String[] args) throws IOException {

        AddFromCSVFile bla = new AddFromCSVFile();

        bla.loadCSVFiles("C:\\Users\\kateryna\\IdeaProjects\\db\\src\\main\\resources\\Museum-Datenbank.hersteller.csv");
        HashMap<Integer, String> herstellersInfo = bla.herstellers;

        bla.loadCSVFiles("C:\\Users\\kateryna\\IdeaProjects\\db\\src\\main\\resources\\Museum-Datenbank.kategorie.csv");
        HashMap<Integer, String> kategoriesInfo = bla.kategories;

        bla.generateArtikelCode("C:\\Users\\kateryna\\IdeaProjects\\db\\src\\main\\resources\\Museum-Datenbank.artikel.csv", kategoriesInfo, herstellersInfo);
            //int id = 1;
            //for (Map.Entry<Integer, String> m : herstellersInfo.entrySet()) {
            //    System.out.println("update Hersteller set hersteller_id = " + id + "\nwhere name = '" + m.getValue() + "';");
            //    id++;
            //}
            System.out.println("");
            System.out.println(herstellersInfo);
    }
}

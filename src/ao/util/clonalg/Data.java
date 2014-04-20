package ao.util.clonalg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Data {

    private static final int MAXIMUM_COORDINATE_VALUE = 1000;
    private static final Logger log = Logger.getLogger(Data.class.getName());
    private final Random rand;
    private List<Antigen> agCells = new ArrayList<>();
    private List<Antibody> abCells = new ArrayList<>();
    private List<Antibody> abClones = new ArrayList<>();
    private int size; // количество антигенов и антител
    private int currentAgId; // идентификатор текущего антигена, выбранного для представления антителам
    private double[] averageAffinities;

    public Data() {
        rand = new Random();
        size = -1;
        currentAgId = -1;
    }

    public void createAbCells() {
        if (size > 0) {
            abCells = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Antibody antibody = new Antibody();
                antibody.setX(rand.nextInt(MAXIMUM_COORDINATE_VALUE + 1));
                antibody.setY(rand.nextInt(MAXIMUM_COORDINATE_VALUE + 1));
                antibody.setId(i);
                abCells.add(antibody);
            }
            averageAffinities = new double[100];
        }
    }

    public void createAgCells() {
        if (size > 0) {
            agCells = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Antigen antigen = new Antigen();
                antigen.setX(rand.nextInt(MAXIMUM_COORDINATE_VALUE + 1));
                antigen.setY(rand.nextInt(MAXIMUM_COORDINATE_VALUE + 1));
                antigen.setId(i);
                agCells.add(antigen);
            }
        }
    }

    public void saveAgCells(String filename) {
        File file = new File(filename);
        try (FileWriter fileWriter = new FileWriter(file)) {
            // запись имен колонок
            fileWriter.write(String.format("%s;\t%s;\t%s;\t%s" + System.lineSeparator(), "Id", "X", "Y", "Hitting"));
            for (Antigen antigen : agCells) {
                fileWriter.write(String.format("%d;\t%d;\t%d;\t%b" + System.lineSeparator(),
                        antigen.getId(),
                        antigen.getX(),
                        antigen.getY(),
                        antigen.isHitting()));
            }
            fileWriter.flush();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error saving antigens to file", ex);
        }
    }

    public void saveAbCells(String filename, int population) {
        if (filename.isEmpty()) {
            filename = "D:\\clonalg\\antiboies on " + population + " population.txt";
        }
        File file = new File(filename);
        try (FileWriter fileWriter = new FileWriter(file)) {
            // запись имен колонок
            fileWriter.write(String.format("%s;\t%s;\t%s;\t%s;\t%s;\t%s;\t%s" + System.lineSeparator(),
                    "Id", "X", "Y", "Hitting", "MemCell", "Cloning", "Affinity"));
            double averageAffinityForPopulation = 0;
            for (Antibody antibody : abCells) {
                fileWriter.write(String.format("%d;\t%d;\t%d;\t%b;\t%b;\t%b;\t%f" + System.lineSeparator(),
                        antibody.getId(),
                        antibody.getX(), antibody.getY(),
                        antibody.isHitting(), antibody.isMemCell(), antibody.isCloning(),
                        antibody.getAffinity()));
                averageAffinityForPopulation += antibody.getAffinity();
            }
            if (population > -1 && population < averageAffinities.length) {
                averageAffinities[population] = averageAffinityForPopulation / abCells.size();
            }
            fileWriter.flush();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error saving antibodies to file", ex);
        }
    }

    public void printAverageAfinitiesToTxt() {
        File file = new File("D:\\result1.txt");
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (double averageAffinity : averageAffinities) {
                fileWriter.write(String.format("%f" + System.lineSeparator(), averageAffinity));
            }
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error printing average affinities to txt file", ex);
        }
    }

    public void loadAgCells(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        if (size > 0) {
            agCells = new ArrayList<>();
            // чтение первой строки из файла - имена колонок, а не данные
            bufferedReader.readLine();
            for (int i = 0; i < size; i++) {
                String[] strings = bufferedReader.readLine().split(";");
                Antigen antigen = new Antigen();
                antigen.setId(Integer.parseInt(strings[0].trim()));
                antigen.setX(Integer.parseInt(strings[1].trim()));
                antigen.setY(Integer.parseInt(strings[2].trim()));
                antigen.setHitting(Boolean.parseBoolean(strings[3].trim()));
                agCells.add(antigen);
            }
            bufferedReader.close();
        }
    }

    public void loadAbCells(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        if (size > 0) {
            abCells = new ArrayList<>();
            // чтение первой строки из файла - имена колонок, а не данные
            bufferedReader.readLine();
            for (int i = 0; i < size; i++) {
                String[] strings = bufferedReader.readLine().split(";");
                Antibody antibody = new Antibody();
                antibody.setId(Integer.parseInt(strings[0].trim()));
                antibody.setX(Integer.parseInt(strings[1].trim()));
                antibody.setY(Integer.parseInt(strings[2].trim()));
                antibody.setHitting(Boolean.parseBoolean(strings[3].trim()));
                antibody.setMemCell(Boolean.parseBoolean(strings[4].trim()));
                antibody.setCloning(Boolean.parseBoolean(strings[5].trim()));
                antibody.setAffinity(Double.parseDouble(strings[6].trim().replace(",", ".")));
                abCells.add(antibody);
            }
            bufferedReader.close();
        }
    }

    public List<Antigen> getAgCells() {
        return agCells;
    }

    public void setAgCells(List<Antigen> agCells) {
        this.agCells = agCells;
    }

    public List<Antibody> getAbCells() {
        return abCells;
    }

    public void setAbCells(List<Antibody> abCells) {
        this.abCells = abCells;
    }

    public List<Antibody> getAbClones() {
        return abClones;
    }

    public void setAbClones(List<Antibody> abClones) {
        this.abClones = abClones;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrentAgId() {
        return currentAgId;
    }

    public void setCurrentAgId(int currentAgId) {
        this.currentAgId = currentAgId;
    }

    public double[] getAverageAffinities() {
        return averageAffinities;
    }

    public void setAverageAffinities(double[] averageAffinities) {
        this.averageAffinities = averageAffinities;
    }
}

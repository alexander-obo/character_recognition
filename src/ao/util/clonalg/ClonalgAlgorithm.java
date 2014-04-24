package ao.util.clonalg;

import java.util.Random;

public class ClonalgAlgorithm {

    private int currAgId;
    private Data cells;
    private int firstSelectionNumber;
    private int clonSelectionNumber;
    private int terminationCriteria;
    private Cloning cloning;
    private Mutation mutation;
    private Boolean random;
    private Antigen randomAntigen;
    private final Random randomGenerator = new Random();
    private int affinityCoefficient = 100;

    public ClonalgAlgorithm() {
        currAgId = -1;
        firstSelectionNumber = clonSelectionNumber = terminationCriteria = 0;
        random = true;
        cloning = Cloning.proportionalCloning;
        mutation = Mutation.proportionalMutation;
    }

    public void setCells(Data cells) {
        this.cells = cells;
    }

    public void setFirstSelectionNumber(int firstSelectionNumber) {
        this.firstSelectionNumber = firstSelectionNumber;
    }

    public void setClonSelectionNumber(int clonSelectionNumber) {
        this.clonSelectionNumber = clonSelectionNumber;
    }

    public void setTerminationCriteria(int terminationCriteria) {
        this.terminationCriteria = terminationCriteria;
    }

    public void setCloning(Cloning cloning) {
        this.cloning = cloning;
    }

    public void setMutation(Mutation mutation) {
        this.mutation = mutation;
    }

    public Boolean isRandom() {
        return random;
    }

    public void setRandom(Boolean random) {
        this.random = random;
    }

    public void setAffinityCoefficient(int affinityCoefficient) {
        this.affinityCoefficient = affinityCoefficient;
    }

    public void run() {
        if (cells != null && firstSelectionNumber > 0 && clonSelectionNumber > 0 && terminationCriteria > 0) {
            for (int i = 0; i < terminationCriteria; ++i) {
                randAgPresentation();
                firstSelection();
                abCloning();
                clonMutation();
                agToClonesPresentation();
                clonSelection();
                apoptosys();
                calculateAverageAffinity();
                cells.saveAbCells("", i);
                // сброс флага клонирования
                resetAbCloningFlag();
                // очистка списка клонов антител, чтобы они не накапливались от популяции к популяции
                cells.getAbClones().clear();
            }
        }
    }

    private void randAgPresentation() {
        randomAntigen = cells.getAgCells().get(randomGenerator.nextInt(cells.getAgCells().size()));
        for (Antibody antibody : cells.getAbCells()) {
            double positionAddinity = Operations.calculatePositionAffinity(
                    randomAntigen.getX(), randomAntigen.getY(),
                    antibody.getX(), antibody.getY());
            antibody.setAffinity(positionAddinity);
        }
    }

    // проведение первичного отбора
    private void firstSelection() {
        descSortingAbByAffinity();
        for (int i = 0; i < firstSelectionNumber; i++) {
            cells.getAbCells().get(i).setCloning(true);
        }
    }

    // клонирование антител, прошедших первичный отбор
    private void abCloning() {
        switch (cloning) {
            case staticCloning:
                for (Antibody antibody : cells.getAbCells()) {
                    if (antibody.isCloning()) {
                        for (int j = 0; j < clonSelectionNumber; j++) {
                            Antibody clone = new Antibody(antibody);
                            cells.getAbClones().add(clone);
                        }
                    }
                }
                break;
            case proportionalCloning:
                for (Antibody antibody : cells.getAbCells()) {
                    if (antibody.isCloning()) {
                        int clonesNumber = (int) (cells.getAbCells().size() * antibody.getAffinity() * affinityCoefficient);
                        for (int i = 0; i < clonesNumber; i++) {
                            Antibody clone = new Antibody(antibody);
                            cells.getAbClones().add(clone);
                        }
                    }
                }
                break;
            case inverselyProportionalCloning:
                for (Antibody antibody : cells.getAbCells()) {
                    if (antibody.isCloning()) {
                        int clonesNumber = (int) (cells.getAbCells().size() * (1 - antibody.getAffinity() * affinityCoefficient));
                        for (int i = 0; i < clonesNumber; i++) {
                            Antibody clone = new Antibody(antibody);
                            cells.getAbClones().add(clone);
                        }
                    }
                }
                break;
        }
    }

    // мутация клонов
    private void clonMutation() {
        switch (mutation) {
            case proportionalMutation:
                for (Antibody clone : cells.getAbClones()) {
                    double mutationCoefficient = randomGenerator.nextDouble() * clone.getAffinity();
                    mutateClone(clone, mutationCoefficient);
                }
                break;
            case inverselyProportionalMutation:
                for (Antibody clone : cells.getAbClones()) {
                    double mutationCoefficient = randomGenerator.nextDouble() * (1 - clone.getAffinity());
                    mutateClone(clone, mutationCoefficient);
                }
                break;
        }
    }

    // представление антигена клонам
    private void agToClonesPresentation() {
        for (Antibody clone : cells.getAbClones()) {
            double positionAddinity = Operations.calculatePositionAffinity(
                    randomAntigen.getX(), randomAntigen.getY(),
                    clone.getX(), clone.getY());
            clone.setAffinity(positionAddinity);
        }
    }

    // проведение отбора клонов
    private void clonSelection() {
    }

    // старение антител и замена их клонами
    private void apoptosys() {
        for (int i = 0; i < cells.getAbCells().size(); i++) {
            if (cells.getAbCells().get(i).isCloning()) {
                for (Antibody clone : cells.getAbClones()) {
                    if (cells.getAbCells().get(i).getAffinity() < clone.getAffinity()) {
                        cells.getAbCells().set(i, new Antibody(clone));
                        clone.setAffinity(-1);
                        break;
                    }
                }
            }
        }
    }

    // определение средней афинности
    private void calculateAverageAffinity() {
        for (Antibody antibody : cells.getAbCells()) {
            antibody.setAffinity(0);
            for (Antigen antigen : cells.getAgCells()) {
                double positionAddinity = Operations.calculatePositionAffinity(
                        antigen.getX(), antigen.getY(),
                        antibody.getX(), antibody.getY());
                antibody.setAffinity(antibody.getAffinity() + positionAddinity);
            }
            antibody.setAffinity(antibody.getAffinity() / cells.getAbCells().size());
        }
    }

    private void resetAbCloningFlag() {
        for (Antibody antibody : cells.getAbCells()) {
            antibody.setCloning(false);
        }
    }

    private void descSortingAbByAffinity() {
        for (int i = 0; i < cells.getAbCells().size(); i++) {
            for (int j = i; j < cells.getAbCells().size(); j++) {
                if (cells.getAbCells().get(i).getAffinity() < cells.getAbCells().get(j).getAffinity()) {
                    Antibody temporary = cells.getAbCells().get(i);
                    cells.getAbCells().set(i, cells.getAbCells().get(j));
                    cells.getAbCells().set(j, temporary);
                }
            }
        }
    }

    private void mutateClone(Antibody clone, double mutationCoefficient) {
        if (randomGenerator.nextInt() % 2 == 0) {
            clone.setX((int) (clone.getX() + mutationCoefficient * (1 / clone.getAffinity() - 1)));
        } else {
            clone.setX((int) (clone.getX() - mutationCoefficient * (1 / clone.getAffinity() - 1)));
        }
        if (randomGenerator.nextInt() % 2 == 0) {
            clone.setY((int) (clone.getY() + mutationCoefficient * (1 / clone.getAffinity() - 1)));
        } else {
            clone.setY((int) (clone.getY() - mutationCoefficient * (1 / clone.getAffinity() - 1)));
        }
    }
}

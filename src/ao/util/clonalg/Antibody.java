package ao.util.clonalg;

public class Antibody {

    private int x;
    private int y;
    private int id;
    private boolean hitting;    // признак восстановления антигена
    private boolean memCell;    // признак принадлежности к клеткам памяти
    private boolean cloning;    // признак прохождения первичного отбора для клонирования
    private double affinity;

    public Antibody() {
        reset();
    }

    public Antibody(Antibody clone) {
        this.id = clone.id;
        this.x = clone.x;
        this.y = clone.y;
        this.affinity = clone.affinity;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHitting() {
        return hitting;
    }

    public void setHitting(boolean hitting) {
        this.hitting = hitting;
    }

    public boolean isMemCell() {
        return memCell;
    }

    public void setMemCell(boolean memCell) {
        this.memCell = memCell;
    }

    public boolean isCloning() {
        return cloning;
    }

    public void setCloning(boolean cloning) {
        this.cloning = cloning;
    }

    public double getAffinity() {
        return affinity;
    }

    public void setAffinity(double affinity) {
        this.affinity = affinity;
    }

    private void reset() {
        x = y = id = -1;
        hitting = false;
        memCell = false;
        cloning = false;
        affinity = 0.0;
    }
}

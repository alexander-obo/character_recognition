package ao.util.clonalg;

public class Antigen {

    private int x;
    private int y;
    private int id;
    private boolean hitting;    // признак восстановления антителом

    public Antigen() {
        reset();
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

    private void reset() {
        x = y = id = -1;
        hitting = false;
    }
}

package ao.util.clonalg;

public class Operations {

    public static final double calculatePositionAffinity(int x1, int y1, int x2, int y2) {
        return 1.0 / (1.0 + Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
    }
}

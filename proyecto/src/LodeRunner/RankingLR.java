package LodeRunner;

import motor.GestorRankingBase;

public class RankingLR extends GestorRankingBase {
    private static final String RUTA = "ranking_loderunner.txt";

    public RankingLR() {
        super(RUTA);
    }
}

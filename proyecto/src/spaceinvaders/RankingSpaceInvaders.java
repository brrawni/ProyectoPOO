package spaceinvaders;

import motor.GestorRankingBase;

public class RankingSpaceInvaders extends GestorRankingBase {
    private static final String RUTA = "ranking_spaceinvaders.txt";

    public RankingSpaceInvaders() {
        super(RUTA);
    }
}

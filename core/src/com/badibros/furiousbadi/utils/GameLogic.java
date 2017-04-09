package com.badibros.furiousbadi.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by canozgen9 on 4/9/17.
 */

public class GameLogic {

    public static Map<Integer, String> gameLogic = new HashMap<Integer, String>();

    public GameLogic() {

    }

    public static boolean isMatched(String enemyType, int bulletType) {
        return gameLogic.get(bulletType).equals(enemyType);
    }

}

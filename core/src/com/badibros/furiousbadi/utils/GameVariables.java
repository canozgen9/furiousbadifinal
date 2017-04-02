package com.badibros.furiousbadi.utils;

public class GameVariables {

    //Aspect ratio
    public static final float ASPECT_RATIO= 16/9f;

    //Screen width and height
    public static final int WIDTH = 1024;
    public static final int HEIGHT = (int) (WIDTH / ASPECT_RATIO);

    //Collision bits
    public static final int BIT_MENUPLAYER = 1;
    public static final int BIT_MENUBUTTON = 2;
    public static final int BIT_MENUWALLS = 4;
    public static final int BIT_GAME_GROUND = 8;
    public static final int BIT_GAME_BULLET = 16;
    public static final int BIT_GAME_ENEMY = 32;
    public static final int BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR = 64;
    public static final int BIT_GAME_BOX = 128;
    public static final int BIT_GAME_PLAYER_BOTTOM_SENSOR = 256;

    //PPM Works
    public static final float PPM = 100;
    public static float scale(float value){
        return value/PPM;
    }


}

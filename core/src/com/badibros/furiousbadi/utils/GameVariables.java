package com.badibros.furiousbadi.utils;

public class GameVariables {

    //Screen width and height
    public static final int WIDTH = 1024;
    //Collision bits
    public static final int BIT_PLAYER = 1;
    public static final int BIT_MENUBUTTON = 2;
    public static final int BIT_MENUWALLS = 4;
    public static final int BIT_GAME_GROUND = 8;
    public static final int BIT_GAME_BULLET = 16;
    public static final int BIT_GAME_FIRING_ENEMY = 32;
    public static final int BIT_GAME_FIRING_ENEMY_PLAYER_DETECTION_SENSOR = 64;
    public static final int BIT_GAME_BOX = 128;
    public static final int BIT_GAME_PLAYER_BOTTOM_SENSOR = 256;
    public static final int BIT_GAME_COIN = 512;
    public static final int BIT_FINISH_AREA = 1024;
    public static final int BIT_GAME_ENEMY_BULLET = 2048;
    public static final int BIT_GAME_PLAYER_TOP_SENSOR = 4096;
    public static final int BIT_GAME_BABY_ENEMY = 8192;
    public static final int BIT_GAME_BABY_ENEMY_PLAYER_DETECTION_SENSOR = 16384;
    //PPM Works
    public static final float PPM = 100;
    //Aspect ratio
    private static final float ASPECT_RATIO = 16 / 9f;
    public static final int HEIGHT = (int) (WIDTH / ASPECT_RATIO);

    public static float scale(float value){
        return value/PPM;
    }


}

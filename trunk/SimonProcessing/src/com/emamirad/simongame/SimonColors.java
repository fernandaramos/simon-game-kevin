package com.emamirad.simongame;
enum SimonColors {
    YELLOW(255, 255, 0), 
    CYAN(0, 255, 255), 
    GREEN(0, 128, 0), 
    RED(255, 0, 0), 
    HOVER_YELLOW(255, 254, 170),
    HOVER_CYAN(209, 255, 255),
    HOVER_GREEN(203, 247, 202),
    HOVER_RED(255, 139, 139),
    NONE(0, 0, 0);
    private int r;
    private int g;
    private int b;
    private SimonColors(int r, int g, int b) {
	this.r = r;
	this.g = g;
	this.b = b;
    }
    public int getR() {
        return r;
    }
    public int getG() {
        return g;
    }
    public int getB() {
        return b;
    }    
}
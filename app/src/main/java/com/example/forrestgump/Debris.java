package com.example.forrestgump;

public class Debris {
    public int x;
    public int y;
    public int type;
    public boolean alive = true;
    public Debris(int x, int y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
    }
    public void die(){
        this.alive = false;
    }
}

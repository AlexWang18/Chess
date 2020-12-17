package domain.Logic;



public class Color {

    
    public enum ColorType{ 
        White, Black;
    }



    private ColorType c; //encapuslates constant type 

    public Color(ColorType c){
        this.c = c;
    }
    public ColorType getColor(){
        return this.c;
    }
    @Override
    public String toString(){ //unused
        if(c.equals(ColorType.White)){
            return "white";
        }
        return "black";
    }
}
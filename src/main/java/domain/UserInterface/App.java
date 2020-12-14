package domain.UserInterface;

import java.util.Arrays;
import java.util.Scanner;

import domain.Logic.Game;

/**
 * Hello world!
 * to do add ui and input
 */
public class App 
{
    /*
    public static void quickSort(int[] arr, int start, int end){

        if(start < end){//end case

            int point = getPartitition(arr, start, end);

            quickSort(arr, point+1, end);
            quickSort(arr, start, point-1);
        }
    }

    public static int getPartitition(int[] arr, int start, int end){
        int pivot = arr[end]; //set to arbitrary final index
        
        int index = start - 1; //starts one behind pointer
        int pointer = start;
        while(pointer<end){
            if(arr[pointer] < pivot){
                index++;
                swapNums(arr, index, pointer);
            }
            pointer++;
        }
        swapNums(arr, index+1, end);

        return index+1;
    }

    public static void swapNums(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }*/

    public static void main(String...strings)
    {

      /*  int[] myArray = {17, 4, 6, 3, 7, 5, 5, 0, 12};
        quickSort(myArray, 0, myArray.length-1);
        System.out.println(Arrays.toString(myArray)); */
        
        Game game = Game.getGame();
        
        UI ui = new UI(game, new Scanner(System.in));
        ui.showGreeting(); 
        
    }
}

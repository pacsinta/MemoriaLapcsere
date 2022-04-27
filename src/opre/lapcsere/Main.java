package opre.lapcsere;

import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in));

        String[] inBuff = reader.readLine().split(",");

        int[] inArray = new int[inBuff.length];
        for (int i = 0; i < inBuff.length; i++) {
            inArray[i] = Math.abs(Integer.parseInt(inBuff[i]));
        }

        Ram ram = new Ram();

        for (int j : inArray) {
            char res = ram.read(j);
            //System.out.println(j+":"+res);
            System.out.print(res);
            ram.tick();
        }

        System.out.println();
        System.out.println(ram.errorCount);
    }
}


class memoryCell{
    char name;
    boolean referenced;
    int value; // 0 = not initialized; 1-99
    int iced = 4;
    private static final int iceLength = 3;

    memoryCell(char name, int value){
        this.name = name;
        this.value = value;
        this.referenced = false;
    }

    void setValue(int value){
        this.value = value;
        iced = 0;
    }

    boolean isIced(){
        return iced <= iceLength;
    }
}

class Ram{
    int errorCount = 0;
    memoryCell[] cells = new memoryCell[3];
    Ram(){
        cells[0] = new memoryCell('A', 0);
        cells[1] = new memoryCell('B', 0);
        cells[2] = new memoryCell('C', 0);
    }

    char read(int value){
        /*for (memoryCell cell : cells) {
            System.out.print(cell.name);
        }
        System.out.println();*/
        for (memoryCell cell : cells) {
            if (cell.value == value) {
                cell.referenced = true;
                return '-';
            }
        }
        errorCount++;

        if (cells[0].isIced() && cells[1].isIced() && cells[2].isIced()) {
            return '*';
        }

        int icedIndex = 0;
        while(true){
            if(cells[icedIndex].referenced){
                cells[icedIndex].referenced = false;
                moveBackSwap(icedIndex);
            }else if(cells[icedIndex].isIced()){
                icedIndex++;
            }else{
                cells[icedIndex].setValue(value);
                moveBackSwap(icedIndex);
                return cells[2].name; // Az első már hátra mozgott
            }
        }
    }

    void tick(){
        for (memoryCell cell : cells) {
            cell.iced++;
        }
    }

    private void moveBackSwap(){
        memoryCell temp = cells[0];
        cells[0] = cells[1];
        cells[1] = cells[2];
        cells[2] = temp;
    }

    private void moveBackSwap(int from){
        if(from == 0){
            moveBackSwap();
        }else if(from == 1){
            memoryCell temp = cells[1];
            cells[1] = cells[2];
            cells[2] = temp;
        }
    }
}
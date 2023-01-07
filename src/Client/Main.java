package Client;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws IOException{
        Scanner sc = new Scanner(System.in);
        Client c = new Client("localhost", 4445);
        String[] strArr;
        Menu m = null;
        int op;
        try{
            while(true){
                String recv = c.getNext();
                strArr = recv.split(":");

                if("200".equals(strArr[0])){ //menu
                    m = new Menu(strArr[1].split("\n"));
                    op = m.run();
                    c.sendResponse(String.valueOf(op));
                }else if("201".equals(strArr[0])){
                    System.out.println("Digite os campos pedidos separados por virgula");
                    System.out.println(strArr[1]);
                    c.sendResponse(sc.nextLine());
                }else{
                    System.out.println(Arrays.toString(strArr));
                }

            }
        }
        catch(Exception e){
            e.printStackTrace();
            sc.close();
        }
    }
}

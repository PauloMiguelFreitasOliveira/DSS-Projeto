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
        boolean running = true;
        System.out.println("\n");
        try{
            while(running){
                String recv = c.getNext();
                //System.out.println(recv);
                strArr = recv.split(":");

                if("200".equals(strArr[0])){ //menu
                    m = new Menu(strArr[1].split("\n"));
                    op = m.run();
                    c.sendResponse(String.valueOf(op));
                    System.out.println("\n");
                }else if("201".equals(strArr[0])) {
                    System.out.println("Digite os campos pedidos separados por virgula");
                    System.out.println(strArr[1]);
                    c.sendResponse(sc.nextLine());
                    System.out.println("\n");
                }else if("202".equals(strArr[0])) {
                    System.out.println("Digite os campos pedidos separados por virgula");
                    System.out.println(strArr[1]);
                    c.sendResponse(sc.nextLine());
                    System.out.println("\n");
                }else if("203".equals(strArr[0])){
                    System.out.println(strArr[1]);
                    System.out.println("\n");
                }else if("205".equals(strArr[0])){
                    System.out.println(strArr[1]);
                    System.out.println("\n");
                }else if("300".equals(strArr[0])){
                    System.out.println("ERRO: "+strArr[1]);
                    System.in.read();
                    System.out.println("\n");

                }else if("500".equals(strArr[0])){
                    sc.close();
                    System.out.println("Exiting...");
                    running = false;
                    System.out.println("\n");
                }else if("100".equals(strArr[0])){
                    System.out.println(strArr[1]);
                    System.out.println("\n");
                }else if("101".equals(strArr[0])){
                    try {
                        System.out.println(strArr[1]);
                    }catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("Vazio");
                    }
                    //System.in.read();
                }else{
                    System.out.println(Arrays.toString(strArr));
                    System.out.println("\n");
                }

            }
        }
        catch(Exception e){
            System.out.println("Exiting...");
            sc.close();
        }
    }
}

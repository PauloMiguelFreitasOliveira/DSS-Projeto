package Client;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static Scanner is = new Scanner(System.in);
    private List<String> opcoes;


    public Menu(String[] opcoes) {
        this.opcoes = Arrays.asList(opcoes);
    }

    public int run() {
        while(true){
            showMenu();
            int op = read();
            if (op != -1){
                return op;
            }
        }
    }

    private void showMenu() {
        //System.out.println("\n *** Menu *** ");
        for (int i=0; i<this.opcoes.size(); i++) {
            System.out.print(i+1);
            System.out.print(" - ");
            System.out.println(this.opcoes.get(i));
        }
        System.out.println("0 - Sair");
    }

    private int read() {
        int op;
        System.out.print("Opção: ");
        try {
            op = Integer.parseInt(is.nextLine());
        }
        catch (Exception e) {
            op = -1;
        }
        if (op<0 || op>this.opcoes.size()) {
            System.out.println("Opção Inválida!!!");
            op = -1;
        }
        return op;
    }
}
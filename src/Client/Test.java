package Client;

public class Test {
    public static void main(String args[]){
        String str = "abcd";
        String[] tokens = str.split(",");  
        for(String s : tokens){
            System.out.println(s);
        }
    }
}

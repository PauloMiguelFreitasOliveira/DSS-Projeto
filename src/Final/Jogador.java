package Final;
import java.util.*;

public class Jogador extends User{
    public List<String> amigos;

    public Jogador(String nome, String email, String password){
        super(nome,email,password);
        this.amigos = new ArrayList<>();
    }

    public ArrayList<String> getAmigos() {
        return new ArrayList<>(amigos);
    }

    public void addAmigo(String email){
        this.amigos.add(email);
    }
}

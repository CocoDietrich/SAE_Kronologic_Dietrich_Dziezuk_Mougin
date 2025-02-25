package Kronologic.MVC;

import javafx.scene.text.Text;
import java.util.List;

public class TextCase extends Text {

    private String info;
    private String etat;

    public TextCase(String text) {
        super(text);
        this.info = "";
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setEtat(String etat){
        List<String> etatValide = List.of("présent", "absent", "neutre");
        if (etatValide.contains(etat)){
            this.etat = etat;
        }
    }

    public String getInfo() {
        return this.info;
    }

    public String getEtat() {
        return this.etat;
    }
}

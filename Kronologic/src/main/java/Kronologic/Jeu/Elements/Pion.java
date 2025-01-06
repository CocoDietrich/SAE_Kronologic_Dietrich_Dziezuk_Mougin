package Kronologic.Jeu.Elements;

import javafx.scene.image.ImageView;

public class Pion extends ImageView  {

    private Note note;
    private int x;
    private int y;

    public Pion(Note note, String image) {
        super(image);
        this.note = note;
        this.x = -1;
        this.y = -1;
    }

    public void deplacerPion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public void setImageView(String image) {
        this.setImage(new javafx.scene.image.Image(image));
    }
}

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
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Les valeurs de x et y doivent être positives");
        }
        setXPion(x);
        setYPion(y);
    }

    public boolean equals(Object o) {
        if (o instanceof Pion p) {
            if (((this.getNote() == null) && (p.getNote() != null)) || ((this.getNote() != null) && (p.getNote() == null))) {
                return false;
            }
            if (this.getNote() == null && p.getNote() == null) {
                return this.getImage().getUrl().equals(p.getImage().getUrl());
            }
            return this.getImage().getUrl().equals(p.getImage().getUrl())
                    && this.getNote().equals(p.getNote());
        }
        return false;
    }

    public Note getNote() {
        return note;
    }

    public int getXPion() {
        return x;
    }

    public int getYPion() {
        return y;
    }

    public void setXPion(int x) {
        this.x = x;
    }

    public void setYPion(int y) {
        this.y = y;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public void setImageView(String image) {
        this.setImage(new javafx.scene.image.Image(image));
    }
}

package sample;

import javafx.scene.control.Label;

//Klasa reprezentuje nawias wąsaty otwierający zbiór
public class SetLabel extends Label {
    private Set set;

    public SetLabel(){
        set = new Set();
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public Set getSet() {
        return set;
    }

    public void add(Object object){
        this.set.add(object);
    }

    public void remove(Object object){
        this.set.remove(object);
    }
}

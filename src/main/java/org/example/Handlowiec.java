package org.example;
public class Handlowiec extends Pracownik {
    private String telefonSluzbowy;
    private int prowizja;
    private double limitKoszt;

    public Handlowiec(String pesel,String imie,String nazwisko,String stanowisko,double wynagrodzenie,String telefonSluzbowy,int prowizja,double limitKoszt){
        super(pesel,imie,nazwisko,"Handlowiec",wynagrodzenie);
        this.telefonSluzbowy = telefonSluzbowy;
        this.prowizja = prowizja;
        this.limitKoszt = limitKoszt;
    }
    public String toString(){
        return super.toString() +
                "\n" +"TelefonSlużbowy: " + telefonSluzbowy +
                "\n" +"Prowizja: " + prowizja +
                "\n" + "LimitKoszt: " + limitKoszt +"\n" ;
    }
    public String getTelefonSluzbowy() {
        return telefonSluzbowy;
    }

    public int getProwizja() {
        return prowizja;
    }

    public double getLimitKoszt() {
        return limitKoszt;
    }
}


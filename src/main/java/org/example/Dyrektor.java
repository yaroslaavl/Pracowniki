package org.example;

public class Dyrektor extends Pracownik {
    private String telefonSluzbowy;
    private double dodatekSluzbowy;
    private String kartaSluzbowa;
    private double limitKoszt;
    public Dyrektor(String pesel, String imie, String nazwisko, String stanowisko, double wynagrodzenie, String telefonSluzbowy, double dodatekSlużbowy, String kartaSlużbowa, double limitKoszt){
        super(pesel,imie,nazwisko,"Dyrektor",wynagrodzenie);
        this.telefonSluzbowy = telefonSluzbowy;
        this.dodatekSluzbowy = dodatekSlużbowy;
        this.kartaSluzbowa = kartaSlużbowa;
        this.limitKoszt = limitKoszt;
    }



    public String toString(){
        return super.toString() +
                "\n" +"TelefonSlużbowy: " + telefonSluzbowy +
                "\n" +"DodatekSlużbowy: " + dodatekSluzbowy +
                "\n" +"KartaSlużbowa: " + kartaSluzbowa +
                "\n" +"LimitKoszt: " + limitKoszt+"\n" ;
    }
    public String getTelefonSluzbowy() {
        return telefonSluzbowy;
    }

    public double getDodatekSluzbowy() {
        return dodatekSluzbowy;
    }

    public String getKartaSluzbowa() {
        return kartaSluzbowa;
    }

    public double getLimitKoszt() {
        return limitKoszt;
    }

}

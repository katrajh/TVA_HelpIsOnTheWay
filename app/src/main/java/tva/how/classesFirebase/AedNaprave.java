package tva.how.classesFirebase;

public class AedNaprave {

    // atributi

    String lokacija;
    String drzava;
    String ulica;
    String kraj;
    String longitude;
    String latitude;
    String aed_znamka;
    String opis;
    String naziv_objekta;
    String slika;
    String uporabnik_id;

    // konstruktor
    public AedNaprave() {
    }

    // geterji in seterji

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAed_znamka() {
        return aed_znamka;
    }

    public void setAed_znamka(String aed_znamka) {
        this.aed_znamka = aed_znamka;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getNaziv_objekta() {
        return naziv_objekta;
    }

    public void setNaziv_objekta(String naziv_objekta) {
        this.naziv_objekta = naziv_objekta;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getUporabnik_id() {
        return uporabnik_id;
    }

    public void setUporabnik_id(String uporabnik_id) {
        this.uporabnik_id = uporabnik_id;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }
}

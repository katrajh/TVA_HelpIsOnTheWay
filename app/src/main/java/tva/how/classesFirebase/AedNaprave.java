package tva.how.classesFirebase;

public class AedNaprave {

    // atributi

    String id_aed;
    String drzava;
    String lokacija;
    String kraj;
    String postna_stevilka;
    Double latitude;
    Double longitude;
    String aed_znamka;
    String opis_lokacije;
    String naziv_objekta;
    String slika;
    String telefonska_stevilka;
    String uporabnik_id;

    // konstruktor
    public AedNaprave() {
    }

    // geterji in seterji

    public String getId_aed() {
        return id_aed;
    }

    public void setId_aed(String id_aed) {
        this.id_aed = id_aed;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAed_znamka() {
        return aed_znamka;
    }

    public void setAed_znamka(String aed_znamka) {
        this.aed_znamka = aed_znamka;
    }

    public String getOpis_lokacije() {
        return opis_lokacije;
    }

    public void setOpis_lokacije(String opis_lokacije) {
        this.opis_lokacije = opis_lokacije;
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

    public String getTelefonska_stevilka() {
        return telefonska_stevilka;
    }

    public void setTelefonska_stevilka(String telefonska_stevilka) {
        this.telefonska_stevilka = telefonska_stevilka;
    }

    public String getPostna_stevilka() {
        return postna_stevilka;
    }

    public void setPostna_stevilka(String postna_stevilka) {
        this.postna_stevilka = postna_stevilka;
    }
}

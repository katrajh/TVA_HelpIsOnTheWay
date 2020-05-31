package tva.how.classesFirebase;

public class Novice {

    String id_novice;
    String naslov_novice;
    String kraj;
    String datum_novice;
    String vsebina_novice;
    String status_novice;   // splošno, zdravstvo, požarna varnost
    int prikaz;

    public Novice() {
    }

    public String getId_novice() {
        return id_novice;
    }

    public void setId_novice(String id_novice) {
        this.id_novice = id_novice;
    }

    public String getNaslov_novice() {
        return naslov_novice;
    }

    public void setNaslov_novice(String naslov_novice) {
        this.naslov_novice = naslov_novice;
    }

    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }

    public String getVsebina_novice() {
        return vsebina_novice;
    }

    public void setVsebina_novice(String vsebina_novice) {
        this.vsebina_novice = vsebina_novice;
    }

    public String getStatus_novice() {
        return status_novice;
    }

    public void setStatus_novice(String status_novice) {
        this.status_novice = status_novice;
    }

    public int getPrikaz() {
        return prikaz;
    }

    public void setPrikaz(int prikaz) {
        this.prikaz = prikaz;
    }

    public String getDatum_novice() {
        return datum_novice;
    }

    public void setDatum_novice(String datum_novice) {
        this.datum_novice = datum_novice;
    }
}

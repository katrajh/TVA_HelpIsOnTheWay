# TVA_HelpIsOnTheWay
Projekt pri predmetu TVA z naslovom Help is on the way. 

# Izdelava projektne dokumentacije
* ER diagram podatkovne baze

UPORABNIKI
  - ID [INT]
  - IME [VARCHAR(100)]
  - PRIIMEK [VARCHAR(100)]
  - STATUS [INT] (0 - admin, 1 - administrator, 2 - navaden uporabnik) 
  
NOVICE
  - ID [INT]
  - NASLOV [VARCHAR(250)]
  - KLJUCNE_BESEDE [VARCHAR(250)]
  - VSEBINA [VARCHAR(2500)]
  - STATUS_NOVICE [INT] (0 - splošno, 1 - zdravstvo, 2 - požarna varnost, ...)
  - PRIKAZ [INT] (0 - skrita novica, 1 - prikazana novica)
  - UPORABNIKI_ID TK [INT]
  
AED_NAPRAVE
  - ID [INT]
  - REGIJA_ID [INT]
  - REGIJA_NAZIV [VARCHAR(100)]
  - KRAJ_ID [INT]
  - KRAJ_NAZIV [VARCHAR(100)]
  - LOKACIJA [VARCHAR(250)]
  - OPIS [VARCHAR(1500)]
  - SLIKA [VARCHAR(500)]
  - UPORABNIKI_ID TK [INT]
  
ZDRAVSTVENI_DOMOVI
  - ID [INT]
  - REGIJA_ID [INT]
  - REGIJA_NAZIV [VARCHAR(100)]
  - KRAJ_ID [INT]
  - KRAJ_NAZIV [VARCHAR(100)]
  - LOKACIJA [VARCHAR(250)]
  - ZD_NAZIV [VARCHAR(250)]

BOLNISNICE
  - ID [INT]
  - REGIJA_ID [INT]
  - REGIJA_NAZIV [VARCHAR(100)]
  - KRAJ_ID [INT]
  - KRAJ_NAZIV [VARCHAR(100)]
  - LOKACIJA [VARCHAR(250)]
  - BOLNISNICA_NAZIV [VARCHAR(250)]
  
* Use-case diagram
* Mock up (zaslonske slike prototipne aplikacije)

  # Glavne funkcionalnosti:
  
UPORABNIKI 
  1. Dodajanje uporabnikov 
  2. Urejanje uporabnikov 
  3. Brisanje uporabnikov 

NOVICE 
  4. Dodajanje novic
  5. Urejanje novic
  6. Brisanje novic 
  7. Prikaz novic 

AED
  8. Dodajanje AED naprav 
  9. Urejanje AED naprav
  10. Brisanje AED naprav
  11. Prikaz AED naprav 

ZDRAVSTVENI DOMOVI 
  12. Prikaz zdravstvenih domov 

BOLNIŠNICE
  13. Prikaz bolnišnic 
  14. Učenjak prve pomoči 
  *

# TVA_HelpIsOnTheWay
Projekt pri predmetu TVA z naslovom Help is on the way. 

# Izdelava projektne dokumentacije
* ER diagram podatkovne baze
UPORABNIKI
  - ID 
  - IME
  - PRIIMEK 
  - STATUS (0 - admin, 1 - administrator, 2 - navaden uporabnik) 
  
NOVICE
  - ID 
  - NASLOV 
  - KLJUCNE_BESEDE 
  - VSEBINA 
  - STATUS_NOVICE (0 - splošno, 1 - zdravstvo, 2 - požarna varnost, ...)
  - PRIKAZ (0 - skrita novica, 1 - prikazana novica)
  - UPORABNIKI_ID TK 
  
AED_NAPRAVE
  - ID 
  - REGIJA_ID
  - REGIJA_NAZIV
  - KRAJ_ID
  - KRAJ_NAZIV
  - LOKACIJA 
  - OPIS
  - SLIKA 
  - UPORABNIKI_ID TK
  
ZDRAVSTVENI_DOMOVI
  - ID
  - REGIJA_ID
  - REGIJA_NAZIV
  - KRAJ_ID
  - KRAJ_NAZIV
  - LOKACIJA 
  - ZD_NAZIV

BOLNISNICE
  - ID
  - REGIJA_ID
  - REGIJA_NAZIV
  - KRAJ_ID
  - KRAJ_NAZIV
  - LOKACIJA 
  - BOLNISNICA_NAZIV
  
* Use-case diagram
* Mock up (zaslonske slike prototipne aplikacije)

  # Glavne funkcionalnosti:
  
  *

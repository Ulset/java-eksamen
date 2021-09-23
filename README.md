# Eksamen - Thomas J. Sourianos && Sander Ulset

NOTE TIL FORELESER:
Vi er naboer og bestekompiser, så veldig mye av denne koden er skrevet mens vi har sittet ved siden av hverandre - derfor er det veldig store commits av gangen.
Men vi har fulgt prinsippene rundt par-programmering, og jobbet sammen på all koden :)

![Java CI with Maven](https://github.com/kristiania/pgr203innevering3-Ulset/workflows/Java%20CI%20with%20Maven/badge.svg)

![db_diagram](https://user-images.githubusercontent.com/36825493/98446917-6958d980-2121-11eb-84f0-f176edaea706.png)

![serverStructure](https://user-images.githubusercontent.com/36825493/98536078-7a235f80-2287-11eb-96db-2e149c052149.png)

![Klassediagram](https://user-images.githubusercontent.com/33099664/98552025-49025980-229e-11eb-98d3-714916f0581e.png)

# Arbeidsmetodikk
Vi har skrevet ca 80% av koden fysisk i samme rom som gjorde det mye lettere å jobbe sammen, siden vi fant ut at å dele skjerm over discord var en tungvindt måte å jobbe på.
Vi har jobbet mye sammen fysisk sammen ettersom vi følte at dette gikk fint, siden vi er gode venner og treffes regelmessig uansett. Vi har forsøkt å fordele arbeidet så jevnt som overhodet mulig, så vi begge skulle få muligheten til å lære så mye som mulig.

# Funskjonalitet
Siden lar en bruker
- Legge til medlemmer med navn og email
- Legge til oppgaver med navn og status
- Tildele oppgaver til medlemmer
- Endre status på tasks
- Filtrere medlemmer etter start på navn
- Redigere navn på eksisterende medlemmer


# Hvordan kjøre programmet
***Du må ha internetttilgang for at index.html skal fungere siden denne bruker jQuery til post requests***
1. Kjør "mvn package" for å generere en .jar fil
2. Legg denne .jar filen i mappe med en fil med navn pgr203.properties som må inneholde variabler for dataSource.url, dataSource.username og dataSource.password. Disse verdeiene burde peke programmet mot en tom database.
3. Naviger til samme mappe med CMD/Terminal og kjør programmet med "java -jar FILNAVN.jar"
4. Åpne nettleseren din og skriv in "localhost:8080" eller "localhost:8080/index.html" for å komme til startsiden.

# Hva vi syntes har gjort bra // Forbedringspotensialer
Vi syntes generelt strukturen på koden og hvordan vi håndterer request i en separat klasse er et stort +, testene våre som bruker task eller members er også 100% tilfeldige med navn,status, email og lignende så det ikke skal kunne oppstå noen tester som funker fordi data allerede ligger i databasen. Vi syntes også vi har fått med mye av det avanserte funskjonaliteten som sto i oppgaveteksten. Vi er også veldig fornøyd med måten vi valgte å håndtere responsen tilbake til klient, her brukes vi en egen HttpResponse klasse som automatisk genererer headers og sender filer hvis det er nødvendig. Hvis det er noe som vi kanskje skulle gjort bedre hadde det vært QueryString klassen vår, den er avhengig av å få data inn i constuctoren som en http query, altså man må fylle ut med '/justfill?' før data fra feks body for at den skal fungere. Vi har også en bug med tasks, hvor noenganger når du endrer status så kommer det opp en enkel '</' i lista, vi vet ikke helt hvorfor men tror kanskje det har noe med encodingen til http responsen.

Vi synes også at samarbeidet har fungert veldig godt. Det har vært perioder der en av oss har "falt" litt ut, eller har slitt med å forstå, og da har vi alltid tatt to steg tilbake sammen og gått igjennom all koden. Dette har ført til en økt forståelse for faget og Java generelt, ettersom det å analysere koden man skriver er en viktig del av det å lære seg programmering. 

# Link til parprogrammeringsvideo
https://www.youtube.com/watch?v=Ffjxx2y0zZ0&feature=youtu.be

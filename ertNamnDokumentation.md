# Ditt namn
Vikram Singh
## Egna reflektioner
Jag tyckte uppgiften var väldigt givande samt en bra avslutande uppgift som omfattar allt vi lärt oss. Man märker verkligen av
hur mycket man har utvecklats genom terminens gång. Märkte dock att de var svårare att göra ett projekt själv då man inte kunde bolla idéer
med andra.

## Projektet
ToDo-list
### Beskrivning av projektet
Projektet innefattar en todo-list där man kan skapa, hämta, uppdatera och radera tasks. Projektet bygger på användning av CRUD, mongodb, mocko och JUnit tester med TDD.

### Vad du har gjort
Jag har skapat User klass och ToDo klass samt fasad klasserna för båda dessa klasser för koppla till mongodb. Jag har sedan 
använt ovanstånde i en menu klass för att få en fungerande todolist med CRUD. Jag har även testat metoderna med mocko och JUnit tester med TDD.

## Planering

### Lösningsförslag innan uppgiften påbörjas

#### Skisser (exempelvis)

#### Hur du tänker försöka lösa uppgiften.(exempelvis)

#### Pseudokod.(exempelvis)
Skapa User klass. Skapa ToDO klass. Skapa fasad för User och Todo klasserna. Skapa menu klass.
#### Diagram.(exempelvis)

### Jira/Trello/Github Project och projekthantering enligt Scrum/Kanban

## Arbetet och dess genomförande
### Vad som varit svårt
Det svåraste skulle jag säga var mockning och komma på tester.

### Beskriv lite olika lösningar du gjort
Den främsta lösning var hur jag skulle ordna testning av metoden findAll. Jag höll på med den i flera timmar och även
chatgbt kunde inte ordna det. Jag gick sedan på ett toabesök där jag fick idé där att kanske lägga alla task i en array 
och sedan skriva ut dem och lägga i en ny array för att sedan testa assertEqualsArray för att så som man fick med alla task och user.
Utifrån att jag hade två fasader för mongodb med väldigt liknande metoder la jag fokus på att fixa ena klassen samt testerna för enbart en klass 
för att sedan koperia av allt och ändra för att anpassa till de andra fasaden.

### Beskriv något som var besvärligt att få till
Jag hade lite svårigheter med testerna. Det var främst testning av findAll, när man skulle skriva ut alla som jag fastnade 
i flera timmar. Försökte även ta hjälp av chatgbt men fick felmeddelande varje gång. 

### Beskriv om du fått byta lösning och varför i sådana fall
Jag ändrade många metoder från public till private som egentligen borde vara private. Jag brukar inte tänka på det annars
men nu när jag vet att man helst skall testa alla  metoder som är public valde jag att ändra de som gick till private.

## Reflektion & Slutsatser
### Vad gick bra
Jag kände att mongodb, CRUD samt skapandet av menyn kändes mycket enklare än vad de varit tidigare.

### Vad gick dåligt
För många timmar gick till att komma på tester men i efterhand kändes de skönt att man lyckats lösa det.

### Vad har du lärt dig
Jag har blivit bättre på mock och JUnit med testning av TDD. Känner mig också mer bekväm inte att använda mig av
mongodb och CRUD. Jag har även lärt mig Github Actions.

### Vad hade ni gjort annorlunda om ni gjort om projektet
Hade nog skapat en egen klass med dbhandler.

### Vilka möjligheter ser du med de kunskaper du fått under kursen.
Jag ser möjligheter att under sommaren kanske satsa på att göra ett eget projekt med allt jag lärt mig.

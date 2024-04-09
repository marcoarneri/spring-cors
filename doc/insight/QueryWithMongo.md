# Guida alla MongoDB
Le query MongoDB sono comandi utilizzati per interagire con un database non relazionale al fine di recuperare, modificare, inserire o eliminare dati. Le query MongoDB seguono una sintassi specifica e possono essere utilizzate per eseguire una varietà di operazioni sui dati del database NoSQL.

## Passaggi
Segui questi passaggi per integrare MongoRepository nel tuo progetto Spring Boot:

### 1. Aggiunta delle Dipendenze Necessarie

- Per iniziare, aggiungi la seguente dipendenza al tuo file pom.xml per integrare PostgresSQL nel tuo progetto:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```
***

### 2. Connessione al db

- Per potersi connettere ad un db Mongo per prima cosa sostituire l'immagine docker di posgress, o del db precedentemente utilizzato, con quella di Mongo allintenro del tuo [docker-compose](..%2F..%2Fdocker-compose.yaml).

 - Come prossimo  passo sostituire il tuo [application.properties](..%2F..%2Fsrc%2Fmain%2Fresources%2Fapplication.properties) di conseguenza al docker precedentemente modificato.
```properties
spring.data.mongodb.uri=mongodb://user:pass@localhost:27017/mydatabase?authSource=admin

spring.data.mongodb.uri.esempio=mongodb://(nomeUser):(password)@localhost:27017/(nomeDb)?(authSource=admin) // per fornirti le autorizzazioni da admin del db
```
- (Opzionale) Inserire l'uri all'interno di una gui per vedere il contenuto del db come ad esempio MongoDB Compass 
***

### 3. Creazione delle Entity

Le entity in MongoDB sono rappresentate da classi Java che corrispondono direttamente ai documenti nel database.

- Utilizza l'annotazione `@Document` per definire un'entity MongoDB. Questo indica a Spring Data MongoDB di trattare la classe come una collezione nel database.


- Assicurati che ogni entity abbia un campo che funge da chiave univoca. In MongoDB, questo campo è comunemente denominato `_id`. Puoi usarlo come campo nella tua classe e annotarlo con `@Id`.


- Gli attributi della tua classe rappresentano i campi dei documenti nel database. Questi possono essere di qualsiasi tipo supportato da MongoDB, come stringhe, numeri, elenchi e così via.

- Puoi utilizzare varie annotazioni fornite da Spring Data MongoDB per personalizzare il mapping dei dati. Ad esempio, puoi utilizzare `@Field` per specificare un nome diverso per il campo nel database rispetto al nome dell'attributo nella classe.

Ecco un esempio di come potrebbe apparire un'entità MongoDB: [DemoEntity.java](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Frepository%2Fmodel%2FDemoEntity.java)
***

### 4. Creazione del Repository

Il repository è un'interfaccia che estende `MongoRepository` e fornisce metodi per interagire con il database.

- Crea un'interfaccia repository che estende `MongoRepository<Entity, ID>`, es. [DemoRepository](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Frepository%2FDemoRepository.java), dove Entity è il tipo dell'entità e ID è il tipo della chiave primaria,devi estendere Serializable, poiché in MongoDB l'ID di default è di tipo ObjectId, che è un wrapper di Serializable.


- All'interno dell'interfaccia repository, puoi definire metodi per eseguire operazioni di base come il recupero, l'inserimento, l'aggiornamento e l'eliminazione delle entità nel database. Spring Data MongoDB offre una vasta gamma di metodi predefiniti che coprono molte operazioni comuni.
***

### 4. Scrivere Query con MongoRepository

### Query Metodi Derivati:

MongoRepository supporta la creazione di query mediante metodi derivati dal nome del metodo. È possibile definire metodi nel repository utilizzando la convenzione di denominazione dei metodi per specificare le condizioni di ricerca.

```java
    DemoEntity findByIuv(String iuv);
```

### Query JSON:
MongoRepository consente di definire query utilizzando il formato JSON per specificare i criteri di ricerca. L'annotazione `@Query` viene utilizzata per definire una query MongoDB direttamente nel metodo del repository. Questo approccio è utile per le query più complesse o quando si desidera una maggiore flessibilità nella definizione dei criteri di ricerca.

```java
  @Query("{'iuv' : ?0, 'city' : {$regex : ?1, $options : 'i'}}")
  List<DemoPOJO> findByIuvAndCityLike(String iuv, String city);

```
Nella query d'esempio ci sono alcuni operatori JSON utilizzati per definire i criteri di ricerca:

1. **$regex:**

   Questo operatore viene utilizzato per eseguire una ricerca basata su espressioni regolari. Accetta due parametri: il pattern regex da cercare e le opzioni.

2. **$options:**

   Questo parametro opzionale specifica le opzioni per l'operatore di ricerca $regex. Nell'esempio, 'i' indica che la ricerca sarà case-insensitive, quindi non farà distinzione tra maiuscole e minuscole.

3. **?0, ?1:**

   Questi sono parametri posizionali nella query. Quando la query viene eseguita, i valori forniti come argomenti nei metodi del repository verranno sostituiti in questi punti. `?0` rappresenta il primo parametro, `?1` rappresenta il secondo parametro e così via.

### Query personalizzate con Aggregation:

  L'aggregazione in MongoDB è un framework di elaborazione dei dati che consente di eseguire operazioni di query avanzate sui dati nel database. Consente di eseguire operazioni come raggruppamento, proiezione, ordinamento, filtraggio e trasformazione dei dati.


  Ecco alcuni concetti chiave relativi all'aggregazione in MongoDB:

1. Pipeline di aggregazione: 

    Le operazioni di aggregazione in MongoDB sono definite tramite un pipeline di operazioni. Ogni operazione nel pipeline accetta i risultati dell'operazione precedente e li elabora ulteriormente. Le operazioni nel pipeline possono includere `$match`, `$group`, `$sort`, `$limit`, `$skip`, `$unwind` e molte altre.

2. Operazioni di aggregazione comuni:
- **$match:** In MongoDB è un operatore di aggregazione che filtra i documenti in base a determinati criteri di ricerca. È simile alla clausola `WHERE` nelle query SQL.
- **$group:** Raggruppa i documenti in base a determinati campi e calcola aggregazioni su di essi.
- **$sort:** Ordina i documenti in base a un campo specificato.
- **$limit:** Limita il numero di documenti restituiti.
- **$skip:** Salta un certo numero di documenti nel risultato.
- **$unwind** De-normalizza i campi di array, producendo un documento separato per ogni elemento dell'array.


Gli operatori sopra elencati sono solo alcuni. Per un ulteriore approfondimento degli operatori puoi consultare questa documentazione [https://www.mongodb.com/docs/manual/reference/operator/](https://www.mongodb.com/docs/manual/reference/operator/)

### Utilizzo di Aggregation nelle query personalizzate:

Ecco un esempio di come utilizzare l'aggregazione in una query personalizzata con Spring Data MongoDB:

```java
    @Query("{$match: {city: ?0}}, {$group: {_id: '$city', count: {$sum: 1}}")
    List<CityCount> countByCity(String city);
```
In questo esempio, stiamo eseguendo un'aggregazione per contare il numero di documenti per ogni città. Utilizziamo `$match` per filtrare i documenti basati sulla città specificata e `$group` per raggrupparli per città e calcolarne il totale.

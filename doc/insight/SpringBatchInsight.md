# Spring Batch Insight

In questa guida andremo ad approfondire, aggiungendo altri componenti al progetto precedente, per aggiungere delle funzionalità di Spring Batch

## Passaggi

Segui questi passaggi per aggiungere i nuovi componenti al tuo progetto Spring Boot:

### 1. Skip degli errori


Per poter fare lo skip degli errori e la memorizzazione del record saltato all'interno del nostro job, cosi da evitare il blocco dello stesso e tener traccia dei record errati, possiamo aggiungere dei metodi di Spring Batch all'interno dei nostri step, come nella configurazione del nostro step `startStep`.

- `.faultTolerant()`: Questo metodo indica che lo step deve essere configurato per gestire errori e skip in modo "fault-tolerant", ovvero in grado di tollerare eventuali problemi senza interrompere completamente l'esecuzione del job.

- `.skipPolicy()`: Questo metodo definisce le condizioni sotto le quali uno skip deve essere eseguito per un dato elemento durante l'elaborazione all'interno dello step.

- `.listener(skipListener)` : Questo metodo imposta un listener per lo step, che sarà responsabile di gestire gli eventi di skip e altri eventi correlati. Il parametro `skipListener` è un component da noi creato ([SkippedRecordListenerConf.java](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fbatchprocessing%2FSkippedRecordListenerConf.java)) che implementa l'interfaccia `SkipListener`, il quale ci permette di fare `@Override` dei metodi presenti all'iterno dell'interfaccia implementata così da poter personalizzare i metodi da chiamare per la memorizzazione di record quando si verifica uno skip di un elemento.

### 2. Step per gestione errori

Per aggiungere uno step allo stesso job , nel nostro caso, basta implemetentare un secondo step `errorHandlingStep` e nel job aggiungere il metodo `.next()`

#### Scrittura dei record skippati su file

Per poter gestire questa casistica sarà necessario creare una configurazione personalizzata di un `FlatFileItemWriter` ([ErrorWriterConfiguration.java](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fbatchprocessing%2Fconfig%2FErrorWriterConfiguration.java)).
All'interno di questa classe andiamo a gestire la creazione della directory(error) e del file(error.csv) sulla quale andremo a scrivere i record skippati e definire il compomportamente del nostro writer; abbiamo impostato i nomi dei nostri header il formato di scrittura ed altri parametri neccessari per il corretto funzionamento del writer



















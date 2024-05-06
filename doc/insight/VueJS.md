# Vue.js
## Cos'è
Vue è un framework JavaScript per la costruzione di interfacce utente. Si basa su standard HTML, CSS e JavaScript e fornisce un modello di programmazione dichiarativo e basato su componenti.  
Vue.js è un framework JavaScript front-end progressivo e leggero, utilizzato per creare interfacce utente e applicazioni web a pagina singola ad alte prestazioni.  

Vue.js è un framework open source lanciato nel 2016, che eredita molti dei suoi concetti da React e Angular.
Infatti ha un data binding bidirezionale come Angular, e fa uso di DOM virtuali ed è basato su componenti proprio come React.

## Passaggi per creare un progetto Vue.js da terminale

Segui questi passaggi per utilizzare Vue.js nel tuo progetto:

### 1. Assicurati di avere Node.js installato sul tuo sistema
### 2. Create-vue:

- Apri il terminale dalla cartella di lavoro in cui vuoi creare il progetto
- Esegui il seguente comando nella tua linea di comando

```shell
> npm create vue@latest
```

- Questo comando installerà ed eseguirà `create-vue`, lo strumento ufficiale per la creazione di un progetto Vue. Ti verranno presentate delle richieste per diverse funzionalità opzionali come il supporto a TypeScript e ai test:

```shell
✔ Project name: … <il-nome-del-tuo-progetto>
✔ Add TypeScript? … No / Yes
✔ Add JSX Support? … No / Yes
✔ Add Vue Router for Single Page Application development? … No / Yes
✔ Add Pinia for state management? … No / Yes
✔ Add Vitest for Unit testing? … No / Yes
✔ Add an End-to-End Testing Solution? … No / Cypress / Playwright
✔ Add ESLint for code quality? … No / Yes
✔ Add Prettier for code formatting? … No / Yes

Scaffolding project in ./<il-nome-del-tuo-progetto>...
Done.
```

- Una volta creato il progetto, segui le istruzioni per installare le dipendenze e avviare il server di sviluppo:

```shell
> cd <il-nome-del-tuo-progetto>
> npm install
> npm run dev
```

- Quando sei pronto per rilasciare la tua app in produzione, esegui il seguente comando:

```shell
> npm run build
```

***

## Caratteristiche principali di Vue.js

Alcune delle caratteristiche principali di Vue.js includono:

- **Component-based architecture:** Vue.js è basato su componenti, che consentono di creare interfacce utente riutilizzabili e modulari.
- **Reactivity**: Vue.js offre un sistema reattivo che consente di aggiornare automaticamente la UI quando cambia lo stato dell'applicazione.
- **Directives and Templates:** Vue.js fornisce direttive e template intuitivi per la creazione di interfacce utente dinamiche e reattive.
- **Virtual DOM:** Vue.js utilizza un DOM virtuale per migliorare le prestazioni e l'efficienza nell'aggiornamento della UI.
- **Routing e State Management:** Vue.js offre soluzioni integrate per il routing e la gestione dello stato dell'applicazione attraverso Vue Router e Vuex.

***

## Componenti Single-File
I file `.vue` sono utilizzati in Vue.js per definire i componenti dell'interfaccia utente.  
La particolarità dei file .vue è che consentono di combinare HTML, CSS e JavaScript in un singolo file, organizzato in sezioni chiaramente definite.

La struttura di base di un file .vue comprende tre sezioni principali:
- `<template>`:  Questa sezione contiene il markup HTML del componente. È dove definisci la struttura dell'interfaccia utente utilizzando elementi HTML, direttive Vue e interpolazioni dei dati. La sezione *template* è obbligatoria in un file vue.
- `<script>`: Questa sezione contiene il codice JavaScript del componente. Qui definisci la logica del componente, come i dati del componente, i metodi e altre proprietà del componente.
- `<style>`:  Questa sezione contiene il CSS del componente. È dove definisci lo stile del componente, come i colori, le dimensioni, i margini, i padding, i font e altri attributi di stile.

***

## Funzionamento
1. **Creazione dei componenti:** Puoi creare nuovi componenti Vue.js utilizzando la sintassi dei file .vue che include le sezioni `<template>, <script>, e <style>`. Queste sezioni definiscono rispettivamente il markup HTML del componente, la logica JavaScript e lo stile CSS. Ad esempio, puoi creare un file `MyComponent.vue` che contiene il template, lo script e lo stile del tuo componente personalizzato.

2. **Importazione dei componenti:** Una volta creato un componente, puoi importarlo all'interno di altri componenti o delle tue viste utilizzando l'istruzione di importazione di JavaScript. Ad esempio, se hai creato un componente chiamato MyComponent.vue, puoi importarlo all'interno di un altro componente o di una vista utilizzando
```JavaScript
import MyComponent from './MyComponent.vue';
```
3. **Utilizzo dei componenti:** Dopo aver importato il componente, puoi utilizzarlo come qualsiasi altro elemento HTML all'interno del markup della tua vista o di un altro componente. Basta includere il tag del componente come faresti con qualsiasi altro elemento HTML, ad esempio
```xml
<MyComponent />
```

***

## DOM in Vue.js

### Cos'è
Il `Document Object Model` (DOM) è una rappresentazione ad albero della struttura di un documento HTML o XML, che consente agli sviluppatori web di manipolare dinamicamente gli elementi e il contenuto di una pagina web utilizzando JavaScript.

### In Vue.js
In Vue.js, il `DOM` è una parte fondamentale della sua architettura reattiva. Quando si utilizza Vue.js, si definiscono le interfacce utente utilizzando un template HTML che contiene le direttive di Vue.js. Durante l'esecuzione dell'applicazione, Vue.js traduce questi template in un vero e proprio albero DOM virtuale, che rappresenta la struttura dell'interfaccia utente.

- **Creazione del DOM virtuale:** Quando si definisce un componente Vue.js, si definiscono anche i relativi template HTML, che contengono direttive Vue.js e interpolazioni di dati. Durante l'esecuzione dell'applicazione, Vue.js converte questi template in un albero `DOM virtuale`.
- **Rendering del DOM virtuale:** Dopo la creazione del DOM virtuale, Vue.js utilizza il suo motore di rendering per trasformare il DOM virtuale in un vero e proprio DOM del browser. Durante questo processo, Vue.js aggiorna dinamicamente il DOM per riflettere qualsiasi cambiamento nello stato dell'applicazione o nei dati.
- **Reattività del DOM:** Una delle caratteristiche distintive di Vue.js è la sua reattività, che consente di aggiornare automaticamente il DOM quando cambia lo stato dell'applicazione o dei dati. Quando si modificano i dati all'interno di un componente Vue.js, Vue.js rileva automaticamente questi cambiamenti e aggiorna il DOM di conseguenza, garantendo una UI sempre sincronizzata con lo stato dell'applicazione.
- **Efficienza del DOM virtuale:** Vue.js utilizza un DOM virtuale per migliorare le prestazioni e l'efficienza nell'aggiornamento del DOM. Il DOM virtuale consente a Vue.js di eseguire operazioni di confronto e aggiornamento in modo efficiente, riducendo al minimo il numero di operazioni di manipolazione del DOM effettivamente eseguite sul browser.

<img height="300" src="C:\Users\rossian\Projects\spring-cors\src\main\resources\img\virtualDOM.png" width="550"/>

***

## Pro e contro di Vue.js

### Vantaggi:
- **Semplicità d'apprendimento:** Vue.js è noto per la sua curva d'apprendimento dolce e la sua sintassi intuitiva, che lo rendono facile da imparare e utilizzare.
- **Flessibilità:** Vue.js offre una maggiore flessibilità rispetto ad altri framework, consentendo di utilizzare solo le parti necessarie e integrarle gradualmente in progetti esistenti.
- **Performance:** Vue.js offre ottime prestazioni grazie al suo efficiente sistema di rendering e gestione dello stato, rendendolo adatto per applicazioni web ad alte prestazioni.
- **Ecosistema ricco di risorse:** Vue.js ha un vasto ecosistema di librerie, plugin e strumenti di sviluppo che possono accelerare il processo di sviluppo e migliorare la qualità dell'applicazione.
- **Community attiva:** Vue.js ha una comunità attiva e solidale di sviluppatori che contribuiscono al suo sviluppo e forniscono supporto attraverso forum, documentazione e risorse online.

### Svantaggi:
- **Scalabilità:** A differenza di Angular, Vue.js potrebbe non essere la scelta migliore per applicazioni di grandi dimensioni e complesse.
- **Supporto aziendale limitato:** Anche se Vue.js è supportato da diverse aziende, potrebbe non avere lo stesso livello di supporto e risorse disponibili come Angular o React.
- **Meno strumenti e librerie:** Anche se l'ecosistema di Vue.js è in crescita, potrebbe non essere altrettanto maturo e completo come quello di Angular o React, con meno strumenti e librerie disponibili per determinate esigenze di sviluppo.











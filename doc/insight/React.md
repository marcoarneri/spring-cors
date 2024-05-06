# Applicativo Gestione Clienti con React

Questo progetto è un'applicazione web sviluppata in React per la gestione dei clienti. Utilizza React Router per la navigazione tra le pagine e Material-UI per la UI.

## Pro e Contro di React rispetto ad altri framework JavaScript

### Pro

1. **Componentizzazione**: React offre una solida struttura basata su componenti, che consente una migliore organizzazione del codice e la riutilizzazione dei componenti.
2. **Virtual DOM**: React utilizza un Virtual DOM, che migliora le prestazioni dell'applicazione e riduce il numero di manipolazioni del DOM.
3. **Community** e Supporto: React ha una vasta e attiva comunità di sviluppatori, che fornisce una vasta gamma di risorse, librerie e supporto.
4. **JSX**: JSX permette di scrivere codice HTML all'interno di JavaScript, rendendo la sintassi più chiara e intuitiva.
5. **State e Props**: React offre un efficiente sistema di gestione dello stato e delle proprietà dei componenti, che semplifica lo sviluppo e il debug dell'applicazione.

### Contro

1. **Curva di Apprendimento Iniziale**: Per i principianti, React può presentare una curva di apprendimento iniziale, specialmente per coloro che non hanno familiarità con il paradigma della programmazione dichiarativa.
2. **Scelta delle Librerie**: A volte può essere difficile scegliere le librerie giuste da utilizzare con React, poiché ce ne sono molte disponibili e la decisione dipende dalle esigenze specifiche del progetto.

## Funzionamento dell'Applicativo

1. **App.js**: Questo componente è il punto di ingresso dell'applicazione e gestisce la navigazione tra le pagine utilizzando React Router. Contiene i link per navigare alla pagina Home e alla pagina di Aggiunta di un nuovo cliente.
2. **Home.js**: Questo componente visualizza una tabella dei clienti recuperati da un'API. Utilizza axios per effettuare richieste HTTP e Material-UI per la UI. Ogni riga della tabella mostra le informazioni di un cliente, compresi pulsanti per aggiornare o eliminare un cliente.
3. **Add.js**: Questo componente gestisce il form per aggiungere un nuovo cliente. Utilizza useState per gestire lo stato del form e axios per inviare i dati del cliente al server.

### Implementazione dell'Applicativo

### Passo 1: Avvio di un Nuovo Progetto React

Per avviare un nuovo progetto React, puoi utilizzare il comando `npx create-react-app nome-progetto`. Una volta creato il progetto, puoi iniziare a sviluppare l'applicazione.
Per creare un componente puoi utilizzare `touch NomeComponente.js`

### Passo 2: Creazione dei Componenti e Implementazione delle Route

Per creare i componenti e implementare le route, puoi seguire la struttura del codice fornito sopra. Assicurati di includere correttamente le dipendenze come React Router e Material-UI nel file `package.json`.

### Passo 3: Implementazione delle Funzionalità

Una volta creati i componenti e definite le route, puoi iniziare a implementare le funzionalità dell'applicazione, come l'aggiunta, l'aggiornamento e l'eliminazione dei clienti. Utilizza `useState` per gestire lo stato dei componenti e `axios` per effettuare richieste HTTP al server.

## Stato in react

Lo stato in React è una parte fondamentale dello sviluppo dell'applicazione. È un oggetto JavaScript che contiene dati sensibili o dinamici che possono cambiare nel tempo. Utilizzando lo stato, è possibile rendere reattiva l'interfaccia utente e aggiornare dinamicamente i componenti in base ai dati.

Ad esempio, nel componente `Add.js`, lo stato viene utilizzato per memorizzare i dati del cliente inseriti dall'utente nel form. Ogni volta che l'utente modifica uno dei campi del form, lo stato viene aggiornato utilizzando il metodo `setCliente`, che provoca il re-rendering del componente con i nuovi dati.

```javascript
const [cliente, setCliente] = useState({
  nome: '',
  cognome: '',
  username: '',
  eta: ''
});
```

package com.mycompany.mavenproject2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.time.LocalDate;

// =====================================================
// CLASSE CLIENTE
// =====================================================
class Cliente {
    private String nome;
    private final String email;

    public Cliente(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

// =====================================================
// CLASSE ASTRATTA DISPOSITIVO
// =====================================================
abstract class Dispositivo {
    private final String codice;
    private String marca;
    private String modello;

    public Dispositivo(String codice, String marca, String modello) {
        this.codice = codice;
        this.marca = marca;
        this.modello = modello;
    }

    public String getCodice() {
        return codice;
    }

    public String getMarca() {
        return marca;
    }

    public String getModello() {
        return modello;
    }

    public abstract String getDescrizione();

    @Override
    public String toString() {
        return "codice='" + codice + '\'' +
                ", marca='" + marca + '\'' +
                ", modello='" + modello + '\'';
    }
}

// =====================================================
// CLASSE LAPTOP
// =====================================================
class Laptop extends Dispositivo {
    private int ram;
    private double disco;

    public Laptop(int ram, double disco,
                  String codice, String marca, String modello) {
        super(codice, marca, modello);
        this.ram = ram;
        this.disco = disco;
    }

    public int getRam() {
        return ram;
    }

    public double getDisco() {
        return disco;
    }

    @Override
    public String getDescrizione() {
        return "Laptop{" + super.toString() +
                ", RAM=" + ram + " GB" +
                ", disco=" + disco + " GB}";
    }

    @Override
    public String toString() {
        return getDescrizione();
    }
}

// =====================================================
// CLASSE SMARTPHONE
// =====================================================
class Smartphone extends Dispositivo {
    private String sistemaOperativo;
    private double memoria;

    public Smartphone(String sistemaOperativo, double memoria,
                      String codice, String marca, String modello) {
        super(codice, marca, modello);
        this.sistemaOperativo = sistemaOperativo;
        this.memoria = memoria;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public double getMemoria() {
        return memoria;
    }

    @Override
    public String getDescrizione() {
        return "Smartphone{" + super.toString() +
                ", sistema operativo='" + sistemaOperativo + '\'' +
                ", memoria=" + memoria + " GB}";
    }

    @Override
    public String toString() {
        return getDescrizione();
    }
}

// =====================================================
// CLASSE RICHIESTA RIPARAZIONE
// =====================================================
class RichiestaRiparazione {
    private final int id;
    private Cliente cliente;
    private Dispositivo dispositivo;
    private String descrizione;
    private LocalDate accettazione;
    private LocalDate consegnaPrevista;

    public RichiestaRiparazione(int id, Cliente cliente,
                                Dispositivo dispositivo,
                                String descrizione, int giorniStimati) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "L'ID deve essere maggiore di zero.");
        }

        if (giorniStimati < 0) {
            throw new IllegalArgumentException(
                    "I giorni stimati non possono essere negativi.");
        }

        if (cliente == null || dispositivo == null) {
            throw new IllegalArgumentException(
                    "Cliente e dispositivo sono obbligatori.");
        }

        this.id = id;
        this.cliente = cliente;
        this.dispositivo = dispositivo;
        this.descrizione = descrizione;

        // Registra la data corrente come data di accettazione.
        this.accettazione = LocalDate.now();

        // Calcola la data prevista aggiungendo i giorni stimati.
        this.consegnaPrevista = accettazione.plusDays(giorniStimati);
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LocalDate getAccettazione() {
        return accettazione;
    }

    public LocalDate getConsegnaPrevista() {
        return consegnaPrevista;
    }

    // Una richiesta è scaduta se la consegna è precedente a oggi.
    public boolean isScaduta() {
        return consegnaPrevista.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return "RichiestaRiparazione{" +
                "id=" + id +
                ", cliente=" + cliente.getNome() +
                ", dispositivo=" + dispositivo.getDescrizione() +
                ", descrizione='" + descrizione + '\'' +
                ", accettazione=" + accettazione +
                ", consegnaPrevista=" + consegnaPrevista +
                ", scaduta=" + isScaduta() +
                '}';
    }
}

// =====================================================
// ECCEZIONE: CLIENTE GIÀ ESISTENTE
// =====================================================
class ClienteEsiste extends Exception {
    public ClienteEsiste(String messaggio) {
        super(messaggio);
    }
}

// =====================================================
// ECCEZIONE: CLIENTE NON REGISTRATO
// =====================================================
class ClienteNonRegistrato extends Exception {
    public ClienteNonRegistrato(String messaggio) {
        super(messaggio);
    }
}

// =====================================================
// ECCEZIONE: CODA VUOTA
// =====================================================
class CodaVuota extends Exception {
    public CodaVuota(String messaggio) {
        super(messaggio);
    }
}

// =====================================================
// CLASSE CENTRO ASSISTENZA
// =====================================================
class CentroAssistenza {
    private ArrayList<Cliente> clienti;
    private Queue<RichiestaRiparazione> richieste;
    private int prossimoId;

    public CentroAssistenza() {
        clienti = new ArrayList<>();
        richieste = new LinkedList<>();
        prossimoId = 1;
    }

    public ArrayList<Cliente> getClienti() {
        // Restituisce una copia della lista.
        return new ArrayList<>(clienti);
    }

    public Queue<RichiestaRiparazione> getRichieste() {
        // Restituisce una copia della coda.
        return new LinkedList<>(richieste);
    }

    // Registra un cliente, impedendo email duplicate.
    public void registraCliente(Cliente nuovoCliente)
            throws ClienteEsiste {

        if (nuovoCliente == null) {
            throw new IllegalArgumentException(
                    "Il cliente non può essere null.");
        }

        if (verificaClienteRegistrato(nuovoCliente.getEmail())) {
            throw new ClienteEsiste(
                    "Esiste già un cliente con email: "
                    + nuovoCliente.getEmail());
        }

        clienti.add(nuovoCliente);
    }

    // Verifica la presenza di un cliente tramite email.
    public boolean verificaClienteRegistrato(String email) {
        if (email == null) {
            return false;
        }

        for (Cliente c : clienti) {
            if (email.equals(c.getEmail())) {
                return true;
            }
        }

        return false;
    }

    // Cerca e restituisce il cliente registrato con questa email.
    private Cliente trovaCliente(String email) {
        for (Cliente c : clienti) {
            if (email != null && email.equals(c.getEmail())) {
                return c;
            }
        }

        return null;
    }

    // Inserisce una richiesta con ID progressivo.
    public void inserisciRichiesta(Cliente cliente,
                                   Dispositivo dispositivo,
                                   String descrizione,
                                   int giorniStimati)
            throws ClienteNonRegistrato {

        if (cliente == null ||
                !verificaClienteRegistrato(cliente.getEmail())) {
            throw new ClienteNonRegistrato(
                    "Il cliente non è registrato nel centro.");
        }

        // Usa l'oggetto cliente effettivamente registrato.
        Cliente clienteRegistrato = trovaCliente(cliente.getEmail());

        RichiestaRiparazione nuovaRichiesta =
                new RichiestaRiparazione(
                        prossimoId,
                        clienteRegistrato,
                        dispositivo,
                        descrizione,
                        giorniStimati);

        richieste.offer(nuovaRichiesta);
        prossimoId++;
    }

    // Legge la prima richiesta senza rimuoverla.
    public RichiestaRiparazione prossimaRichiesta()
            throws CodaVuota {

        RichiestaRiparazione richiesta = richieste.peek();

        if (richiesta == null) {
            throw new CodaVuota(
                    "Non ci sono richieste in attesa.");
        }

        return richiesta;
    }

    // Estrae e rimuove la prima richiesta.
    public RichiestaRiparazione estraiProssimaRichiesta()
            throws CodaVuota {

        RichiestaRiparazione richiesta = richieste.poll();

        if (richiesta == null) {
            throw new CodaVuota(
                    "Impossibile estrarre: la coda è vuota.");
        }

        return richiesta;
    }

    // Restituisce il numero di richieste in attesa.
    public int getNumeroRichiesteInAttesa() {
        return richieste.size();
    }

    // Stampa tutte le richieste senza rimuoverle dalla coda.
    public void stampaRichiesteInAttesa() {
        if (richieste.isEmpty()) {
            System.out.println("Nessuna richiesta in attesa.");
            return;
        }

        for (RichiestaRiparazione r : richieste) {
            System.out.println(r);
        }
    }

    // Cerca le richieste di un cliente tramite email.
    public ArrayList<RichiestaRiparazione> cercaRichiestePerCliente(
            String email) {

        ArrayList<RichiestaRiparazione> risultato =
                new ArrayList<>();

        if (email == null) {
            return risultato;
        }

        for (RichiestaRiparazione r : richieste) {
            if (email.equals(r.getCliente().getEmail())) {
                risultato.add(r);
            }
        }

        return risultato;
    }

    // Restituisce le richieste scadute ancora in attesa.
    public ArrayList<RichiestaRiparazione> getRichiesteScadute() {
        ArrayList<RichiestaRiparazione> scadute =
                new ArrayList<>();

        for (RichiestaRiparazione r : richieste) {
            if (r.isScaduta()) {
                scadute.add(r);
            }
        }

        return scadute;
    }

    @Override
    public String toString() {
        return "CentroAssistenza{" +
                "clienti=" + clienti +
                ", richieste=" + richieste +
                ", prossimoId=" + prossimoId +
                '}';
    }
}

// =====================================================
// PROGRAMMA PRINCIPALE
// =====================================================
public class Main {

    public static void main(String[] args) {

        CentroAssistenza centro = new CentroAssistenza();

        // ---------------------------------------------
        // CREAZIONE E REGISTRAZIONE CLIENTI
        // ---------------------------------------------
        Cliente c1 = new Cliente("Marco", "marco@email.it");
        Cliente c2 = new Cliente("Giulia", "giulia@email.it");
        Cliente c3 = new Cliente("Luca", "luca@email.it");

        try {
            centro.registraCliente(c1);
            centro.registraCliente(c2);
            centro.registraCliente(c3);

            System.out.println("Clienti registrati correttamente.");

        } catch (ClienteEsiste e) {
            System.out.println(e.getMessage());
        }

        // Test email duplicata.
        try {
            Cliente duplicato =
                    new Cliente("Marco Due", "marco@email.it");

            centro.registraCliente(duplicato);

        } catch (ClienteEsiste e) {
            System.out.println("Errore registrazione: "
                    + e.getMessage());
        }

        // Visualizzazione clienti.
        System.out.println("\n--- CLIENTI REGISTRATI ---");

        for (Cliente c : centro.getClienti()) {
            System.out.println(c);
        }

        // ---------------------------------------------
        // CREAZIONE DISPOSITIVI
        // ---------------------------------------------
        Dispositivo laptop1 = new Laptop(
                16, 512, "L001", "Lenovo", "ThinkPad");

        Dispositivo smartphone1 = new Smartphone(
                "Android", 256, "S001", "Samsung", "Galaxy");

        Dispositivo laptop2 = new Laptop(
                8, 256, "L002", "HP", "Pavilion");

        System.out.println("\n--- DISPOSITIVI ---");
        System.out.println(laptop1.getDescrizione());
        System.out.println(smartphone1.getDescrizione());
        System.out.println(laptop2.getDescrizione());

        // ---------------------------------------------
        // INSERIMENTO RICHIESTE
        // ---------------------------------------------
        try {
            centro.inserisciRichiesta(
                    c1, laptop1, "Problema alla batteria", 3);

            centro.inserisciRichiesta(
                    c2, smartphone1, "Schermo danneggiato", 5);

            centro.inserisciRichiesta(
                    c1, laptop2, "Problema al disco", 2);

            System.out.println("\nRichieste inserite correttamente.");

        } catch (ClienteNonRegistrato e) {
            System.out.println(e.getMessage());
        }

        // Test cliente non registrato.
        try {
            Cliente c4 = new Cliente("Anna", "anna@email.it");

            centro.inserisciRichiesta(
                    c4, laptop1, "Test cliente non registrato", 4);

        } catch (ClienteNonRegistrato e) {
            System.out.println("Errore richiesta: "
                    + e.getMessage());
        }

        // ---------------------------------------------
        // VISUALIZZAZIONE RICHIESTE
        // ---------------------------------------------
        System.out.println("\n--- RICHIESTE IN ATTESA ---");
        centro.stampaRichiesteInAttesa();

        System.out.println("\nNumero richieste in attesa: "
                + centro.getNumeroRichiesteInAttesa());

        // ---------------------------------------------
        // RICERCA PER CLIENTE
        // ---------------------------------------------
        System.out.println("\n--- RICHIESTE DI MARCO ---");

        ArrayList<RichiestaRiparazione> richiesteMarco =
                centro.cercaRichiestePerCliente("marco@email.it");

        for (RichiestaRiparazione r : richiesteMarco) {
            System.out.println(r);
        }

        // ---------------------------------------------
        // RICERCA RICHIESTE SCADUTE
        // ---------------------------------------------
        System.out.println("\n--- RICHIESTE SCADUTE ---");

        ArrayList<RichiestaRiparazione> scadute =
                centro.getRichiesteScadute();

        if (scadute.isEmpty()) {
            System.out.println("Non ci sono richieste scadute.");
        } else {
            for (RichiestaRiparazione r : scadute) {
                System.out.println(r);
            }
        }

        // ---------------------------------------------
        // LETTURA SENZA ESTRAZIONE: PEEK
        // ---------------------------------------------
        System.out.println("\n--- PROSSIMA RICHIESTA ---");

        try {
            System.out.println(centro.prossimaRichiesta());

            System.out.println("Numero richieste dopo peek: "
                    + centro.getNumeroRichiesteInAttesa());

        } catch (CodaVuota e) {
            System.out.println(e.getMessage());
        }

        // ---------------------------------------------
        // ESTRAZIONE FIFO
        // ---------------------------------------------
        System.out.println("\n--- ESTRAZIONE FIFO ---");

        try {
            RichiestaRiparazione estratta =
                    centro.estraiProssimaRichiesta();

            System.out.println("Richiesta estratta: " + estratta);

            System.out.println("Numero richieste rimaste: "
                    + centro.getNumeroRichiesteInAttesa());

        } catch (CodaVuota e) {
            System.out.println(e.getMessage());
        }

        // Estrae tutte le richieste rimaste.
        System.out.println("\n--- ESTRAZIONE RICHIESTE RIMANENTI ---");

        try {
            while (centro.getNumeroRichiesteInAttesa() > 0) {
                System.out.println(
                        centro.estraiProssimaRichiesta());
            }

        } catch (CodaVuota e) {
            System.out.println(e.getMessage());
        }

        // ---------------------------------------------
        // TEST CODA VUOTA
        // ---------------------------------------------
        System.out.println("\n--- TEST CODA VUOTA ---");

        try {
            centro.prossimaRichiesta();

        } catch (CodaVuota e) {
            System.out.println("Errore lettura: "
                    + e.getMessage());
        }

        try {
            centro.estraiProssimaRichiesta();

        } catch (CodaVuota e) {
            System.out.println("Errore estrazione: "
                    + e.getMessage());
        }

        System.out.println("\nTest terminati.");
    }
}

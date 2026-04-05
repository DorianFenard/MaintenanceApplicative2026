import event.Event;
import event.*;
import manager.CalendarManager;
import valueobject.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarApp extends JFrame {

    private final CalendarManager calendar = new CalendarManager();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JLabel statusLabel;
    private String utilisateur = "Utilisateur";

    public CalendarApp() {
        super("CalendarManager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // ====== TOP : titre ======
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        JLabel titleLabel = new JLabel("CalendarManager");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        topPanel.add(titleLabel);
        JLabel userLabel = new JLabel("— " + utilisateur);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userLabel.setForeground(Color.GRAY);
        topPanel.add(userLabel);
        add(topPanel, BorderLayout.NORTH);

        // ====== CENTER : tableau ======
        String[] columns = {"ID", "Type", "Description", "Conflits"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(450);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ====== BOTTOM : boutons + status dans un panel vertical ======
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));

        JButton btnAddRdv = createButton("+ RDV Personnel", new Color(29, 158, 117));
        btnAddRdv.addActionListener(e -> dialogAjouterRdv());
        buttonPanel.add(btnAddRdv);

        JButton btnAddReunion = createButton("+ Réunion", new Color(83, 74, 183));
        btnAddReunion.addActionListener(e -> dialogAjouterReunion());
        buttonPanel.add(btnAddReunion);

        JButton btnAddPeriodique = createButton("+ Périodique", new Color(216, 90, 48));
        btnAddPeriodique.addActionListener(e -> dialogAjouterPeriodique());
        buttonPanel.add(btnAddPeriodique);

        buttonPanel.add(Box.createHorizontalStrut(20));

        JButton btnDelete = createButton("Supprimer", new Color(180, 50, 50));
        btnDelete.addActionListener(e -> supprimerSelection());
        buttonPanel.add(btnDelete);

        buttonPanel.add(Box.createHorizontalStrut(20));

        JButton btnFilterMonth = createButton("Filtrer par mois", new Color(100, 100, 100));
        btnFilterMonth.addActionListener(e -> dialogFiltrerMois());
        buttonPanel.add(btnFilterMonth);

        JButton btnShowAll = createButton("Tout afficher", new Color(100, 100, 100));
        btnShowAll.addActionListener(e -> refreshTable());
        buttonPanel.add(btnShowAll);

        bottomPanel.add(buttonPanel);

        // Status bar
        statusLabel = new JLabel(" Prêt. Cliquez sur un bouton pour commencer.");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        statusLabel.setForeground(Color.GRAY);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 15, 8, 15));
        bottomPanel.add(statusLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 20, 35));
        return btn;
    }

    private void refreshTable() {
        showEvents(calendar.tousLesEvenements());
    }

    private void showEvents(List<Event> events) {
        tableModel.setRowCount(0);
        events.forEach(e -> {
            List<Event> conflits = calendar.detecterConflits(e);
            String conflitStr = conflits.isEmpty()
                    ? ""
                    : conflits.size() + " conflit(s)";
            String type = e.accept(new EventVisitor<String>() {
                @Override
                public String visitRendezVous(RendezVousPersonnel rdv) { return "RDV"; }
                @Override
                public String visitReunion(Reunion reunion) { return "Réunion"; }
                @Override
                public String visitPeriodique(EvenementPeriodique periodique) { return "Périodique"; }
            });
            tableModel.addRow(new Object[]{
                    e.id().value().substring(0, 8),
                    type,
                    e.description(),
                    conflitStr
            });
        });
        statusLabel.setText(" " + events.size() + " événement(s) affiché(s).");
    }

    private void dialogAjouterRdv() {
        JTextField titreField = new JTextField(15);
        JTextField anneeField = new JTextField("2026", 5);
        JTextField moisField = new JTextField("4", 3);
        JTextField jourField = new JTextField("15", 3);
        JTextField heureField = new JTextField("10", 3);
        JTextField minuteField = new JTextField("0", 3);
        JTextField dureeField = new JTextField("30", 5);

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.add(new JLabel("Titre :")); panel.add(titreField);
        panel.add(new JLabel("Année :")); panel.add(anneeField);
        panel.add(new JLabel("Mois (1-12) :")); panel.add(moisField);
        panel.add(new JLabel("Jour (1-31) :")); panel.add(jourField);
        panel.add(new JLabel("Heure (0-23) :")); panel.add(heureField);
        panel.add(new JLabel("Minute (0-59) :")); panel.add(minuteField);
        panel.add(new JLabel("Durée (minutes) :")); panel.add(dureeField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter un RDV Personnel",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        handleDialogResult(result, () -> {
            RendezVousPersonnel rdv = new RendezVousPersonnel(
                    EventId.generate(),
                    new TitreEvenement(titreField.getText().trim()),
                    new Proprietaire(utilisateur),
                    new DateEvenement(
                            Integer.parseInt(anneeField.getText().trim()),
                            Integer.parseInt(moisField.getText().trim()),
                            Integer.parseInt(jourField.getText().trim()),
                            Integer.parseInt(heureField.getText().trim()),
                            Integer.parseInt(minuteField.getText().trim())),
                    new DureeEvenement(Integer.parseInt(dureeField.getText().trim())));

            calendar.ajouter(rdv);
            refreshTable();
            showConflitAlert(rdv);
            statusLabel.setText(" RDV ajouté : " + rdv.description());
        });
    }

    private void dialogAjouterReunion() {
        JTextField titreField = new JTextField(15);
        JTextField anneeField = new JTextField("2026", 5);
        JTextField moisField = new JTextField("4", 3);
        JTextField jourField = new JTextField("15", 3);
        JTextField heureField = new JTextField("10", 3);
        JTextField minuteField = new JTextField("0", 3);
        JTextField dureeField = new JTextField("60", 5);
        JTextField lieuField = new JTextField(15);
        JTextField participantsField = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5));
        panel.add(new JLabel("Titre :")); panel.add(titreField);
        panel.add(new JLabel("Année :")); panel.add(anneeField);
        panel.add(new JLabel("Mois (1-12) :")); panel.add(moisField);
        panel.add(new JLabel("Jour (1-31) :")); panel.add(jourField);
        panel.add(new JLabel("Heure (0-23) :")); panel.add(heureField);
        panel.add(new JLabel("Minute (0-59) :")); panel.add(minuteField);
        panel.add(new JLabel("Durée (minutes) :")); panel.add(dureeField);
        panel.add(new JLabel("Lieu :")); panel.add(lieuField);
        panel.add(new JLabel("Participants (virgules) :")); panel.add(participantsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter une Réunion",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        handleDialogResult(result, () -> {
            String[] noms = participantsField.getText().trim().split(",");
            List<String> participantList = new ArrayList<>();
            participantList.add(utilisateur);
            for (String nom : noms) {
                String trimmed = nom.trim();
                participantList.addAll(trimmed.isEmpty() ? List.of() : List.of(trimmed));
            }

            Reunion reunion = new Reunion(
                    EventId.generate(),
                    new TitreEvenement(titreField.getText().trim()),
                    new Proprietaire(utilisateur),
                    new DateEvenement(
                            Integer.parseInt(anneeField.getText().trim()),
                            Integer.parseInt(moisField.getText().trim()),
                            Integer.parseInt(jourField.getText().trim()),
                            Integer.parseInt(heureField.getText().trim()),
                            Integer.parseInt(minuteField.getText().trim())),
                    new DureeEvenement(Integer.parseInt(dureeField.getText().trim())),
                    new Lieu(lieuField.getText().trim()),
                    new Participants(participantList));

            calendar.ajouter(reunion);
            refreshTable();
            showConflitAlert(reunion);
            statusLabel.setText(" Réunion ajoutée : " + reunion.description());
        });
    }

    // ========================================================================
    // Add Periodique
    // ========================================================================

    private void dialogAjouterPeriodique() {
        JTextField titreField = new JTextField(15);
        JTextField anneeField = new JTextField("2026", 5);
        JTextField moisField = new JTextField("4", 3);
        JTextField jourField = new JTextField("1", 3);
        JTextField heureField = new JTextField("8", 3);
        JTextField minuteField = new JTextField("0", 3);
        JTextField freqField = new JTextField("7", 5);

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.add(new JLabel("Titre :")); panel.add(titreField);
        panel.add(new JLabel("Année :")); panel.add(anneeField);
        panel.add(new JLabel("Mois (1-12) :")); panel.add(moisField);
        panel.add(new JLabel("Jour (1-31) :")); panel.add(jourField);
        panel.add(new JLabel("Heure (0-23) :")); panel.add(heureField);
        panel.add(new JLabel("Minute (0-59) :")); panel.add(minuteField);
        panel.add(new JLabel("Fréquence (jours) :")); panel.add(freqField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter un Événement Périodique",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        handleDialogResult(result, () -> {
            EvenementPeriodique ep = new EvenementPeriodique(
                    EventId.generate(),
                    new TitreEvenement(titreField.getText().trim()),
                    new Proprietaire(utilisateur),
                    new DateEvenement(
                            Integer.parseInt(anneeField.getText().trim()),
                            Integer.parseInt(moisField.getText().trim()),
                            Integer.parseInt(jourField.getText().trim()),
                            Integer.parseInt(heureField.getText().trim()),
                            Integer.parseInt(minuteField.getText().trim())),
                    new FrequenceJours(Integer.parseInt(freqField.getText().trim())));

            calendar.ajouter(ep);
            refreshTable();
            statusLabel.setText(" Événement périodique ajouté : " + ep.description());
        });
    }


    private void supprimerSelection() {
        int row = table.getSelectedRow();
        Runnable deleteAction = (row >= 0)
                ? () -> {
            Event event = calendar.tousLesEvenements().get(row);
            calendar.supprimer(event.id());
            refreshTable();
            statusLabel.setText(" Événement supprimé.");
        }
                : () -> JOptionPane.showMessageDialog(this, "Sélectionnez d'abord un événement dans le tableau.");

        deleteAction.run();
    }


    private void dialogFiltrerMois() {
        JTextField anneeField = new JTextField("2026", 5);
        JTextField moisField = new JTextField("4", 3);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Année :")); panel.add(anneeField);
        panel.add(new JLabel("Mois (1-12) :")); panel.add(moisField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Filtrer par mois",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        handleDialogResult(result, () -> {
            int annee = Integer.parseInt(anneeField.getText().trim());
            int mois = Integer.parseInt(moisField.getText().trim());
            DateEvenement debut = new DateEvenement(annee, mois, 1, 0, 0);
            DateEvenement fin = new DateEvenement(
                    LocalDateTime.of(annee, mois, 1, 0, 0).plusMonths(1).minusSeconds(1));
            List<Event> filtered = calendar.evenementsDansPeriode(new Periode(debut, fin));
            showEvents(filtered);
        });
    }


    private void handleDialogResult(int result, Runnable onOk) {
        Runnable action = (result == JOptionPane.OK_OPTION) ? onOk : () -> {};
        action.run();
    }

    private void showConflitAlert(Event event) {
        List<Event> conflits = calendar.detecterConflits(event);
        Runnable alert = conflits.isEmpty()
                ? () -> {}
                : () -> {
            StringBuilder sb = new StringBuilder("Conflits détectés :\n\n");
            conflits.forEach(c -> sb.append("- ").append(c.description()).append("\n"));
            JOptionPane.showMessageDialog(this, sb.toString(), "Conflit", JOptionPane.WARNING_MESSAGE);
        };
        alert.run();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalendarApp app = new CalendarApp();
            app.setVisible(true);
        });
    }
}

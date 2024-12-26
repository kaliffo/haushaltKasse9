package haushaltKasse8;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.DefaultComboBoxModel;






public class HaushaltKasse extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable uebersicht;
	
	private JScrollPane uebersichtScrollPane;
	
	private String[] uebersichtColumnNames;
	private DefaultTableModel uebersichtTable;
	
	private String uebersichten = "SELECT h.haushaltKasseEintraegeID AS ID, h.datum AS Datum, m.haushaltMitgliederName AS Mitglieder, e.eintragName AS Name, h.betrag AS Betrag, a.art AS FixVar, ein.art AS EinnahmeAusgabe  FROM `haushaltkasseeintraege` h JOIN haushaltmitglieder m ON h.haushaltMitgliederID = m.haushaltMitgliederID JOIN eintraege e ON h.EintragID = e.eintraegeID JOIN einnahmeausgabe ein ON e.einnahmeAusgabeID = ein.einnahmeAusgabeID JOIN eintragart a ON e.eintragArtID = a.eintragArt ORDER BY h.haushaltKasseEintraegeID;";
	private String bilanzen = "SELECT(SELECT SUM(h.betrag) FROM haushaltkasseeintraege h JOIN eintraege e ON h.EintragID = e.eintraegeID WHERE e.einnahmeAusgabeID = 1) AS Einnahmen, (SELECT SUM(h.betrag) FROM haushaltkasseeintraege h JOIN eintraege e ON h.EintragID = e.eintraegeID WHERE e.einnahmeAusgabeID = 2) AS Ausgaben,(SELECT SUM(h.betrag) AS Einnahmen FROM haushaltkasseeintraege h JOIN eintraege e ON h.EintragID = e.eintraegeID WHERE e.einnahmeAusgabeID = 1)-(SELECT SUM(h.betrag) AS Ausgaben FROM haushaltkasseeintraege h JOIN eintraege e ON h.EintragID = e.eintraegeID WHERE e.einnahmeAusgabeID = 2) AS Differenz;";
	private String mitgliederList = "SELECT * FROM haushaltmitglieder;";
	private String eintraegeList = "SELECT * FROM eintraege;";
	private String insertRecord = "INSERT INTO `haushaltkasseeintraege`(`haushaltMitgliederID`, `EintragID`, `betrag`, `datum`) VALUES (?,?,?,?)";
	private String haushaltKasseID = "SELECT haushaltKasseEintraegeID FROM haushaltkasseeintraege GROUP BY haushaltKasseEintraegeID;";
	private String loeschEintrag = "DELETE FROM haushaltkasseeintraege WHERE haushaltKasseEintraegeID = ?;";
	private String sql = "SELECT haushaltMitgliederID FROM haushaltMitglieder WHERE haushaltMitgliederName = ?;";
	private String sql2 = "SELECT eintraegeID FROM eintraege WHERE eintragName = ?;";
	
	private String haushaltMitgID;
	private String eintragID;
	private String eintragBetrag;
	private String eintragDatum;

	private JPanel uebersichtPanel;
	private JPanel controllPanel;
	private JButton uebersichtUpdate;
	private JButton close;
	private JPanel manipulationPanel;
	private JLabel mietgliedEingabeTextLabel;
	private JComboBox<String> haushaltMitglieder;
	private JLabel eintragNameJcomboxLabel;
	private JComboBox<String> eintraege;
	private JLabel betragEingabeTextLabel;
	private JTextField betrag;
	private JLabel datumEingabeTextLabel;
	private JTextField datum;
	private JButton addRecord;
	private JButton design;
	private JLabel einnahmeLabel;
	private JLabel einnahmeBetragLabel;
	private JLabel ausgabeLabel;
	private JLabel ausgabeBetragLabel;
	private JLabel differenzLabel;
	private JLabel differenzBetragLabel;
	private JLabel menu;
	private JComboBox<String> menuComboBox;
	private JComboBox <String> eintragIDCombox;
	private JLabel eintragnr;
	public boolean darkMode = true;
	 

	public void zeigAnsicht() {
		try
		{
			Mysql.display(uebersichten);
		} catch (ClassNotFoundException | SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
		{
			uebersichtTable.setRowCount(0);
			while (Mysql.resultSet.next()) {
		        Object[] row = new Object[7];
		        for (int i = 1; i <= 7; i++) {
		            row[i - 1] = Mysql.resultSet.getObject(i);
		        }
		        uebersichtTable.addRow(row);
		    }
				
			}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		try
		{
			Mysql.display(bilanzen);
			
		} catch (ClassNotFoundException | SQLException e1)
		{
			e1.printStackTrace();
		}
		try
		{
			while (Mysql.resultSet.next()) {
		        einnahmeBetragLabel.setText(Mysql.resultSet.getString(1)+"€");
		        ausgabeBetragLabel.setText(Mysql.resultSet.getString(2)+"€");
		        differenzBetragLabel.setText(Mysql.resultSet.getString(3)+"€");
		        }
		    }
				
			
		catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		}
	public void aktHaushaltID() throws ClassNotFoundException, SQLException {
		eintragIDCombox.removeAllItems();
		Mysql.display(haushaltKasseID);
		while (Mysql.resultSet.next()) {
		String item2 = Mysql.resultSet.getString("haushaltKasseEintraegeID");
		eintragIDCombox.addItem(item2);
	}
	}
	public void aktMitgliederName() throws ClassNotFoundException, SQLException {
		haushaltMitglieder.removeAllItems();
		Mysql.display(mitgliederList);
		while (Mysql.resultSet.next()) {
		String item = Mysql.resultSet.getString("haushaltMitgliederName");
		haushaltMitglieder.addItem(item);
		}
	}
public HaushaltKasse() throws ClassNotFoundException, SQLException
{
	getContentPane().setBackground(new Color(0, 0, 0));
	
	
	

setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setBounds(100, 100, 1100, 600);

//Styling with FlatLaf
try {
    UIManager.setLookAndFeel(new FlatDarkLaf());
} catch (Exception ex) {
    ex.printStackTrace();
}






//create Table Ansicht:
uebersichtPanel = new JPanel();
getContentPane().add(uebersichtPanel, BorderLayout.EAST);

uebersichtColumnNames = new String[] {
	"Nr", "Datum", "Haushaltmitglied", "Eintrag", "Betrag", "Fix-Var", "Einnahme-Ausgabe"};
uebersichtTable = new DefaultTableModel(uebersichtColumnNames, 0);
getContentPane().add(uebersichtPanel, BorderLayout.WEST);





controllPanel = new JPanel();
getContentPane().add(controllPanel, BorderLayout.SOUTH);

//Ansicht Tabelle Button

uebersichtUpdate = new JButton("Übersicht");
uebersichtUpdate.addActionListener(e-> zeigAnsicht());

//Button um Design zu ändern

design = new JButton();
design.setText("Dark/Light");

design.addActionListener(e->{
	if (darkMode) {
	FlatLightLaf.setup();
	darkMode = false;
SwingUtilities.updateComponentTreeUI(rootPane);}
	else {
		FlatDarkLaf.setup();
		darkMode = true;
		SwingUtilities.updateComponentTreeUI(rootPane);
	}
});
controllPanel.add(design);
controllPanel.add(uebersichtUpdate);

// Close Button

close = new JButton("Close");
close.addActionListener(e -> {
System.out.println("Closing the application...");
Mysql.disconnect();
System.exit(0); // Close the frame
});
controllPanel.add(close);

einnahmeLabel = new JLabel("Einnahmen :");
controllPanel.add(einnahmeLabel);


einnahmeBetragLabel = new JLabel("         €");
controllPanel.add(einnahmeBetragLabel);

ausgabeLabel = new JLabel("Ausgaben :");
controllPanel.add(ausgabeLabel);

ausgabeBetragLabel = new JLabel("         €");
controllPanel.add(ausgabeBetragLabel);

differenzLabel = new JLabel("Differenz :");
controllPanel.add(differenzLabel);

differenzBetragLabel = new JLabel("         €");
controllPanel.add(differenzBetragLabel);



//Formular

manipulationPanel = new JPanel();
getContentPane().add(manipulationPanel, BorderLayout.NORTH);
menu = new JLabel("Menu");
manipulationPanel.add(menu);
menuComboBox = new JComboBox<String>();
menuComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"","Addieren", "Löschen"}));
String add = "Addieren";
String loesc = "Löschen";
menuComboBox.addActionListener(e->{
	if((String)menuComboBox.getSelectedItem()== add) {
		eintragIDCombox.setEnabled(false);
		betrag.setEnabled(true);
		datum.setEnabled(true);
		haushaltMitglieder.setEnabled(true);
		eintraege.setEnabled(true);
	}
	else if((String)menuComboBox.getSelectedItem()== loesc) {
		eintragIDCombox.setEnabled(true);
		betrag.setEnabled(false);
		datum.setEnabled(false);
		haushaltMitglieder.setEnabled(false);
		eintraege.setEnabled(false);
	}
	else {
		eintragIDCombox.setEnabled(false);
		betrag.setEnabled(false);
		datum.setEnabled(false);
		haushaltMitglieder.setEnabled(false);
		eintraege.setEnabled(false);
	}
});
manipulationPanel.add(menuComboBox);


eintragnr = new JLabel("EintragNr");
manipulationPanel.add(eintragnr);
eintragIDCombox = new JComboBox<String>();
aktHaushaltID();
eintragIDCombox.setEnabled(false);
manipulationPanel.add(eintragIDCombox);

mietgliedEingabeTextLabel = new JLabel("Mietglied");
mietgliedEingabeTextLabel.setToolTipText("");
mietgliedEingabeTextLabel.setEnabled(false);
manipulationPanel.add(mietgliedEingabeTextLabel);

haushaltMitglieder = new JComboBox<String>();
aktMitgliederName();
haushaltMitglieder.setEnabled(false);
manipulationPanel.add(haushaltMitglieder);

eintragNameJcomboxLabel = new JLabel("Eintragsname");
manipulationPanel.add(eintragNameJcomboxLabel);

eintraege = new JComboBox<String>();
Mysql.display(eintraegeList);
while (Mysql.resultSet.next()) {
String item = Mysql.resultSet.getString("eintragName");
eintraege.addItem(item);
}
eintraege.setEnabled(false);
manipulationPanel.add(eintraege);

betragEingabeTextLabel = new JLabel("Betrag");
manipulationPanel.add(betragEingabeTextLabel);

betrag = new JTextField();
betrag.setToolTipText("");
betrag.setText("");
betrag.setColumns(10);
betrag.setEnabled(false);
manipulationPanel.add(betrag);

datumEingabeTextLabel = new JLabel("Datum");
manipulationPanel.add(datumEingabeTextLabel);

datum = new JTextField();
datum.setToolTipText("");
datum.setText("");
datum.setColumns(10);
datum.setEnabled(false);
manipulationPanel.add(datum);



addRecord = new JButton("Senden");

addRecord.addActionListener(e->{
	if((String)menuComboBox.getSelectedItem()== add) {
	if (datum.getText().isEmpty() | betrag.getText().isEmpty()) {
        JOptionPane.showMessageDialog(rootPane, "bitte alle Felder ausfüllen");
        return;}
	
String hmitID = (String) haushaltMitglieder.getSelectedItem();
String eintID = (String) eintraege.getSelectedItem();
eintragBetrag = betrag.getText().trim();
eintragDatum = datum.getText().trim();

PreparedStatement statement;

try
{
	
	statement = Mysql.con.prepareStatement(sql);
	statement.setString(1, hmitID);
	ResultSet resulSet = statement.executeQuery();
	while(resulSet.next()) {
		haushaltMitgID = resulSet.getString(1);
	}
	
} catch (SQLException e1)
{
	// TODO Auto-generated catch block
	e1.printStackTrace();
}

try
{
	
	statement = Mysql.con.prepareStatement(sql2);
	statement.setString(1, eintID);
	ResultSet resulSet = statement.executeQuery();
	while(resulSet.next()) {
		eintragID = resulSet.getString(1);
	}
} catch (SQLException e1)
{
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
try
{
	
	statement = Mysql.con.prepareStatement(insertRecord);
	statement.setString(1, haushaltMitgID);
	statement.setString(2, eintragID);
	statement.setString(3, eintragBetrag);
	statement.setString(4, eintragDatum);
	int rowsInserted = statement.executeUpdate();

    if (rowsInserted > 0) {
    	JOptionPane.showMessageDialog( rootPane, "Daten erfolgreich gespeichert!");
    	
	betrag.setText("");
	datum.setText("");
	zeigAnsicht();
	aktHaushaltID();
	aktMitgliederName();
	}
} catch (SQLException e1)
{
	// TODO Auto-generated catch block
	e1.printStackTrace();
} catch (ClassNotFoundException e1)
{
	// TODO Auto-generated catch block
	e1.printStackTrace();
}


}
	if((String)menuComboBox.getSelectedItem()== loesc) {
		PreparedStatement st;
		try
		{
			st = Mysql.con.prepareStatement(loeschEintrag);
			st.setString(1, (String) eintragIDCombox.getSelectedItem());
			st.executeUpdate();
			Mysql.display(haushaltKasseID);
			eintragIDCombox.removeAllItems();
			while (Mysql.resultSet.next()) {
			String item2 = Mysql.resultSet.getString("haushaltKasseEintraegeID");
			eintragIDCombox.addItem(item2);}
			JOptionPane.showMessageDialog(rootPane, "Eintrag gelöscht");
			zeigAnsicht();
			aktHaushaltID();
			aktMitgliederName();
			
		} catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	});
manipulationPanel.add(addRecord);
uebersicht = new JTable(uebersichtTable);
uebersichtScrollPane = new JScrollPane(uebersicht);
getContentPane().add(uebersichtScrollPane, BorderLayout.CENTER);


}
}



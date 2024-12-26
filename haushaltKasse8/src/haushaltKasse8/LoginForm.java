package haushaltKasse8;

import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
//import org.mindrot.jbcrypt.BCrypt;

import com.formdev.flatlaf.FlatDarkLaf;

public class LoginForm
{
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		LoginForm lg = new LoginForm();
		try {
		    UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		
        // JFrame erstellen
		
        JFrame frame = new JFrame("Login");
        frame.setSize(400, 200);
        JLabel label = new JLabel();
        label.setBounds(0, 0,100, 200);
        frame.getContentPane().add(label);
        Image image = new ImageIcon(lg.getClass().getResource("/HaushaltKasse.jpg")).getImage();
        label.setIcon(new ImageIcon(image));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        

        // Benutzername-Label und -Textfeld
        
        JLabel usernameLabel = new JLabel("Benutzername:");
        usernameLabel.setBounds(110, 30, 100, 25);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(230, 30, 150, 25);

        // Passwort-Label und -Feld
        
        JLabel passwordLabel = new JLabel("Passwort:");
        passwordLabel.setBounds(110, 70, 100, 25);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(230, 70, 150, 25);

        // Login-Button
        
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(230, 110, 80, 30);

        // Fehlermeldungs-Label
        
        JLabel messageLabel = new JLabel("");
        messageLabel.setBounds(100, 140, 300, 25);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Button-ActionListener 
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                //String password = passwordField.getSelectedText();
                //String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                //System.out.println(username+" "+hashedPassword+" "+BCrypt.hashpw("p@$$w.rd", BCrypt.gensalt()));
               // System.out.println(BCrypt.checkpw(password, hashedPassword));
             
                

                try
				{
					if (authenticate(username, hashPassword(password))) {
					    JOptionPane.showMessageDialog(frame, "Login erfolgreich!");
					    frame.dispose(); // Login-Fenster schließen
					    lg.openMainApplication(); // Hauptanwendung öffnen
					} else {
					    messageLabel.setText("Ungültiger Benutzername oder Passwort.");
					}
				} catch (HeadlessException | ClassNotFoundException | SQLException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        // Elemente zum Frame hinzufügen
        
        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(messageLabel);

        // Frame sichtbar machen
        frame.setVisible(true);
    }
	//hash password
	public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            // Convert the byte array to a Base64 encoded string
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Authentifizierungsmethode
    private static boolean authenticate(String username, String password) throws ClassNotFoundException {
        // Beispiel: Datenbankabfrage
        PreparedStatement ps; 
        try {
        	Mysql.connect(); 
        	ps = Mysql.con.prepareStatement("SELECT * FROM haushaltmitglieder WHERE userName = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet resultSet = ps.executeQuery()) {
                return resultSet.next(); // True, wenn ein Benutzer gefunden wurde
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        /*finally {
        	Mysql.disconnect();
        	
        }*/
    }

  
	// Hauptanwendung öffnen
    private void openMainApplication() throws ClassNotFoundException, SQLException {
    	HaushaltKasse frame = new HaushaltKasse();
		frame.setVisible(true);
		frame.setTitle("Haushaltkasse");
    }
	
}


package armameeldoparti.frames;

import armameeldoparti.utils.Main;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;
import net.miginfocom.swing.MigLayout;

/**
 * Clase correspondiente a la ventana del menú principal del programa.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 3.0.0
 *
 * @since 27/02/2021
 */
public class MainFrame extends JFrame {

    // ---------------------------------------- Constantes públicas ------------------------------

    /**
     * Imagen estándar del icono de la aplicación.
     */
    public static final ImageIcon ICON = new ImageIcon(MainFrame.class
                                                                .getClassLoader()
                                                                .getResource(Main.IMG_PATH + Main.ICON_FILENAME));

    /**
     * Imagen escalada del icono de la aplicación.
     */
    public static final ImageIcon SCALED_ICON = new ImageIcon(ICON.getImage()
                                                                  .getScaledInstance(50, 50, Image.SCALE_SMOOTH));

    // ---------------------------------------- Constantes privadas ------------------------------

    /**
     * Configuración utilizada frecuentemente.
     */
    private static final String GROWX = "growx";

    /**
     * Nombre del archivo de imagen de fondo para el menú principal.
     */
    private static final String BG_IMG_FILENAME = "bg.png";

    // ---------------------------------------- Campos privados ----------------------------------

    private JButton startButton;
    private JButton helpButton;

    private Font programFont;

    // ---------------------------------------- Constructor --------------------------------------

    /**
     * Construye la ventana principal.
     */
    public MainFrame() {
        setGUIProperties();

        ImageIcon bgImage = new ImageIcon(this.getClass()
                                              .getClassLoader()
                                              .getResource(Main.IMG_PATH + BG_IMG_FILENAME));

        JLabel bgLabel = new JLabel("", bgImage, SwingConstants.CENTER);

        JPanel panel = new JPanel(new MigLayout("wrap"));

        startButton = new JButton("Comenzar");
        helpButton = new JButton("Ayuda");

        startButton.setEnabled(true);
        startButton.addActionListener(e -> {
            try {
                InputFrame inputFrame = new InputFrame(MainFrame.this, Main.PLAYERS_PER_TEAM);

                inputFrame.setVisible(true);

                MainFrame.this.setVisible(false);
                MainFrame.this.setLocationRelativeTo(null);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        });

        helpButton.setEnabled(true);
        helpButton.addActionListener(e -> help());

        panel.add(bgLabel, GROWX);
        panel.add(startButton, GROWX);
        panel.add(helpButton, GROWX);
        panel.setBackground(Main.LIGHT_GREEN);

        add(panel);
        setResizable(false);
        setTitle(Main.PROGRAM_TITLE + " " + Main.PROGRAM_VERSION);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(ICON.getImage());
        pack();
        setLocationRelativeTo(null);
    }

    // ---------------------------------------- Métodos privados ---------------------------------

    /**
     * Despliega las instrucciones de uso del programa.
     */
    private void help() {
        HelpFrame helpFrame = new HelpFrame(MainFrame.this);

        helpFrame.setVisible(true);

        MainFrame.this.setVisible(false);
        MainFrame.this.setLocationRelativeTo(null);
    }

    /**
     * Configura las propiedades de la interfaz gráfica del programa.
     */
    private void setGUIProperties() {
        UIManager.put("OptionPane.background", Main.LIGHT_GREEN);
        UIManager.put("Panel.background", Main.LIGHT_GREEN);
        UIManager.put("CheckBox.background", Main.LIGHT_GREEN);
        UIManager.put("Separator.background", Main.LIGHT_GREEN);
        UIManager.put("Button.background", Main.DARK_GREEN);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("CheckBox.focus", Main.LIGHT_GREEN);
        UIManager.put("Button.focus", Main.DARK_GREEN);
        UIManager.put("ToggleButton.focus", Main.DARK_GREEN);
        UIManager.put("ComboBox.focus", Color.WHITE);

        try {
            // Se registra la fuente para poder utilizarla
            programFont = Font.createFont(Font.TRUETYPE_FONT,
                                          this.getClass()
                                              .getClassLoader()
                                              .getResourceAsStream(Main.TTF_PATH + Main.FONT_NAME))
                              .deriveFont(Main.FONT_SIZE);

            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(programFont);
        } catch (IOException | FontFormatException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        setUIFont(programFont);
    }

    /**
     * Aplica la fuente para el programa.
     *
     * @param f Fuente a utilizar.
     */
    private void setUIFont(Font f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();

        while (keys.hasMoreElements()) {
            Object k = keys.nextElement();
            Object value = UIManager.get(k);

            if (value instanceof FontUIResource) {
                UIManager.put(k, f);
            }
        }
    }
}
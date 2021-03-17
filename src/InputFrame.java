/**
 * Clase correspondiente a la ventana de ingreso
 * de nombres de jugadores y mezcla de los mismos
 * para el sorteo de los equipos.
 * 
 * @author Bonino, Francisco Ignacio.
 * 
 * @version 2.0.0
 * 
 * @since 28/02/2021
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class InputFrame extends JFrame implements ActionListener {

    // Constantes privadas.
    private static final int frameWidth = 450; // Ancho de la ventana.
    private static final int frameHeight = 375; // Alto de la ventana.
    private static final String imagesPath = "src/graphics/"; // Carpeta donde buscar las imágenes.
    private static final String[] optionsComboBox = { "Agregar defensores centrales", // Opciones para el menú desplegable.
                                                      "Agregar defensores laterales",
                                                      "Agregar mediocampistas",
                                                      "Agregar delanteros",
                                                      "Agregar comodines" };
    private static final String[] optionsMix = { "Aleatoriamente", "Por puntajes" }; // Opciones de distribución de
                                                                                     // jugadores.
    private static final Rectangle labelPosition = new Rectangle(341, 100, 85, 85); // Dimensión y posición para las
                                                                                    // imágenes.
    private static final Color bgColor = new Color(200, 200, 200); // Color de fondo de la ventana.

    // Campos privados.
    private ArrayList<JTextField> textFieldCD, textFieldLD, textFieldMF, textFieldFW, textFieldWC; // Arreglos de campos
                                                                                                   // de texto para
                                                                                                   // ingresar nombres.
    private Player[] setCD, setLD, setMF, setFW, setWC; // Arreglos que almacenan los nombres de los jugadores de cada
                                                        // posición.
    private List<Player[]> playersSets; // Lista con los arreglos de jugadores para reducir líneas de código en ciertos
                                        // métodos.
    private EnumMap<Position, Integer> playersAmountMap; // Mapa que asocia a cada posición un valor numérico (cuántos
                                                         // jugadores por posición por equipo).
    private ImageIcon icon, smallIcon; // Iconos para las ventanas.
    private JLabel cdLabel, ldLabel, mfLabel, fwLabel, wcLabel; // Imágenes para cada posición.
    private JTextArea textArea; // Área de texto donde se mostrarán los jugadores añadidos en tiempo real.
    private JButton mixButton; // Botón para mezclar jugadores.
    private JCheckBox anchor; // Checkbox de anclaje de jugadores a un mismo equipo.
    private JComboBox<String> comboBox; // Menú desplegable.
    private JPanel panel; // Panel para la ventana de mezcla.
    private AnchorageFrame anchorageFrame; // Ventana de anclaje de jugadores.
    private ResultFrame resultFrame; // Ventana de resultados.

    /**
     * Se crea la ventana de mezcla.
     * 
     * @param playersAmount Cantidad de jugadores por equipo.
     * @param icon          Ícono para la ventana.
     * 
     * @throws IOException Cuando hay un error de lectura en los archivos PDA.
     */
    public InputFrame(int playersAmount, ImageIcon icon) throws IOException {
        this.icon = icon;

        playersAmountMap = new EnumMap<>(Position.class);

        collectPDData(playersAmount);

        initializeComponents("Ingreso de jugadores - Fútbol " + playersAmount);
    }

    // ----------------------------------------Métodos privados---------------------------------

    /**
     * Este método rescata la cantidad de jugadores para cada posición por equipo
     * mediante expresiones regulares.
     * 
     * X[CLMFW].>+.[0-9] : Matchea las líneas que comiencen con la cantidad de jugadores por equipo,
     * seguido por C, L, M, F, ó W, seguido por al menos un caracter '>', y luego tengan algún número.
     * 
     * [0-9][A-Z].>+. : Matchea el trozo de la línea que no es un número que nos interese.
     * 
     * ¡¡¡IMPORTANTE!!!
     * 
     * Si el archivo .PDA es modificado en cuanto a orden de las líneas
     * importantes (C >> NÚMERO, etc.), se debe tener en cuenta que
     * Position.values()[index] confía en que lo hallado se corresponde con el orden
     * en el que están declarados los valores en el enum Position. Idem, si se
     * cambian de orden los valores del enum Position, se deberá tener en cuenta que
     * Position.values()[index] confía en el orden en el que se leerán los datos del
     * archivo .PDA y, por consiguiente, se deberá rever el orden de las líneas
     * importantes de dichos archivos.
     * 
     * @param playersAmount Cantidad de jugadores por equipo.
     * 
     * @throws IOException Si el archivo no existe.
     */
    private void collectPDData(int playersAmount) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("useful/DIST.PDA"))) {
            String line;
            int index = 0;

            while ((line = br.readLine()) != null)
                if (line.matches(playersAmount + "[CLMFW].>+.[0-9]")) {
                    playersAmountMap.put(Position.values()[index], Integer.parseInt(line.replaceAll("[0-9][A-Z].>+.", "")));
                    index++;
                }
        }
    }

    /**
     * Este método inicializa el conjunto de jugadores recibido con jugadores sin
     * nombre y la posición recibida.
     * 
     * @param set      Arreglo de jugadores a inicializar.
     * @param position Posición de los jugadores del arreglo.
     */
    private void initializeSet(Player[] set, Position position) {
        for (int i = 0; i < set.length; i++)
            set[i] = new Player("", position);
    }

    /**
     * Este método se encarga de inicializar los componentes de la ventana de
     * mezcla.
     * 
     * @param frameTitle Título de la ventana.
     */
    private void initializeComponents(String frameTitle) {
        textFieldCD = new ArrayList<>();
        textFieldLD = new ArrayList<>();
        textFieldMF = new ArrayList<>();
        textFieldFW = new ArrayList<>();
        textFieldWC = new ArrayList<>();

        setCD = new Player[(int) (playersAmountMap.get(Position.CENTRALDEFENDER) * 2)];
        setLD = new Player[(int) (playersAmountMap.get(Position.LATERALDEFENDER) * 2)];
        setMF = new Player[(int) (playersAmountMap.get(Position.MIDFIELDER) * 2)];
        setFW = new Player[(int) (playersAmountMap.get(Position.FORWARD) * 2)];
        setWC = new Player[(int) (playersAmountMap.get(Position.WILDCARD) * 2)];

        initializeSet(setCD, Position.CENTRALDEFENDER);
        initializeSet(setLD, Position.LATERALDEFENDER);
        initializeSet(setMF, Position.MIDFIELDER);
        initializeSet(setFW, Position.FORWARD);
        initializeSet(setWC, Position.WILDCARD);

        playersSets = Arrays.asList(setCD, setLD, setMF, setFW, setWC);

        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(frameTitle);
        setResizable(false);
        setIconImage(icon.getImage());

        panel = new JPanel();
        panel.setBounds(0, 0, frameWidth, frameHeight);
        panel.setLayout(null);

        addTextFields(Position.CENTRALDEFENDER, textFieldCD, setCD);
        addTextFields(Position.LATERALDEFENDER, textFieldLD, setLD);
        addTextFields(Position.MIDFIELDER, textFieldMF, setMF);
        addTextFields(Position.FORWARD, textFieldFW, setFW);
        addTextFields(Position.WILDCARD, textFieldWC, setWC);

        addAnchorCheckBox();

        cdLabel = new JLabel();
        ldLabel = new JLabel();
        mfLabel = new JLabel();
        fwLabel = new JLabel();
        wcLabel = new JLabel();

        addImage(cdLabel, "cd.jpg", panel);
        addImage(ldLabel, "ld.jpg", panel);
        addImage(mfLabel, "mf.jpg", panel);
        addImage(fwLabel, "fw.jpg", panel);
        addImage(wcLabel, "wc.jpg", panel);

        addComboBox();

        addButtons();

        addTextArea();

        panel.setBackground(bgColor);

        add(panel);
    }

    /**
     * Este método se encarga de crear, almacenar y configurar los campos de texto
     * correspondientes a cada posición.
     * 
     * @param position     Posición a buscar en el EnumMap.
     * @param textFieldSet Arreglo de campos de texto para cada posición.
     * @param playersSet   Arreglo de jugadores donde se almacenarán los nombres
     *                     ingresados en los campos de texto.
     */
    private void addTextFields(Position position, ArrayList<JTextField> textFieldSet, Player[] playersSet) {
        for (int i = 0; i < (playersAmountMap.get(position) * 2); i++) {
            JTextField aux = new JTextField();

            aux.setBounds(5, (45 * (i + 1)), 201, 30);

            aux.addActionListener(new ActionListener() {
                int index; // Índice que indica el campo de texto donde se ingresó el nombre.

                /**
                 * En este método se evalúa que el string ingresado como nombre de jugador sea
                 * válido. Una vez validado, se chequea según el campo de texto si tal jugador
                 * está en el arreglo correspondiente o no. Si lo está, se lo reemplaza por un
                 * nuevo jugador con el nombre cambiado. Si no está, simplemente se crea un
                 * nuevo jugador con el nombre ingresado.
                 * 
                 * @param e Evento ocurrido (nombre ingresado).
                 */
                public void actionPerformed(ActionEvent e) {
                    JTextField auxTF = (JTextField) e.getSource();

                    for (index = 0; index < textFieldSet.size(); index++)
                        if (auxTF == textFieldSet.get(index))
                            break;

                    // Nombre sin espacios ni al principio ni al fin, en mayúsculas,
                    // y cualquier espacio intermedio es reemplazado por un guión bajo.
                    String name = aux.getText().trim().toUpperCase().replaceAll(" ", "_");

                    if (name.length() == 0 || name.length() > 12 || isEmptyString(name) || alreadyExists(name))
                        JOptionPane.showMessageDialog(null,
                                "El nombre del jugador no puede estar vacío, tener más de 12 caracteres o estar repetido",
                                "¡Error!", JOptionPane.ERROR_MESSAGE, null);
                    else {
                        playersSet[index].setName(name);

                        updateTextArea();

                        mixButton.setEnabled(checkMixButton());
                    }
                }
            });

            textFieldSet.add(aux);
        }

        for (int i = 0; i < textFieldSet.size(); i++)
            panel.add(textFieldSet.get(i));
    }

    /**
     * Este método se encarga de agregar al panel las imágenes correspondientes a
     * cada posición cuya visibilidad se toggleará en base al item seleccionado en
     * la lista desplegable.
     * 
     * @param label    Etiqueta donde se seteará la imagen.
     * @param fileName Nombre del archivo a buscar.
     * @param panel    Panel donde se agregará la imagen.
     */
    private void addImage(JLabel label, String fileName, JPanel panel) {
        label.setIcon(new ImageIcon(imagesPath + fileName));
        label.setBounds(labelPosition);
        label.setVisible(true);

        panel.add(label);
    }

    /**
     * Este método se encarga de agregar la lista desplegable al frame, y setear el
     * handler de eventos a la misma.
     */
    private void addComboBox() {
        comboBox = new JComboBox<>(optionsComboBox);

        comboBox.setBounds(5, 5, 200, 30);
        comboBox.addActionListener(this);

        updateOutput(comboBox.getSelectedItem().toString()); // Se muestra el output correspondiente al estado inicial
                                                             // del JComboBox.

        panel.add(comboBox);
    }

    /**
     * Este método se encarga de añadir los botones al panel.
     */
    private void addButtons() {
        smallIcon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        mixButton = new JButton("Mezclar");

        mixButton.setBounds(224, 274, 100, 30);
        mixButton.setEnabled(false);
        mixButton.setVisible(true);
        mixButton.addActionListener(new ActionListener() {
            /**
             * Este método se encarga de tomar el criterio de búsqueda especificado por el
             * usuario. Además, se chequea si se deben anclar jugadores y se trabaja en base
             * a eso.
             * 
             * @param e Evento (criterio elegido).
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int distribution = JOptionPane.showOptionDialog(null,
                        "Seleccione el criterio de distribución de jugadores", "Antes de continuar...", 2,
                        JOptionPane.QUESTION_MESSAGE, smallIcon, optionsMix, optionsMix[0]);

                if (distribution == 0 || (distribution != JOptionPane.CLOSED_OPTION)) {
                    if (anchor.isSelected()) {
                        anchorageFrame = new AnchorageFrame(InputFrame.this.icon, InputFrame.this.playersSets, distribution,
                                InputFrame.this);

                        anchorageFrame.addWindowListener(new WindowEventsHandler(InputFrame.this));
                        anchorageFrame.setVisible(true);
                    } else {
                        resultFrame = new ResultFrame(distribution, icon, playersSets);

                        resultFrame.addWindowListener(new WindowEventsHandler(InputFrame.this));
                        resultFrame.setVisible(true);
                    }
                }
            }
        });

        panel.add(mixButton);
    }

    /**
     * Este método se encarga de añadir al panel el campo de texto de sólo lectura
     * donde se mostrarán los nombres de jugadores ingresados por el usuario.
     */
    private void addTextArea() {
        textArea = new JTextArea();

        textArea.setBounds(215, 5, 118, 260);
        textArea.setBorder(BorderFactory.createBevelBorder(1));
        textArea.setEditable(false);
        textArea.setVisible(true);

        panel.add(textArea);

        updateTextArea();
    }

    /**
     * Este método se encarga de actualizar lo mostrado en la ventana en base al
     * item seleccionado en la lista desplegable.
     * 
     * @param text Opción seleccionada del arreglo de Strings 'optionsComboBox'.
     */
    private void updateOutput(String text) {
        switch (text) {
        case "Agregar defensores centrales": {
            textFieldCD.forEach(tf -> tf.setVisible(true));
            textFieldLD.forEach(tf -> tf.setVisible(false));
            textFieldMF.forEach(tf -> tf.setVisible(false));
            textFieldFW.forEach(tf -> tf.setVisible(false));
            textFieldWC.forEach(tf -> tf.setVisible(false));

            cdLabel.setVisible(true);
            ldLabel.setVisible(false);
            mfLabel.setVisible(false);
            fwLabel.setVisible(false);
            wcLabel.setVisible(false);

            break;
        }

        case "Agregar defensores laterales": {
            textFieldCD.forEach(tf -> tf.setVisible(false));
            textFieldLD.forEach(tf -> tf.setVisible(true));
            textFieldMF.forEach(tf -> tf.setVisible(false));
            textFieldFW.forEach(tf -> tf.setVisible(false));
            textFieldWC.forEach(tf -> tf.setVisible(false));

            cdLabel.setVisible(false);
            ldLabel.setVisible(true);
            mfLabel.setVisible(false);
            fwLabel.setVisible(false);
            wcLabel.setVisible(false);

            break;
        }

        case "Agregar mediocampistas": {
            textFieldCD.forEach(tf -> tf.setVisible(false));
            textFieldLD.forEach(tf -> tf.setVisible(false));
            textFieldMF.forEach(tf -> tf.setVisible(true));
            textFieldFW.forEach(tf -> tf.setVisible(false));
            textFieldWC.forEach(tf -> tf.setVisible(false));

            cdLabel.setVisible(false);
            ldLabel.setVisible(false);
            mfLabel.setVisible(true);
            fwLabel.setVisible(false);
            wcLabel.setVisible(false);

            break;
        }

        case "Agregar delanteros": {
            textFieldCD.forEach(tf -> tf.setVisible(false));
            textFieldLD.forEach(tf -> tf.setVisible(false));
            textFieldMF.forEach(tf -> tf.setVisible(false));
            textFieldFW.forEach(tf -> tf.setVisible(true));
            textFieldWC.forEach(tf -> tf.setVisible(false));

            cdLabel.setVisible(false);
            ldLabel.setVisible(false);
            mfLabel.setVisible(false);
            fwLabel.setVisible(true);
            wcLabel.setVisible(false);

            break;
        }

        default: {
            textFieldCD.forEach(tf -> tf.setVisible(false));
            textFieldLD.forEach(tf -> tf.setVisible(false));
            textFieldMF.forEach(tf -> tf.setVisible(false));
            textFieldFW.forEach(tf -> tf.setVisible(false));
            textFieldWC.forEach(tf -> tf.setVisible(true));

            cdLabel.setVisible(false);
            ldLabel.setVisible(false);
            mfLabel.setVisible(false);
            fwLabel.setVisible(false);
            wcLabel.setVisible(true);

            break;
        }
        }
    }

    /**
     * Indica si una cadena está vacía o no. Si la cadena está compuesta por
     * caracteres en blanco (espacios), se la tomará como vacía.
     * 
     * @param string Cadena a analizar
     * 
     * @return Si la cadena está vacía o no.
     */
    private boolean isEmptyString(String string) {
        char[] charArray = string.toCharArray();

        for (int i = 0; i < charArray.length; i++)
            if (charArray[i] != ' ')
                return false;

        return true;
    }

    /**
     * Este método se encarga de chequear si un nombre está repetido en un arreglo
     * de jugadores.
     * 
     * @param name Nombre a chequear.
     * 
     * @return Si hay algún jugador con el mismo nombre.
     */
    private boolean alreadyExists(String name) {
        for (int i = 0; i < playersSets.size(); i++)
            for (int j = 0; j < playersSets.get(i).length; j++)
                if(playersSets.get(i)[j].getName().equals(name))
                    return true;

        return false;
    }

    /**
     * Este método se encarga de actualizar el texto mostrado en el campo de sólo
     * lectura. Se muestran los jugadores ingresados en el orden en el que estén
     * posicionados en sus respectivos arreglos. El orden en el que se muestran es:
     * Defensores centrales > Defensores laterales > Mediocampistas > Delanteros >
     * Comodines.
     */
    private void updateTextArea() {
        int counter = 0;

        textArea.setText(null);

        for (int i = 0; i < playersSets.size(); i++)
            for (int j = 0; j < playersSets.get(i).length; j++)
                if(!playersSets.get(i)[j].getName().equals("")) {
                    textArea.append(" " + (counter + 1) + ". " + playersSets.get(i)[j].getName() + "\n");
                    counter++;
                }
    }

    /**
     * Este método se encarga de chequear si se han ingresado los nombres de todos
     * los jugadores para habilitar el botón de mezcla.
     */
    private boolean checkMixButton() {
        for (int i = 0; i < playersSets.size(); i++)
            for (int j = 0; j < playersSets.get(i).length; j++)
                if(playersSets.get(i)[j].getName().equals(""))
                    return false;

        return true;
    }

    /**
     * Este método se encarga de agregar el checkbox de anclaje de jugadores a un
     * mismo equipo en el panel del frame.
     */
    private void addAnchorCheckBox() {
        anchor = new JCheckBox("Anclar jugadores", false);

        anchor.setBounds(212, 310, 122, 20);
        anchor.setBackground(bgColor);
        anchor.setVisible(true);

        panel.add(anchor);
    }

    // ----------------------------------------Métodos públicos---------------------------------

    /**
     * Handler para los eventos ocurridos de la lista desplegable. Se trata la
     * fuente del evento ocurrido como un JComboBox y se trata como un String el
     * item seleccionado en el mismo para pasarlo al método updateOutput.
     * 
     * @param e Evento ocurrido (item seleccionado).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        updateOutput((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
    }
}
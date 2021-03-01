/**
 * @author Bonino, Francisco Ignacio.
 * 
 * @version 1.0.0
 * 
 * @since 28/02/2021
 */

import javax.swing.JFrame;

public class MixFrame extends JFrame {

    // Campos privados.

    /**
     * Constructor.
     */
    public MixFrame() {
        setSize(200, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("MixFrame");
        setVisible(true);
        setResizable(false);
    }
}
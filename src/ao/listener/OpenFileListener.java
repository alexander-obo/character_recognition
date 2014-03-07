package ao.listener;

import ao.ApplicationPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenFileListener implements ActionListener {

    private final JFileChooser fileChooser = new JFileChooser();
    private final FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
    private final ApplicationPanel applicationPanel;

    public OpenFileListener(ApplicationPanel applicationPanel) {
        this.applicationPanel = applicationPanel;
        fileChooser.setFileFilter(filter);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage image = ImageIO.read(fileChooser.getSelectedFile());
                if (image != null) {
                    applicationPanel.setImage(image);
                    applicationPanel.updateDisplayedImage();
                } else {
                    JOptionPane.showMessageDialog(null, "File cannot be read as an image.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(null, "Error reading image.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

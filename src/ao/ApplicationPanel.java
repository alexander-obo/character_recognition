package ao;

import ao.listener.FilterListener;
import ao.listener.OpenFileListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import util.OtsuBinarize;
import util.ZongSyn;

public class ApplicationPanel extends JPanel {

    private final GridBagLayout gridBagLayout = new GridBagLayout();
    private final GridBagConstraints gridBagConstraints = new GridBagConstraints();
    private final JLabel sourceLabel = new JLabel("Source");
    private final JButton selectImageButton = new JButton("Select image");
    private final JLabel displayedImage = new JLabel();
    private BufferedImage image;
    private final JButton filterButton = new JButton("Filter");
    private final JLabel targetLabel = new JLabel("Target");
    private final JLabel filteredImage = new JLabel();

    public ApplicationPanel() {
        init();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void updateDisplayedImage() {
        if (image != null) {
            displayedImage.setIcon(new ImageIcon(image));
            filterButton.setVisible(true);
        }
    }

    public void updateFilteredImage() {
        if (image != null) {
            BufferedImage binarizedImage = OtsuBinarize.binarize(image);
            WritableRaster binarizedData = binarizedImage.getRaster();
            int[][] matrix = new int[binarizedData.getHeight()][binarizedData.getWidth()];
            for (int y = 0; y < binarizedData.getHeight(); y++) {
                for (int x = 0; x < binarizedData.getWidth(); x++) {
                    int[] a = new int[3];
                    a = binarizedData.getPixel(x, y, a);
                    //255 - white, 0 - black
                    if (a[0] == 255) {
                        matrix[y][x] = 0;
                    } else {
                        matrix[y][x] = 1;
                    }
                }
            }
            binarizedData = ZongSyn.skeletonizaciya(matrix, binarizedData);
            filteredImage.setIcon(new ImageIcon(binarizedImage));
            updateUI();
        }
    }

    private void init() {
        setLayout(gridBagLayout);
        selectImageButton.addActionListener(new OpenFileListener(this));
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(sourceLabel, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        add(selectImageButton, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        add(displayedImage, gridBagConstraints);
        gridBagConstraints.gridy = 3;
        filterButton.setVisible(false);
        filterButton.addActionListener(new FilterListener(this));
        add(filterButton, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(targetLabel, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        add(filteredImage, gridBagConstraints);
    }
}

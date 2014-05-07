package ao.ui;

import ao.ui.listener.GibridButtonListener;
import ao.ui.listener.OpenFileListener;
import ao.ui.listener.ZongSyenButtonListener;
import ao.util.OtsuBinarize;
import ao.util.ZongSyn;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ApplicationPanel extends JPanel {

    private final GridBagLayout gridBagLayout = new GridBagLayout();
    private final GridBagConstraints gridBagConstraints = new GridBagConstraints();
    private final JLabel sourceImageLabel = new JLabel("Исходное изображение");
    private final JLabel displayedSourceImage = new JLabel();
    private final JButton selectImageButton = new JButton("Выбрать изображение");
    private BufferedImage image;
    private final JButton zongSyenButton = new JButton("Старт алгоритма Зонга-Суня");
    private final JButton gibridButton = new JButton("Старт гибридного алгоритма");
    private final JLabel zongSyenLabel = new JLabel("Алгоритм Зонга-Суня");
    private final JLabel zongSyenImage = new JLabel();
    private final JLabel gibridLabel = new JLabel("Гибридный алгоритм");
    private final JLabel gibridImage = new JLabel();

    public ApplicationPanel() {
        init();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void updateDisplayedSourceImage() {
        if (image != null) {
            displayedSourceImage.setIcon(new ImageIcon(image));
            zongSyenButton.setVisible(true);
            gibridButton.setVisible(true);
        }
    }

    public void updateZongSyenImage() {
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
            binarizedData = ZongSyn.corruptSkeletonization(matrix, binarizedData);
            zongSyenImage.setIcon(new ImageIcon(binarizedImage));
            updateUI();
        }
    }

    public void updateGibridImage() {
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
            binarizedData = ZongSyn.skeletonization(matrix, binarizedData);
            gibridImage.setIcon(new ImageIcon(binarizedImage));
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
        add(sourceImageLabel, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        add(displayedSourceImage, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        add(selectImageButton, gridBagConstraints);

        gridBagConstraints.gridy = 3;
        zongSyenButton.setVisible(false);
        add(zongSyenButton, gridBagConstraints);

        gridBagConstraints.gridy = 4;
        gibridButton.setVisible(false);
        add(gibridButton, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(zongSyenLabel, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        add(zongSyenImage, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        add(gibridLabel, gridBagConstraints);

        gridBagConstraints.gridy = 3;
        add(gibridImage, gridBagConstraints);

        zongSyenButton.addActionListener(new ZongSyenButtonListener(this));
        gibridButton.addActionListener(new GibridButtonListener(this));
    }
}

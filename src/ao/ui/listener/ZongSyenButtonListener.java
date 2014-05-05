package ao.ui.listener;

import ao.ui.ApplicationPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZongSyenButtonListener implements ActionListener {

    private final ApplicationPanel applicationPanel;

    public ZongSyenButtonListener(ApplicationPanel applicationPanel) {
        this.applicationPanel = applicationPanel;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        applicationPanel.updateZongSyenImage();
    }
}

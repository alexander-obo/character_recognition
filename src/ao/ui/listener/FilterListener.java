package ao.ui.listener;

import ao.ui.ApplicationPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FilterListener implements ActionListener {

    private final ApplicationPanel applicationPanel;

    public FilterListener(ApplicationPanel applicationPanel) {
        this.applicationPanel = applicationPanel;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        applicationPanel.updateFilteredImage();
    }
}

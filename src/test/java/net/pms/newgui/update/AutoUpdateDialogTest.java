package net.pms.newgui.update;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import net.pms.update.AutoUpdater;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import java.awt.*;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AutoUpdateDialogTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Window parent;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AutoUpdater autoUpdater;

    @Before
    public void setUp() throws ConfigurationException {
        // Silence all log messages from the UMS code that is being tested
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.OFF);

        // Ensure the auto updater window should be visible
        when(autoUpdater.getState()).thenReturn(AutoUpdater.State.DOWNLOAD_IN_PROGRESS);
    }

    /**
     * Test that the dialog is modeless (i.e. does not take precedence over other windows).
     */
    @Test
    public void dialogIsModeless() {
        AutoUpdateDialog dialog = new AutoUpdateDialog(parent, autoUpdater);
        assertTrue(dialog.getModalityType() == Dialog.ModalityType.MODELESS);
    }

    /**
     * Test that the dialog has no parent window.
     */
    @Test
    public void dialogHasNoParent() {
        AutoUpdateDialog dialog = new AutoUpdateDialog(parent, autoUpdater);
        assertNull(dialog.getParent());
    }

    /**
     * Test that despite the dialog having no parent window, it is still initially placed "inside" the parent.
     */
    @Test
    public void dialogIsPlacedInsideParent() {
        when(parent.isShowing()).thenReturn(true);
        new AutoUpdateDialog(parent, autoUpdater);
        verify(parent).getLocationOnScreen();
    }

}

package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class ColorEditorTest
{
    private ColorEditor defEditor;
    
    private JTextField  textEditor;
    private JButton     colorButton;
    private JComponent  feedbackWindow;
    
    /** The dialog containing the color chooser. */
    private JDialog         chooserDialog;
    /** The JColorChooser component of the chooserDialog. */
    private JColorChooser   chooser;
    /** The OK button on the color chooser dialog. */
    private JButton         chooserOKButton;
    /** The cancel button on the color chooser dialog. */
    private JButton         chooserCancelButton;
    
    private boolean     actionListener1Fired;
    private boolean     actionListener2Fired;
    
    @BeforeEach
    public void beforeEach()
    {
        GUIUtils.schedEDTAndWait( () -> {
            defEditor = new ColorEditor();
            textEditor = defEditor.getTextEditor();
            colorButton = defEditor.getColorButton();
            feedbackWindow = defEditor.getFeedback();
        });
        actionListener1Fired = false;
        actionListener2Fired = false;
    }

    @Test
    void testColorEditor()
    {
        assertNotNull( textEditor );
        assertNotNull( colorButton );
        assertNotNull( feedbackWindow );
    }

    @Test
    void testAddActionListener()
    {
        GUIUtils.schedEDTAndWait( () -> {
            defEditor.addActionListener( e -> actionListener1Fired = true );
            commitEdit();
            assertTrue( actionListener1Fired );
            assertFalse( actionListener2Fired );
    
            actionListener1Fired = false;
            actionListener2Fired = false;
            defEditor.addActionListener( e -> actionListener2Fired = true );
            commitEdit();
            assertTrue( actionListener1Fired );
            assertTrue( actionListener2Fired );
        });
    }

    @Test
    void testRemoveActionListener()
    {
        ActionListener  listener1   = e -> actionListener1Fired = true;
        ActionListener  listener2   = e -> actionListener2Fired = true;
        GUIUtils.schedEDTAndWait( () -> {
            defEditor.addActionListener( listener1 );
            defEditor.addActionListener( listener2 );
            commitEdit();
            assertTrue( actionListener1Fired );
            assertTrue( actionListener2Fired );
    
            actionListener1Fired = false;
            actionListener2Fired = false;
            defEditor.removeActionListener( listener2 );
            commitEdit();
            assertTrue( actionListener1Fired );
            assertFalse( actionListener2Fired );
    
            actionListener1Fired = false;
            actionListener2Fired = false;
            defEditor.removeActionListener( listener1 );
            commitEdit();
            assertFalse( actionListener1Fired );
            assertFalse( actionListener2Fired );
        });
    }

    @Test
    void testGetPanel()
    {
        // Just make sure we get back a JPanel containing all three 
        // ColorEditor components somewhere in the panel's window hierarchy.
        
        Predicate<JComponent>   isTextEditor    = c -> (c == textEditor);
        Predicate<JComponent>   isColorButton   = c -> (c == colorButton);
        Predicate<JComponent>   isFeedback      = c -> (c == feedbackWindow);
        GUIUtils.schedEDTAndWait( () -> {
            JPanel  panel   = defEditor.getPanel();
            assertNotNull( ComponentFinder.find( panel, isTextEditor ) );
            assertNotNull( ComponentFinder.find( panel, isColorButton ) );
            assertNotNull( ComponentFinder.find( panel, isFeedback ) );
        });
    }
    
    @Test
    public void testGetTextEditor()
    {
        GUIUtils.schedEDTAndWait( () -> {
            int testRGB = getUniqueRGB();
            textEditor.setText( "" + testRGB );        
            commitEdit();
            assertEquals( testRGB, getFeedbackRGB() );
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    void testGetColorButton( int iRGB )
    {
        // startColorSelector pokes the colorButton which causes
        // the chooser dialog to be posted.
        Thread  thread      = startColorSelector();
        Color   testColor   = new Color( iRGB );
        GUIUtils.schedEDTAndWait( () -> {
            chooser.setColor( testColor );
            chooserOKButton.doClick();
        });
        Utils.join( thread );
        GUIUtils.schedEDTAndWait( () ->
            assertEquals( iRGB, getFeedbackRGB() )
        );
        
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    void testSelectColorAndCancel( int iRGB )
    {
        // start color selector and select a color; poke the cancel
        // button, and verify that the selected color is not applied.
        // the chooser dialog to be posted.
        Thread  thread      = startColorSelector();
        Color   testColor   = new Color( iRGB );
        GUIUtils.schedEDTAndWait( () -> {
            chooser.setColor( testColor );
            chooserCancelButton.doClick();
        });
        Utils.join( thread );
        GUIUtils.schedEDTAndWait( () -> {
            assertEquals( iRGB, getFeedbackRGB() );
        });
    }

    @Test
    void testGetColor()
    {
        int     testRGB     = getUniqueRGB();
        textEditor.setText( "" + testRGB );        
        commitEdit();
        
        int     actRGB      = getFeedbackRGB();
        assertEquals( testRGB, actRGB );
    }
    
    @Test
    public void testEditTextNeg()
    {
        // Enter an invalid value into the text editor, and
        // verify that the GUI behaves accordingly.
        Color   origColor   = getFeedbackColor();
        GUIUtils.schedEDTAndWait( () ->  textEditor.setText( "invalid" ) );
        commitEdit();
        GUIUtils.schedEDTAndWait( () -> {
            String  actText = textEditor.getText().toUpperCase();
            assertTrue( actText.contains( "ERROR" ) );
        });
        assertEquals( origColor, getFeedbackColor() );
    }
    
    // Assumption: this method is called from the EDT.
    private Color getFeedbackColor()
    {
        Color   bgColor = feedbackWindow.getBackground();
        return bgColor;
    }
    
    private int getUniqueRGB()
    {
        int fRGB    = getFeedbackRGB();
        int rgb     = fRGB ^ 0xff;
        assertNotEquals( fRGB, rgb );
        return rgb;
    }
    
    private int getFeedbackRGB()
    {
        int rgb = getRGB( getFeedbackColor() );
        return rgb;
    }
    
    private int getRGB( Color color )
    {
        int rgb = color.getRGB() & 0xffffff;
        return rgb;
    }
    
    private Thread startColorSelector()
    {
        Thread  thread  = 
            new Thread( () -> colorButton.doClick(), "ColorSelectorThread" );
        thread.start();
        Utils.pause( 250 );
        GUIUtils.schedEDTAndWait( () -> {
            getChooserDialog();
            getChooser();
            getChooserOKButton();
            getChooserCancelButton();
        });
        return thread;
    }
    
    private void getChooserDialog()
    {
        boolean canBeDialog     = true;
        boolean canBeFrame      = false;
        boolean mustBeVisible   = true;
        ComponentFinder finder  = 
            new ComponentFinder( canBeDialog, canBeFrame, mustBeVisible );
        Window  comp    = finder.findWindow( c -> true );
        assertNotNull( comp );
        assertTrue( comp instanceof JDialog );
        chooserDialog = (JDialog)comp;
    }
    
    private void getChooser()
    {
        // Assume getColorDialog called first
        JComponent  comp    = 
             ComponentFinder.find( 
                 chooserDialog, 
                 c -> (c instanceof JColorChooser) 
             );
        assertNotNull( comp );
        assertTrue( comp instanceof JColorChooser );
        chooser = (JColorChooser)comp;
    }
    
    private void getChooserOKButton()
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( "OK" );
        JComponent              comp    =
            ComponentFinder.find( chooserDialog, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        chooserOKButton = (JButton)comp;
    }
    
    private void getChooserCancelButton()
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( "Cancel" );
        JComponent              comp    =
            ComponentFinder.find( chooserDialog, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        chooserCancelButton = (JButton)comp;
    }
    
    private void commitEdit()
    {
        textEditor.postActionEvent();
        Utils.pause( 250 );
    }
}

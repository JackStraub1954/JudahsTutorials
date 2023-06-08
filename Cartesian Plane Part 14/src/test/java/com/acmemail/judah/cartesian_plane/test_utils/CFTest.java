package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CFTest
{
    private static final String     frameID             = "Frame";
    private static final String     dialogID            = "Dialog";
    private static final String     disposedPrefix      = "Disposed";
    private static final String     notVisiblePrefix    = "NotVisible";
    private static final String     visiblePrefix       = "Visible";
    
    private static final String     titleLabel          = "Title";
    private static final String     abortLabel          = "Exit";
    private static final String     cancelLabel         = "Cancel";
    private static final String     exitLabel           = "Exit";
    private static final String     okLabel             = "OK";
    
    private static TestDialog   visibleDialog;
    private static TestDialog   notVisibleDialog;
    private static TestDialog   disposedDialog;
    
    private static TestFrame    visibleFrame;
    private static TestFrame    notVisibleFrame;
    private static TestFrame    disposedFrame;
    
    @BeforeAll
    public static void tearDown() throws Exception
    {
        visibleDialog = new TestDialog( visiblePrefix );
        visibleDialog.show();
        notVisibleDialog = new TestDialog( notVisiblePrefix );
        disposedDialog = new TestDialog( disposedPrefix );
        disposedDialog.dispose();
        
        visibleFrame = new TestFrame( visiblePrefix );
        visibleFrame.start( true );
        notVisibleFrame = new TestFrame( notVisiblePrefix );
        notVisibleFrame.start( false );
        disposedFrame = new TestFrame( disposedPrefix );
        disposedFrame.start( false );
        disposedFrame.dispose();
    }

    /**
     * Search for all JButtons from the top of the hierarchy.<br>
     * Can-be-dialog: true<br>
     * Can-be-frame: true<br>
     * Must-be-displayable: true
     * Must-be-visible: false
     */
    @Test
    void testAllWindowsDialogFrame()
    {
        List<String>    shouldFind      = 
            getLabels( 
                visibleDialog, 
                notVisibleDialog, 
                visibleFrame,
                notVisibleFrame
            );
        
        List<String>    shouldNotFind   = 
            getLabels( disposedDialog, disposedFrame );

        // can be dialog, can be frame, NOT must be visible
        ComponentFinder finder  = 
            new ComponentFinder( true, true, false );
        for ( String str : shouldFind )
        {
            Predicate<JComponent>   pred    = getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNotNull( jComp, str );
            assertTrue( jComp instanceof JButton, str );
            assertEquals( str, ((JButton)jComp).getText() );
        }
        
        for ( String str : shouldNotFind )
        {
            Predicate<JComponent>   pred    = getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNull( jComp, str );
        }
    }

    /**
     * Search for all JButtons in all dialogs
     * whether they're disposed or not.<br>
     * Can-be-dialog: true<br>
     * Can-be-frame: false<br>
     * Must-be-displayable: false<br>
     * Must-be-visible: false
     */
    @Test
    void testAllDialogDisposed()
    {
        List<String>    shouldFind      = 
            getLabels( visibleDialog, notVisibleDialog, disposedDialog );
        
        List<String>    shouldNotFind   = 
            getLabels( visibleFrame, notVisibleFrame, disposedFrame );

        // can be dialog, NOT frame, NOT must be visible, NOT is displayable
        ComponentFinder finder  = 
            new ComponentFinder( true, false, false );
        // override displayable
        finder.setMustBeDisplayable( false );
        for ( String str : shouldFind )
        {
            Predicate<JComponent>   pred    = getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNotNull( jComp, str );
            assertTrue( jComp instanceof JButton, str );
            assertEquals( str, ((JButton)jComp).getText() );
        }
        
        for ( String str : shouldNotFind )
        {
            Predicate<JComponent>   pred    = getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNull( jComp, str );
        }
    }

    /**
     * Search for all JButtons in visible dialog.
     * Can-be-dialog: true<br>
     * Can-be-frame: false<br>
     * Must-be-displayable: true<br>
     * Must-be-visible: true
     */
    @Test
    void testAllDialogVisible()
    {
        List<String>    shouldFind      = 
            getLabels( visibleDialog );
        
        List<String>    shouldNotFind   = 
            getLabels(
                notVisibleDialog, 
                disposedDialog, 
                visibleFrame, 
                notVisibleFrame,
                disposedFrame
            );

        // can be dialog, NOT frame, NOT must be visible, NOT is displayable
        ComponentFinder finder  = 
            new ComponentFinder( true, false, true );
        for ( String str : shouldFind )
        {
            Predicate<JComponent>   pred    = getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNotNull( jComp, str );
            assertTrue( jComp instanceof JButton, str );
            assertEquals( str, ((JButton)jComp).getText() );
        }
        
        for ( String str : shouldNotFind )
        {
            Predicate<JComponent>   pred    = getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNull( jComp, str );
        }
    }

    /**
     * Search for all JButtons in visible frame.
     * Can-be-dialog: false<br>
     * Can-be-frame: true<br>
     * Must-be-displayable: true<br>
     * Must-be-visible: true
     */
    @Test
    void testAllFrameVisible()
    {
        List<String>    shouldFind      = 
            getLabels( visibleFrame );
        
        List<String>    shouldNotFind   = 
            getLabels(
                visibleDialog, 
                notVisibleDialog, 
                disposedDialog, 
                notVisibleFrame,
                disposedFrame
            );

        ComponentFinder finder  = 
            new ComponentFinder( false, true, true );
        for ( String str : shouldFind )
        {
            Predicate<JComponent>   pred    = getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNotNull( jComp, str );
            assertTrue( jComp instanceof JButton, str );
            assertEquals( str, ((JButton)jComp).getText() );
        }
        
        for ( String str : shouldNotFind )
        {
            Predicate<JComponent>   pred    = getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNull( jComp, str );
        }
    }

    /**
     * Search for all JButtons in all visible windows.
     * Can-be-dialog: true<br>
     * Can-be-frame: true<br>
     * Must-be-displayable: true<br>
     * Must-be-visible: true
     */
    @Test
    void testAllDialogFrameVisible()
    {
        List<String>    shouldFind      = 
            getLabels(
                visibleDialog, 
                visibleFrame 
            );
        
        List<String>    shouldNotFind   = 
            getLabels(
                notVisibleDialog, 
                disposedDialog, 
                notVisibleFrame,
                disposedFrame
            );

        ComponentFinder finder  = 
            new ComponentFinder( true, true, true );
        for ( String str : shouldFind )
        {
            Predicate<JComponent>   pred    = getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNotNull( jComp, str );
            assertTrue( jComp instanceof JButton, str );
            assertEquals( str, ((JButton)jComp).getText() );
        }
        
        for ( String str : shouldNotFind )
        {
            Predicate<JComponent>   pred    = getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNull( jComp, str );
        }
    }
    
    /**
     * Get a specific top-level window;
     * consider all top-level windows except
     * those that have been disposed.<br>
     * Can-be-dialog: true<br>
     * Can-be-frame: true<br>
     * Must-be-displayable: true<br>
     * Must-be-visible: false
     */
    public void testAllDialogFrameWindow()
    {
        List<TestWindow>    shouldFind      =
            List.of( 
                visibleDialog,
                notVisibleDialog,
                visibleFrame,
                notVisibleFrame
            );
        List<TestWindow>    shouldNotFind   =
            List.of( 
                disposedDialog,
                disposedFrame
            );
        
        ComponentFinder finder  = 
            new ComponentFinder( true, true, false );
        for ( TestWindow window : shouldFind )
        {
            String              title   = window.getTitle();
            Predicate<Window>   pred    = w -> testWindowTitle( w, title );
            Window              found   = finder.findWindow( pred );
            assertEquals( window.getWindow(), found, title );
        }
        for ( TestWindow window : shouldNotFind )
        {
            String              title   = window.getTitle();
            Predicate<Window>   pred    = w -> testWindowTitle( w, title );
            Window              found   = finder.findWindow( pred );
            assertNull( found, title );
        }
    }
    
    /**
     * Get a specific top-level window;
     * consider all visible top-level windows.<br>
     * Can-be-dialog: true<br>
     * Can-be-frame: true<br>
     * Must-be-displayable: true<br>
     * Must-be-visible: true
     */
    public void testAllDialogFrameVisibleWindow()
    {
        List<TestWindow>    shouldFind      =
            List.of( 
                visibleDialog,
                visibleFrame
            );
        List<TestWindow>    shouldNotFind   =
            List.of( 
                disposedDialog,
                notVisibleDialog,
                disposedFrame,
                notVisibleFrame
            );
        
        ComponentFinder finder  = 
            new ComponentFinder( true, true, true );
        for ( TestWindow window : shouldFind )
        {
            String              title   = window.getTitle();
            Predicate<Window>   pred    = w -> testWindowTitle( w, title );
            Window              found   = finder.findWindow( pred );
            assertEquals( window.getWindow(), found, title );
        }
        for ( TestWindow window : shouldNotFind )
        {
            String              title   = window.getTitle();
            Predicate<Window>   pred    = w -> testWindowTitle( w, title );
            Window              found   = finder.findWindow( pred );
            assertNull( found, title );
        }
    }
    
    /**
     * Get a specific top-level window;
     * consider only visible dialogs.<br>
     * Can-be-dialog: true<br>
     * Can-be-frame: true<br>
     * Must-be-displayable: true<br>
     * Must-be-visible: true
     */
    public void testAllDialogVisibleWindow()
    {
        List<TestWindow>    shouldFind      =
            List.of( 
                visibleDialog
            );
        List<TestWindow>    shouldNotFind   =
            List.of( 
                disposedDialog,
                notVisibleDialog,
                disposedFrame,
                visibleFrame,
                notVisibleFrame
            );
        
        ComponentFinder finder  = 
            new ComponentFinder( true, false, true );
        for ( TestWindow window : shouldFind )
        {
            String              title   = window.getTitle();
            Predicate<Window>   pred    = w -> testWindowTitle( w, title );
            Window              found   = finder.findWindow( pred );
            assertEquals( window.getWindow(), found, title );
        }
        for ( TestWindow window : shouldNotFind )
        {
            String              title   = window.getTitle();
            Predicate<Window>   pred    = w -> testWindowTitle( w, title );
            Window              found   = finder.findWindow( pred );
            assertNull( found, title );
        }
    }
    
    /**
     * Get a specific top-level window;
     * consider only visible dialogs.<br>
     * Can-be-dialog: true<br>
     * Can-be-frame: true<br>
     * Must-be-displayable: true<br>
     * Must-be-visible: true
     */
    public void testAllFrameVisibleWindow()
    {
        List<TestWindow>    shouldFind      =
            List.of( 
                visibleFrame
            );
        List<TestWindow>    shouldNotFind   =
            List.of( 
                disposedDialog,
                visibleDialog,
                notVisibleDialog,
                disposedFrame,
                notVisibleFrame
            );
        
        ComponentFinder finder  = 
            new ComponentFinder( false, true, true );
        for ( TestWindow window : shouldFind )
        {
            String              title   = window.getTitle();
            Predicate<Window>   pred    = w -> testWindowTitle( w, title );
            Window              found   = finder.findWindow( pred );
            assertEquals( window.getWindow(), found, title );
        }
        for ( TestWindow window : shouldNotFind )
        {
            String              title   = window.getTitle();
            Predicate<Window>   pred    = w -> testWindowTitle( w, title );
            Window              found   = finder.findWindow( pred );
            assertNull( found, title );
        }
    }
    
    /**
     * Find components when the top-level filter
     * is a custom predicate,
     * one that overrides all defaults.
     */
    private void testCustomPredicate()
    {
        List<TestWindow>    shouldFind      = 
            List.of(
                visibleDialog, 
                disposedFrame 
            );
        
        List<TestWindow>    shouldNotFind   = 
            List.of(
                notVisibleDialog, 
                disposedDialog, 
                notVisibleFrame,
                visibleFrame
            );

        ComponentFinder finder  = 
            new ComponentFinder();
        for ( TestWindow window : shouldFind )
        {
            String                  title   = window.getTitle();
            Predicate<Window>       wPred   = w -> testWindowTitle( w, title );
            String                  label   = window.getLabels().get( 0 );
            Predicate<JComponent>   cPred   = getButtonPredicate( label );
            finder.setTopWindowFilter( wPred );
            JComponent              jComp   = finder.find( cPred );
            assertNotNull( jComp, label );
            assertTrue( jComp instanceof JButton, label );
            assertEquals( label, ((JButton)jComp).getText() );
        }
        
        TestWindow          negFinder   = shouldFind.get( 0 );
        String              negTitle    = negFinder.getTitle();
        Predicate<Window>   wPred       = w -> testWindowTitle( w, negTitle );
        for ( TestWindow window : shouldNotFind )
        {
            String                  label   = window.getLabels().get( 0 );
            Predicate<JComponent>   cPred   = getButtonPredicate( label );
            finder.setTopWindowFilter( wPred );
            JComponent              jComp   = finder.find( cPred );
            assertNull( jComp, label );
        }
    }
    
    /**
     * Find all components starting with a container.
     */
    public void testFromWindow()
    {
        List<TestWindow>    allWindows  =
            List.of( 
                visibleDialog,
                notVisibleDialog,
                disposedDialog,
                visibleFrame,
                notVisibleFrame,
                disposedFrame
            );
        ComponentFinder finder  = new ComponentFinder();
        for ( TestWindow window : allWindows )
        {
            Window  top = window.getWindow();
            for ( String label : window.getLabels() )
            {
                Predicate<JComponent>   pred    = getButtonPredicate( label );
                JComponent              comp    = finder.find( top, pred );
                assertNotNull( comp );
                assertTrue( comp instanceof JButton );
                assertEquals( label, ((JButton)comp).getText() );
            }
            String                  label   = "not found";
            Predicate<JComponent>   pred    = getButtonPredicate( label );
            JComponent              comp    = finder.find( top, pred );
            assertNull( comp );
        }
    }
    
    /**
     * Find all components starting with a container.
     */
    public void testFromContainer()
    {
        List<TestWindow>    allWindows  =
            List.of( 
                visibleDialog,
                notVisibleDialog,
                disposedDialog,
                visibleFrame,
                notVisibleFrame,
                disposedFrame
            );
        ComponentFinder finder  = new ComponentFinder();
        for ( TestWindow window : allWindows )
        {
            JComponent  pane    = window.getContentPane();
            for ( String label : window.getLabels() )
            {
                Predicate<JComponent>   pred    = getButtonPredicate( label );
                JComponent              comp    = finder.find( pane, pred );
                assertNotNull( comp );
                assertTrue( comp instanceof JButton );
                assertEquals( label, ((JButton)comp).getText() );
            }
            String                  label   = "not found";
            Predicate<JComponent>   pred    = getButtonPredicate( label );
            JComponent              comp    = finder.find( pane, pred );
            assertNull( comp );
        }
    }
    
    private Predicate<JComponent> getButtonPredicate( String label )
    {
        Predicate<JComponent>   isButton    = jc -> jc instanceof JButton;
        Predicate<JComponent>   hasLabel    = 
            jc -> ((JButton)jc).getText().equals( label );
        Predicate<JComponent>   pred        = isButton.and( hasLabel );
        return pred;
    }
    
    private static boolean testWindowTitle( Window window, String title )
    {
        String  testTitle;
        if ( window instanceof JDialog )
            testTitle = ((JDialog)window).getTitle();
        else if ( window instanceof JFrame )
            testTitle = ((JFrame)window).getTitle();
        else
            testTitle = null;
        
        boolean match   = title.equals( testTitle );
        return match;
    }
    
    private static List<String> getLabels( TestWindow... windows )
    {
        List<String>    list    =
            Arrays.stream( windows )
                .flatMap( w -> w.getLabels().stream() )
                .collect( Collectors.toList() );
        return list;
    }
    
    private interface TestWindow
    {
        String  getTitle();
        List<String> getLabels();
        JPanel getContentPane();
        Window getWindow();
    }

    private static class TestDialog implements TestWindow
    {
        private final JDialog           dialog;
        private final TestContentPane   contentPane;
        private final String            title;
        
        public TestDialog( String prefix )
        {
            String  thisID  = prefix + dialogID;
            title = thisID + titleLabel;
            dialog = new JDialog( (Window)null, title );
            dialog.setModal( false );
            contentPane = new TestContentPane( thisID, dialog );
            dialog.setContentPane( contentPane );
            
            dialog.pack();
        }
    
        public void show()
        {
            dialog.setVisible( true );
        }
        
        public void dispose()
        {
            dialog.dispose();
        }
        
        public JPanel getContentPane()
        {
            return contentPane;
        }
        
        public String getTitle()
        {
            return title;
        }
        
        public Window getWindow()
        {
            return dialog;
        }
        
        public List<String> getLabels()
        {
            return contentPane.getLabels();
        }
    }

    private static class TestFrame implements TestWindow
    {
        private final JFrame            frame;
        private final TestContentPane   contentPane;
        private final String            title;
        
        public TestFrame( String prefix )
        {
            String  thisID  = prefix + frameID;
            title = thisID + titleLabel;
            frame = new JFrame( title );
            contentPane = new TestContentPane( thisID, frame );
            frame.setContentPane( contentPane );
        }
        
        public void start( boolean makeVisible )
        {
            try
            {
                SwingUtilities.invokeAndWait( () -> {
                    frame.pack();
                });
                frame.setVisible( makeVisible );
            }
            catch ( InterruptedException | InvocationTargetException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );
            }
        }
        
        public JPanel getContentPane()
        {
            return contentPane;
        }
        
        public void dispose()
        {
            frame.dispose();
        }
        
        public String getTitle()
        {
            return title;
        }
        
        public Window getWindow()
        {
            return frame;
        }
        
        public List<String> getLabels()
        {
            return contentPane.getLabels();
        }
    }
    
    @SuppressWarnings("serial")
    public static class TestContentPane extends JPanel
    {
        private final JPanel            panel1;
        private final JPanel            panel2;
        private final JButton           okButton;
        private final JButton           cancelButton;
        private final JButton           exitButton;
        private final JButton           abortButton;
        
        public TestContentPane( String prefix, Window window )
        {
            super( new GridLayout( 2, 1 ) );
            
            panel1 = new JPanel( new GridLayout( 1, 2 ) );
            this.add( panel1 );
            okButton = new JButton( prefix + okLabel );
            cancelButton = new JButton( prefix + cancelLabel );
            panel1.add( okButton );
            panel1.add( cancelButton );
            
            panel2 = new JPanel( new GridLayout( 1, 2 ) );
            this.add( panel2 );
            exitButton = new JButton( prefix + exitLabel );
            abortButton = new JButton( prefix + abortLabel);
            panel2.add( exitButton );
            panel2.add( abortButton );
            
            ActionListener  closeDialog = 
                e -> window.setVisible( false );
            okButton.addActionListener( closeDialog );
            cancelButton.addActionListener( closeDialog );
            exitButton.addActionListener( closeDialog );
            abortButton.addActionListener( closeDialog );
        }
        
        public List<String> getLabels()
        {
            List<String>    list    = List.of( 
                okButton.getText(),
                cancelButton.getText(),
                exitButton.getText(),
                abortButton.getText()
            );
            return list;
        }
    }
}

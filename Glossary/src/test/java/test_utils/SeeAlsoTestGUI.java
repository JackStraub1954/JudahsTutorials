package test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.judahstutorials.glossary.ConnectionMgr;
import com.judahstutorials.glossary.SeeAlso;

import Controls.SeeAlsoPanel;

public class SeeAlsoTestGUI
{
    private static  SeeAlsoTestGUI  testGUI;
    private final   JFrame          frame;
    private final   SeeAlsoPanel    seeAlsoPanel;
    private final   JTextField      newLink;
    private final   JButton         deleteButton;
    private final   JList<SeeAlso>  seeAlsoList;
    
    private SeeAlsoTestGUI()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  contentPane     = new JPanel( new BorderLayout() );
        seeAlsoPanel = new SeeAlsoPanel();
        newLink = getTextField();
        seeAlsoList = getList();
        deleteButton = getButton();
    }
    
    public static SeeAlsoTestGUI getTestGUI()
    {
        if ( testGUI == null )
        {
            try
            {
                SwingUtilities.invokeAndWait( () -> 
                    testGUI = new SeeAlsoTestGUI()
                );
            }
            catch (  InvocationTargetException | InterruptedException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );            }
        }
        return testGUI;
    }
    
    public String getNewLinkText()
    {
        Object  obj     = getProperty( () -> newLink.getText() );
        assertTrue( obj instanceof String );
        return (String)obj;
    }
    
    public void setNewLinkText( String text )
    {
        GUIUtils.schedEDTAndWait( () -> {
            newLink.setText( text );
            newLink.postActionEvent();
        });
    }
    
    @SuppressWarnings("unchecked")
    public List<SeeAlso> getElements()
    {
        Object  obj = getProperty( () -> seeAlsoList.getModel() );
        assertTrue( obj instanceof DefaultListModel );
        DefaultListModel<SeeAlso>   model   = 
            (DefaultListModel<SeeAlso>)obj;
        
        obj = getProperty( () -> model.elements() );
        assertTrue( obj instanceof Enumeration );        
        Enumeration<SeeAlso>    enumer  = (Enumeration<SeeAlso>)obj;
        
        List<SeeAlso>           list    = new ArrayList<>();
        while ( enumer.hasMoreElements() )
            list.add( enumer.nextElement() );
        return list;
    }
    
    public void delete()
    {
        GUIUtils.schedEDTAndWait( () -> deleteButton.doClick() );
    }
    
    public void commit()
    {
        List<SeeAlso>   list    = getElements();
        GUIUtils.schedEDTAndWait( () -> SeeAlso.commit( list ) );
        ConnectionMgr.closeConnection();
    }
    
    private Object getProperty( Supplier<Object> supplier  )
    {
        Object[]    obj = new Object[1];
        GUIUtils.schedEDTAndWait( () -> obj[0] = supplier.get() );
        return obj[0];
    }
    
    private JButton getButton()
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( "Delete" );
        JComponent  comp    = ComponentFinder.find( seeAlsoPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
    }
    
    private JTextField getTextField()
    {
        Predicate<JComponent>   pred    = c -> (c instanceof JTextField);
        JComponent  comp    = ComponentFinder.find( seeAlsoPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JTextField );
        return (JTextField)comp;
    }
    
    @SuppressWarnings("unchecked")
    private JList<SeeAlso> getList()
    {
        Predicate<JComponent>   pred    = c -> (c instanceof JList);
        JComponent  comp    = ComponentFinder.find( seeAlsoPanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JList );
        return (JList<SeeAlso>)comp;
    }
}

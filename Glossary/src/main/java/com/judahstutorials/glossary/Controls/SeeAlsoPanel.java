package com.judahstutorials.glossary.Controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.judahstutorials.glossary.Definition;
import com.judahstutorials.glossary.SeeAlso;

@SuppressWarnings("serial")
public class SeeAlsoPanel extends JPanel
{
    public static final String  SEE_ALSO_JLIST      = "seeAlsoJList";
    public static final String  SEE_ALSO_NEW_TEXT   = "seeAlsoJList";
    public static final String  DELETE_BUTTON       = 
        "seeAlsoDeleteButton";
    
    private final DefaultListModel<SeeAlso> seeAlsoModel    =
        new DefaultListModel<>();
    private final JList<SeeAlso>            seeAlsoList     = 
        new JList<>( seeAlsoModel );
    private final JFormattedTextField       addField        = 
        new JFormattedTextField();
    
    private Definition currDef;

    public SeeAlsoPanel()
    {
        super( new BorderLayout() );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        setBorder( border );
        
        String  seeAlsoText = "<html><strong>See Also:</strong></html>";
        add( new JLabel( seeAlsoText ), BorderLayout.NORTH );
        add( getAddDeletePanel(), BorderLayout.SOUTH );
        add( getScrolledList(), BorderLayout.CENTER );
        setDefinition( null );
    }
    
    public void setDefinition( Definition def )
    {
        currDef = def;
        reset();
        if ( def != null )
        {
            seeAlsoModel.addAll( def.getSeeAlso() );
            setEnabled( true );
        }
    }
    
    public void reset()
    {
        seeAlsoModel.clear();
        addField.setValue( "" );
        setEnabled( false );
    }

    private JScrollPane getScrolledList()
    {
        JScrollPane pane = new JScrollPane();
        pane.setViewportView( seeAlsoList );
        seeAlsoList.setName( SEE_ALSO_JLIST );
        
        seeAlsoList.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent evt )
            {
                editSeeAlso( evt );
            }
        });
        
        return pane;
    }

    private JPanel getAddDeletePanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setLayout( layout );
        panel.setBorder( border );
        
        JButton delete  = new JButton( "Delete Selected" );
        delete.setName( DELETE_BUTTON );
        delete.addActionListener( this::toggleDelete );
        panel.add( delete );
        panel.add( getAddPanel() );
        return panel;
    }
    
    private JPanel getAddPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );

        addField.setName( SEE_ALSO_NEW_TEXT );
        addField.setValue( "" );
        addField.addPropertyChangeListener( "value", this::newSeeAlso );
        addField.addActionListener( this::newSeeAlso );
        
        panel.add( new JLabel( "New link: " ) );
        panel.add( addField );
        return panel;
    }
    
    private void toggleDelete( ActionEvent evt )
    {
        SeeAlso see = seeAlsoList.getSelectedValue();
        if ( see != null )
        {
            boolean deleted = see.isMarkedForDelete();
            see.markForDelete( !deleted );
            seeAlsoList.repaint();
        }
    }
    
    private void editSeeAlso( MouseEvent evt )
    {
        final String    editURL = "Edit URL";
        if ( evt.getClickCount() == 2 )
        {
            SeeAlso see = seeAlsoList.getSelectedValue();
            if ( see != null )
            {
                String  text    = 
                    JOptionPane.showInputDialog( 
                        this, 
                        editURL, 
                        see.getURL()
                    );
                if ( text != null && !text.isEmpty() )
                {
                    see.updateURL( text );
                    seeAlsoList.repaint();
                }
            }
        }
    }
    
    private void newSeeAlso( ActionEvent evt )
    {
        String  url = addField.getText();
        newSeeAlso( url );
    }
    
    private void newSeeAlso( PropertyChangeEvent evt )
    {
        String  url = (String)addField.getValue();
        newSeeAlso( url );
    }
    
    private void newSeeAlso( String url )
    {
        if ( url != null && !url.isEmpty() )
        {
            SeeAlso next    = new SeeAlso( currDef.getID(), url );
            seeAlsoModel.addElement( next );
            int rowCount    = seeAlsoModel.getSize();
            if ( rowCount > 0 )
                seeAlsoList.setSelectedIndex( rowCount - 1 );
            addField.setValue( "" );
        }
    }
}

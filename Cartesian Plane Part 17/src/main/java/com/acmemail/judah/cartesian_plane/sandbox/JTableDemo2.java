package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Vector;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class JTableDemo2
{
    private static String[]     headings = { "State", "Capital", "Action" };
    private static Object[][]   data    =
    {
        { "Alabama", "Montgomery", Boolean.valueOf( Boolean.valueOf( false ) ) },
        { "Alaska", "Juneau", Boolean.valueOf( false ) },
        { "Arizona", "Phoenix", Boolean.valueOf( false ) },
        { "Arkansas", "Little Rock", Boolean.valueOf( false ) },
        { "California", "Sacramento", Boolean.valueOf( false ) },
        { "Colorado", "Denver", Boolean.valueOf( false ) },
        { "Connecticut", "Hartford", Boolean.valueOf( false ) },
        { "Delaware", "Dover", Boolean.valueOf( false ) },
        { "Florida", "Tallahassee", Boolean.valueOf( false ) },
        { "Georgia", "Atlanta", Boolean.valueOf( false ) },
        { "Hawaii", "Honolulu", Boolean.valueOf( false ) },
        { "Idaho", "Boise", Boolean.valueOf( false ) },
        { "Illinois", "Springfield", Boolean.valueOf( false ) },
    };
    
    private JTable              table;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        JTableDemo2 demo    = new JTableDemo2();
        SwingUtilities.invokeLater( () -> demo.buildGUI() );
    }
    
    private void buildGUI()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        table = new JTable( new LocalTableModel() );
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getButtonPanel()
    {
        JPanel      buttonPanel = new JPanel();
        JButton     apply       = new JButton( "Apply" );
        JButton     exit        = new JButton( "Exit" );
        JButton     delete      = new JButton( "Delete Selected" );
        JButton     add         = new JButton( "New" );
        apply.addActionListener( this::applyAction );
        delete.addActionListener( this::deleteAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( apply );
        buttonPanel.add( add );
        buttonPanel.add( delete );
        buttonPanel.add( exit );
        return buttonPanel;
    }
    
    private void deleteAction( ActionEvent evt )
    {
        int         numRows     = table.getRowCount();
        TableModel  tableModel  = table.getModel();
        LocalTableModel newModel    = new LocalTableModel( headings );
        if ( tableModel instanceof DefaultTableModel )
        {
            DefaultTableModel   defModel    = (DefaultTableModel)tableModel;
            Vector<Vector>  oldVec  = defModel.getDataVector();
            IntStream.range( 0, numRows )
                .filter( i -> !(Boolean)(table.getValueAt( i, 2 )) )
                .forEach( i -> newModel.addRow( oldVec.get( i ) ) );
            table.setModel( newModel );
        }
    }
    
    private void applyAction( ActionEvent evt )
    {
        TableModel  model   = table.getModel();
        int         rows    = model.getRowCount();
        int         cols    = model.getColumnCount();
        for ( int row = 0 ; row < rows ; ++row )
            for ( int col = 0 ; col < cols ; ++col )
            {
                System.out.print( model.getValueAt( row, col ) );
                if ( col < (cols - 1 ) )
                    System.out.print( ", " );
                else
                    System.out.println();
            }
    }
    
    private class LocalTableModel extends DefaultTableModel
    {
        public LocalTableModel()
        {
            super( data, headings );
        }
        
        public LocalTableModel( Object[] headings )
        {
            super( null, headings );
        }
        
        @Override
        public Class<?> getColumnClass( int col )
        {
            Class<?>    clazz   = col == 2 ?
                Boolean.class : super.getColumnClass( col );
            return clazz;
        }
    }
    
    @SuppressWarnings("serial")
    private class LocalCellRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(
            JTable table, 
            Object value, 
            boolean isSelected, 
            boolean hasFocus, 
            int row, 
            int col
        )
        {
            Component   comp    = value instanceof Component ?
                (Component)value :
                super.getTableCellRendererComponent(
                    table, 
                    value, 
                    isSelected, 
                    hasFocus, 
                    row, 
                    col
                );
            return comp;       
        }
    }
}

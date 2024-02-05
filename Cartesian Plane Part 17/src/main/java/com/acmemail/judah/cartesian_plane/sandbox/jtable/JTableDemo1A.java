package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

public class JTableDemo1A
{
    private static String[]     headers = { "State", "Capital", "Population" };
    private static Object[][]   data    =
        State.getDataSet( "name", "capital", "population" );
    
    private JTable              table;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        JTableDemo1A demo    = new JTableDemo1A();
        SwingUtilities.invokeLater( () -> demo.buildGUI() );
    }
    
    private void buildGUI()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        table = new JTable( data, headers );
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     apply       = new JButton( "Apply" );
        JButton     exit        = new JButton( "Exit" );
        apply.addActionListener( this::applyAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( apply );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
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
}

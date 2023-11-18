package com.acmemail.judah.cartesian_plane.sandbox.app;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.LinePropertiesPanelGUI;

public class ShowLinePropertiesGUI
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> makeGUI() );
    }
    
    private static void makeGUI()
    {
        JFrame  frame   = new JFrame( "Show Line Properties GUI" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  panel   = new LinePropertiesPanelGUI();
        frame.setContentPane( panel );
        frame.setLocation( 300, 300 );
        frame.pack();
        frame.setVisible( true );
    }
}

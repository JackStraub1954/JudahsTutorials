package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.FontEditor;

public class GraphicsCaptureDemo2
{
    private JPanel          copyFromPanel;
    private feedbackPanel   feedbackPanel;
    private JLabel          feedback;
    
    private ActivityLog activityLog;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        GraphicsCaptureDemo2    demo    = new GraphicsCaptureDemo2();
        SwingUtilities.invokeLater( () -> demo.buildAll() );
    }
    
    private void buildAll()
    {
        activityLog = new ActivityLog();
        buildCopyFromFrame();
        buildFeedbackFrame();
    }
    
    private void buildCopyFromFrame()
    {
        JFrame  frame       = new JFrame( "Image to Copy" );
        
        Border      border  = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        FontEditor  editor  = new FontEditor();
        feedback = editor.getFeedback();
        copyFromPanel = editor.getPanel();
        copyFromPanel.setBorder( border );
        
        frame.setLocation( 200, 200 );
        frame.setContentPane( copyFromPanel );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void buildFeedbackFrame()
    {
        JFrame  frame       = new JFrame( "Colors Found" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            
        JPanel  contentPane = new JPanel( new BorderLayout() );
        Border  outerBorder = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        Border  innerBorder = 
            BorderFactory.createRaisedBevelBorder();
        Border  border      =
            BorderFactory.createCompoundBorder( outerBorder, innerBorder );
        contentPane.setBorder( border );
        
        feedbackPanel = new feedbackPanel();
        contentPane.add( feedbackPanel, BorderLayout.CENTER );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 300, 300 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel       = new JPanel();
        JButton     countButton = new JButton( "Count" );
        JButton     exitButton  = new JButton( "Exit" );
        
        panel.add( countButton );
        panel.add( exitButton );
        
        countButton.addActionListener( e -> countColors() );
        exitButton.addActionListener( e -> System.exit( 0 ) );
        
        return panel;
    }
    
    private void countColors()
    {
        int             width   = feedback.getWidth();
        int             height  = feedback.getHeight();
        int             type    = BufferedImage.TYPE_INT_ARGB;
        BufferedImage   image   = new BufferedImage( width, height, type );
        Graphics2D      gtx     = image.createGraphics();
        gtx.fillRect( 0,  0, width, height );        
        feedback.paint( gtx );
        
        System.out.println( feedback.getClass().getName() );
        List<Integer>   allColors   = new ArrayList<>();
        for ( int row = 0 ; row < height ; ++row )
            for ( int col = 0 ; col < width ; ++col )
            {
                Integer color   = image.getRGB( col, row ) & 0xffffff;
                if ( !allColors.contains( color ) )
                {
                    allColors.add( color );
                    String  strColor    = Integer.toHexString( color ) ;
                    activityLog.append( strColor, "\"font-family: monospace;\""  );
                    if ( color.equals( 0xff ) )
                        System.out.println( strColor );
                }
            }
        activityLog.append( "count: " + allColors.size() );
        int     rectWidth   = 75;
        int     rectHeight  = 25;
        int     spacing     = 5;
        int     nColors     = allColors.size();
        width = rectWidth + 2 * spacing;
        height = rectHeight * (nColors + 1 );
        image = new BufferedImage( width, height, type );
        gtx = image.createGraphics();
        gtx.setColor( Color.WHITE );
        gtx.fillRect( 0, 0, width, height );
        
        int xco     = spacing;
        int incr    = rectHeight + spacing;
        for ( int inx=0,yco=spacing ; inx < nColors ; ++inx, yco += incr )
        {
            int     iColor  = allColors.get( inx );
            gtx.setColor( new Color( iColor ) );
            gtx.fillRect( xco, yco, rectWidth, rectHeight );
        }
        
        feedbackPanel.setImage( image );
        feedbackPanel.repaint();
    }
    
    @SuppressWarnings("serial")
    private class feedbackPanel extends JPanel
    {
        private final BufferedImage tile    = makeTile();
        private BufferedImage   copy    = null;
        private Graphics2D      gtx;
        
        public feedbackPanel()
        {
            Dimension   size    = new Dimension( 300, 300 );
            setPreferredSize( size );
        }
        
        public void setImage( BufferedImage image )
        {
            this.copy = image;
        }
        
        @Override
        public void paint( Graphics graphics )
        {
            super.paint( graphics );
            gtx = (Graphics2D)graphics.create();
            
            int width       = getWidth();
            int height      = getHeight();
            int tileWidth   = tile.getWidth();
            int tileHeight  = tile.getHeight();
            
            for ( int row=0,yco=0 ; yco <= height ; yco+=tileHeight,row++ )
            {
                int randomOff   = (int)(Math.random() * tileWidth);
                int rowStart    = (row % 2) == 0 ? 0 : -tileWidth / 2;
                rowStart -= randomOff;
                for ( int xco = rowStart ; xco <= width ; xco += tileWidth )
                    gtx.drawImage( tile, xco, yco, this );
            }
            
            if ( copy != null )
                gtx.drawImage( copy, 50, 50, this );
        }
        
        private BufferedImage makeTile()
        {
            Color   background      = new Color( 0xcccccc );
            char    alpha           = '\u0391';
            char    pie             = '\u03A0';
            char    omega           = '\u03A9';
            int     tileMargin      = 0;
            String  tileFontName    = Font.DIALOG_INPUT;
            int     tileFontStyle   = Font.ITALIC;
            int     tileFontSize    = 40;
            Color   tileFontColor   = Color.YELLOW;
            String  tileString      = 
                alpha + " " + pie + " " + omega + " ";
            
            // This is just temporary BufferedImage which is used to
            // obtain a graphics context to calculate font metrics.
            int             type    = BufferedImage.TYPE_4BYTE_ABGR;
            BufferedImage   temp    = new BufferedImage( 10, 10, type );
            Graphics2D      gtx     = temp.createGraphics();
            
            Font            font        = 
                new Font( tileFontName, tileFontStyle, tileFontSize );
            FontMetrics     metrics     = gtx.getFontMetrics( font );
            Rectangle2D     strDim      = 
                metrics.getStringBounds( tileString, gtx );
            int             rectWidth   = (int)(strDim.getWidth() + .5);
            int             rectHeight  = (int)(strDim.getHeight() + .5);
            int             strAscent   = metrics.getAscent();
            int             strXco      = tileMargin;
            int             strYco      = tileMargin + strAscent;
            int             tileWidth   = rectWidth + tileMargin;
            int             tileHeight  = rectHeight +  tileMargin;
            
            BufferedImage   tile        = 
                new BufferedImage( tileWidth, tileHeight, type );
            gtx = tile.createGraphics();
            gtx.setColor( background );
            gtx.fillRect( 0, 0, tileWidth, tileHeight );
            
            gtx.setFont( font );
            gtx.setColor( tileFontColor ); 
            gtx.drawString( tileString, strXco, strYco );
            return tile;
        }
    }
}

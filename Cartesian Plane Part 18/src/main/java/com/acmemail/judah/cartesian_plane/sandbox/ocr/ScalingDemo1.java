package com.acmemail.judah.cartesian_plane.sandbox.ocr;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This application incorporates a simple demonstration
 * of scaling in a Swing application.
 * The {@link #paintComponent(Graphics)} method
 * applies a scaling factor under the control of the operator
 * and draws some lines of text,
 * incorporating both alpha and numeric characters.
 * <p>
 * For emphasis,
 * the scaling logic is encapsulated in the {@link #applyScale()} method,
 * which creates a scaling operation
 * and concatenates it with the translation operation
 * that is typically present in the graphics context
 * of a Swing application.
 * 
 * @author Jack Straub
 * 
 * @see ScalingDemo3
 */
public class ScalingDemo1 extends JPanel
{
    private static final long serialVersionUID = -6779305390811349326L;
    
    private final int           widthOrig       = 15;
    private final int           heightOrig      = widthOrig;
    private final int           margin          = 3;
    private final float         scaleFactor     = 1.5f;
    private final int           defWeight       = 3;
    private final BufferedImage imageOrig;
    private final BufferedImage imageScaled;

    /** Background color of the principal GUI window. */
    private final Color         bgColor         = Color.WHITE;
    /** Color for drawing text in the application window. */
    private final Color         fgColor         = Color.BLACK;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> {
            ScalingDemo1    demo  = new ScalingDemo1();
            demo.build();
        });
    }
    
    /**
     * Constructor.
     * Establishes the BufferedImages to paint.
     */
    public ScalingDemo1()
    {
        imageOrig = getImageOrig();
        imageScaled = getImageScaled();
    }

    /**
     * Builds and displays the application GUI.
     */
    public void build()
    {
        JFrame          frame       = new JFrame( "Scaling Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel          contentPane = new JPanel( new BorderLayout() );
        contentPane.add( this, BorderLayout.CENTER );
        
        int         prefWidth   = imageScaled.getWidth() + 2 * margin;
        int         prefHeight  = 
            imageOrig.getHeight() + imageScaled.getHeight() + 3 * margin;
        Dimension   prefSize    = new Dimension( prefWidth, prefHeight );
        setPreferredSize( prefSize );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setLocation( 100, 200 );
        frame.setVisible( true );
        
        showDialog( frame );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        int         width   = getWidth();
        int         height  = getHeight();
        Graphics2D  gtx     = (Graphics2D)graphics.create();
        
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, width, height );
        
        gtx.setColor( fgColor );
        int         xco     = 
            margin + width / 2 - imageOrig.getWidth() / 2;
        int         yco     = margin;
        gtx.drawImage( imageOrig, xco, yco, this );
        
        xco = margin + width / 2 - imageScaled.getWidth() / 2;
        yco += margin + imageOrig.getHeight();
        gtx.drawImage( imageScaled, xco, yco, this );
    }
    
    /**
     * Creates the non-scaled BufferedImage.
     * 
     * @return  the non-scaled BufferedImage
     */
    private BufferedImage getImageOrig()
    {
        int             type    = BufferedImage.TYPE_INT_RGB;
        int             endCap  = BasicStroke.CAP_SQUARE;
        BufferedImage   image   = 
            new BufferedImage( widthOrig, heightOrig, type );
        Point           upperLeft   = new Point( 0, 0 );
        Point           upperRight  = new Point( widthOrig, 0 );
        Point           lowerLeft   = new Point( 0, heightOrig );
        Point           lowerRight  = new Point( widthOrig, heightOrig );
        Line2D          line1       = 
            new Line2D.Double( upperLeft, lowerRight );
        Line2D          line2       = 
            new Line2D.Double( upperRight, lowerLeft );
        Stroke          stroke      = 
            new BasicStroke( defWeight, endCap, 0 );
        
        Graphics2D  gtx     = image.createGraphics();
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, widthOrig, heightOrig );
        
        gtx.setColor( fgColor );
        gtx.setStroke( stroke );
        gtx.draw( line1 );
        gtx.draw( line2 );
        
        return image;
    }
    
    /**
     * Creates the scaled BufferedImage.
     * <p>
     * Precondition: non-scaled BufferedImage is fully configured
     * 
     * @return  the scaled BufferedImage
     */
    private BufferedImage getImageScaled()
    {
        int             imageType   = imageOrig.getType();
        int             width       = 
            (int)(imageOrig.getWidth() * scaleFactor + .5);
        int             height      =  
            (int)(imageOrig.getHeight() * scaleFactor + .5);
        BufferedImage   image       = 
            new BufferedImage( width, height, imageType );
        Graphics2D      gtx         = image.createGraphics();
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, width, height );

        // Scale the image
        AffineTransform     transform       = new AffineTransform();
        transform.scale( scaleFactor, scaleFactor );
        AffineTransformOp   scaleOp         = 
            new AffineTransformOp( 
                transform, 
                AffineTransformOp.TYPE_BICUBIC
            );
        scaleOp.filter( imageOrig, image );
        return image;
    }
    
    private void showDialog( JFrame frame )
    {
        JPanel  contentPane = new JPanel( new BorderLayout() );
        JPanel  content     = new ScalingDemo1A( this );
        JDialog dialog      = new JDialog( frame, false );
        contentPane.add( content, BorderLayout.CENTER );
        dialog.setContentPane( contentPane );
        
        Dimension   frameDim    = frame.getPreferredSize();
        Point       frameLoc    = frame.getLocation();
        int         frameXco    = frameLoc.x;
        int         frameYco    = frameLoc.y;
        int         dialogXco   = frameXco + frameDim.width + 10;
        dialog.setLocation( dialogXco, frameYco );
        
        dialog.pack();
        dialog.setVisible( true );
    }
}
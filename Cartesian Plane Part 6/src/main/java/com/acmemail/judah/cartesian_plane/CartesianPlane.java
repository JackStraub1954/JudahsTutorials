package com.acmemail.judah.cartesian_plane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.JPanel;

/**
 * This class encapsulates the display of a Cartesian plane.
 * The plane consists of the following components,
 * each of which is customizable by the user.
 * <p>
 * <img 
 *     src="doc-files/SamplePlane.png" 
 *     alt="Sample Plane"
 *     style="width:25%; height:auto;"
 * >
 * </p>
 * 
 *    <table class="js-plain">
 *         <caption>Summary of Properties</caption>
 *         <tr>
 *             <th class="js-plain">
 *                 Property
 *             </th>
 *             <th class="js-plain">
 *                 Type
 *             </th>
 *             <th class="js-plain">
 *                 Description
 *             </th>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridUnit
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The grid unit, 
 *                 in pixels per unit.
 *                 If this property has a value of 25
 *                 each unit of display
 *                 will have span 25 pixels.
 *                 The grid unit 
 *                 is always the same
 *                 for both the x- and y-axes.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 mwBGColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The background color of the main window.
 *             </td>
 *         </tr>
 *         <tr class="js-empty"><td></td><td></td><td></td></tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginTopWidth
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The width of the top margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginTopBGColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The background color of the top margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginRightWidth
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The width of the right margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginRightBGColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The background color of the right margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginBottomWidth
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The width of the bottom margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginBottomBGColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The background color of the bottom margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginLeftWidth
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The width of the left margin.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 marginLeftBGColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The background color of the left margin.
 *             </td>
 *         </tr>
 *         <tr class="js-empty"><td></td><td></td><td></td></tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMinorColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The color of the minor tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMinorWeight
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The weight of the minor tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMinorLen
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The length of the minor tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMinorMPU
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The number of minor tic marks per unit.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMinorDraw
 *             </td>
 *             <td class="js-plain">
 *                 boolean
 *             </td>
 *             <td class="js-plain">
 *                 True to include the minor tic marks
 *                 in the graphic,
 *                 false to omit them.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMajorColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The color of the major tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMajorWeight
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The weight of the major tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMajorLen
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The length of the major tic marks.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMajorMPU
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The number of major tic marks per unit.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 ticMajorDraw
 *             </td>
 *             <td class="js-plain">
 *                 boolean
 *             </td>
 *             <td class="js-plain">
 *                 True to include the major tic marks
 *                 in the graphic,
 *                 false to omit them.
 *             </td>
 *         </tr>
 *         <tr class="js-empty"><td></td><td></td><td></td></tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridLineColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The color of the grid lines.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridLineWeight
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The weight of the grid lines.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridLineLPU
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The number of grid lines per unit.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridLineDraw
 *             </td>
 *             <td class="js-plain">
 *                 boolean
 *             </td>
 *             <td class="js-plain">
 *                 True to include the grid lines
 *                 in the graphic,
 *                 false to omit them.
 *             </td>
 *         </tr>
 *         <tr class="js-empty"><td></td><td></td><td></td></tr>
 *         <tr>
 *             <td class="js-plain">
 *                 axisColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The color of the x- and y-axes.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 gridLineWeight
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The weight of the x- and y-axes.
 *             </td>
 *         </tr>
 *         <tr class="js-empty"><td></td><td></td><td></td></tr>
 *         <tr>
 *             <td class="js-plain">
 *                 labelFontColor
 *             </td>
 *             <td class="js-plain">
 *                 Color
 *             </td>
 *             <td class="js-plain">
 *                 The color used to draw the labels on the x- and y-axes.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 labelFontName
 *             </td>
 *             <td class="js-plain">
 *                 String
 *             </td>
 *             <td class="js-plain">
 *                 The name of the font used to draw the labels on the x- and y-axes.
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 labelFontStyle
 *             </td>
 *             <td class="js-plain">
 *                 int
 *             </td>
 *             <td class="js-plain">
 *                 The style of the font used to draw the labels on the x- and y-axes
 *                 (from the Font class,
 *                 e.g. Font.PLAIN).
 *             </td>
 *         </tr>
 *         <tr>
 *             <td class="js-plain">
 *                 labelFontSize
 *             </td>
 *             <td class="js-plain">
 *                 float
 *             </td>
 *             <td class="js-plain">
 *                 The size of the font used to draw the labels on the x- and y-axes.
 *             </td>
 *         </tr>
 *    </table>
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class CartesianPlane extends JPanel
{
    private static final int    mainWindowWidthDV   =
        CPConstants.asInt( CPConstants.MW_WIDTH_DV );
    private static final int    mainWindowHeightDV   =
        CPConstants.asInt( CPConstants.MW_HEIGHT_DV );
    
    /////////////////////////////////////////////////
    //   General grid properties
    /////////////////////////////////////////////////
    /** Grid units (pixels-per-unit) default value: float. */
    private float   gridUnit            = 
        CPConstants.asFloat( CPConstants.GRID_UNIT_DV );

    /////////////////////////////////////////////////
    //   Main window properties
    //   Note: "width" and "height" are included as
    //   main window properties in CPConstants,
    //   but it is not necessary to encapsulate
    //   their values in instance variables.
    //   See the default constructor.
    /////////////////////////////////////////////////
    private Color   mwBGColor           = 
        CPConstants.asColor( CPConstants.MW_BG_COLOR_DV );
    
    /////////////////////////////////////////////////
    //   Margin properties
    /////////////////////////////////////////////////
    private float   marginTopWidth      =
        CPConstants.asFloat( CPConstants.MARGIN_TOP_WIDTH_DV );
    private Color   marginTopBGColor    =
        CPConstants.asColor( CPConstants.MARGIN_TOP_BG_COLOR_DV );
    private float   marginRightWidth      =
        CPConstants.asFloat( CPConstants.MARGIN_RIGHT_WIDTH_DV );
    private Color   marginRightBGColor    =
        CPConstants.asColor( CPConstants.MARGIN_RIGHT_BG_COLOR_DV );
    private float   marginBottomWidth      =
        CPConstants.asFloat( CPConstants.MARGIN_BOTTOM_WIDTH_DV );
    private Color   marginBottomBGColor    =
        CPConstants.asColor( CPConstants.MARGIN_BOTTOM_BG_COLOR_DV );
    private float   marginLeftWidth      =
        CPConstants.asFloat( CPConstants.MARGIN_LEFT_WIDTH_DV );
    private Color   marginLeftBGColor    =
        CPConstants.asColor( CPConstants.MARGIN_LEFT_BG_COLOR_DV );
    
    /////////////////////////////////////////////////
    //   Tic mark properties
    /////////////////////////////////////////////////
    private Color   ticMinorColor       =
        CPConstants.asColor( CPConstants.TIC_MINOR_COLOR_DV );
    private float   ticMinorWeight      =
        CPConstants.asFloat( CPConstants.TIC_MINOR_WEIGHT_DV );
    private float   ticMinorLen         =
        CPConstants.asFloat( CPConstants.TIC_MINOR_LEN_DV );
    private float   ticMinorMPU         =
        CPConstants.asFloat( CPConstants.TIC_MINOR_MPU_DV );
    private boolean ticMinorDraw        =
        CPConstants.asBoolean( CPConstants.TIC_MINOR_DRAW_DV );
    private Color   ticMajorColor       =
        CPConstants.asColor( CPConstants.TIC_MAJOR_COLOR_DV );
    private float   ticMajorWeight      =
        CPConstants.asFloat( CPConstants.TIC_MAJOR_WEIGHT_DV );
    private float   ticMajorLen         =
        CPConstants.asFloat( CPConstants.TIC_MAJOR_LEN_DV );
    private float   ticMajorMPU         =
        CPConstants.asFloat( CPConstants.TIC_MAJOR_MPU_DV );
    private boolean ticMajorDraw        =
        CPConstants.asBoolean( CPConstants.TIC_MAJOR_DRAW_DV );

    /////////////////////////////////////////////////
    //   Grid line properties
    /////////////////////////////////////////////////
    private Color   gridLineColor       = 
        CPConstants.asColor( CPConstants.GRID_LINE_COLOR_DV );
    private float   gridLineWeight      = 
        CPConstants.asFloat( CPConstants.GRID_LINE_WEIGHT_DV );
    private float   gridLineLPU         = 
        CPConstants.asFloat( CPConstants.GRID_LINE_LPU_DV );
    private boolean gridLineDraw        =
        CPConstants.asBoolean( CPConstants.GRID_LINE_DRAW_DV );

    /////////////////////////////////////////////////
    //   Axis properties
    /////////////////////////////////////////////////
    private Color   axisColor           = 
        CPConstants.asColor( CPConstants.AXIS_COLOR_DV );
    private float   axisWeight          = 
        CPConstants.asFloat( CPConstants.AXIS_WEIGHT_DV );

    /////////////////////////////////////////////////
    //   Label properties (these are the labels that
    //   go on the x- and y-axes, e.g., 1.1, 1.2)
    /////////////////////////////////////////////////
    private Color   labelFontColor      =
        CPConstants.asColor( CPConstants.LABEL_FONT_COLOR_DV );
    private String  labelFontName       =
        CPConstants.LABEL_FONT_NAME_DV;
    private int     labelFontStyle      =
        CPConstants.asFontStyle( CPConstants.LABEL_FONT_STYLE_DV );
    private float   labelFontSize       = 
        CPConstants.asFloat( CPConstants.LABEL_FONT_SIZE_DV );

    ///////////////////////////////////////////////////////
    //
    // The following values are recalculated every time 
    // paintComponent is invoked.
    //
    ///////////////////////////////////////////////////////
    private int                 currWidth;
    private int                 currHeight;
    private Graphics2D          gtx;
    private Rectangle2D         gridRect;
    private Font                labelFont;
    private FontRenderContext   labelFRC;
        
    /**
     * Constructor.
     * Builds a CartesianPlane with a default width and height.
     */
    public CartesianPlane()
    {
        this( mainWindowWidthDV, mainWindowHeightDV );
    }
    
    /**
     * Constructor.
     * Builds a CartesianPlane with a given width and height.
     *
     * @param width     the given width
     * @param height    the given height
     */
    public CartesianPlane( int width, int height )
    {
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
    }
    
    /**
     * This method is where you do all your drawing.
     * Note the the window must be COMPLETELY redrawn
     * every time this method is called;
     * Java does not remember anything you previously drew.
     * 
     * @param graphics  Graphics context, for doing all drawing.
     */
    @Override
    public void paintComponent( Graphics graphics )
    {
        // begin boilerplate
        super.paintComponent( graphics );
        currWidth = getWidth();
        currHeight = getHeight();
        gtx = (Graphics2D)graphics.create();
        gtx.setColor( mwBGColor );
        gtx.fillRect( 0,  0, currWidth, currHeight );
        // end boilerplate
        
        // set up the label font
        //     round font size to nearest int
        int     fontSize    = (int)(labelFontSize + .5);
        labelFont = new Font( labelFontName, labelFontStyle, fontSize );
        gtx.setFont( labelFont );
        labelFRC = gtx.getFontRenderContext();

        // Describe the rectangle containing the grid
        float   gridWidth   = currWidth - marginLeftWidth - marginRightWidth;
        float   minXco      = marginLeftWidth;
        float   gridHeight  = currHeight - marginTopWidth - marginBottomWidth;
        float   minYco      = marginTopWidth;
        gridRect = 
            new Rectangle2D.Float( minXco, minYco, gridWidth, gridHeight );

        // Set the clip region to the rectangle bounding the grid before
        // drawing any lines. Don't forget to restore the original clip
        // region after drawing the lines.
        Shape   origClip    = gtx.getClip();
//        gtx.setClip( gridRect );

        drawGridLines();
        drawMinorTics();
        drawMajorTics();
        drawAxes();
        
        gtx.setClip( origClip );

        drawHorizontalLabels();
        drawVerticalLabels();
        paintMargins();
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    private void drawAxes()
    {
        gtx.setColor( axisColor );
        gtx.setStroke( new BasicStroke( axisWeight ) );
        
        Iterator<Line2D>    axes    = 
            LineGenerator.axesIterator( gridRect );
        gtx.draw( axes.next() );
        gtx.draw( axes.next() );
    }
    
    private void drawGridLines()
    {
        if ( gridLineDraw )
        {
            LineGenerator   lineGen = 
                new LineGenerator( gridRect, gridUnit, gridLineLPU );
            gtx.setStroke( new BasicStroke( gridLineWeight ) );
            gtx.setColor( gridLineColor );
            for ( Line2D line : lineGen )
                gtx.draw( line );
        }
    }
    
    private void drawMinorTics()
    {
        if ( ticMinorDraw )
        {
            LineGenerator   lineGen = 
                new LineGenerator( 
                    gridRect, 
                    gridUnit, 
                    ticMinorMPU,
                    ticMinorLen,
                    LineGenerator.BOTH
                );
            gtx.setStroke( new BasicStroke( ticMinorWeight ) );
            gtx.setColor( ticMinorColor );
            for ( Line2D line : lineGen )
                gtx.draw( line );
        }
    }
    
    private void drawMajorTics()
    {
        if ( ticMajorDraw )
        {
            LineGenerator   lineGen = 
                new LineGenerator( 
                    gridRect, 
                    gridUnit, 
                    ticMajorMPU,
                    ticMajorLen,
                    LineGenerator.BOTH
                );
            gtx.setStroke( new BasicStroke( ticMajorWeight ) );
            gtx.setColor( ticMajorColor );
            for ( Line2D line : lineGen )
                gtx.draw( line );
        }
    }
    
    /**
     * Draw the labels on the horizontal tic marks
     * (top to bottom of y-axis).
     */
    private void drawHorizontalLabels()
    {
        // padding between tic mark and label
        final int   labelPadding    = 3;
        
        LineGenerator   lineGen = 
            new LineGenerator( 
                gridRect, 
                gridUnit, 
                ticMajorMPU,
                ticMajorLen,
                LineGenerator.HORIZONTAL
            );
        float       spacing     = gridUnit / ticMajorMPU;
        float       labelIncr   = 1 / ticMajorMPU;
        float       originYco   = (float)gridRect.getCenterY();
        for ( Line2D line : lineGen )
        {
            float       xco2    = (float)line.getX2();
            float       yco1    = (float)line.getY1();
            int         dist    = (int)((originYco - yco1) / spacing);
            float       next    = dist * labelIncr;
            String      label   = String.format( "%3.2f", next );
            TextLayout  layout  = 
                new TextLayout( label, labelFont, labelFRC );
            Rectangle2D bounds  = layout.getBounds();
            float       yOffset = (float)(bounds.getHeight() / 2);
            float       xco     = xco2 + labelPadding;
            float       yco     = yco1 + yOffset;
            layout.draw( gtx, xco, yco );
        }
    }
    
    /**
     * Draw the labels on the vertical tic marks
     * (left to right on x-axis).
     */
    private void drawVerticalLabels()
    {
        // padding between tic mark and label
        final int   labelPadding    = 3;
        
        LineGenerator   lineGen = 
            new LineGenerator( 
                gridRect, 
                gridUnit, 
                ticMajorMPU,
                ticMajorLen,
                LineGenerator.VERTICAL
            );
        float       spacing     = gridUnit / ticMajorMPU;
        float       labelIncr   = 1 / ticMajorMPU;
        float       originXco   = (float)gridRect.getCenterX();
        for ( Line2D line : lineGen )
        {
            float       xco1    = (float)line.getX2();
            int         dist    = (int)((xco1 - originXco) / spacing);
            float       next    = dist * labelIncr;
            String      label   = String.format( "%3.2f", next );

            TextLayout  layout  = 
                new TextLayout( label, labelFont, labelFRC );
            Rectangle2D bounds  = layout.getBounds();
            float       yOffset = 
                (float)(bounds.getHeight() + labelPadding);
            float       xOffset = (float)(bounds.getWidth() / 2);
            float       xco     = xco1 - xOffset;
            float       yco     = (float)line.getY2() + yOffset;
            layout.draw( gtx, xco, yco );
        }
    }
    
    private void paintMargins()
    {
        Rectangle2D rect    = new Rectangle2D.Float();
        
        // top margin
        rect.setRect( 0, 0, currWidth, marginTopWidth );
        gtx.setColor( marginTopBGColor );
        gtx.fill( rect );
        
        // right margin
        float   marginRightXco  = currWidth - marginRightWidth;
        rect.setRect( marginRightXco, 0, marginRightWidth, currHeight );
        gtx.setColor( marginRightBGColor );
        gtx.fill( rect );
        
        // bottom margin
        float   marginBottomXco  = currHeight - marginBottomWidth;
        rect.setRect( 0, marginBottomXco, currWidth, marginBottomWidth );
        gtx.setColor( marginBottomBGColor );
        gtx.fill( rect );
        
        // left margin
        rect.setRect( 0, 0, marginLeftWidth, currHeight );
        gtx.setColor( marginLeftBGColor );
        gtx.fill( rect );
    }

    /**
     * Gets the value of the grid unit
     * (the number of pixels consumed per unit).
     * 
     * @return the grid unit
     */
    public float getGridUnit()
    {
        return gridUnit;
    }

    /**
     * Sets the grid unit
     * (the number of pixels consumed per unit)
     * to the given value.
     * 
     * @param gridUnit the given value
     */
    public void setGridUnit(float gridUnit)
    {
        this.gridUnit = gridUnit;
    }

    /**
     * @return the mwBGColor
     */
    public Color getMwBGColor()
    {
        return mwBGColor;
    }

    /**
     * @param mwBGColor the mwBGColor to set
     */
    public void setMwBGColor(Color mwBGColor)
    {
        this.mwBGColor = mwBGColor;
    }

    /**
     * @return the marginTopWidth
     */
    public float getMarginTopWidth()
    {
        return marginTopWidth;
    }

    /**
     * @param marginTopWidth the marginTopWidth to set
     */
    public void setMarginTopWidth(float marginTopWidth)
    {
        this.marginTopWidth = marginTopWidth;
    }

    /**
     * @return the marginTopBGColor
     */
    public Color getMarginTopBGColor()
    {
        return marginTopBGColor;
    }

    /**
     * @param marginTopBGColor the marginTopBGColor to set
     */
    public void setMarginTopBGColor(Color marginTopBGColor)
    {
        this.marginTopBGColor = marginTopBGColor;
    }

    /**
     * @return the marginRightWidth
     */
    public float getMarginRightWidth()
    {
        return marginRightWidth;
    }

    /**
     * @param marginRightWidth the marginRightWidth to set
     */
    public void setMarginRightWidth(float marginRightWidth)
    {
        this.marginRightWidth = marginRightWidth;
    }

    /**
     * @return the marginRightBGColor
     */
    public Color getMarginRightBGColor()
    {
        return marginRightBGColor;
    }

    /**
     * @param marginRightBGColor the marginRightBGColor to set
     */
    public void setMarginRightBGColor(Color marginRightBGColor)
    {
        this.marginRightBGColor = marginRightBGColor;
    }

    /**
     * @return the marginBottomWidth
     */
    public float getMarginBottomWidth()
    {
        return marginBottomWidth;
    }

    /**
     * @param marginBottomWidth the marginBottomWidth to set
     */
    public void setMarginBottomWidth(float marginBottomWidth)
    {
        this.marginBottomWidth = marginBottomWidth;
    }

    /**
     * @return the marginBottomBGColor
     */
    public Color getMarginBottomBGColor()
    {
        return marginBottomBGColor;
    }

    /**
     * @param marginBottomBGColor the marginBottomBGColor to set
     */
    public void setMarginBottomBGColor(Color marginBottomBGColor)
    {
        this.marginBottomBGColor = marginBottomBGColor;
    }

    /**
     * @return the marginLeftWidth
     */
    public float getMarginLeftWidth()
    {
        return marginLeftWidth;
    }

    /**
     * @param marginLeftWidth the marginLeftWidth to set
     */
    public void setMarginLeftWidth(float marginLeftWidth)
    {
        this.marginLeftWidth = marginLeftWidth;
    }

    /**
     * @return the marginLeftBGColor
     */
    public Color getMarginLeftBGColor()
    {
        return marginLeftBGColor;
    }

    /**
     * @param marginLeftBGColor the marginLeftBGColor to set
     */
    public void setMarginLeftBGColor(Color marginLeftBGColor)
    {
        this.marginLeftBGColor = marginLeftBGColor;
    }

    /**
     * @return the ticMinorColor
     */
    public Color getTicMinorColor()
    {
        return ticMinorColor;
    }

    /**
     * @param ticMinorColor the ticMinorColor to set
     */
    public void setTicMinorColor(Color ticMinorColor)
    {
        this.ticMinorColor = ticMinorColor;
    }

    /**
     * @return the ticMinorWeight
     */
    public float getTicMinorWeight()
    {
        return ticMinorWeight;
    }

    /**
     * @param ticMinorWeight the ticMinorWeight to set
     */
    public void setTicMinorWeight(float ticMinorWeight)
    {
        this.ticMinorWeight = ticMinorWeight;
    }

    /**
     * @return the ticMinorLen
     */
    public float getTicMinorLen()
    {
        return ticMinorLen;
    }

    /**
     * @param ticMinorLen the ticMinorLen to set
     */
    public void setTicMinorLen(float ticMinorLen)
    {
        this.ticMinorLen = ticMinorLen;
    }

    /**
     * @return the ticMinorMPU
     */
    public float getTicMinorMPU()
    {
        return ticMinorMPU;
    }

    /**
     * @param ticMinorMPU the ticMinorMPU to set
     */
    public void setTicMinorMPU(float ticMinorMPU)
    {
        this.ticMinorMPU = ticMinorMPU;
    }

    /**
     * @return the ticMinorDraw
     */
    public boolean isTicMinorDraw()
    {
        return ticMinorDraw;
    }

    /**
     * @param ticMinorDraw the ticMinorDraw to set
     */
    public void setTicMinorDraw(boolean ticMinorDraw)
    {
        this.ticMinorDraw = ticMinorDraw;
    }

    /**
     * @return the ticMajorColor
     */
    public Color getTicMajorColor()
    {
        return ticMajorColor;
    }

    /**
     * @param ticMajorColor the ticMajorColor to set
     */
    public void setTicMajorColor(Color ticMajorColor)
    {
        this.ticMajorColor = ticMajorColor;
    }

    /**
     * @return the ticMajorWeight
     */
    public float getTicMajorWeight()
    {
        return ticMajorWeight;
    }

    /**
     * @param ticMajorWeight the ticMajorWeight to set
     */
    public void setTicMajorWeight(float ticMajorWeight)
    {
        this.ticMajorWeight = ticMajorWeight;
    }

    /**
     * @return the ticMajorLen
     */
    public float getTicMajorLen()
    {
        return ticMajorLen;
    }

    /**
     * @param ticMajorLen the ticMajorLen to set
     */
    public void setTicMajorLen(float ticMajorLen)
    {
        this.ticMajorLen = ticMajorLen;
    }

    /**
     * @return the ticMajorMPU
     */
    public float getTicMajorMPU()
    {
        return ticMajorMPU;
    }

    /**
     * @param ticMajorMPU the ticMajorMPU to set
     */
    public void setTicMajorMPU(float ticMajorMPU)
    {
        this.ticMajorMPU = ticMajorMPU;
    }

    /**
     * @return the ticMajorDraw
     */
    public boolean isTicMajorDraw()
    {
        return ticMajorDraw;
    }

    /**
     * @param ticMajorDraw the ticMajorDraw to set
     */
    public void setTicMajorDraw(boolean ticMajorDraw)
    {
        this.ticMajorDraw = ticMajorDraw;
    }

    /**
     * @return the gridLineColor
     */
    public Color getGridLineColor()
    {
        return gridLineColor;
    }

    /**
     * @param gridLineColor the gridLineColor to set
     */
    public void setGridLineColor(Color gridLineColor)
    {
        this.gridLineColor = gridLineColor;
    }

    /**
     * @return the gridLineWeight
     */
    public float getGridLineWeight()
    {
        return gridLineWeight;
    }

    /**
     * @param gridLineWeight the gridLineWeight to set
     */
    public void setGridLineWeight(float gridLineWeight)
    {
        this.gridLineWeight = gridLineWeight;
    }

    /**
     * @return the gridLineLPU
     */
    public float getGridLineLPU()
    {
        return gridLineLPU;
    }

    /**
     * @param gridLineLPU the gridLineLPU to set
     */
    public void setGridLineLPU(float gridLineLPU)
    {
        this.gridLineLPU = gridLineLPU;
    }

    /**
     * @return the gridLineDraw
     */
    public boolean isGridLineDraw()
    {
        return gridLineDraw;
    }

    /**
     * @param gridLineDraw the gridLineDraw to set
     */
    public void setGridLineDraw(boolean gridLineDraw)
    {
        this.gridLineDraw = gridLineDraw;
    }

    /**
     * @return the axisColor
     */
    public Color getAxisColor()
    {
        return axisColor;
    }

    /**
     * @param axisColor the axisColor to set
     */
    public void setAxisColor(Color axisColor)
    {
        this.axisColor = axisColor;
    }

    /**
     * @return the axisWeight
     */
    public float getAxisWeight()
    {
        return axisWeight;
    }

    /**
     * @param axisWeight the axisWeight to set
     */
    public void setAxisWeight(float axisWeight)
    {
        this.axisWeight = axisWeight;
    }

    /**
     * @return the labelFontColor
     */
    public Color getLabelFontColor()
    {
        return labelFontColor;
    }

    /**
     * @param labelFontColor the labelFontColor to set
     */
    public void setLabelFontColor(Color labelFontColor)
    {
        this.labelFontColor = labelFontColor;
    }

    /**
     * @return the labelFontName
     */
    public String getLabelFontName()
    {
        return labelFontName;
    }

    /**
     * @param labelFontName the labelFontName to set
     */
    public void setLabelFontName(String labelFontName)
    {
        this.labelFontName = labelFontName;
    }

    /**
     * @return the labelFontStyle
     */
    public int getLabelFontStyle()
    {
        return labelFontStyle;
    }

    /**
     * @param labelFontStyle the labelFontStyle to set
     */
    public void setLabelFontStyle(int labelFontStyle)
    {
        this.labelFontStyle = labelFontStyle;
    }

    /**
     * @return the labelFontSize
     */
    public float getLabelFontSize()
    {
        return labelFontSize;
    }

    /**
     * @param labelFontSize the labelFontSize to set
     */
    public void setLabelFontSize(float labelFontSize)
    {
        this.labelFontSize = labelFontSize;
    }
}

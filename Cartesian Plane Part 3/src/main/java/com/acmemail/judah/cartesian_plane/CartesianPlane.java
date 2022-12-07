package com.acmemail.judah.cartesian_plane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

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
    //   go on the x- and y-axes, e.g., 1.1, 1.2
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
    private int             currWidth;
    private int             currHeight;
    private Graphics2D      gtx;
    
    // These variables describe the shape of the rectangle, exclusive
    // of the margins, in which the grid is drawn. Their values
    // are recalculated every time paintComponent is invoked.
    private float           gridWidth;      // width of the rectangle
    private float           gridHeight;     // height of the rectangle
    private float           centerXco;      // center x-coordinate
    private float           minXco;         // left-most x-coordinate
    private float           maxXco;         // right-most x-coordinate
    private float           centerYco;      // center y-coordinate
    private float           minYco;         // top-most y-coordinate
    private float           maxYco;         // bottom-most y-coordinate
    
    public CartesianPlane()
    {
        this( mainWindowWidthDV, mainWindowHeightDV );
    }
    
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
        
        // Describe the rectangle containing the grid
        gridWidth = currWidth - marginLeftWidth - marginRightWidth;
        minXco = marginLeftWidth;
        maxXco = minXco + gridWidth;
        centerXco = minXco + gridWidth / 2f;
        gridHeight = currHeight - marginTopWidth - marginBottomWidth;
        minYco = marginTopWidth;
        maxYco = minYco + gridHeight;
        centerYco = minYco + gridHeight / 2f;

        drawGrid();
        drawAxes();
        paintMargins();
        
        // begin boilerplate
        gtx.dispose();
        // end boilerplate
    }
    
    private void drawGrid()
    {
        gtx.setColor( gridLineColor );
        gtx.setStroke( new BasicStroke( gridLineWeight ) );
        float   gridSpacing = gridUnit / gridLineLPU;
        
        float   numLeft = (float)Math.floor( gridWidth / 2 / gridSpacing );
        float   leftXco = centerXco - numLeft * gridSpacing;
        for ( float xco = leftXco ; xco <= maxXco ; xco += gridSpacing )
        {
            Line2D  gridLine    = 
                new Line2D.Float( xco, minYco, xco, maxYco );
            gtx.draw( gridLine );
        }
        
        float   numTop  = (float)Math.floor( gridHeight / 2f / gridSpacing );
        float   topYco  = centerYco - numTop * gridSpacing;
        for ( float yco = topYco ; yco <= maxYco ; yco += gridSpacing )
        {
            Line2D  gridLine    = 
                new Line2D.Float( minXco, yco, maxXco, yco );
            gtx.draw( gridLine );
        }
    }
    
    private void drawAxes()
    {
        gtx.setColor( axisColor );
        gtx.setStroke( new BasicStroke( axisWeight ) );
        Line2D  line    = new Line2D.Float();
        
        // x axis
        line.setLine( centerXco, minYco, centerXco, maxYco );
        gtx.draw( line );
        
        // y axis
        line.setLine( minXco, centerYco, maxXco, centerYco );
        gtx.draw( line );
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
     * @return the gridUnit
     */
    public float getGridUnit()
    {
        return gridUnit;
    }

    /**
     * @param gridUnit the gridUnit to set
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

    /**
     * @return the gridWidth
     */
    public float getGridWidth()
    {
        return gridWidth;
    }

    /**
     * @param gridWidth the gridWidth to set
     */
    public void setGridWidth(float gridWidth)
    {
        this.gridWidth = gridWidth;
    }

}

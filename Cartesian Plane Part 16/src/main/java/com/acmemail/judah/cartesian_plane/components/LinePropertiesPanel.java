package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * This panel
 * builds the GUI
 * for the LinePropertiesPanel
 * without putting in
 * any of the management logic.
 * In other words,
 * if you display this panel
 * you'll see what 
 * the GUI should look like,
 * but the control buttons
 * (Reset, Apply, Close)
 * aren't hooked up.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class LinePropertiesPanel extends JPanel
{
    /** Label for the minor tics radio button. */
    private static final String minorTics   = "Minor Tics";
    /** Label for the major tics radio button. */
    private static final String majorTics   = "Major Tics";
    /** Label for the grid lines radio button. */
    private static final String gridLines   = "Grid Lines";
    /** Label for axes radio button. */
    private static final String axes        = "Axes";
    
    /** 
     * Establishes, based on label, what subclass of 
     * LinePropertySet is required.
     */
    private static final 
    Map<String, Supplier<LinePropertySet>> typeMap = 
        Map.ofEntries( 
            Map.entry( minorTics, LinePropertySetTicMinor::new ),
            Map.entry( majorTics, LinePropertySetTicMajor::new ),
            Map.entry( gridLines, LinePropertySetGridLines::new ),
            Map.entry( axes, LinePropertySetAxes::new) 
        );

    /** 
     * Margin around the edge of this panel; 
     * implemented via an EmptyBorder.
     */
    private static final int        margin      = 10;
    
    /** Collection of all radio buttons in the panel. */
    private final PButtonGroup<LinePropertySet> buttonGroup =
        new PButtonGroup<>();
    
    /**
     * Constructor.
     * Fully configures the line properties GUI.
     */
    public LinePropertiesPanel()
    {
        super( new BorderLayout() );
        Border  lineBorder      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        Border  titledBorder    = 
            BorderFactory.createTitledBorder( lineBorder, "Lines" );
        Border  emptyBorder     =
            BorderFactory.createEmptyBorder( 
                margin, 
                margin, 
                margin, 
                margin 
            );
        Border  border          =
            BorderFactory.createCompoundBorder( 
                emptyBorder, 
                titledBorder 
            );
        setBorder( border );
        
        add( getMainPanel(), BorderLayout.CENTER );
        add( getControlPanel(), BorderLayout.SOUTH );
        buttonGroup.selectIndex( 0 );
    }
    
    /**
     * Configures the main panel
     * for this GUI.
     * It consist
     * of a panel
     * containing the radio buttons
     * on the left,
     * and the feedback components
     * on the right.
     * 
     * @return  the main panel
     */
    private JPanel getMainPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        Component horSpace  =
            Box.createRigidArea( new Dimension( 25, 0 ) );
        
        panel.setLayout( layout );
        panel.add( getRadioButtonPanel() );
        panel.add( horSpace );
        panel.add( new PropertiesPanel() );
        return panel;
    }
    
    /**
     * Configures the radio button panel.
     * This consists of 
     * four PRadioButtons
     * arranged vertically.
     * 
     * @return the radio button panel
     */
    private JPanel getRadioButtonPanel()
    {
        JPanel  panel   = new JPanel( new GridLayout( 4, 1 ) );
        Stream.of( axes, majorTics, minorTics, gridLines )
            .map( this::newRadioButton )
            .peek( panel::add )
            .forEach( buttonGroup::add );
        return panel;
    }
    
    /**
     * Helper method
     * for getRadioButtonPanel.
     * Creates a PRadionButton 
     * with a given label,
     * and a LinePropertySet object.
     * 
     * @param given label
     * 
     * @return  
     *      a PRadioButton configured with a label
     *      and a LinePropertySet object
     *      
     * @see #typeMap
     */
    private PRadioButton<LinePropertySet> newRadioButton( String label )
    {
        LinePropertySet                 set     = 
            typeMap.get( label ).get();
        PRadioButton<LinePropertySet>   button  = 
            new PRadioButton<>( set, label );
        return button;
    }
    
    /**
     * Configures a panel
     * containing the control buttons.
     * This consists of 
     * the Apply, Reset and Close buttons
     * arranged horizontally.
     * 
     * @return  the control button panel
     */
    private JPanel getControlPanel()
    {
        JPanel  panel   = new JPanel();
        Border  border  = BorderFactory.createEmptyBorder( 10, 0, 0, 0 );
        panel.setBorder( border );
        
        JButton applyButton = new JButton( "Apply" );
        JButton resetButton = new JButton( "Reset" );
        JButton closeButton = new JButton( "Close" );
        panel.add( applyButton );
        panel.add( resetButton );
        panel.add( closeButton );
        
        applyButton.addActionListener( this::applyAction );
        resetButton.addActionListener( this::resetAction );
        closeButton.addActionListener( this::closeAction );
        
        return panel;
    }
    
    /**
     * Action method
     * that is executed
     * when the Apply button pushed.
     * 
     * @param evt   
     *      event object associated with the ActionEvent
     *      that caused this method to be invoked; not used
     */
    private void applyAction( ActionEvent evt )
    {
        LinePropertySet set     = buttonGroup.getSelectedProperty();
        if ( set == null  )
        {
            String  msg = "LinePropertySet not found";
            throw new ComponentException( msg );
        }
    }
    
    /**
     * Action method
     * that is executed
     * when the Reset button pushed.
     * 
     * @param evt   
     *      ActionEvent object
     *      that caused this method to be invoked; not used
     */
    private void resetAction( ActionEvent evt )
    {
        // Reset all LinePropertySets to their original values
        buttonGroup.getButtons().stream()
            .map( b -> b.get() )
            .forEach( s -> s.reset() );
        
        // Invoke all ItemListeners on the selected button, passing
        // a SELECTED event. This will cause the GUI to be reinitialized
        // with the selected button's LinePropertySet values.
        PRadioButton<LinePropertySet>   selectedButton  = 
            buttonGroup.getSelectedButton();
        ItemEvent   event   = 
            new ItemEvent(
                selectedButton,
                ItemEvent.ITEM_FIRST,
                selectedButton,
                ItemEvent.SELECTED
            );
        Stream.of( selectedButton.getItemListeners() )
            .forEach( l -> l.itemStateChanged( event ) );
    }
    
    /**
     * Action method
     * that is executed
     * when the Close button pushed.
     * If the root component
     * in the source's containment hierarchy
     * is a JDialog it is closed,
     * otherwise no action is take.
     * if there is no top-level window
     * in the source's containment hierarchy
     * a ComponentException is thrown.
     * 
     * @param evt   
     *      event object associated with the ActionEvent
     *      that caused this method to be invoked; not used
     *      
     * @throws ComponentException
     *      if the event sources component
     *      does not have a top-level window parent
     *      in its containment hierarchy.
     */
    private void closeAction( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof JComponent )
        {
            Container   testObj = ((JComponent)source).getParent();
            while ( !(testObj instanceof Window ) && testObj != null )
                testObj = testObj.getParent();
            if ( testObj == null )
            {
                StringBuilder   bldr    = new StringBuilder()
                    .append( "Top-level window of LinePropertiesPanel " )
                    .append( "not found; " )
                    .append( "source type = " )
                    .append( source.getClass().getName() );
                throw new ComponentException( bldr.toString() );
            }
            
            if ( testObj instanceof JDialog )
                ((JDialog)testObj).setVisible( false );
        }
    }

    /**
     * Encapsulation of the panel
     * that contains all the configuration
     * controls for the parent panel.
     * 
     * @author Jack Straub
     */
    private class PropertiesPanel extends JPanel 
        implements ItemListener
    {
        /** Default value for configuring all spinners. */
        private static final float  defVal          = 1.0f;
        /** Minimum value for configuring all spinners. */
        private static final float  defMin          = 0.0f;
        /** Maximum value for configuring all spinners. */
        private static final float  defMax          = 500.0f;
        /** Step value for configuring all spinners. */
        private static final float  defStep         = .1f;
        
        /** The text for the label on the StrokeFeedback component. */
        private static final String strokeText      = "Stroke";
        /** The text for the label on the LengthFeedback component. */
        private static final String lengthText      = "Length";
        /** The text for the label on the SpacingFeedback component. */
        private static final String spacingText     = "Spacing";
        /** The text for the label on the Draw check button. */
        private static final String drawText        = "Draw";
        
        /** Label to accompany the StrokeFeedback component. */
        private final JLabel                strokeLabel     = 
            new JLabel( strokeText, SwingConstants.RIGHT );
        /** Spinner model to accompany the StrokeFeedback spinner. */
        private final SpinnerNumberModel    strokeModel     =
            new SpinnerNumberModel( defVal, defMin, defMax, defStep );
        /** Spinner for control of the StrokeFeedback component. */
        private final JSpinner              strokeSpinner   = 
            new JSpinner( strokeModel );
        /** The StrokeFeedback component. */
        private final StrokeFeedback  strokeFB      = 
            new StrokeFeedback( () -> doubleValue( strokeModel ) );

        /** Label to accompany the LengthFeedback component. */
        private final JLabel        lengthLabel     = 
            new JLabel( lengthText, SwingConstants.RIGHT );
        /** Spinner model to accompany the LengthFeedback spinner. */
        private final SpinnerNumberModel    lengthModel     =
            new SpinnerNumberModel( defVal, defMin, defMax, defStep );
        /** Spinner for control of the LengthFeedback component. */
        private final JSpinner              lengthSpinner   =
            new JSpinner( lengthModel );
        /** The LengthFeedback component. */
        private final LengthFeedback lengthFB       = 
            new LengthFeedback( () -> doubleValue( lengthModel ) );

        /** Label to accompany the SpacinFeedback component. */
        private final JLabel                spacingLabel    = 
            new JLabel( spacingText, SwingConstants.RIGHT );
        /** Spinner model to accompany the SpacingFeedback spinner. */
        private final SpinnerNumberModel    spacingModel    =
            new SpinnerNumberModel( defVal, defMin, defMax, defStep );
        /** Spinner for control of the SpacingFeedback component. */
        private final JSpinner              spacingSpinner  =
            new JSpinner( spacingModel );
        /** The SpacingFeedback component. */
        private final SpacingFeedback   spacingFB   = 
            new SpacingFeedback( () -> doubleValue( spacingModel ) );
        
        /** Editor for managing colors. */
        private final ColorEditor   colorEditor     = new ColorEditor();
        /** Push button from the ColorEdtor. */
        private final JButton       colorButton     = 
            colorEditor.getColorButton();
        /** Text field from the color editor. */
        private final JTextField    colorField      = 
            colorEditor.getTextEditor();
        /** Feedback window from the color editor. */
        private final JComponent    colorFB         = 
            colorEditor.getFeedback();  
                
        /** Label to identify the Draw check box. */
        private final JLabel        drawLabel       = 
            new JLabel( drawText, SwingConstants.RIGHT );
        /** The draw check box. */
        private final JCheckBox     drawToggle      = new JCheckBox();
        
        /**
         * Constructor.
         * Fully configures
         * the panel containing
         * the properties components.
         */
        private PropertiesPanel()
        {
            super( new GridLayout( 5, 3, 5, 3 ) );
            
            // sanity check; radio buttons must be created prior to
            // this class being instantiated.
            if ( buttonGroup.getButtonCount() < 1 )
                throw new RuntimeException( "no radio buttons found" );
            
            add( strokeLabel );
            add( strokeSpinner );
            add( strokeFB );
            strokeSpinner.addChangeListener( e -> strokeFB.repaint() );
            
            add( lengthLabel );
            add( lengthSpinner );
            add( lengthFB );
            lengthSpinner.addChangeListener( e -> lengthFB.repaint() );
            
            add( spacingLabel );
            add( spacingSpinner );
            add( spacingFB );
            spacingSpinner.addChangeListener( e -> spacingFB.repaint() );
            
            add( colorButton );
            add( colorField );
            add( colorFB );
            
            add( drawLabel );
            add( drawToggle );
            add( new JLabel() ); // placeholder
            
            buttonGroup.getButtons()
                .forEach( b -> b.addItemListener( this ) );
        }
        
        /**
         * Convenience method
         * for configuring the DoubleSupplier
         * for the feedback components.
         * For a given SpinnerNumberModel,
         * returns the encapsulated double value.
         * 
         * @param model the given SpinnerNumberModel
         * 
         * @return  
         *      the double value encapsulated in 
         *      the given SpinnerNumberModel
         */
        private Double doubleValue( SpinnerNumberModel model )
        {
            Double  value   = model.getNumber().doubleValue();
            return value;
        }
        
        /**
         * Method to listen for 
         * ItemListener events.
         * Associated with this panel's
         * radio button objects.
         * 
         * @param evt   
         *      event object associated with 
         *      ItemListener activation
         */
        @SuppressWarnings("unchecked")
        @Override
        public void itemStateChanged(ItemEvent evt)
        {
            Object  source  = evt.getSource();
            if ( source instanceof PRadioButton )
            {
                PRadioButton<LinePropertySet>   button  =
                    (PRadioButton<LinePropertySet>)source;
                LinePropertySet                 set     = button.get();
                if ( button.isSelected() )
                    itemSelected( set );
                else
                    itemDeselected( set );
            }
        }
        
        /**
         * Given a LinePropertySet 
         * associated with a deselected radio button
         * copy the values of 
         * the GUI controls
         * into the corresponding fields
         * of the set.
         * 
         * @param set   the given LinePropertySet
         */
        private void itemDeselected( LinePropertySet set )
        {
            if ( set.hasDraw() )
                set.setDraw( drawToggle.isSelected() );
            if ( set.hasLength() )
                set.setLength( lengthModel.getNumber().floatValue() );
            if ( set.hasSpacing() )
                set.setSpacing( spacingModel.getNumber().floatValue() );
            if ( set.hasStroke() )
                set.setStroke( strokeModel.getNumber().floatValue() );
            if ( set.hasColor() )
                set.setColor( colorEditor.getColor().orElse( null ) );
        }
        
        /**
         * Given a LinePropertySet 
         * associated with a selected radio button
         * copy the property values
         * from the set 
         * to the corresponding.
         * If a particular property
         * of the set
         * is not supported,
         * disable the corresponding GUI component,
         * otherwise make sure
         * the component is enabled.
         * 
         * @param set   the given LinePropertySet
         */
        private void itemSelected( LinePropertySet set )
        {
            boolean hasDraw     = set.hasDraw();
            drawToggle.setEnabled( hasDraw );
            drawLabel.setEnabled( hasDraw );
            if ( hasDraw )
                drawToggle.setSelected( set.getDraw() );
            
            boolean hasLength   = set.hasLength();
            lengthSpinner.setEnabled( hasLength );
            lengthLabel.setEnabled( hasLength );
            if ( hasLength )
                lengthModel.setValue( set.getLength() );

            boolean hasSpacing  = set.hasSpacing();
            spacingLabel.setEnabled( hasSpacing );
            spacingSpinner.setEnabled( hasSpacing );
            if ( hasSpacing )
                spacingModel.setValue( set.getSpacing() );

            boolean hasStroke  = set.hasStroke();
            strokeLabel.setEnabled( hasSpacing );
            strokeSpinner.setEnabled( hasStroke );
            if ( hasStroke )
                strokeModel.setValue( set.getStroke() );

            boolean hasColor    = set.hasColor();
            colorButton.setEnabled( hasColor );
            colorField.setEditable( hasColor );
            if ( hasColor )
                colorEditor.setColor( set.getColor() );
        }
    }
}

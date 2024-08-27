package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Color;
import java.util.stream.Stream;

import javax.swing.JComponent;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileEditorTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

public class ProfileEditorTest
{
    private static final String axesSetName         =
        LinePropertySetAxes.class.getSimpleName();
    private static final String gridLinesSetName    =
        LinePropertySetGridLines.class.getSimpleName();
    private static final String ticMajorSetName     =
        LinePropertySetTicMajor.class.getSimpleName();
    private static final String ticMinorSetName     =
        LinePropertySetTicMinor.class.getSimpleName();
    private static final String[]   allSetNames     =
    { axesSetName, gridLinesSetName, ticMajorSetName, ticMinorSetName };

    /**
     * Represents the properties of a Profile as determined by the
     * PropertyManager. Used as needed to return the PropertyManager
     * to its original state (see for example, {@link #beforeEach()}.
     * Never modified after initialization.
     */
    private static Profile          baseProfile     = new Profile();
    /** 
     * Contains property values guaranteed to be different from those
     * stored in the BaseProfile. Never modified after initialization.
     */
    private static Profile          distinctProfile = 
        ProfileUtils.getDistinctProfile( baseProfile );
    /** 
     * Profile used to initialize the test GUI/ProfileEditor.
     * After initialization the reference must not be changed,
     * but the contents of the object may be changed as needed.
     * The contents are restored to their original values before
     * each test (see {@link #beforeEach()}).
     */
    private static Profile              profile = new Profile();
    /** 
     * The object that displays and manager the ProfileEditor.
     * Guarantees that all interaction with the ProfileEditor
     * components is conducted via the EDT.
     */
    private static ProfileEditorTestGUI testGUI;
    
    @BeforeAll
    static void beforeAll() throws Exception
    {
        testGUI = ProfileEditorTestGUI.getTestGUI( profile );
    }
    
    @AfterAll
    public static void afterAll()
    {
        // For the sake of tests run in suites, make sure
        // the original profile is restored at the end of the test.
        // Also make sure all GUI windows are disposed.
        baseProfile.apply();
        ComponentFinder.disposeAll();
    }

    @BeforeEach
    public void beforeEach() throws Exception
    {
        // Restore the properties in the PropertyManager
        // to their original values.
        baseProfile.apply();
        
        // Return the working profile to its original state;
        // reset the components of the ProfileEditor to their
        // original values.
        testGUI.reset();
        
        // Verify that the working profile has been returned
        // to its original state.
        assertEquals( baseProfile, profile );
        
        // Verify that the components of the ProfileEditor GUI
        // have been returned to their original states.
        validateCurrState( baseProfile );
    }
    
    /**
     * Verify that all ProfileEditor components
     * are correctly initialized
     * immediately after instantiation.
     */
    @Test
    public void testProfileEditor()
    {
        validateCurrState( baseProfile );
    }

    /**
     * This test does little beside
     * increasing test coverage
     * by calling ProfileEditor.getFeedback().
     */
    @Test
    public void testGetFeedBack()
    {
        JComponent  feedback    = testGUI.getFeedback();
        assertNotNull( feedback );
    }

    @Test
    public void testApply()
    {
        // Sanity check: verify that the test data is in
        // the expected state.
        validateCurrState( baseProfile );
        
        // Changes the values of all the ProfileEditor GUI components.
        applyDistinctProperties();
        
        // Verify all components have been changed to distinct values.
        validateCurrState( distinctProfile );
        
        // Apply the modified values and verify that the
        // PropertyManager has been updated.
        testGUI.apply();
        Profile profile = new Profile();
        validateCurrState( profile );
    }

    @Test
    public void testReset()
    {
        // Sanity check: verify that the test data is in
        // the expected state.
        validateCurrState( baseProfile );
        
        // Changes the values of all the ProfileEditor GUI components.
        applyDistinctProperties();
        
        // Verify all components have been changed to distinct values.
        validateCurrState( distinctProfile );

        // Reset the GUI; verify that all components have been
        // returned to their original values.
        testGUI.reset();
        validateCurrState( baseProfile );
    }

    /**
     * Set the values of those components of the ProfileEditor
     * that are responsible for editing a profile's properties
     * to unique values.
     */
    private void applyDistinctProperties()
    {
        applyDistinctGraphProperties();
        Stream.of( allSetNames )
            .forEach( this::applyDistinctLineProperties );
    }
    
    /**
     * Set the values of those components of the ProfileEditor
     * that are responsible for editing a profile's GraphPropertySet
     * to unique values.
     */
    private void applyDistinctGraphProperties()
    {
        GraphPropertySet    graphSet    = distinctProfile.getMainWindow();
        int                 iFGColor    = getRGB( graphSet.getFGColor() );
        int                 iBGColor    = getRGB( graphSet.getBGColor() );
        
        testGUI.setFontDraw( graphSet.isFontDraw() );
        testGUI.setBGColor( iBGColor );
        testGUI.setName( distinctProfile.getName() );
        testGUI.setGridUnit( distinctProfile.getGridUnit() );
        testGUI.setGridWidth( graphSet.getWidth() );
        
        Thread  thread  = testGUI.editFont();
        testGUI.setFGColor( iFGColor );
        testGUI.setFontBold( graphSet.isBold() );
        testGUI.setFontItalic( graphSet.isItalic() );
        testGUI.setFontName( graphSet.getFontName() );
        testGUI.setFontSize( graphSet.getFontSize() );
        testGUI.selectFDOK();
        Utils.join( thread );
    }
    
    /**
     * Set the values of those components of the ProfileEditor
     * that are responsible for editing a given LinePropertySet
     * to unique values.
     * 
     * @param propSet   the given LinePropertySet
     */
    private void applyDistinctLineProperties( String propSet )
    {
        LinePropertySet lineSet = 
            distinctProfile.getLinePropertySet( propSet );
        if ( lineSet.hasColor() )
        {
            Color   color   = lineSet.getColor();
            int     rgb     = getRGB( color );
            testGUI.setColor( propSet, rgb );
        }
        if ( lineSet.hasDraw() )
            testGUI.setDraw( propSet, lineSet.getDraw() );
        if ( lineSet.hasLength() )
            testGUI.setLength( propSet, lineSet.getLength() );
        if ( lineSet.hasSpacing() )
            testGUI.setSpacing( propSet, lineSet.getSpacing() );
        if ( lineSet.hasStroke() )
            testGUI.setStroke( propSet, lineSet.getStroke() );
    }

    /**
     * Verify that the values of all components
     * in the ProfileEditor
     * match the given profile.
     * 
     * @param currState the given profile
     */
    private void validateCurrState( Profile currState )
    {
        Profile profile = new Profile();
        
        collectGraphProperties( profile );
        assertEquals( currState.getMainWindow(), profile.getMainWindow() );
        Stream.of( allSetNames )
            .peek( s -> collectLineProperties( s, profile ) )
            .forEach( s -> {
                LinePropertySet currSet = 
                    currState.getLinePropertySet( s );
                LinePropertySet testSet = profile.getLinePropertySet( s );
                assertEquals( currSet, testSet );
            });
        assertEquals( currState, profile );
    }
    
    /**
     * Use the values of the components of the ProfileEditor.
     * to initialize the GraphPropertySetMW of a given profile.
     * 
     * @param profile   the given profile
     */
    private void collectGraphProperties( Profile profile )
    {
        // Make sure the font dialog is initialized
        Thread  thread  = testGUI.editFont();
        testGUI.selectFDCancel();
        Utils.join( thread );
        
        GraphPropertySet    graphSet    = profile.getMainWindow();
        Color               bgColor     =
            new Color( testGUI.getBGColor() );
        Color               fgColor     =
            new Color( testGUI.getFGColor() );
        
        profile.setName( testGUI.getName() );
        profile.setGridUnit( testGUI.getGridUnit() );
        graphSet.setWidth( testGUI.getGridWidth() );
        graphSet.setBGColor( bgColor );
        graphSet.setBold( testGUI.getFontBold() );
        graphSet.setFGColor( fgColor );
        graphSet.setFontDraw( testGUI.getFontDraw() );
        graphSet.setFontName( testGUI.getFontName() );
        graphSet.setFontSize( testGUI.getFontSize() );
        graphSet.setItalic( testGUI.getFontItalic() );
    }
    /**
     * Use the values of the components of the ProfileEditor.
     * to initialize the given LinePropertySet of a given profile.
     * 
     * @param profile   the given profile
     */
    private void collectLineProperties( String propSet, Profile profile )
    {
        LinePropertySet lineProperties  = 
            profile.getLinePropertySet( propSet );
        if ( lineProperties.hasColor() )
        {
            Color   color   = new Color( testGUI.getColor( propSet ) );
            lineProperties.setColor( color );
        }
        if ( lineProperties.hasDraw() )
            lineProperties.setDraw( testGUI.getDraw( propSet ) );
        if ( lineProperties.hasLength() )
            lineProperties.setLength( testGUI.getLength( propSet ) );
        if ( lineProperties.hasSpacing() )
            lineProperties.setSpacing( testGUI.getSpacing( propSet ) );
        if ( lineProperties.hasStroke() )
            lineProperties.setStroke( testGUI.getStroke( propSet ) );
    }
    
    /**
     * Gets the RGB value of a given color
     * with the alpha bits suppressed.
     * 
     * @param color the given color
     * 
     * @return  the RGB value of the given color
     */
    private int getRGB( Color color )
    {
        int rgb = color.getRGB() & 0xffffff;
        return rgb;
    }
}

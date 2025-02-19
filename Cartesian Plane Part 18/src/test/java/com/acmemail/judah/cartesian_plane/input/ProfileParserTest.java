package com.acmemail.judah.cartesian_plane.input;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JDialog;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

/**
 * @author Jack Straub
 */
@Timeout( 2 )
public class ProfileParserTest
{
    /**
     * Prototype Profile; 
     * contains the values of the profile properties
     * obtained from the PropertyManager
     * before any edited property values
     * are committed.
     * Never changed after initialization;
     * used to update the PropertyManager to original value.
     * 
     * @see #afterEach()
     */
    private final Profile   protoProfile    = new Profile();
    /**
     * Profile initialized to values guaranteed to be different
     * from protoProfile.
     * Never changed after initialization.
     * 
     * @see ProfileUtils#getDistinctProfile(Profile)
     */
    private final Profile   distinctProfile = 
        ProfileUtils.getDistinctProfile( protoProfile );
    
    /** 
     * Profile to be modified as needed by tests;
     * returned to initial value in {@link #beforeEach()}.
     */
    private Profile workingProfile;
    
    /** The JOptionPane dialog used to display error messages. */
    private JDialog errorDialog  = null;
    
    /** The OK button from {@link #errorDialog}. */
    private AbstractButton errorDialogOKButton;
    
    @BeforeEach
    public void beforeEach() throws Exception
    {
        // Refresh the workingProfile from the PropertyManager;
        // the refreshed Profile must be equal to protoProfile.
        workingProfile = new Profile();
    }
    
    @AfterEach
    public void afterEach()
    {
        // Restore the property manager fields to their original values
        protoProfile.apply();
    }

    @Test
    public void testProfileParser()
    {
        // Default constructor instantiates a profile that is expected
        // to be initialized with default values from the PropertyManager.
        ProfileParser   parser  = new ProfileParser();
        Profile         profile = parser.getProfile();
        assertNotNull( profile );
        assertEquals( protoProfile, profile );
    }

    @Test
    public void testProfileParserProfile()
    {
        // Instantiate parser with explicit profile and make sure
        // the profile is encapsulated.
        float           diffGridUnit    = distinctProfile.getGridUnit();
        workingProfile.setGridUnit( diffGridUnit );
        ProfileParser   parser          = 
            new ProfileParser( workingProfile );
        Profile         testProfile     = parser.getProfile();
        assertSame( workingProfile, testProfile );
        assertEquals( diffGridUnit, testProfile.getGridUnit() );
    }

    @Test
    public void testGetProperties()
    {
        ProfileParser   outParser   = new ProfileParser( distinctProfile );
        Stream<String>  stream      = outParser.getProperties();
        ProfileParser   inParser    = new ProfileParser();
        inParser.loadProperties( stream );
        assertEquals( distinctProfile, inParser.getProfile() );
    }
    
    /**
     * Create a list of string to encapsulate an input stream
     * for the loadProperties method, for example:
     * <ul>
     * <li>list.add( "PROFILE: name" );</li>
     * </ul>
     * <p>
     * Add strings to change a property in a profile's main window, 
     * for example:
     * <ul>
     * <li>list.add( "class: GraphPropertySetMW" );</li>
     * <li>list.add( "font_size: 22" );</li>
     * </ul>
     * <p>
     * For each LinePropertySet subclass add strings to change
     * one property, for example:
     * <ul>
     * <li>list.add( "class: LinePropertySetAxes" );</li>
     * <li>list.add( "stroke: 5" );</li>
     * </ul>
     * <p>
     */
    @Test
    public void testLoadProperties()
    {
        Mutator[]   mutators    =
        {
            new Mutator(
                LinePropertySetAxes.class.getSimpleName(),
                ProfileParser.LENGTH,
                p -> p.getLength(),
                (p,v) -> p.setLength( v )
            ),
            new Mutator(
                LinePropertySetGridLines.class.getSimpleName(),
                ProfileParser.STROKE,
                p -> p.getStroke(),
                (p,v) -> p.setStroke( v )
            ),
            new Mutator(
                LinePropertySetTicMajor.class.getSimpleName(),
                ProfileParser.SPACING,
                p -> p.getSpacing(),
                (p,v) -> p.setSpacing( v )
            ),
            new Mutator(
                LinePropertySetTicMinor.class.getSimpleName(),
                ProfileParser.STROKE,
                p -> p.getStroke(),
                (p,v) -> p.setStroke( v )
            ),
        };
        
        // Change the profile name
        List<String>    list            = new ArrayList<>();
        String          distinctName    = distinctProfile.getName();
        list.add( ProfileParser.PROFILE + " " + distinctName );
        workingProfile.setName( distinctName );
        
        // Change the main window font size
        addFontSize( list );
        
        // Change one property in each of the LinePropertySets
        for ( Mutator mutator : mutators )
            mutator.add( list );
        
        ProfileParser   testParser  = new ProfileParser();
        testParser.loadProperties( list.stream() );
        assertEquals( workingProfile, testParser.getProfile() );
    }

    @Test
    public void testGetProfile()
    {
        // see testProfileParserProfile
    }
    
    @Test
    public void testExcessWhitespace()
    {
        // Testing input containing empty strings, and strings
        // with extra whitespace at beginning and end of line
        // and in between tokens.
        List<String>    props   = getTestPropertyList();
        int             len     = props.size();
        for ( int inx = 0 ; inx < len ; ++inx )
        {
            String  newString   = excessWhitespace( props.get( inx ) );
            props.set( inx, newString );
        }
        props.add( 0, "     " );
        props.add( len / 2, "     " );
        props.add( "     " );
        ProfileParser   testParser  = new ProfileParser();
        testParser.loadProperties( props.stream() );
        Profile         testProfile = testParser.getProfile();
        assertEquals( workingProfile, testProfile );
    }
    
    @Test
    public void testNVPairWrongTokenCount()
    {
        List<String>    props   = getTestPropertyList();
        // Add line with too few tokens
        props.add( 3, ProfileParser.FONT_SIZE + " " );
        // Add line with too many tokens
        props.add( 4, ProfileParser.FONT_SIZE + " " + 10 + ", a"  );
        
        ProfileParser   testParser  = new ProfileParser();
        int             testCount   = expectDialog( () ->
            testParser.loadProperties( props.stream() )
        );
        
        assertEquals( 2, testCount );
        Profile         testProfile = testParser.getProfile();
        assertEquals( testProfile, workingProfile );
    }
    
    @Test
    public void testMissingClassDecl()
    {
        // Try to parse a line property and a graph property
        // before any class has been declared
        List<String>    props   = getTestPropertyList();
        props.add( 2, ProfileParser.FONT_SIZE + " " + 10 );
        props.add( 3, ProfileParser.STROKE + " " + 2  );
        
        ProfileParser   testParser  = new ProfileParser();
        int testCount   = expectDialog( () ->
            testParser.loadProperties( props.stream() )
        );
        
        assertEquals( 2, testCount );
        Profile         testProfile = testParser.getProfile();
        assertEquals( testProfile, workingProfile );
    }
    
    @Test
    public void testInvalidClassDecl()
    {
        // After processing graph properties (lines 2, 3), before
        // starting line properties (line 4) add an invalid class
        // declaration followed by a graph property and a line property.
        // Should yield 3 error dialogs.
        List<String>    props   = getTestPropertyList();
        props.add( 4, ProfileParser.CLASS + " " + "notAValidClassName" );
        props.add( 5, ProfileParser.STROKE + " " + 2  );
        props.add( 6, ProfileParser.FONT_SIZE + " " + 10  );
        
        ProfileParser   testParser  = new ProfileParser();
        int testCount   = expectDialog( () ->
            testParser.loadProperties( props.stream() )
        );
        
        assertEquals( 3, testCount );
        Profile         testProfile = testParser.getProfile();
        assertEquals( testProfile, workingProfile );
    }
    
    @Timeout( 5 )
    @Test
    public void testMisplacedPropertyMisc()
    {
        // This test is mainly to pick up more points for code coverage.
        // Add a graph property class declaration followed by all the line 
        // properties. Then add a line property class declaration
        // followed by all the graph properties
        List<String>    props       = getTestPropertyList();
        String          lineName    = 
            LinePropertySetAxes.class.getSimpleName();
        String  graphName           = 
            GraphPropertySetMW.class.getSimpleName();
        props.add( ProfileParser.CLASS + " " + graphName );
        props.add( ProfileParser.STROKE + " " + 2  );
        props.add( ProfileParser.LENGTH + " " + 2  );
        props.add( ProfileParser.SPACING + " " + 2  );
        props.add( ProfileParser.DRAW + " " + 2  );
        props.add( ProfileParser.COLOR + " " + 2  );

        props.add( ProfileParser.CLASS + " " + lineName );
        props.add( ProfileParser.FONT_BOLD + " " + false );
        props.add( ProfileParser.FONT_ITALIC + " " + 2  );
        props.add( ProfileParser.FONT_NAME + " " + 2  );
        props.add( ProfileParser.FONT_DRAW + " " + 2  );
        props.add( ProfileParser.BG_COLOR + " " + 2  );
        props.add( ProfileParser.FG_COLOR + " " + 2  );
        props.add( ProfileParser.WIDTH + " " + 2  );
        
        ProfileParser   testParser  = new ProfileParser();
        int testCount   = expectDialog( () ->
            testParser.loadProperties( props.stream() )
        );
        
        assertEquals( 12, testCount );
        Profile         testProfile = testParser.getProfile();
        assertEquals( testProfile, workingProfile );
    }
    
    @Test
    public void testMisplacedProperty()
    {
        // After declaring the graph property class try to add
        // a line property. After adding the line property class
        // try to add a graph property. Should yield 2 error dialogs.
        List<String>    props   = getTestPropertyList();
        props.add( 3, ProfileParser.STROKE + " " + 2  );
        // line property class declaration is now at line 5.
        props.add( 6, ProfileParser.FONT_SIZE + " " + 10  );
        
        ProfileParser   testParser  = new ProfileParser();
        int testCount   = expectDialog( () ->
            testParser.loadProperties( props.stream() )
        );
        
        assertEquals( 2, testCount );
        Profile         testProfile = testParser.getProfile();
        assertEquals( testProfile, workingProfile );
    }
    
    @Test
    public void testInvalidPropertyName()
    {
        // Add a property with an invalid name before the fist class
        // declaration, and another one after. Should yield two error
        // dialogs.
        List<String>    props   = getTestPropertyList();
        props.add( 3, "notAValidPropertyName" + " " + 10 );
        // After adding previous line, first class declaration is at
        // line 4. Add the next invalid line at line 5.
        props.add( 5, "alsoNotValid" + " " + 2  );
        
        ProfileParser   testParser  = new ProfileParser();
        int testCount   = expectDialog( () ->
            testParser.loadProperties( props.stream() )
        );
        
        assertEquals( 2, testCount );
        Profile         testProfile = testParser.getProfile();
        assertEquals( testProfile, workingProfile );
    }
    
    @Test
    public void testInvalidPropertyValue()
    {
        // Before adding stroke property (line 6) add:
        // a) a property with an invalid float value;
        // b) a property with an invalid Color value; and
        // Note: for any value other than "true," parseBoolean returns
        // false, so we can't test for an invalid Boolean value.
        // Should yield two error dialogs.
        List<String>    props   = getTestPropertyList();
        props.add( 6, ProfileParser.LENGTH + " " + "xx" );
        props.add( 7, ProfileParser.COLOR + " " + "yy" );
        
        ProfileParser   testParser  = new ProfileParser();
        int testCount   = expectDialog( () ->
            testParser.loadProperties( props.stream() )
        );
        
        assertEquals( 2, testCount );
        Profile         testProfile = testParser.getProfile();
        assertEquals( testProfile, workingProfile );
    }
    
    @Test
    public void testNVPairBeforeClassDecl()
    {
        // Try to parse a line property and a graph property
        // before any class has been declared
        List<String>    props   = getTestPropertyList();
        props.add( 2, ProfileParser.FONT_SIZE + " " + 10 );
        props.add( 3, ProfileParser.STROKE + " " + 2  );
        
        ProfileParser   testParser  = new ProfileParser();
        int testCount   = expectDialog( () ->
            testParser.loadProperties( props.stream() )
        );
        
        assertEquals( 2, testCount );
        Profile         testProfile = testParser.getProfile();
        assertEquals( testProfile, workingProfile );
    }
    
    /**
     * This method produces list of strings
     * to use as the starting point
     * of an input stream
     * suitable for processing by ProfileParser. 
     * All values are taken from {@link #distinctProfile}.
     * The list returned
     * contains the following strings
     * in the given order.
     * <p>
     * List returned:
     * <ul>
     * <li>[0]  PROFILE distinctName</li>
     * <li>[1]  gridUnit distinctGridUnit</li>
     * <li>[2]  class GraphPropertySetMW</li>
     * <li>[3]  fontSize distinctFontSize</li>
     * <li>[4]  class LinePropertySetAxes</li>
     * <li>[6]  stroke distinctStroke</li>
     * </ul>
     * 
     * @return 
     *      a list of strings from which to generate
     *      a Profile data input stream
     */
    private List<String> getTestPropertyList()
    {
        String  name        = distinctProfile.getName();
        float   gridUnit    = distinctProfile.getGridUnit();
        String  lineName    = LinePropertySetAxes.class.getSimpleName();
        String  graphName   = GraphPropertySetMW.class.getSimpleName();
        float   fontSize    = 
            distinctProfile.getMainWindow().getFontSize();
        float   stroke      = 
            distinctProfile.getLinePropertySet( lineName ).getStroke();
        
        workingProfile.setName( name );
        workingProfile.setGridUnit( gridUnit );
        workingProfile.getMainWindow().setFontSize( fontSize );
        workingProfile.getLinePropertySet( lineName ).setStroke( stroke );
        
        List<String>    props   = new ArrayList<>();
        props.add( ProfileParser.PROFILE + " " + name );
        props.add( ProfileParser.GRID_UNIT + " " + gridUnit );
        props.add( ProfileParser.CLASS + " " + graphName );
        props.add( ProfileParser.FONT_SIZE + " " + fontSize  );
        props.add( ProfileParser.CLASS + " " + lineName ) ;
        props.add( ProfileParser.STROKE + " " + stroke );

        return props;
    }
   
    /**
     * Given a properly formatted name/value pair,
     * formulate a new string containing the original name/value pair
     * but with extra whitespace inserted 
     * before and after each token.
     * 
     * @param nvPair    the given name/value pair
     * 
     * @return  
     *      a string containing the name/value pair
     *      with excess whitespace inserted
     */
    private String excessWhitespace( String nvPair )
    {
        final String whitespaceStr  = "   \t   ";
        String[]    tokens  = nvPair.split( "\\s+" );
        String      name    = tokens[0];
        String      value   = tokens[1];
        StringBuilder   bldr    = new StringBuilder()
            .append( whitespaceStr )
            .append( name )
            .append( whitespaceStr )
            .append( value )
            .append( whitespaceStr );
        return bldr.toString();
    }
    
    /**
     * This method adds to a list 
     * the directive and property name/value pairs
     * necessary to record a font size.
     * The font size is guaranteed to be different
     * from the font size declared
     * in the working profile.
     * 
     * @param list  
     *      the list to which to add
     *      the directive and property name/value pairs
     */
    private void addFontSize( List<String> list )
    {
        String              propSetName     = 
            GraphPropertySetMW.class.getSimpleName();
        GraphPropertySet    srcPropSet      = 
            distinctProfile.getMainWindow();
        GraphPropertySet    dstPropSet      = 
            workingProfile.getMainWindow();
        float               distinctVal     = srcPropSet.getFontSize();
        dstPropSet.setFontSize( distinctVal );
        String              classDecl       =
            ProfileParser.CLASS + " " + propSetName;
        String              propDecl        =
            ProfileParser.FONT_SIZE + " " + distinctVal;
        list.add( classDecl );
        list.add( propDecl );
    }
    
    /**
     * Execute a given operation,
     * wait for an error errorDialog to post,
     * then dismiss the error errorDialog
     * by selecting its OK button.
     * The given operation
     * is executed via a dedicated thread
     * and this method does not return
     * until the thread
     * has reached the terminated state.
     * The given operation may post many error dialogs;
     * the number of dialogs posted is returned.
     * 
     * 
     * @param runner    the given operation
     * 
     * @return  
     *      the number of dialogs posted 
     *      while executing the given operation
     */
    private int expectDialog( Runnable runner )
    {
        Thread  thread          = new Thread( runner );
        int     dialogCounter   = 0;
        thread.start();
        while ( thread.isAlive() )
        {
            Utils.pause( 250 );
            getDialogAndOKButton();
            if ( errorDialogOKButton != null )
            {
                ++dialogCounter;
                okAndWait();
            }
        }
        return dialogCounter;
    }
    
    /**
     * Pushes the OK button
     * indicated by {@link #errorDialogOKButton}
     * and wait for its constituent dialog to terminate.
     * The {@link #errorDialogOKButton} is set to null
     * after being pushed.
     * <p>
     * Precondition:<br>
     * The {@link #errorDialogOKButton} is non-null.
     * Postcondition:<br>
     * The {@link #errorDialogOKButton} is null.
     * Postcondition:<br>
     * The error dialog is no longer visible.
     */
    private void okAndWait()
    {
        errorDialogOKButton.doClick();
        errorDialogOKButton = null;
        while ( errorDialog.isVisible() )
            Utils.pause( 125 );
    }
    
    /**
     * Searches for the first visible JDialog,
     * locates its constituent OK button,
     * and sets {@link #errorDialogOKButton}
     * to the ID of the button.
     * If no dialog is visible
     * {@link #errorDialogOKButton} is set to null.
     */
    private void getDialogAndOKButton()
    {
        final boolean canBeFrame    = false;
        final boolean canBeDialog   = true;
        final boolean mustBeVis     = true;
        GUIUtils.schedEDTAndWait( () ->  {
            errorDialogOKButton = null;
            errorDialog = null;
            ComponentFinder finder  = 
                new ComponentFinder( canBeDialog, canBeFrame, mustBeVis );
            Window          window  = finder.findWindow( c -> true );
            if ( window != null )
            {
                assertTrue( window instanceof JDialog );
                errorDialog = (JDialog)window;
                Predicate<JComponent>   pred    = 
                    ComponentFinder.getButtonPredicate( "OK" );
                JComponent              comp    = 
                    ComponentFinder.find( window, pred );
                assertNotNull( comp );
                assertTrue( comp instanceof AbstractButton );
                errorDialogOKButton = (AbstractButton)comp;
            }
        });
    }
    
    /**
     * Encapsulates a getter and setter
     * for one property of type float
     * in a concrete LinePropertySet.
     * The principal purpose of the class
     * is to support the transfer of a unique property value
     * from the Profile containing unique property values
     * (#distinctProfile)
     * to the working Profile (#workingProfile).
     * 
     * @author Jack Straub
     */
    private class Mutator
    {
        /** 
         * The simple name of the LinePropertySet class 
         * containing the encapsulated property.
         */
        public final String                             propSetName;
        /** The name of the encapsulated property, e.g. "stroke." */
        public final String                             propName;
        /** The getter for the encapsulated property. */
        public final Function<LinePropertySet,Float>    getter;
        /** The setter for the encapsulated property. */
        public final BiConsumer<LinePropertySet,Float>  setter;
        
        
        /**
         * Constructor.
         * Full initializes an instance of this class.
         * `
         * @param propSetName
         *      the simple class name of the property set
         *      that contains the encapsulated Profile property
         * @param propName
         *      the name of the encapsulated property
         * @param getter
         *      the getter for the encapsulated property
         * @param setter
         *      the setter for the encapsulated property
         */
        public Mutator(
            String propSetName, 
            String propName, 
            Function<LinePropertySet, Float> getter,
            BiConsumer<LinePropertySet, Float> setter
        )
        {
            super();
            this.propSetName = propSetName;
            this.propName = propName;
            this.getter = getter;
            this.setter = setter;
        }

        /**
         * Adds to a given list of strings
         * a class declaration for the encapsulated property
         * and a name/value pair
         * incorporating the distinct value
         * of the encapsulated property.
         * For example:
         * <p>
         * class: LinePropertySetAxes<br>
         * stroke: 3
         * <p>
         * The new property value
         * is also recorded in the #workingProfile,
         * presumably for later use
         * in test validation.
         * 
         * @param list  the given list of strings
         */
        public void add( List<String> list )
        {
            LinePropertySet dstPropSet  = 
                workingProfile.getLinePropertySet( propSetName );
            LinePropertySet srcPropSet  = 
                distinctProfile.getLinePropertySet( propSetName );
            float           distinctVal = 
                getter.apply( srcPropSet );
            setter.accept( dstPropSet, distinctVal );
            
            String  classDecl   = ProfileParser.CLASS + " " + propSetName;
            String  valueDecl   = propName + " " + distinctVal;
            list.add( classDecl );
            list.add( valueDecl );
        }
    }
}

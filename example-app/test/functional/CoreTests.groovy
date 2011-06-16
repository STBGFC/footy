import functionaltestplugin.FunctionalTestCase

class CoreTests extends FunctionalTestCase {

    void testIndex() {
        get ('/')
        assertStatus 200
        assertContentContains "Welcome"
    }

}
        

package org.davisononline.footy.core.utils

import grails.test.*

class TemplateUtilsTests  extends GrailsUnitTestCase {

    void testEval() {
        def txt = 'this is a ${type} test'
        def out = TemplateUtils.eval (txt, [type:'good'])
        assertEquals 'this is a good test', out
    }
}

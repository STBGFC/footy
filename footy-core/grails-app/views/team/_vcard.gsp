<%--
    According to the current (3.0) specification, vCards must contain the VERSION, N, and FN properties between
    the BEGIN:VCARD and END:VCARD entities.
--%>BEGIN:VCARD
VERSION:3.0
N:${it.familyName};${it.givenName}
FN:${it.fullName};;;
<g:if test="${it.phone1}">TEL;TYPE=CELL:${it.phone1}</g:if>
<g:if test="${it.phone2}">TEL;TYPE=HOME,VOICE:${it.phone2}</g:if>
EMAIL;TYPE=HOME:${it.email}
END:VCARD

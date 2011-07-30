
<g:form action="paymentMade">
    <p>
        <g:message code="org.davisononline.footy.core.invoice.notes" default="Notes on invoice (optional)"/>
    </p>
    <g:textArea name="notes" rows="4" cols="40" style="width:auto"></g:textArea>
    <p>
        <g:message
            code="org.davisononline.footy.core.invoice.paymentamount.text"
            default="Confirm or amend the amount that has been paid" />
    </p>
    <input type="text" name="amount" value="${totalAmount}" style="width:auto"/> <input type="submit" value="submit"/>
    <g:hiddenField name="id" value="${id}" />
</g:form>
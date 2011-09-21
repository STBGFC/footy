
<g:form action="paymentMade">
    <p>
        <g:message code="org.davisononline.footy.core.invoice.notes" default="Notes on invoice (optional)"/>
    </p>
    <g:textArea name="notes" rows="4" cols="40" style="width:auto"></g:textArea>
    <p>
        <g:message
            code="org.davisononline.footy.core.invoice.paymentamount.text"
            default="Confirm or amend the total amount of the invoice (NOT the amount that is being added or refunded, this will be calculated)" />
    </p>
    <input type="text" name="amount" value="${totalAmount}" style="width:auto"/> <input type="submit" value="submit"/>
    <g:hiddenField name="id" value="${id}" />
    <g:hiddenField name="refund" value="${params.refund}" />
</g:form>
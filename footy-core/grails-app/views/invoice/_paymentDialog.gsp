<p>
    <g:message
        code="org.davisononline.footy.core.invoice.paymentamount.text"
        default="Confirm or amend the amount that has been paid" />
</p>
<g:form action="paymentMade">
<input type="text" name="amount" value="${totalAmount}" style="width:auto"/> <input type="submit" value="submit"/>
<g:hiddenField name="id" value="${id}" />
</g:form>
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
        <meta name="layout" content="main" />
		<title>Transaction Complete</title>
	</head>
	<body>
        <div class="body">
            <h1>Thank you!</h1>
            <div class="dialog">
	    	<p>
                Your purchase is complete and entry to the tournament is confirmed
                for the teams listed below.
            </p>
            <p>
                A confirmation email has been sent to the individual contact email
                address for each team.  You may also wish to print a copy of this confirmation 
                for your records.
            </p>
            <p>We look forward to welcoming you on the day.</p>

            <table>
                <tbody>
                <tr class="prop">
                    <td  class="name">Tournament</td>
                    <td  class="value">${payment.paymentItems[0].itemName.encodeAsHTML()}</td>
                </tr>
                <tr class="prop">
                    <td  class="name">Transaction ID</td>
                    <td  class="value">${payment.transactionId}</td>
                </tr>
                <tr class="prop">
                    <td  class="name">Amount Paid</td>
                    <td  class="value">${payment.currency} ${payment.paymentItems[0].amount}</td>
                </tr>
                </tbody>
            </table>

            <g:render template="entryConfirmed" collection="${request.entries}"/>
    		</div>
        </div>
	</body>
</html>

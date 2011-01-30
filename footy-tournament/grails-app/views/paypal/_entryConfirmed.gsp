            <table>
                <tbody>
                <tr class="prop">
                    <td  class="name">Club</td>
                    <td  class="value">${it.club.encodeAsHTML()}</td>
                </tr>
                <tr class="prop">
                    <td  class="name">Team</td>
                    <td  class="value"><strong>${it.ageGroup.encodeAsHTML()} ${it.teamName.encodeAsHTML()}</strong></td>
                </tr>
                <tr class="prop">
                    <td  class="name">Colours</td>
                    <td  class="value">${it.club.colours.encodeAsHTML()}</strong></td>
                </tr>
                <tr class="prop">
                    <td  class="name">Team Contact</td>
                    <td  class="value">
                        ${it.contactName.encodeAsHTML()} (<a href="mailto:${it.email.encodeAsHTML()}">${it.email.encodeAsHTML()}</a>)<br/>
                        ${it.contactAddress.encodeAsHTML()}
                    </td>
                </tr>
                <tr class="prop">
                    <td  class="name">Club Contact</td>
                    <td  class="value">
                        ${it.club.clubSecretaryName}<br/>
                        ${it.club.clubSecretaryAddress}
                    </td>
                </tr>
                </tbody>
            </table>

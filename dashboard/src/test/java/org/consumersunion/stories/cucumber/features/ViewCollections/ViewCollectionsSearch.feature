Feature: View Collections Search

  Scenario: Verify Search Functionality
    Given a pre-existing organization 'viewCollectionsOrgA'
    And a pre-existing 'viewCollectionsOrgA' user account 'viewCollectionsA' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewCollectionsA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    And I click the ViewCollections Tab
    And I type 'ATMCollection' into field by Locator 'By.cssSelector(".GNRSWN1CAO.GNRSWN1CI3")'
    And I press the enter key on 'By.cssSelector(".GNRSWN1CAO.GNRSWN1CI3")'


  Scenario: Cancel the Search on View Collections
    Given a pre-existing organization 'viewCollectionsOrgA'
    And a pre-existing 'viewCollectionsOrgA' user account 'viewCollectionsA' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewCollectionsA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    And I click the ViewCollections Tab
    And I type 'ATMCollection' into field by Locator 'By.cssSelector(".GNRSWN1CAO.GNRSWN1CI3")'
    And I press the enter key on 'By.cssSelector(".GNRSWN1CAO.GNRSWN1CI3")'
    Then I click the X icon on the View Collection Search  

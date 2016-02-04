Feature: View Collections

  @wip
  @broken
  Scenario: Verify the Search Functionality
    Given a pre-existing organization 'viewCollectionsSearchOrgA'
    And a pre-existing 'viewCollectionsSearchOrgA' user account 'viewCollectionsSearchA' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewCollectionsSearchA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    And I click the ViewCollections Tab
    And I type 'ATM_Collection' into field by locator 'By.cssSelector(".GNRSWN1CAO.GNRSWN1CI3")'
    And I press the enter key on 'By.cssSelector(".GNRSWN1CAO.GNRSWN1CI3")'

  @wip
  @broken
  Scenario: Verify if the user is able to cancel the search
    Given a pre-existing organization 'viewCollectionsSearchOrgA'
    And a pre-existing 'viewCollectionsSearchOrgA' user account 'viewCollectionsSearchA' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewCollectionsSearchA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    And I click the ViewCollections Tab
    And I type 'ATM_Collection' into field by locator 'By.cssSelector(".GNRSWN1CAO.GNRSWN1CI3")'
    And I press the enter key on 'By.cssSelector(".GNRSWN1CAO.GNRSWN1CI3")'
    Then I click the X icon on the View Collection Search

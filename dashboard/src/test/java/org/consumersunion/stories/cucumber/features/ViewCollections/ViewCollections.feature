Feature: View Collections

  Scenario: Verify New Collection Modal Window
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
    And I click the New Collection Button
    Then the Modal Window New Collection is displayed

  Scenario: Verify New Questionnaire Modal Window
    Given a pre-existing organization 'viewCollectionsOrgB'
    And a pre-existing 'viewCollectionsOrgB' user account 'viewCollectionsB' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewCollectionsB' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    And I click the ViewCollections Tab
    And I click the New Questionnaire Button
    Then the Modal Window New Questionnaire is displayed

  @wpi
  Scenario: Verify Collection Sort Options
    Given a pre-existing organization 'viewCollectionsOrgB'
    And a pre-existing 'viewCollectionsOrgB' user account 'viewCollectionsB' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewCollectionsB' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    And I click the ViewCollections Tab
    When I click the Sort Dropdown on View Collection Search
    Then The options in the dropdown are being displayed

Feature: View Stories Add Notes

  @wip
  Scenario: Add New Notes
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddNotesOrgA'
    And a pre-existing 'viewStoriesAddNotesOrgA' user account 'viewStoriesAddNotesAuthA' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddNotesAuthA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed


  @wip
  Scenario: Add Notes to Selected Stories
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddNotesOrgB'
    And a pre-existing 'viewStoriesAddNotesOrgB' user account 'viewStoriesAddNotesAuthB' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddNotesAuthB' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed

  @wip
  Scenario: System Keeps previous selected items
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddNotesOrgC'
    And a pre-existing 'viewStoriesAddNotesOrgC' user account 'viewStoriesAddNotesAuthC' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddNotesAuthC' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed

  @wip
  Scenario: Verify the Count Number of Selected Stories Add Notes
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddNotesOrgD'
    And a pre-existing 'viewStoriesAddNotesOrgD' user account 'viewStoriesAddNotesAuthD' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddNotesAuthD' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed

  @wip
  Scenario: Add Notes using long text
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddNotesOrgE'
    And a pre-existing 'viewStoriesAddNotesOrgE' user account 'viewStoriesAddNotesAuthE' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddNotesAuthE' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed

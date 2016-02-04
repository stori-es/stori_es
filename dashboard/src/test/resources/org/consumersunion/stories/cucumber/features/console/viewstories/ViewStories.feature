Feature: View Stories

  @wip
  @broken
  Scenario: Verify View Stories Page Loads
    Given I am logged out
    And a pre-existing organization 'viewStoriesOrgA'
    And a pre-existing 'viewStoriesOrgA' user account 'viewStoriesAuthA' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAuthA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    And I click the ViewStories Tab
    Then I see the ViewStories page

  @wip
  @broken
  Scenario: Verify Options in the Sort dropdown View Stories Page
    Given I am logged out
    And a pre-existing organization 'viewStoriesOrgB'
    And a pre-existing 'viewStoriesOrgB' user account 'viewStoriesAuthB' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAuthB' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    And I click the ViewStories Tab
    Then I see the ViewStories page
    When I click the sort Dropdown
    Then the Sort dropdown options are displayed

  @wip
  Scenario: Verify displayed Bulk Options
    Given I am logged out
    And a pre-existing organization 'viewStoriesOrgC'
    And a pre-existing 'viewStoriesOrgC' user account 'viewStoriesAuthC' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAuthC' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed

  @wip
  Scenario: Select the Add To Collection Option
    Given I am logged out
    And a pre-existing organization 'viewStoriesOrgD'
    And a pre-existing 'viewStoriesOrgD' user account 'viewStoriesAuthD' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAuthD' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed
    And I click the Add To Collection Option
    Then the Add Collection widget is displayed

  @wip
  Scenario: Select the Add Tags Option
    Given I am logged out
    And a pre-existing organization 'viewStoriesOrgE'
    And a pre-existing 'viewStoriesOrgE' user account 'viewStoriesAuthE' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAuthE' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed
    And I click the Add Tags Option
    Then the Add Tags widget is displayed

  @wip
  Scenario: Select the Add Note Option
    Given I am logged out
    And a pre-existing organization 'viewStoriesOrgF'
    And a pre-existing 'viewStoriesOrgF' user account 'viewStoriesAuthF' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAuthF' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed
    And I click the Add Notes Option
    Then the Add Notes widget is displayed

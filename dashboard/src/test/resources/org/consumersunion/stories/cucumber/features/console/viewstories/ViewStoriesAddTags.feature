Feature: View Stories Add Tags

  @wip
  Scenario: Autocomplete List
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddTagsOrgA'
    And a pre-existing 'viewStoriesAddTagsOrgA' user account 'viewStoriesAddTagsAuthA' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddTagsAuthA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed

  @wip
  Scenario: Add New Tags
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddTagsOrgB'
    And a pre-existing 'viewStoriesAddTagsOrgB' user account 'viewStoriesAddTagsAuthB' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddTagsAuthB' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed

  @wip
  Scenario: Add Tags to Selected Stories
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddTagsOrgC'
    And a pre-existing 'viewStoriesAddTagsOrgC' user account 'viewStoriesAddTagsAuthC' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddTagsAuthC' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed

  @wip
  Scenario: System Keeps previous selected items Add Tags
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddTagsOrgD'
    And a pre-existing 'viewStoriesAddTagsOrgD' user account 'viewStoriesAddTagsAuthD' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddTagsAuthD' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed


  @wip
  Scenario: Verify the Count Number of Selected Stories Add Tags
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddTagsOrgE'
    And a pre-existing 'viewStoriesAddTagsOrgE' user account 'viewStoriesAddTagsAuthE' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddTagsAuthE' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed

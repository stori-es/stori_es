Feature: View Stories Add to Collection

  @wpi
  Scenario: Verify Questionnaires are not in the list
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddToCollectionOrgA'
    And a pre-existing 'viewStoriesAddToCollectionOrgA' user account 'viewStoriesAddToCollectionAuthA' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddToCollectionAuthA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed
    And I click the Add To Collection Option
    Then the Add Collection widget is displayed

  @wpi
  Scenario: Selecting one Collection
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddToCollectionOrgB'
    And a pre-existing 'viewStoriesAddToCollectionOrgB' user account 'viewStoriesAddToCollectionAuthB' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddToCollectionAuthB' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed
    And I click the Add To Collection Option
    Then the Add Collection widget is displayed
    And I click the Collections link
    Then the Add Collection Modal is displayed
    Then I Select a Collection from the list with Index 1

  @wpi
  Scenario: Selecting several Collections
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddToCollectionOrgC'
    And a pre-existing 'viewStoriesAddToCollectionOrgC' user account 'viewStoriesAddToCollectionAuthC' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddToCollectionAuthC' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed
    And I click the Add To Collection Option
    Then the Add Collection widget is displayed
    And I click the Collections link
    Then the Add Collection Modal is displayed
    Then I Select a Collection from the list with Index 2
    Then I Select a Collection from the list with Index 3
    Then I Select a Collection from the list with Index 4


  Scenario: Cancel Add To Collection Action
    Given I am logged out
    And a pre-existing organization 'viewStoriesAddToCollectionOrgD'
    And a pre-existing 'viewStoriesAddToCollectionOrgD' user account 'viewStoriesAddToCollectionAuthD' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'viewStoriesAddToCollectionAuthD' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    When I click the Plus Icon Bulk Options View Stories
    Then the dropdown options are displayed
    And I click the Add To Collection Option
    Then the Add Collection widget is displayed
    And I click the Collections link
    Then the Add Collection Modal is displayed
    And I Click the Add to Collection Cancel Link
    Then the Add Collection Text is displayed Without Selection

Feature: Story Details

  @wip
  @broken
  Scenario: Validate_Task325
    Given I am logged out
    And a pre-existing organization 'StoryDetailsOrgA'
    And a pre-existing 'StoryDetailsOrgA' user account 'storyDetailsAuthA' with password 'password'
    And the l10n selection is 'en'
    And a pre-existing story 'StoryA'
    When I load the sign in page
    And I see the sign in page
    And I type 'storyDetailsAuthA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    When I click the ViewStories Tab
    Then I see the ViewStories page
    And I Select a Story from the list with Index 1 on the View Stories Page
    When I click the Add To Collection Plus Icon in Story Details Page
    Then the Verify Add to Collection Modal is displayed in Story Details Page
    When I click the New Collection Button in Story Details Page Add to Collection Modal
    When I click the Done Button in Story Details Page Add to Collection Modal
    Then the Required Collection Name Message is displayed on the Story Details Page
    When I click the Cancel Button in Story Details Page Add to Collection Modal
    When I click the New Collection Button in Story Details Page Add to Collection Modal
    Then the Add to Collection Modal is displayed in Story Details Page

  @wip
  @broken
  Scenario: Validate_Task581
    Given I am logged out
    And a pre-existing organization 'StoryDetailsOrgB'
    And a pre-existing 'StoryDetailsOrgB' user account 'storyDetailsAuthB' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'storyDetailsAuthB' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    When I click the ViewStories Tab
    Then I see the ViewStories page
    And I Select a Story from the list with Index 1 on the View Stories Page
    When I click the Add To Collection Plus Icon in Story Details Page
    Then the Verify Add to Collection Modal is displayed in Story Details Page

  @wip
  @broken
  Scenario: Validate_Task427
    Given I am logged out
    And a pre-existing organization 'StoryDetailsOrgC'
    And a pre-existing 'StoryDetailsOrgC' user account 'storyDetailsAuthC' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'storyDetailsAuthC' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    When I click the ViewStories Tab
    Then I see the ViewStories page
    And I Select a Story from the list with Index 1 on the View Stories Page
    Then I click the Original Tab in the Story Details Page


  @wip
  @broken
  Scenario: Validate_Task451
    Given I am logged out
    And a pre-existing organization 'StoryDetailsOrgD'
    And a pre-existing 'StoryDetailsOrgD' user account 'storyDetailsAuthD' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'storyDetailsAuthD' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    When I click the ViewStories Tab
    Then I see the ViewStories page
    And I Select a Story from the list with Index 1 on the View Stories Page

  @wip
  @broken
  Scenario: Validate_Task425
    Given I am logged out
    And a pre-existing organization 'StoryDetailsOrgE'
    And a pre-existing 'StoryDetailsOrgE' user account 'storyDetailsAuthE' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'storyDetailsAuthE' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.

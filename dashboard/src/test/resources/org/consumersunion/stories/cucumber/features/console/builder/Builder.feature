Feature: Builder

  @wip
  @broken
  Scenario: Verify Edit Permalink TASK-575
    Given I am logged out
    And a pre-existing organization 'builderOrgA'
    And a pre-existing 'viewStoriesOrgA' user account 'builderAuthA' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'builderAuthA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    And I click the ViewCollections Tab
    When I click the New Questionnaire Button
    Then the Modal Window New Questionnaire is displayed
    When I insert a new Questionnaire Name in the Modal Window 'QuestionnaireTASK575'
    And I click the Done button in the New Questionnaire Modal
    Then the Questionnaire Page is displayed
    When I execute a View Collection Search using the following text 'QuestionnaireTASK575'
    And I Select a Collection from the list with Index 1 on the View Collections Page
    When I click the Edit Questionnaire Button
    And I click the Publication Tab
    And I click the Edit Permalink Icon
    And I insert a new Permalink 'edited-questionnaire-task-575' value
    And I click the Confirm Edit Permalink Icon
    And I click the Preview Button on the Publication Tab
    Then the Questionnaire Preview is displayed

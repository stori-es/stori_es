Feature: Questionnaires

  @wip
  @broken
  Scenario: Verify New Questionnaires on the List TASK-585
    Given I am logged out
    And a pre-existing organization 'newQuestionnairesOrgA'
    And a pre-existing 'newQuestionnairesOrgA' user account 'newQuestionnaireAuthA' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'newQuestionnaireAuthA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    And I click the ViewCollections Tab
    When I click the New Questionnaire Button
    Then the Modal Window New Questionnaire is displayed
    When I insert a new Questionnaire Name in the Modal Window 'QuestionnaireTASK585'
    And I click the Done button in the New Questionnaire Modal
    Then the Questionnaire Page is displayed
    When I click the ViewCollections Tab
    When I execute a View Collection Search using the following text 'QuestionnaireTASK585'
    And I Select a Collection from the list with Index 1 on the View Collections Page

  @wip
  @broken
  Scenario: Create New Questionnaires TASK-510
    Given I am logged out
    And a pre-existing organization 'newQuestionnairesOrgB'
    And a pre-existing 'newQuestionnairesOrgB' user account 'newQuestionnaireAuthB' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'newQuestionnaireAuthB' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    And I click the ViewCollections Tab
    When I click the New Questionnaire Button
    Then the Modal Window New Questionnaire is displayed
    When I insert a new Questionnaire Name in the Modal Window 'QuestionnaireTASK510'
    And I click the Done button in the New Questionnaire Modal
    Then the Questionnaire Page is displayed

  @wip
  @broken
  Scenario: Verify Map Reset Icon when search is cancelled TASK-461
    Given I am logged out
    And a pre-existing organization 'newQuestionnairesOrgC'
    And a pre-existing 'newQuestionnairesOrgA' user account 'newQuestionnaireAuthC' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'newQuestionnaireAuthC' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.
    When I execute a View Collection Search using the following text 'QuestionnaireTASK461'
    And I Select a Collection from the list with Index 1 on the View Collections Page
    When I click the Map Icon in the Questionnaire Details Page
    When I cancel the Story Search in the Questionnaire Page
    Then the Map icon is still displayed on the Page

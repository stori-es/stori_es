Feature: Console Authentication

  @golden
  Scenario: Successful Login
    Given I am logged out
    And a pre-existing organization 'consoleAuthOrgA'
    And a pre-existing 'consoleAuthOrgA' user account 'consoleAuthA' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'consoleAuthA' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then the console dashboard loads.

  @user-error
  Scenario: Bad Password
    Given a pre-existing organization 'consoleAuthOrgB'
    And a pre-existing 'consoleAuthOrgA' user account 'consoleAuthB' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'consoleAuthB' into field 'j_username'
    And I type 'wrong' into field 'j_password'
    And I hit the submit button
    Then I see the sign in page
    And I see the error message 'Invalid Username or Password. Please try again.'.

  @user-error
  Scenario: Bad User ID
    Given a pre-existing organization 'consoleAuthOrgC'
    And a pre-existing 'consoleAuthOrgC' user account 'consoleAuthC' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'wrong' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    Then I see the sign in page
    And I see the error message 'Invalid Username or Password. Please try again.'.

  @golden
  Scenario: Successful Sign Out
    Given I am logged out
    And a pre-existing organization 'consoleAuthOrgD'
    And a pre-existing 'consoleAuthOrgD' user account 'consoleAuthD' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type 'consoleAuthD' into field 'j_username'
    And I type 'password' into field 'j_password'
    And I hit the submit button
    And the console dashboard loads.
    And I click the 'Sign out' link
    Then I see the sign in page

  @user-error
  Scenario: Empty Fields
    Given a pre-existing organization 'consoleAuthOrgE'
    And a pre-existing 'consoleAuthOrgE' user account 'consoleAuthE' with password 'password'
    And the l10n selection is 'en'
    When I load the sign in page
    And I see the sign in page
    And I type '' into field 'j_username'
    And I type '' into field 'j_password'
    And I hit the submit button
    Then I see the sign in page
    And I see the error message 'Invalid Username or Password. Please try again.'.

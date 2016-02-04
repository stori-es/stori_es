@heartbeat
Feature: Heartbeat

  Scenario: Load Console Sign-In Page
    Given no preconditions
    When I load the sign in page
    Then I see the sign in page.
Feature: Sync From Survey

  Scenario:New Person Record
    Given I access the form at /share/crh-share-your-health-experience
    When I see the survey form
    And I fill out the form with a new email address
    And I tell a story
    And I fill in an address
    And I fill in a phone number
    And I submit the form
    And I see the submission finishes processing
    Then I see the Convio constituent
    And I see the address matches
    And I see the phone number unchanged.

  @wip
  Scenario:Existing Person Record with All New Data
    Given I access the form at /share/crh-share-your-health-experience
    When I see the survey form
    And I fill out the form with the previous email address
    And I tell a story
    And I fill in an address
    And I fill in a phone number
    And I submit the form
    And I see the submission finishes processing
    Then I see the Convio constituent
    And I see the address matches
    And I see the phone number unchanged.

  @wip
  Scenario:Existing Person Record with New Address
    Given I access the form at /share/crh-share-your-health-experience
    When I see the survey form
    And I fill out the form with the previous email address
    And I tell a story
    And I fill in an address
    And I submit the form
    And I see the submission finishes processing
    Then I see the Convio constituent
    And I see the address matches
    And I see the phone number unchanged.

  @wip
  Scenario:Existing Person Record with New Phone Number
    Given I access the form at /share/some-survey-that-does-not-require-an-address
    When I see the survey form
    And I fill out the form with the previous email address
    And I tell a story
    And I fill in a phone number
    And I submit the form
    And I see the submission finishes processing
    Then I see the Convio constituent
    And I see the address unchanged
    And I see the phone number matches.

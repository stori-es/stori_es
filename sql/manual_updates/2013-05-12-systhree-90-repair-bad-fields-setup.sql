
-- Start database cleanup before Convio Sync fields setup

DROP PROCEDURE IF EXISTS convioOrganizationFieldSetup;

DROP PROCEDURE IF EXISTS convioEndpointFieldSetup;

DELETE from dbUpdate where scriptName = '2013-05-06-systhree-90-add-convio-org-fields.sql';

DELETE from dbUpdate where scriptName = '2013-05-08-systhree-90-add-convio-endpoint-field.sql';

ALTER TABLE organization
  DROP COLUMN crm_endpoint;

ALTER TABLE organization
  DROP COLUMN crm_api_login;

ALTER TABLE organization
  DROP COLUMN crm_api_key;

-- End database cleanup before Convio Sync fields setup


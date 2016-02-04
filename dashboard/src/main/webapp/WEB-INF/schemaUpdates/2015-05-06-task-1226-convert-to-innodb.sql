ALTER TABLE roles ENGINE = InnoDB;
ALTER TABLE roles
ADD CONSTRAINT fk_roles_grantor_entity FOREIGN KEY (grantor) REFERENCES entity (id),
ADD CONSTRAINT fk_roles_grantee_entity FOREIGN KEY (grantee) REFERENCES entity (id);

ALTER TABLE white_list ENGINE = InnoDB;
ALTER TABLE white_list
ADD CONSTRAINT fk_white_list_organization FOREIGN KEY (organization) REFERENCES organization (id),
ADD CONSTRAINT fk_white_list_member_entity FOREIGN KEY (member) REFERENCES entity (id);



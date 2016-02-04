DELETE acl.*
FROM acl_entry acl
  JOIN profile p ON acl.sid = p.id
  JOIN organization o ON o.id = acl_object_identity
WHERE acl_object_identity != p.organization;

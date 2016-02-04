UPDATE acl_entry
SET mask = granting, granting = 1
WHERE granting != 0 AND granting != 1;

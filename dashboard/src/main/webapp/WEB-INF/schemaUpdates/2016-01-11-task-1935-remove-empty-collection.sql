SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

DELETE dse, d, cc, se, e FROM collection cc
  JOIN systemEntity se ON se.id = cc.id
  JOIN entity e ON e.id = cc.id
  JOIN document d ON d.systemEntity = cc.id
  JOIN systemEntity dse ON d.id = dse.id
WHERE cc.id=850072;

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

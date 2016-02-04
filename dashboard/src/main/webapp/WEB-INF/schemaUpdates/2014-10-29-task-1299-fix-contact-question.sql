UPDATE formElement
SET formType = 'CONTACT'
WHERE formType = 'TEXT' AND (standardMeaning LIKE 'EMAIL%' OR standardMeaning LIKE 'PHONE%');

UPDATE systemEntity seo JOIN (SELECT
                                d.id,
                                MAX(d.version) AS version
                              FROM systemEntity se JOIN document d ON se.id = d.id
                              GROUP BY d.id) AS tmp ON seo.id = tmp.id
SET seo.version = tmp.version;

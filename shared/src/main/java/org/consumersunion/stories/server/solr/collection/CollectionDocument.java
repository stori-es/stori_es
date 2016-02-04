package org.consumersunion.stories.server.solr.collection;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.consumersunion.stories.server.solr.Document;

public class CollectionDocument implements Document {
    private final Set<String> tags;

    private int id;
    private String title;
    private int ownerId;
    private boolean isPublic;
    private boolean deleted;
    private Date created;
    private Date lastModified;
    private Set<String> admins;
    private Set<Integer> readAuths;
    private Set<Integer> writeAuths;
    private Set<Integer> adminAuths;

    public CollectionDocument() {
        tags = new LinkedHashSet<String>();
        readAuths = new LinkedHashSet<Integer>();
        writeAuths = new LinkedHashSet<Integer>();
        adminAuths = new LinkedHashSet<Integer>();
        admins = new LinkedHashSet<String>();
    }

    public CollectionDocument(
            org.consumersunion.stories.common.shared.model.Collection collection,
            Set<String> tags,
            Set<Integer> readAuths,
            Set<Integer> writeAuths,
            Set<Integer> adminAuths,
            Set<String> admins) {
        this(collection.getId(), collection.getTitle(), collection.getCreated(), collection.getUpdated(),
                collection.isPublic(), collection.getDeleted(), collection.getOwner(), tags, readAuths, writeAuths,
                adminAuths, admins);
    }

    public CollectionDocument(
            int id,
            String title,
            Date created,
            Date lastModified,
            boolean isPublic,
            boolean deleted,
            int ownerId,
            Set<String> tags,
            Set<Integer> readAuths,
            Set<Integer> writeAuths,
            Set<Integer> adminAuths,
            Set<String> admins) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
        this.created = created;
        this.lastModified = lastModified == null ? created : lastModified;
        this.isPublic = isPublic;
        this.deleted = deleted;
        this.tags = tags;
        this.readAuths = readAuths;
        this.writeAuths = writeAuths;
        this.adminAuths = adminAuths;
        this.admins = admins;
    }

    public CollectionDocument(SolrDocument document) {
        this();

        this.id = Integer.parseInt((String) document.getFieldValue("id"));
        this.title = (String) document.getFieldValue("title");
        this.isPublic = (Boolean) document.getFieldValue("public");
        this.deleted = (Boolean) document.getFieldValue("deleted");
        this.ownerId = (Integer) document.getFieldValue("ownerId");
        this.created = (Date) document.getFieldValue("created");
        this.lastModified = (Date) document.getFieldValue("lastModified");

        Collection<Object> indexedTags = document.getFieldValues("tags");
        if (indexedTags != null) {
            for (Object item : indexedTags) {
                tags.add((String) item);
            }
        }

        Collection<Object> indexedReadAuths = document.getFieldValues("readAuths");
        if (indexedReadAuths != null) {
            for (Object item : indexedReadAuths) {
                readAuths.add((Integer) item);
            }
        }

        Collection<Object> indexedWriteAuths = document.getFieldValues("writeAuths");
        if (indexedWriteAuths != null) {
            for (Object item : indexedWriteAuths) {
                writeAuths.add((Integer) item);
            }
        }

        Collection<Object> indexedAdminAuths = document.getFieldValues("adminAuths");
        if (indexedAdminAuths != null) {
            for (Object item : indexedAdminAuths) {
                adminAuths.add((Integer) item);
            }
        }

        Collection<Object> indexedAdmins = document.getFieldValues("admins");
        if (indexedAdmins != null) {
            for (Object item : indexedAdmins) {
                admins.add((String) item);
            }
        }
    }

    @Override
    public SolrInputDocument toDocument() {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", id);
        document.addField("title", title);
        document.addField("public", isPublic);
        document.addField("deleted", deleted);
        document.addField("ownerId", ownerId);
        document.addField("created", created);
        document.addField("lastModified", lastModified);

        if (tags != null) {
            for (String tag : tags) {
                document.addField("tags", tag);
            }
        }

        if (readAuths != null) {
            for (Integer auths : readAuths) {
                document.addField("readAuths", auths);
            }
        }

        if (writeAuths != null) {
            for (Integer auths : writeAuths) {
                document.addField("writeAuths", auths);
            }
        }

        if (adminAuths != null) {
            for (Integer auths : adminAuths) {
                document.addField("adminAuths", auths);
            }
        }

        if (admins != null) {
            for (String admin : admins) {
                document.addField("admins", admin);
            }
        }

        return document;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<String> admins) {
        this.admins = admins;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Set<Integer> getReadAuths() {
        return readAuths;
    }

    public void setReadAuths(Set<Integer> readAuths) {
        this.readAuths = readAuths;
    }

    public Set<Integer> getWriteAuths() {
        return writeAuths;
    }

    public void setWriteAuths(Set<Integer> writeAuths) {
        this.writeAuths = writeAuths;
    }

    public Set<Integer> getAdminAuths() {
        return adminAuths;
    }

    public void setAdminAuths(Set<Integer> adminAuths) {
        this.adminAuths = adminAuths;
    }

    public int getId() {
        return id;
    }
}

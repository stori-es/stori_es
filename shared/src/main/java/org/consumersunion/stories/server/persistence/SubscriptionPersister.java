package org.consumersunion.stories.server.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.Subscription;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.notification_channel.SubscriptionSummary;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;

@Component
public class SubscriptionPersister {
    private static final int DUPLICATE_KEY = 23000;

    private final PersistenceService persistenceService;

    @Inject
    SubscriptionPersister(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Subscription create(Subscription subscription) {
        return persistenceService.process(new ProcessFunc<Subscription, Subscription>(subscription) {
            @Override
            public Subscription process() {
                try {
                    PreparedStatement insert = conn.prepareStatement(
                            "INSERT INTO subscription (profile, target, type, active) VALUES (?,?,?,?)");

                    insert.setInt(1, input.getProfile());
                    insert.setInt(2, input.getTarget());
                    insert.setString(3, input.getType().name());
                    insert.setBoolean(4, true);

                    insert.execute();

                    return input;
                } catch (SQLException e) {
                    if (e.getErrorCode() == 1062) { // Because there's already a subscription, which may be inactive.
                        try {
                            conn.rollback();
                            conn.setSavepoint();
                            return setActive(input);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                            throw new GeneralException(e1);
                        }
                    } else {
                        throw new GeneralException(e);
                    }
                }
            }
        });
    }

    public Subscription setActive(Subscription subscription) {
        return persistenceService.process(new ProcessFunc<Subscription, Subscription>(subscription) {
            @Override
            public Subscription process() {
                try {
                    PreparedStatement update = conn.prepareStatement(
                            "UPDATE subscription SET active=1 WHERE profile=? AND target=? AND type=?");

                    update.setInt(1, input.getProfile());
                    update.setInt(2, input.getTarget());
                    update.setString(3, input.getType().name());

                    update.execute();

                    input.setActive(true);

                    return input;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public List<Subscription> getSubscriptions(final int target, NotificationTrigger trigger) {
        return persistenceService.process(new ProcessFunc<NotificationTrigger, List<Subscription>>(trigger) {
            @Override
            public List<Subscription> process() {
                try {
                    PreparedStatement select = conn.prepareStatement(
                            "SELECT profile, active FROM subscription WHERE target=? AND type=?");

                    select.setInt(1, target);
                    select.setString(2, input.name());

                    ResultSet resultSet = select.executeQuery();

                    List<Subscription> subscriptions = Lists.newArrayList();
                    while (resultSet.next()) {
                        subscriptions.add(Subscription.builder()
                                .withProfile(resultSet.getInt(1))
                                .withTarget(target)
                                .withType(input)
                                .withActive(resultSet.getBoolean(2))
                                .build());
                    }

                    return subscriptions;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public List<SubscriptionSummary> getActiveSubscriptions(final SystemEntity target, NotificationTrigger trigger) {
        return persistenceService.process(new ProcessFunc<NotificationTrigger, List<SubscriptionSummary>>(trigger) {
            @Override
            public List<SubscriptionSummary> process() {
                try {
                    PreparedStatement select = conn.prepareStatement(
                            "SELECT p.id, p.organization, pse.version, p.surname, p.givenName, p.user, " +
                                    "o.name, IF(acl.acl_object_identity IS NOT NULL, TRUE, FALSE) AS isAdmin, c.email" +
                                    " " +
                                    "FROM subscription s " +
                                    "   JOIN profile p " +
                                    "       ON p.id = s.profile " +
                                    "   JOIN systemEntity pse " +
                                    "       ON pse.id = p.id " +
                                    "   JOIN organization o " +
                                    "       ON o.id = p.organization " +
                                    "   JOIN (SELECT entityId, value AS email FROM contact c WHERE medium='EMAIL' " +
                                    "               AND status LIKE '%VERIFIED' " +
                                    "         ORDER BY FIELD(status, 'VERIFIED', 'UNVERIFIED'), FIELD(type, 'Home', " +
                                    "'Mobile', 'Other')) c" +
                                    "       ON c.entityId = p.id" +
                                    "   LEFT OUTER JOIN acl_entry acl " +
                                    "       ON acl.sid = p.id " +
                                    "           AND acl.acl_object_identity = p.organization " +
                                    "           AND acl.mask >= " + ROLE_ADMIN + " " +
                                    "WHERE s.target = ? " +
                                    "   AND s.type = ? " +
                                    "   AND s.active = 1 " +
                                    "GROUP BY s.profile, s.target, s.type");

                    select.setInt(1, target.getId());
                    select.setInt(2, target.getId());
                    select.setString(2, input.name());

                    ResultSet resultSet = select.executeQuery();

                    List<SubscriptionSummary> subscriptions = Lists.newArrayList();

                    while (resultSet.next()) {
                        Profile profile = new Profile(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3));
                        profile.setSurname(resultSet.getString(4));
                        profile.setGivenName(resultSet.getString(5));
                        profile.setUserId(resultSet.getInt(6));

                        ProfileSummary profileSummary = new ProfileSummary(profile, resultSet.getString(7),
                                resultSet.getBoolean(8), resultSet.getString(9));

                        Subscription subscription = Subscription.builder()
                                .withProfile(resultSet.getInt(1))
                                .withTarget(target.getId())
                                .withType(input)
                                .withActive(true)
                                .build();

                        subscriptions.add(SubscriptionSummary.builder()
                                .subscription(subscription)
                                .systemEntity(target)
                                .profileSummary(profileSummary)
                                .build());
                    }

                    resultSet.close();
                    select.close();

                    return subscriptions;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public Map<Integer, Collection<Subscription>> getSubscriptions(List<Integer> targets, final int profileId) {
        return getSubscriptions(targets, profileId, null);
    }

    public Map<Integer, Collection<Subscription>> getActiveSubscriptions(List<Integer> targets, final int profileId) {
        return getSubscriptions(targets, profileId, true);
    }

    public void setInactive(Subscription subscription) {
        persistenceService.process(new ProcessFunc<Subscription, Void>(subscription) {
            @Override
            public Void process() {
                try {
                    PreparedStatement update = conn.prepareStatement(
                            "UPDATE subscription SET active=0 WHERE profile=? AND target=? AND type=?");

                    update.setInt(1, input.getProfile());
                    update.setInt(2, input.getTarget());
                    update.setString(3, input.getType().name());

                    update.execute();

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public void deleteInactives() {
        persistenceService.process(new ProcessFunc<Void, Void>(null) {
            @Override
            public Void process() {
                try {
                    PreparedStatement delete = conn.prepareStatement(
                            "DELETE FROM subscription WHERE active=0");

                    delete.execute();

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    @VisibleForTesting
    void deleteAll() {
        persistenceService.process(new ProcessFunc<Subscription, Void>(null) {
            @Override
            public Void process() {
                try {
                    PreparedStatement delete = conn.prepareStatement("DELETE FROM subscription");

                    delete.execute();

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    private Map<Integer, Collection<Subscription>> getSubscriptions(
            List<Integer> targets,
            final int profileId,
            final Boolean active) {
        return persistenceService.process(
                new ProcessFunc<List<Integer>, Map<Integer, Collection<Subscription>>>(targets) {
                    @Override
                    public Map<Integer, Collection<Subscription>> process() {
                        try {
                            if (!input.isEmpty()) {
                                String targetQueryIds = "(" + StringUtils.chop(
                                        Strings.repeat("?,", input.size())) + ")";
                                String activeQuery = active == null ? "" : " AND active=" + (active ? 1 : 0);

                                PreparedStatement select = conn.prepareStatement("SELECT target, type, active " +
                                        "FROM subscription " +
                                        "WHERE target IN " + targetQueryIds + " AND profile=?" + activeQuery);

                                for (int i = 0; i < input.size(); i++) {
                                    select.setInt(i + 1, input.get(i));
                                }

                                select.setInt(input.size() + 1, profileId);

                                ResultSet resultSet = select.executeQuery();

                                ArrayListMultimap<Integer, Subscription> multimap = ArrayListMultimap.create();
                                while (resultSet.next()) {
                                    int target = resultSet.getInt(1);

                                    Subscription subscription = Subscription.builder()
                                            .withProfile(profileId)
                                            .withTarget(target)
                                            .withType(NotificationTrigger.valueOf(resultSet.getString(2)))
                                            .withActive(resultSet.getBoolean(3))
                                            .build();

                                    multimap.put(target, subscription);
                                }

                                return multimap.asMap();
                            }
                        } catch (SQLException e) {
                            throw new GeneralException(e);
                        }

                        return Maps.newHashMap();
                    }
                });
    }
}

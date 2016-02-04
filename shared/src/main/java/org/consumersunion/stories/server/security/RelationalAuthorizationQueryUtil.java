package org.consumersunion.stories.server.security;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

import org.consumersunion.stories.common.shared.dto.AuthParam;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.MineCallbackProvider;

import com.google.common.base.Strings;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ANY;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_OWN;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_PRIVILEGED;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_PUBLIC;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ROOT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROOT_ID;

public class RelationalAuthorizationQueryUtil {
    private final MineCallbackProvider mineProvider;

    public RelationalAuthorizationQueryUtil(MineCallbackProvider mineProvider) {
        this.mineProvider = mineProvider;
    }

    public PreparedStatement prepareAuthorizationSelect(
            String outerSelectClause,
            String innerSelectClause,
            String fromClause,
            String whereClause,
            Object[] parameters,
            AuthParam<?> authInput,
            Connection conn,
            boolean includeSort,
            Integer permissionMask) throws SQLException {

        return prepareAuthorizationSelect(outerSelectClause, innerSelectClause, fromClause, whereClause, parameters,
                authInput, conn, includeSort, permissionMask, false);
    }

    public PreparedStatement prepareAuthorizationSelect(
            String innerSelectClause,
            String fromClause,
            String whereClause,
            Object[] parameters,
            AuthParam<?> authInput,
            Connection conn,
            boolean includeSort,
            Integer permissionMask) throws SQLException {

        return prepareAuthorizationSelect(createOuterSelect(innerSelectClause), innerSelectClause, fromClause,
                whereClause, parameters, authInput, conn, includeSort, permissionMask, false);
    }

    public PreparedStatement prepareAuthorizationSelect(
            String outerSelectClause,
            String innerSelectClause,
            String fromClause, String whereClause,
            Object[] parameters,
            AuthParam<?> authInput,
            Connection conn,
            boolean includeSort,
            Integer permissionMask,
            boolean distributeOrderAndLimit) {

        try {
            String limitClause = getLimitClause(authInput);

            String groupClause = "";
            if (whereClause != null && whereClause.contains("/")) {
                groupClause = whereClause.split("/")[1];
                whereClause = whereClause.split("/")[0];
            }

            String sql = wrapSQL(outerSelectClause, innerSelectClause, fromClause, whereClause, authInput, includeSort,
                    permissionMask, "", groupClause);

            PreparedStatement statement = conn.prepareStatement(sql + limitClause);
            if (parameters != null) {
                fillParameters(parameters, authInput, statement, distributeOrderAndLimit);
            }
            return statement;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralException(e);
        }
    }

    private String wrapSQL(
            String outerSelectClause,
            String innerSelectClause,
            String fromClause,
            String whereClause,
            AuthParam<?> authInput,
            boolean includeSort,
            Integer permissionMask,
            String limitClause,
            String groupClause) {
        int authRelation = authInput.getAuthRelation();
        StringBuilder sql = new StringBuilder();

        if (authInput.getEffectiveId() != null && authInput.getEffectiveId() == ROOT_ID) {
            sql.append(generateRelationSelect(authRelation, authInput, innerSelectClause, fromClause, whereClause,
                    permissionMask));
            sql.append(" "
                    + includeSortAndGroup(authInput, includeSort, groupClause) + limitClause).append(" ");
        } else if (authRelation == ACCESS_MODE_ANY) {
            sql.append(outerSelectClause)
                    .append(" FROM ((")
                    .append(generateRelationSelect(ACCESS_MODE_PUBLIC, authInput, innerSelectClause, fromClause,
                            whereClause, permissionMask))
                    .append(" "
                            + includeSortAndGroup(authInput, includeSort, groupClause) + limitClause).append(") ");
            if (mineProvider.hasMineClause()) {
                sql.append(" UNION (")
                        .append(generateRelationSelect(ACCESS_MODE_OWN, authInput, innerSelectClause, fromClause,
                                whereClause, permissionMask))
                        .append(" "
                                + includeSortAndGroup(authInput, includeSort, groupClause) + limitClause).append(") ");
            }
            if (authInput.getEffectiveId() != null) {
                sql.append(" UNION (")
                        .append(generateRelationSelect(ACCESS_MODE_PRIVILEGED, authInput, innerSelectClause,
                                fromClause, whereClause, permissionMask))
                        .append(" "
                                + includeSortAndGroup(authInput, includeSort, groupClause) + limitClause).append(")");
            }
            sql.append(") AS tmp ");
            if (includeSort) {
                sql.append(cleanSortClause(
                        mineProvider.getSortClause(authInput.getSortField(), authInput.isAscending()), true));
            }
        } else if (authRelation == ACCESS_MODE_EXPLICIT && mineProvider.hasMineClause()) {
            sql.append(outerSelectClause);
            sql.append(" FROM ((")
                    .append(generateRelationSelect(ACCESS_MODE_OWN, authInput, innerSelectClause, fromClause,
                            whereClause, permissionMask))
                    .append(" "
                            + includeSortAndGroup(authInput, includeSort, groupClause) + limitClause).append(") ");
            if (authInput.getEffectiveId() != null) {
                sql.append(" UNION (")
                        .append(generateRelationSelect(ACCESS_MODE_PRIVILEGED, authInput, innerSelectClause,
                                fromClause, whereClause, permissionMask))
                        .append(" "
                                + includeSortAndGroup(authInput, includeSort, groupClause) + limitClause).append(")");
            }
            sql.append(") AS tmp ");
            if (includeSort) {
                sql.append(cleanSortClause(
                        mineProvider.getSortClause(authInput.getSortField(), authInput.isAscending()), true));
            }
        } else {
            sql.append(generateRelationSelect(authRelation, authInput, innerSelectClause, fromClause, whereClause,
                    permissionMask));
            sql.append(" "
                    + includeSortAndGroup(authInput, includeSort, groupClause) + limitClause).append(" ");
        }

        return sql.toString();
    }

    private String includeSortAndGroup(AuthParam<?> authInput, boolean includeSort, String groupClause) {
        return (Strings.isNullOrEmpty(groupClause) ? "" : groupClause) + (includeSort ? mineProvider.getSortClause(
                authInput.getSortField(),
                authInput.isAscending()) + " " : "");
    }

    private String cleanSortClause(String sort, boolean anyRelation) {
        if (anyRelation) {
            sort = sort.replaceAll("\\w\\.", "");
        }
        return sort;
    }

    private String createOuterSelect(String selectClause) {
        if (Pattern.compile("^\\s*select\\s+distinct\\(.*\\)\\s*", Pattern.CASE_INSENSITIVE).matcher(selectClause)
                .find()) {
            return "SELECT count(count) ";
        } else {
            // Pulls out column names from the original select clause in the form of
            // "SELECT foo.bar AS bing" so that the outer clause reads 'SELECT bing'.
            // E.g., removes 'foo.bar AS', 'foo.*' and 'bar AS' from the string.
            selectClause = selectClause.replaceAll("((\\w+\\.\\w+)|\\w+\\(.*?\\))\\s+[aA][sS]\\s+(\\w+)", "$3");
            return selectClause.replaceAll("\\w+\\.", "");
        }
    }

    private String generateRelationSelect(
            int authRelation,
            AuthParam<?> authParam,
            String selectClause,
            String fromClause,
            String whereClause,
            Integer permissionMask) {
        String joinClause = "";
        String authWhereClause = null;

        if (authParam.getAuthRelation() == ACCESS_MODE_ROOT) {
            if (whereClause != null && whereClause.trim().length() > 0) {
                return selectClause + " " + fromClause + " " + joinClause + " WHERE " + whereClause;
            } else {
                return selectClause + " " + fromClause + " " + joinClause;
            }
        } else {
            switch (authRelation) {
                case ACCESS_MODE_OWN:
                    if (mineProvider.hasMineClause()) {
                        authWhereClause = mineProvider.getMineWhereClause(authParam);
                        joinClause = mineProvider.getMineJoinClause(authParam);
                    } else {
                        authWhereClause = " FALSE";
                    }
                    break;
                case ACCESS_MODE_PUBLIC:
                    authWhereClause = " e.public=TRUE";
                    break;
                case ACCESS_MODE_PRIVILEGED:
                    if (permissionMask == null) {
                        throw new GeneralException("Must specify permissions to make an authorization query.");
                    }

                    Integer authSubject;
                    if (authParam.getEffectiveId() != null) {
                        authSubject = authParam.getEffectiveId();
                    } else {
                        authSubject = null;
                    }
                    joinClause += (authSubject == null ? "" : " JOIN acl_entry acl ON e.id = acl.acl_object_identity ");
                    authWhereClause = authSubject == null ? "" : "acl.sid=" + authSubject + " AND acl.mask>="
                            + permissionMask;
                    break;
                default:
                    break;
            }

            String effectiveWhereClause;
            if (whereClause == null && authWhereClause == null) {
                effectiveWhereClause = "";
            } else if (whereClause == null) {
                effectiveWhereClause = "WHERE " + authWhereClause;
            } else if (authWhereClause == null) {
                effectiveWhereClause = "WHERE " + whereClause;
            } else {
                effectiveWhereClause = "WHERE (" + whereClause + ") AND (" + authWhereClause + ")";
            }

            return selectClause + " " + fromClause + " " + joinClause + " " + effectiveWhereClause;
        }
    }

    private String getLimitClause(AuthParam<?> authInput) {
        String limitClause;
        if (authInput.getLength() > 0 && authInput.getStart() > 0) {
            limitClause = " LIMIT ? OFFSET ?";
        } else if (authInput.getLength() > 0) {
            limitClause = " LIMIT ?";
        } else if (authInput.getStart() > 0) {
            limitClause = " OFFSET ?";
        } else {
            limitClause = "";
        }

        return limitClause;
    }

    private void fillParameters(
            Object[] parameters,
            AuthParam<?> authInput,
            PreparedStatement statement,
            boolean distributeOrderAndLimit) throws SQLException {
        int unionCount = getUnionCountForRelation(authInput);

        int limitParameterCount;
        if (!distributeOrderAndLimit) {
            limitParameterCount = 0;
        } else if (authInput.getLength() > 0 && authInput.getStart() > 0) {
            limitParameterCount = 2;
        } else if (authInput.getLength() > 0) {
            limitParameterCount = 1;
        } else if (authInput.getStart() > 0) {
            limitParameterCount = 1;
        } else {
            limitParameterCount = 0;
        }

        for (int i = 0; i < unionCount; i++) {
            for (int j = 0; j < parameters.length; j++) {
                int index = i * (parameters.length + limitParameterCount) + j + 1;
                if (parameters[j] instanceof String) {
                    statement.setString(index, (String) parameters[j]);
                } else if (parameters[j] instanceof BigDecimal) {
                    statement.setBigDecimal(index, (BigDecimal) parameters[j]);
                } else if (parameters[j] instanceof Date) {
                    statement.setDate(index, (Date) parameters[j]);
                } else if (parameters[j] instanceof Float) {
                    statement.setFloat(index, (Float) parameters[j]);
                } else if (parameters[j] instanceof Integer) {
                    statement.setInt(index, (Integer) parameters[j]);
                } else if (parameters[j] instanceof Long) {
                    statement.setLong(index, (Long) parameters[j]);
                } else {
                    throw new GeneralException("Unknown object type: " + parameters[j].getClass().getCanonicalName());
                }
            }
        }

        if (authInput.getLength() > 0 && authInput.getStart() > 0) {
            statement.setInt((parameters.length + limitParameterCount) * unionCount + 1, authInput.getLength());
            statement.setInt((parameters.length + limitParameterCount) * unionCount + 2, authInput.getStart());
        } else if (authInput.getLength() > 0) {
            statement.setInt((parameters.length + limitParameterCount) * unionCount + 1, authInput.getLength());
        } else if (authInput.getStart() > 0) {
            statement.setInt((parameters.length + limitParameterCount) * unionCount + 1, authInput.getStart());
        }
    }

    private int getUnionCountForRelation(AuthParam<?> authInput) {
        final int authRelation = authInput.getAuthRelation();
        if (authInput.getEffectiveId() == null || authInput.getEffectiveId() != ROOT_ID) {
            if (authRelation == ACCESS_MODE_ANY && authInput.getEffectiveId() != null) {
                return !mineProvider.hasMineClause() ? 2 : 3;
            } else if (authRelation == ACCESS_MODE_ANY) {
                return !mineProvider.hasMineClause() ? 1 : 2;
            } else if (authRelation == ACCESS_MODE_EXPLICIT && authInput.getEffectiveId() != null) {
                return !mineProvider.hasMineClause() ? 1 : 2;
            }
        }

        return 1;
    }
}

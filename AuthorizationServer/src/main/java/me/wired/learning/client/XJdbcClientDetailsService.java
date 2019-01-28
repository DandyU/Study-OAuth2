package me.wired.learning.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.util.DefaultJdbcListFactory;
import org.springframework.security.oauth2.common.util.JdbcListFactory;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class XJdbcClientDetailsService extends JdbcClientDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(XJdbcClientDetailsService.class);

    private ObjectMapper mapper = new ObjectMapper();

    private static final String CLIENT_FIELDS_FOR_UPDATE = "resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information";

    private static final String CLIENT_FIELDS = "client_secret, " + CLIENT_FIELDS_FOR_UPDATE;

    private static final String BASE_FIND_STATEMENT = "select client_id, " + CLIENT_FIELDS
            + " from oauth_client_details";

    private static final String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

    private static final String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

    private static final String DEFAULT_INSERT_STATEMENT = "insert into oauth_client_details (" + CLIENT_FIELDS
            + ", client_id) values (?,?,?,?,?,?,?,?,?,?)";

    private static final String DEFAULT_UPDATE_STATEMENT = "update oauth_client_details " + "set "
            + CLIENT_FIELDS_FOR_UPDATE.replaceAll(", ", "=?, ") + "=? where client_id = ?";

    private static final String DEFAULT_UPDATE_SECRET_STATEMENT = "update oauth_client_details "
            + "set client_secret = ? where client_id = ?";

    private static final String DEFAULT_DELETE_STATEMENT = "delete from oauth_client_details where client_id = ?";

    private RowMapper<ClientDetails> rowMapper = new ClientDetailsRowMapper();

    private String deleteClientDetailsSql = DEFAULT_DELETE_STATEMENT;

    private String findClientDetailsSql = DEFAULT_FIND_STATEMENT;

    private String updateClientDetailsSql = DEFAULT_UPDATE_STATEMENT;

    private String updateClientSecretSql = DEFAULT_UPDATE_SECRET_STATEMENT;

    private String insertClientDetailsSql = DEFAULT_INSERT_STATEMENT;

    private String selectClientDetailsSql = DEFAULT_SELECT_STATEMENT;

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final JdbcTemplate jdbcTemplate;

    private JdbcListFactory listFactory;

    public XJdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
        Assert.notNull(dataSource, "DataSource required");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.listFactory = new DefaultJdbcListFactory(new NamedParameterJdbcTemplate(jdbcTemplate));
    }

    /**
     * @param passwordEncoder the password encoder to set
     */
    @Override
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ClientDetails loadClientByClientId(String client_id) throws InvalidClientException {
        ClientDetails details;
        try {
            details = jdbcTemplate.queryForObject(selectClientDetailsSql, new ClientDetailsRowMapper(), client_id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new NoSuchClientException("No client with requested id: " + client_id);
        }

        return details;
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        try {
            jdbcTemplate.update(insertClientDetailsSql, getFields(clientDetails));
        }
        catch (DuplicateKeyException e) {
            throw new ClientAlreadyExistsException("Client already exists: " + clientDetails.getClientId(), e);
        }
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        int count = jdbcTemplate.update(updateClientDetailsSql, getFieldsForUpdate(clientDetails));
        if (count != 1) {
            throw new NoSuchClientException("No client found with id = " + clientDetails.getClientId());
        }
    }

    @Override
    public void updateClientSecret(String client_id, String secret) throws NoSuchClientException {
        int count = jdbcTemplate.update(updateClientSecretSql, passwordEncoder.encode(secret), client_id);
        if (count != 1) {
            throw new NoSuchClientException("No client found with id = " + client_id);
        }
    }

    @Override
    public void removeClientDetails(String client_id) throws NoSuchClientException {
        int count = jdbcTemplate.update(deleteClientDetailsSql, client_id);
        if (count != 1) {
            throw new NoSuchClientException("No client found with id = " + client_id);
        }
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return listFactory.getList(findClientDetailsSql, Collections.<String, Object> emptyMap(), rowMapper);
    }

    private Object[] getFields(ClientDetails clientDetails) {
        Object[] fieldsForUpdate = getFieldsForUpdate(clientDetails);
        Object[] fields = new Object[fieldsForUpdate.length + 1];
        System.arraycopy(fieldsForUpdate, 0, fields, 1, fieldsForUpdate.length);
        fields[0] = clientDetails.getClientSecret() != null ? passwordEncoder.encode(clientDetails.getClientSecret())
                : null;
        return fields;
    }

    private Object[] getFieldsForUpdate(ClientDetails clientDetails) {
        String json = null;
        try {
            json = mapper.writeValueAsString(clientDetails.getAdditionalInformation());
        }
        catch (Exception e) {
            logger.warn("Could not serialize additional information: " + clientDetails, e);
        }
        return new Object[] {
                clientDetails.getResourceIds() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails
                        .getResourceIds()) : null,
                clientDetails.getScope() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails
                        .getScope()) : null,
                clientDetails.getAuthorizedGrantTypes() != null ? StringUtils
                        .collectionToCommaDelimitedString(clientDetails.getAuthorizedGrantTypes()) : null,
                clientDetails.getRegisteredRedirectUri() != null ? StringUtils
                        .collectionToCommaDelimitedString(clientDetails.getRegisteredRedirectUri()) : null,
                clientDetails.getAuthorities() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails
                        .getAuthorities()) : null, clientDetails.getAccessTokenValiditySeconds(),
                clientDetails.getRefreshTokenValiditySeconds(), json, clientDetails.getClientId() };
    }

    @Override
    public void setSelectClientDetailsSql(String selectClientDetailsSql) {
        this.selectClientDetailsSql = selectClientDetailsSql;
    }

    @Override
    public void setDeleteClientDetailsSql(String deleteClientDetailsSql) {
        this.deleteClientDetailsSql = deleteClientDetailsSql;
    }

    @Override
    public void setUpdateClientDetailsSql(String updateClientDetailsSql) {
        this.updateClientDetailsSql = updateClientDetailsSql;
    }

    @Override
    public void setUpdateClientSecretSql(String updateClientSecretSql) {
        this.updateClientSecretSql = updateClientSecretSql;
    }

    @Override
    public void setInsertClientDetailsSql(String insertClientDetailsSql) {
        this.insertClientDetailsSql = insertClientDetailsSql;
    }

    @Override
    public void setFindClientDetailsSql(String findClientDetailsSql) {
        this.findClientDetailsSql = findClientDetailsSql;
    }

    /**
     * @param listFactory the list factory to set
     */
    @Override
    public void setListFactory(JdbcListFactory listFactory) {
        this.listFactory = listFactory;
    }

    /**
     * @param rowMapper the rowMapper to set
     */
    @Override
    public void setRowMapper(RowMapper<ClientDetails> rowMapper) {
        this.rowMapper = rowMapper;
    }

    /**
     * Row mapper for ClientDetails.
     *
     * @author Dave Syer
     *
     */
    private static class ClientDetailsRowMapper implements RowMapper<ClientDetails> {
        private ObjectMapper mapper = new ObjectMapper();

        @Override
        public ClientDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            BaseClientDetails details = new BaseClientDetails(rs.getString(1), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(7), rs.getString(6));
            details.setClientSecret(rs.getString(2));
            if (rs.getObject(8) != null) {
                details.setAccessTokenValiditySeconds(rs.getInt(8));
            }
            if (rs.getObject(9) != null) {
                details.setRefreshTokenValiditySeconds(rs.getInt(9));
            }
            String json = rs.getString(10);
            if (json != null) {
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> additionalInformation = mapper.readValue(json, Map.class);
                    details.setAdditionalInformation(additionalInformation);
                }
                catch (Exception e) {
                    logger.warn("Could not decode JSON for additional information: " + details, e);
                }
            }
            return details;
        }
    }

}

package com.azure.spring.cloud.appconfiguration.config.implementation.autofailover;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;

public class DNSLookup {

    private static final String ORIGIN_PREFIX = "dns:/_origin._tcp.";

    private static final String REPLICA_PREFIX_ALT = "dns:/_alt";

    private static final String REPLICA_PREFIX_TCP = "._tcp.";

    private static final String SRC_RECORD = "SRV";

    private InitialDirContext context;

    public DNSLookup() throws NamingException {
        this.context = new InitialDirContext();
    }

    public SRVRecord getOriginRecord(String url) throws NamingException {
        Attribute attribute = requestRecord(ORIGIN_PREFIX + url);
        if (attribute != null) {
            return parseHosts(attribute).get(0);
        }
        return null;
    }

    public List<SRVRecord> getReplicaRecords(SRVRecord origin) throws NamingException {
        List<SRVRecord> replicas = new ArrayList<>();
        int i = 0;
        while (true) {
            Attribute attribute = requestRecord(
                REPLICA_PREFIX_ALT + i + REPLICA_PREFIX_TCP + origin.getTarget());

            if (attribute == null) {
                break;
            }

            replicas.addAll(parseHosts(attribute));
            i++;
        }
        return replicas;
    }

    private Attribute requestRecord(String name) throws NamingException {
        try {
            return context.getAttributes(name, new String[] { SRC_RECORD }).get("SRV");
        } catch (NameNotFoundException e) {
            // Found Last Record, should be the case that no SRV Record exists.
            return null;
        }
    }

    private List<SRVRecord> parseHosts(Attribute attribute) {
        List<SRVRecord> hosts = new ArrayList<>();
        try {
            NamingEnumeration<?> records = attribute.getAll();
            while (records.hasMore()) {
                hosts.add(new SRVRecord(((String) records.next()).toString().split(" ")));
            }
        } catch (NamingException e) {
        }

        return hosts;
    }

}
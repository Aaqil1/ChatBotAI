package com.contextsupport.incident.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "email")
public class EmailProperties {

    private final Smtp smtp = new Smtp();
    private final Inbox inbox = new Inbox();
    private String portalBaseUrl = "http://localhost:3000";

    public Smtp getSmtp() {
        return smtp;
    }

    public Inbox getInbox() {
        return inbox;
    }

    public String getPortalBaseUrl() {
        return portalBaseUrl;
    }

    public void setPortalBaseUrl(String portalBaseUrl) {
        this.portalBaseUrl = portalBaseUrl;
    }

    public static class Smtp {
        private String host = "localhost";
        private String bindHost = "0.0.0.0";
        private int port = 2525;
        private String fromAddress = "noreply@contextsupport.ai";
        private boolean enabled = true;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getBindHost() {
            return bindHost;
        }

        public void setBindHost(String bindHost) {
            this.bindHost = bindHost;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getFromAddress() {
            return fromAddress;
        }

        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Inbox {
        private boolean enabled = true;
        private String address = "support@contextsupport.ai";
        private long pollingIntervalMs = 5000L;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getPollingIntervalMs() {
            return pollingIntervalMs;
        }

        public void setPollingIntervalMs(long pollingIntervalMs) {
            this.pollingIntervalMs = pollingIntervalMs;
        }
    }
}

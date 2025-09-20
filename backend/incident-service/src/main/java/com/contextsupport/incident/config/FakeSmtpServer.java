package com.contextsupport.incident.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import org.subethamail.smtp.server.Wiser;
import org.subethamail.smtp.server.WiserMessage;

@Component
public class FakeSmtpServer implements SmartLifecycle {

    private static final Logger log = LoggerFactory.getLogger(FakeSmtpServer.class);

    private final EmailProperties properties;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Wiser wiser;

    public FakeSmtpServer(EmailProperties properties) {
        this.properties = properties;
    }

    @Override
    public void start() {
        if (!properties.getInbox().isEnabled() || !properties.getSmtp().isEnabled()) {
            log.info("Fake SMTP server disabled via configuration");
            return;
        }
        if (running.compareAndSet(false, true)) {
            wiser = new Wiser();
            wiser.setHostname(properties.getSmtp().getBindHost());
            wiser.setPort(properties.getSmtp().getPort());
            try {
                wiser.start();
                log.info("Fake SMTP server started on {}:{} (client host: {})",
                        properties.getSmtp().getBindHost(), properties.getSmtp().getPort(), properties.getSmtp().getHost());
            } catch (RuntimeException ex) {
                running.set(false);
                wiser = null;
                log.error("Unable to start fake SMTP server on {}:{}", properties.getSmtp().getBindHost(), properties.getSmtp().getPort(), ex);
            }
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false) && wiser != null) {
            wiser.stop();
            wiser = null;
            log.info("Fake SMTP server stopped");
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    public List<WiserMessage> drainMessages() {
        if (!isRunning() || wiser == null) {
            return Collections.emptyList();
        }
        synchronized (wiser) {
            List<WiserMessage> copy = new ArrayList<>(wiser.getMessages());
            wiser.getMessages().clear();
            return copy;
        }
    }
}

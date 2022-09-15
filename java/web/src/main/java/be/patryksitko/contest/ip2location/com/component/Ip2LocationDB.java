package be.patryksitko.contest.ip2location.com.component;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.zip.ZipInputStream;

import javax.inject.Singleton;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@Component
public class Ip2LocationDB {

    public static class InitializationException extends RuntimeException {
        public InitializationException() {
            super("Could not initialize databases.");
        }
    }

    @PropertySource(value = "classpath:ip2location.properties")
    public static enum Download {
        DB11LITEBIN, DB11LITEBINIPV6;

        // @Value("${ip2location.token}")
        private static final String authenticationToken = "";
        private URL downloadURL;

        private Download() {
            try {
                this.downloadURL = new URL(
                        "https://www.ip2location.com/download/?token=" + authenticationToken + "&file=" + this.name());
            } catch (MalformedURLException e) {
                log.error(e.getMessage());
            }
        }

        public Optional<byte[]> fetch() {
            byte[] requestedContent;
            final ArrayList<Byte> downloadedContent = new ArrayList<>();
            try (ZipInputStream in = new ZipInputStream(this.downloadURL.openStream());) {
                byte dataBuffer[] = new byte[1024];
                while (in.read(dataBuffer, 0, 1024) != -1) {
                    for (byte character : dataBuffer) {
                        downloadedContent.add(character);
                    }
                }
                requestedContent = new byte[downloadedContent.size()];
                for (int index = 0; index < requestedContent.length; index++) {
                    requestedContent[index] = downloadedContent.get(index);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
                return Optional.empty();
            }
            return Optional.of(requestedContent);
        }
    }

    // v4
    private static volatile byte[] v4Content;
    private static volatile IP2Location ip2LocationV4;
    private static volatile boolean ip2LocationV4Opened;
    private static volatile boolean ip2LocationV4OpeningFinnished = true;
    private static volatile LocalDateTime ipV4Downloaded;

    // v6
    private static volatile byte[] v6Content;
    private static volatile IP2Location ip2LocationV6;
    private static volatile boolean ip2LocationV6Opened;
    private static volatile boolean ip2LocationV6OpeningFinnished = true;
    private static volatile LocalDateTime ipV6Downloaded;

    public Ip2LocationDB() throws InitializationException {
        this.fetchData();
        this.reinitialize();
    }

    private static Runnable ipV4DownloadRunnable = () -> {
        ipV4Downloaded = null;
        Optional<byte[]> dbContent;
        do {
            dbContent = Download.DB11LITEBIN.fetch();
        } while (dbContent.isEmpty());
        ipV4Downloaded = LocalDateTime.now();
        v4Content = dbContent.get();
        System.out.println("v4-downloaded.");
    };

    private static Runnable ipV4InitializatorRunnable = () -> {
        if (ip2LocationV4 != null) {
            ip2LocationV4.Close();
        }
        ip2LocationV4OpeningFinnished = false;
        ip2LocationV4Opened = false;
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        } while (ipV4Downloaded == null);
        ip2LocationV4 = new IP2Location();
        int currentOpeningTry = 0;
        do {
            try {
                ip2LocationV4.Open(v4Content);
                ip2LocationV4Opened = true;
            } catch (IOException e) {
                log.error(e.getMessage());
                ++currentOpeningTry;
            }
        } while (!ip2LocationV4Opened && currentOpeningTry < 3);
        ip2LocationV4OpeningFinnished = true;
    };

    private static Runnable ipV6DownloadRunnable = () -> {
        ipV6Downloaded = null;
        Optional<byte[]> dbContent;
        do {
            dbContent = Download.DB11LITEBIN.fetch();
        } while (dbContent.isEmpty());
        ipV6Downloaded = LocalDateTime.now();
        v6Content = dbContent.get();
        System.out.println("v6-downloaded.");
    };

    private static Runnable ipV6InitializatorRunnable = () -> {
        if (ip2LocationV6 != null) {
            ip2LocationV6.Close();
        }
        ip2LocationV6OpeningFinnished = false;
        ip2LocationV6Opened = false;
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        } while (ipV6Downloaded == null);
        ip2LocationV6 = new IP2Location();
        int currentOpeningTry = 0;
        do {
            try {
                ip2LocationV6.Open(v6Content);
                ip2LocationV6Opened = true;
            } catch (IOException e) {
                log.error(e.getMessage());
                ++currentOpeningTry;
            }
        } while (!ip2LocationV6Opened && currentOpeningTry < 3);
        ip2LocationV6OpeningFinnished = true;
    };

    private void fetchData() {
        final ArrayList<Thread> threads = new ArrayList<>();
        threads.add(new Thread(ipV4DownloadRunnable));
        threads.add(new Thread(ipV6DownloadRunnable));
        for (Thread thread : threads) {
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void reinitialize() throws InitializationException {
        final Thread initializatorThread = new Thread(() -> {
            int initializationTry = 0;
            do {
                if (!(ip2LocationV4OpeningFinnished && ip2LocationV6OpeningFinnished)) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                    continue;
                }
                final ArrayList<Thread> threads = new ArrayList<>();
                if (!ip2LocationV4Opened && ip2LocationV4OpeningFinnished) {
                    threads.add(new Thread(ipV4InitializatorRunnable));
                }
                if (!ip2LocationV6Opened && ip2LocationV6OpeningFinnished) {
                    threads.add(new Thread(ipV6InitializatorRunnable));
                }
                for (Thread thread : threads) {
                    thread.setDaemon(true);
                    thread.start();
                }
                if (++initializationTry > 7) {
                    throw new InitializationException();
                }
            } while (!ip2LocationV4Opened || !ip2LocationV6Opened);
        });
        initializatorThread.setDaemon(true);
        initializatorThread.start();
    }

    public void performPeriodicCheck() {

    }

    public boolean isIpV4QueryUsable() {
        return ip2LocationV4Opened;
    }

    public boolean isIpV6QueryUsable() {
        return ip2LocationV6Opened;
    }

    public boolean isIPv4(String IPAddress) throws UnknownHostException {
        return InetAddress.getByName(IPAddress) instanceof Inet4Address;
    }

    public boolean isIPv6(String IPAddress) throws UnknownHostException {
        return InetAddress.getByName(IPAddress) instanceof Inet6Address;
    }

    public Optional<IPResult> IPQuery(String IPAddress) {
        try {
            if (this.isIPv4(IPAddress)) {
                return this.IPQueryV4(IPAddress);
            }
            if (this.isIPv6(IPAddress)) {
                return this.IPQueryV6(IPAddress);
            }
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<IPResult> IPQueryV4(String IPv4Address) {
        try {
            return Optional.of(ip2LocationV4.IPQuery(IPv4Address));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<IPResult> IPQueryV6(String IPv6Address) {
        try {
            return Optional.of(ip2LocationV6.IPQuery(IPv6Address));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public void close() {
        ip2LocationV4Opened = false;
        ip2LocationV4.Close();
        ip2LocationV6Opened = false;
        ip2LocationV6.Close();
    }
}

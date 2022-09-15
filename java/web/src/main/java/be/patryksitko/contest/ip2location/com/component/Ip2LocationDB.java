package be.patryksitko.contest.ip2location.com.component;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.zip.ZipInputStream;

import javax.inject.Singleton;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;

import be.patryksitko.contest.ip2location.com.component.exception.DownloadLimitException;
import be.patryksitko.contest.ip2location.com.component.exception.InitializationException;
import be.patryksitko.contest.ip2location.com.other.anonFunctions.ByteArrayFunction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@Component
public class Ip2LocationDB {

    @PropertySource(value = "classpath:ip2location.properties")
    public static enum Download {
        DB11LITEBIN, DB11LITEBINIPV6;

        public static enum DownloadedDataExceptionTriggers {
            THIS_FILE_CAN_ONLY_BE_DOWNLOADED_5_TIMES_PER_HOUR(new byte[] { 84, 72, 73, 83,
                    32,
                    70, 73, 76, 69, 32, 67, 65, 78, 32, 79, 78, 76, 89, 32, 66, 69, 32, 68, 79, 87, 78, 76, 79, 65, 68,
                    69,
                    68,
                    32, 53, 32, 84, 73, 77, 69, 83, 32, 80, 69, 82, 32, 72, 79, 85, 82 });

            private byte[] exceptionArray;

            private DownloadedDataExceptionTriggers(byte[] exceptionArray) {
                this.exceptionArray = exceptionArray;
            }

            public byte[] getArray() {
                return this.exceptionArray;
            }
        }

        // @Value("${ip2location.token}")
        private static final String authenticationToken = "1FCRhWx3qBrxt9LXD6fdjBJXk3xfFZ3wbxqTievWjTMAnUsTjfVK75X6HzE9H5Hr";
        private URL downloadURL;

        private Download() {
            try {
                this.downloadURL = new URL(
                        "https://www.ip2location.com/download/?token=" + authenticationToken + "&file=" + this.name());
            } catch (MalformedURLException e) {
                log.error(e.getMessage());
            }
        }

        private byte[] readInputSteam(InputStream in) throws IOException {
            byte[] content;
            final ArrayList<Byte> contentBuffer = new ArrayList<>();
            boolean finnished = true;
            do {
                byte dataBuffer[] = new byte[in.available()];
                finnished = in.read(dataBuffer, 0, dataBuffer.length) != -1;
                for (byte character : dataBuffer) {
                    contentBuffer.add(character);
                }
            } while (!finnished);
            content = new byte[contentBuffer.size()];
            for (int index = 0; index < content.length; index++) {
                content[index] = contentBuffer.get(index);
            }
            return content;
        }

        public Optional<byte[]> fetch() {

            final ByteArrayFunction checkForDownloadExceptions = (byte[] bytes) -> {
                if (Arrays.equals(bytes,
                        DownloadedDataExceptionTriggers.THIS_FILE_CAN_ONLY_BE_DOWNLOADED_5_TIMES_PER_HOUR.getArray())) {
                    throw new DownloadLimitException();
                }
                return bytes;
            };

            try (BufferedInputStream in = new BufferedInputStream(this.downloadURL.openStream());
                    ZipInputStream zipIn = new ZipInputStream(
                            new ByteArrayInputStream(checkForDownloadExceptions.__(readInputSteam(in))))) {
                return Optional.of(readInputSteam(zipIn));
            } catch (IOException | DownloadLimitException e) {
                log.error(e.getMessage());
                return Optional.empty();
            }
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
            dbContent = Download.DB11LITEBINIPV6.fetch();
        } while (dbContent.isEmpty());
        ipV6Downloaded = LocalDateTime.now();
        v6Content = dbContent.get();
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
        final Thread ipV4DownloadThread = new Thread(ipV4DownloadRunnable);
        ipV4DownloadThread.setName("ipV4Download");
        threads.add(ipV4DownloadThread);
        final Thread ipV6DownloadThread = new Thread(ipV6DownloadRunnable);
        ipV6DownloadThread.setName("ipV6Download");
        threads.add(new Thread(ipV6DownloadThread));
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
                    final Thread ipV4InitializatorThread = new Thread(ipV4InitializatorRunnable);
                    ipV4InitializatorThread.setName("ipV4Initializator");
                    threads.add(ipV4InitializatorThread);
                }
                if (!ip2LocationV6Opened && ip2LocationV6OpeningFinnished) {
                    final Thread ipV6InitializatorThread = new Thread(ipV6InitializatorRunnable);
                    ipV6InitializatorThread.setName("ipV6Initializator");
                    threads.add(ipV6InitializatorThread);
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
        initializatorThread.setName("initializator");
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

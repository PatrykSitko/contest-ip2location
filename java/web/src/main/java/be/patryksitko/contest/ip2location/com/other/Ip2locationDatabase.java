package be.patryksitko.contest.ip2location.com.other;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.zip.ZipInputStream;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
// @PropertySource(value = "classpath:ip2location.properties")
public class Ip2locationDatabase {

    private IP2Location ip2locationV4 = new IP2Location();
    private volatile boolean ip2locationV4Loaded = false;
    private IP2Location ip2locationV6 = new IP2Location();
    private volatile boolean ip2locationV6Loaded = false;

    // @Value("${ip2location.token}")
    private String authenticationToken = "";

    private URL databaseIPV4;
    {
        try {
            databaseIPV4 = new URL(
                    "https://www.ip2location.com/download/?token=" + authenticationToken + "&file=DB11LITEBIN");
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
    }

    private URL databaseIPV6;
    {
        try {
            databaseIPV6 = new URL(
                    "https://www.ip2location.com/download/?token=" + authenticationToken + "&file=DB11LITEBINIPV6");
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
    }

    private void loadDatabaseIPV4() {
        ArrayList<Byte> downloadedContent = new ArrayList<>();
        try (ZipInputStream in = new ZipInputStream(databaseIPV4.openStream());) {
            byte dataBuffer[] = new byte[1024];

            while (in.read(dataBuffer, 0, 1024) != -1) {
                for (byte character : dataBuffer) {
                    downloadedContent.add(character);
                }
            }
            byte[] ip2locationDB = new byte[downloadedContent.size()];
            for (byte entry : ip2locationDB) {
                downloadedContent.add(entry);
            }
            ip2locationV4.Open(ip2locationDB);
            ip2locationV4Loaded = true;
        } catch (IOException e) {
            log.error(e.getMessage());
            ip2locationV4Loaded = false;
        }
    }

    private void loadDatabaseIPV6() {
        ArrayList<Byte> downloadedContent = new ArrayList<>();
        try (ZipInputStream in = new ZipInputStream(databaseIPV6.openStream());) {
            byte dataBuffer[] = new byte[1024];

            while (in.read(dataBuffer, 0, 1024) != -1) {
                for (byte character : dataBuffer) {
                    downloadedContent.add(character);
                }
            }
            byte[] ip2locationDB = new byte[downloadedContent.size()];
            for (byte entry : ip2locationDB) {
                downloadedContent.add(entry);
            }
            ip2locationV6.Open(ip2locationDB);
            ip2locationV6Loaded = true;
        } catch (IOException e) {
            log.error(e.getMessage());
            ip2locationV6Loaded = false;
        }
    }

    public Ip2locationDatabase() {
        final Thread ipv4 = new Thread(() -> loadDatabaseIPV4());
        ipv4.setDaemon(true);
        ipv4.start();

        final Thread ipv6 = new Thread(() -> loadDatabaseIPV6());
        ipv6.setDaemon(true);
        ipv6.start();
    }

    public Optional<IPResult> IPQueryV4(String IPAddress) {
        if (ip2locationV4Loaded) {
            try {
                return Optional.of(ip2locationV4.IPQuery(IPAddress));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return Optional.empty();
    }

    public Optional<IPResult> IPQueryV6(String IPAddress) {
        if (ip2locationV6Loaded) {
            try {
                return Optional.of(ip2locationV6.IPQuery(IPAddress));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return Optional.empty();
    }

    // To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

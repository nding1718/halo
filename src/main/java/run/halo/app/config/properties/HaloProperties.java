package run.halo.app.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import static run.halo.app.model.support.HaloConst.*;
import static run.halo.app.utils.HaloUtils.ensureSuffix;


/**
 * Halo configuration properties.
 *
 * Before we dive down into the learning of configuration, we should know a concept at first.
 *
 * POJO:
 *  POJO stands for Plain Old Java Object, you can simply refer it as simple java beans, as a different to EJB, they are
 *  not constraint by any other rules(not like EJB) but only the java standards
 *
 * One handy features of Spring Boot is externalized configurations and easy access to properties defined in properties
 * files. Here we talk about sth about the ConfigurationProperties annotation:
 *  -- it works best with hierarchical properties that all have the same prefix. ==> here would be 'halo'
 * @author johnniang
 */
@Data // annotation used to auto generate get and set methods
@ConfigurationProperties("halo") // set this POJO as configurations and add prefix "halo"
public class HaloProperties {

    /**
     * Doc api disabled. (Default is true)
     */
    private boolean docDisabled = true;

    /**
     * Production env. (Default is true)
     */
    private boolean productionEnv = true;

    /**
     * Authentication enabled
     */
    private boolean authEnabled = true;

    /**
     * Admin path.
     */
    private String adminPath = "admin";

    /**
     * Work directory.
     */
    private String workDir = ensureSuffix(USER_HOME, FILE_SEPARATOR) + ".halo" + FILE_SEPARATOR;

    /**
     * Halo backup directory.(Not recommended to modify this config);
     */
    private String backupDir = ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "halo-backup" + FILE_SEPARATOR;

    /**
     * Upload prefix.
     */
    private String uploadUrlPrefix = "upload";

    /**
     * Download Timeout.
     */
    private Duration downloadTimeout = Duration.ofSeconds(30);

    public HaloProperties() throws IOException {
        // Create work directory if not exist
        Files.createDirectories(Paths.get(workDir));
        Files.createDirectories(Paths.get(backupDir));
    }
}

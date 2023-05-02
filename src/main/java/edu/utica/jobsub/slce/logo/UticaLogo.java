package edu.utica.jobsub.slce.logo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class UticaLogo implements Logo{

    private final ResourceLoader resourceLoader;

    Logger log = LoggerFactory.getLogger(UticaLogo.class);

    @Autowired
    public UticaLogo(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public byte[] getLogo() {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(getUticaLogoPath());
        } catch (IOException ioException) {
            try {
                bytes = getUticaLogoResource().getInputStream().readAllBytes();
            } catch (IOException ioException2) {
                log.error("Utica Logo Image not found in $BANNER_LINKS on jobsub server, or as resource to be loaded from classpath.");
            }
        }
        return bytes;
    }

    public Path getUticaLogoPath() {
        return Paths.get("/u01/app/sct/banPROD/links","utica_logo.png");
    }

    public Resource getUticaLogoResource() {
        return resourceLoader.getResource("classpath:/logo/utica_logo.png");
    }

}

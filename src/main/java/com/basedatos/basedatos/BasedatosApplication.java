package com.basedatos.basedatos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class BasedatosApplication {

	public static void main(String[] args) {
		// If the environment variable TNS_ADMIN (or ORACLE_WALLET_PATH) is set, use it
		// to tell the Oracle driver where to find tnsnames.ora / sqlnet.ora (wallet).
		String tnsAdmin = System.getenv("TNS_ADMIN");
		if (tnsAdmin == null || tnsAdmin.isBlank()) {
			tnsAdmin = System.getenv("ORACLE_WALLET_PATH");
		}

		if (tnsAdmin != null && !tnsAdmin.isBlank()) {
			System.setProperty("oracle.net.tns_admin", tnsAdmin);
			System.out.println("Set oracle.net.tns_admin=" + tnsAdmin);
		} else {
			// Try to extract a bundled wallet from classpath:/wallet to a temp folder
			// and point oracle.net.tns_admin there. This helps when the wallet is
			// packaged inside resources (JAR/IDE classpath).
			try {
				Path extracted = extractWalletFromClasspath();
				if (extracted != null) {
					System.setProperty("oracle.net.tns_admin", extracted.toString());
					System.out.println("Extracted wallet and set oracle.net.tns_admin=" + extracted);
				}
			} catch (Exception e) {
				System.err.println("Could not extract wallet from classpath: " + e.getMessage());
			}
		}

		SpringApplication.run(BasedatosApplication.class, args);
	}

	/**
	 * Try to copy known wallet files from classpath:/wallet into a temp directory.
	 * Returns the directory path if at least one file was copied, otherwise null.
	 */
	private static Path extractWalletFromClasspath() throws IOException {
		List<String> walletFiles = Arrays.asList(
				"cwallet.sso",
				"ewallet.p12",
				"ewallet.pem",
				"keystore.jks",
				"ojdbc.properties",
				"sqlnet.ora",
				"tnsnames.ora",
				"truststore.jks",
				"README"
		);

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Path tmpDir = Files.createTempDirectory("oracle-wallet-");
		boolean any = false;

		for (String name : walletFiles) {
			String resourcePath = "wallet/" + name;
			try (InputStream is = cl.getResourceAsStream(resourcePath)) {
				if (is != null) {
					Path dest = tmpDir.resolve(name);
					Files.copy(is, dest, StandardCopyOption.REPLACE_EXISTING);
					any = true;
				}
			} catch (IOException e) {
				// continue copying other files; log the error
				System.err.println("Failed to copy resource " + resourcePath + ": " + e.getMessage());
			}
		}

		if (any) {
			return tmpDir;
		} else {
			// delete the empty temp dir
			try {
				Files.deleteIfExists(tmpDir);
			} catch (IOException ignored) {
			}
			return null;
		}
	}

}
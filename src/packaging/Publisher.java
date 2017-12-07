package packaging;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/**
 * Publishes an exported game to Google Drive.
 *
 * @see <a href="https://developers.google.com/drive/v3/web/quickstart/java"></a> reference source code
 *
 * Add the following Maven dependencies to run:
 *     com.google.api-client:google-api-client:1.23.0
 *     com.google.oauth-client:google-oauth-client-jetty:1.23.0
 *     com.google.apis:google-api-services-drive:v3-rev90-1.23.0
 *
 * @author Ben Schwennesen
 */
public class Publisher {

    public static final String WEB_CONTENT_LINK_FIELD = "webContentLink";
    private final String CLIENT_SECRETS_JSON = "client_secrets.json";
    private final String USER_ID = "user";

    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private HttpTransport httpTransport;
    private Drive drive;

    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);

    /**
     * Load the Google Drive instance, which entails logging into a Google account through a browser.
     *
     * @param applicationName the name of your application
     * @throws GeneralSecurityException if a safe http transport connection cannot be established
     * @throws IOException if the user's client secrets JSON cannot be accessed
     */
    public Publisher(String applicationName) throws GeneralSecurityException, IOException   {
        initialize(applicationName);
    }

    private void initialize(String applicationName) throws GeneralSecurityException, IOException {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize();
        drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(applicationName).build();
    }

    /** Authorizes the installed application to access user's protected data. */
    private Credential authorize() throws IOException {
        // load client secrets
        InputStream in = getClass().getClassLoader().getResourceAsStream(CLIENT_SECRETS_JSON);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .build();
        return new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize(USER_ID);
    }

    /**
     * Uploads a file to the Google Drive account logged into when the exporter is created.

     * @param mimeType the MIME type of the file to upload
     * @param uploadFilePath the path to the file to be uploaded
     * @return a shareable URL link to the file
     * @throws IOException if the file cannot be found
     *
     * @see <a href="https://tinyurl.com/ybmqlv6z"></a> reference source code
     * @see <a href="https://tinyurl.com/ycwww59d"></a> MIME types list
     */
    public String uploadFile(String mimeType, String uploadFilePath) throws IOException {
        File fileMetadata = new File();
        java.io.File uploadFile = new java.io.File(uploadFilePath);
        fileMetadata.setName(uploadFile.getName());
        FileContent mediaContent = new FileContent(mimeType, uploadFile);
        File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                .setFields(WEB_CONTENT_LINK_FIELD).execute();
        return uploadedFile.getWebContentLink();
    }
}
